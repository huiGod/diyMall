package cn._51app.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.druid.pool.vendor.SybaseExceptionSorter;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;

public class CommonUtil {

	 private static final String dirPath=PropertiesUtil.getValue("diy.makes.path");
	 
	 private static final String dirOrder=PropertiesUtil.getValue("diy.order.path");
	 
	 private final static String updownloadRootDir =PropertiesUtil.getValue("uploadUrl.sys");
	 
	 private static DecimalFormat df= new DecimalFormat("######0.00");
	 
	public static String imageMagickPath = "D:/Program Files/ImageMagick-6.9.1-Q16";
	 public static boolean isLinux = false;

	/**
	 * tengh 2015年9月16日 下午5:39:57
	 * @return 随机产生订单号（S+12位数）
	 * TODO
	 */
	public static String createOrderNo(String flag,int pwd_len) {
		//35是因为数组是从0开始的，26个字母+10个数字
		pwd_len=pwd_len+7;
	    final int maxNum = 36;
	    int i; //生成的随机数
	    int count = 0; //生成的密码的长度
	    char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	    StringBuffer pwd = new StringBuffer("");
	    if(flag!=null)
	    pwd.append(flag);
	    
	    Random r = new Random();
	    while(count < pwd_len){
	     //生成随机数，取绝对值，防止生成负数，
	   
	     i = Math.abs(r.nextInt(maxNum)); //生成的数最大为36-1
	   
	     if (i >= 0 && i < str.length) {
	      pwd.append(str[i]);
	      count ++;
	     }
	    }
	    return pwd.toString();
	}
	
	public static String subStr(String str){
		try {
			if(StringUtils.isNotBlank(str)){
				return str.substring(0, str.length()-1);
			}
		} catch (Exception e) {
			
		}
		return ""; 
	}
	
	/**
	 * 批量上传照片书
	 * @param req   http图片请求
	 * @param imgpath   图片根目录
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public static String batchImg(HttpServletRequest req,String imgpath) throws Exception{
		String clientfileName="",filesuffix="",path="",basefileName="",resultImg="",resultSuffix="";
		/**  时间文件  **/
		String sdf=new SimpleDateFormat("yyyyMMdd").format(new Date())+"/";
		path=updownloadRootDir+dirPath+imgpath+"/"+sdf;
		File file =new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		CommonsMultipartResolver resolver = new CommonsMultipartResolver(req.getSession().getServletContext());
		//是否存在文件上传请求
		if(resolver.isMultipart(req)){
			//转换多文件请求
			MultipartHttpServletRequest multipart = (MultipartHttpServletRequest)req;
			//所有文件名读取到迭代器
			Iterator<String>iterator = multipart.getFileNames();
			//随机名
			String uuid=UUID.randomUUID().toString().replaceAll("-","").substring(0,8);
			int count=1;
			while(iterator.hasNext()){
				MultipartFile Clientfile = multipart.getFile(iterator.next());
				//文件不为空上传
				if(Clientfile !=null && Clientfile.getSize()>0){
					/** 客户端原始文件名 */
					clientfileName = Clientfile.getOriginalFilename();
					/** 文件后缀 **/
					filesuffix = clientfileName.substring(clientfileName.lastIndexOf("."));
					//原图
				    basefileName=uuid+"_"+count+filesuffix;
					Clientfile.transferTo(new File(path+basefileName));
					//获取图片宽高
					BufferedImage bufferedImage = ImageIO.read(new File(path+basefileName));   
					int width = bufferedImage.getWidth()/3;   
					int height = bufferedImage.getHeight()/3;   
					 //裁剪图片,压缩图片
				    CommonUtil.cutImage2(width, height,path+basefileName,path+uuid+"_"+count+"@cut"+filesuffix,50.0);
				}	
				count++;
			}
			resultImg=dirPath+imgpath+"/"+sdf+uuid+"_"+(count-1);
			resultSuffix=filesuffix;
		}
		System.out.println(resultImg+"#"+resultSuffix);
		return resultImg+"#"+resultSuffix;
	}
	
	/**定制图上传
	 * 
	 * @param goodsType  商品属性1单面2双面3照片书
	 * @param imgFile   原图
	 * @param imgOrderFile   合成图
	 * @param imgFile2    背面原图
	 * @param imgOrderFile2   背面合成图
	 * @return
	 * @throws Exception
	 */
	public static String backANDFront(String goodsType,MultipartFile imgFile,MultipartFile imgOrderFile,MultipartFile imgFile2,MultipartFile imgOrderFile2) throws Exception{
			String pathName=new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
			//随机名
			String uuid=UUID.randomUUID().toString().replaceAll("-","").substring(0,8);
			/**  时间文件  **/
			File file=new File(updownloadRootDir+dirPath+pathName);
			String resultPath=pathName+"/"+uuid;
			String sufFormat=".jpg";
			String result="";
			//生成文件夹
			if(!file.exists()){
				file.mkdirs();
			}
			//原图
			if(imgFile!=null && !imgFile.isEmpty()){
				File imgFilePath=new File(updownloadRootDir+dirPath+resultPath+sufFormat);
				if(!imgFilePath.exists()){
					imgFilePath.createNewFile();
				}
				imgFile.transferTo(imgFilePath);
			}
			//定制图
			if(imgOrderFile!=null && !imgOrderFile.isEmpty()){
				File previewBackFilePath=new File(updownloadRootDir+dirPath+resultPath+"@b"+sufFormat);
				if(!previewBackFilePath.exists()){
					previewBackFilePath.createNewFile();
				}
				imgOrderFile.transferTo(previewBackFilePath);
			}
			//设定返回路径
			result=dirPath+resultPath+"#"+sufFormat;
			if(goodsType.equals("2")){
				//为后背定制图生成path
				//原图
				if(imgFile2!=null && !imgFile2.isEmpty()){
					File imgFilePath2=new File(updownloadRootDir+dirPath+resultPath+"@p"+sufFormat);
					if(!imgFilePath2.exists()){
						imgFilePath2.createNewFile();
					}
					imgFile2.transferTo(imgFilePath2);
				}
				//定制图
				if(imgOrderFile2!=null && !imgOrderFile2.isEmpty()){
					File previewBackFilePath=new File(updownloadRootDir+dirPath+resultPath+"@pb"+sufFormat);
					if(!previewBackFilePath.exists()){
						previewBackFilePath.createNewFile();
					}
					imgOrderFile2.transferTo(previewBackFilePath);
				}
				//设定返回路径
				result=dirPath+resultPath+"@pb#"+sufFormat;
			}
		return result;
	}
	
	/**
	 * 删除图片（原图+效果图）
	 * @param img  图片
	 * @param flag  状态
	 */
	public static void delWorks(String img,String suf,String flag)throws Exception{
		if(flag==null){
			if(img.contains("_")){
				String mm[]=img.split("_");
				int length=Integer.parseInt(mm[1]);
				File file=null;
				File file2=null;
				for(int i=1;i<=length;i++){
					file=new File(mm[0]+"_"+i+suf);
					file2=new File(mm[0]+"_"+i+"@ef"+suf);
					if(file.exists()){
						file.delete();
					}
					if(file2.exists()){
						file2.delete();
					}
				}
			}else{
				String arg[]={"","@p","@b","@pb"};
				for(int a=0;a<arg.length;a++){
					File delFile = new File(img+arg[a]+suf);
					if(delFile.exists()){
						delFile.delete();
					}
				}
			}
		}
	}
	
	/**订单定制图上传
	 * 
	 * @param goodsType  商品属性1单面2双面3照片书
	 * @param imgFile   原图
	 * @param imgOrderFile   合成图
	 * @param imgFile2    背面原图
	 * @param imgOrderFile2   背面合成图
	 * @return
	 * @throws Exception
	 */
	public static String orderBF(String goodsType,MultipartFile imgFile,MultipartFile imgOrderFile,MultipartFile imgFile2,MultipartFile imgOrderFile2,String orderNo) throws Exception{
			String pathName=new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
			/**  时间文件  **/
			File file=new File(updownloadRootDir+dirOrder+pathName);
			String resultPath=pathName+"/"+orderNo;
			String sufFormat=".jpg";
			String result="";
			//生成文件夹
			if(!file.exists()){
				file.mkdirs();
			}
			//原图
			if(imgFile!=null && !imgFile.isEmpty()){
				File imgFilePath=new File(updownloadRootDir+dirOrder+resultPath+sufFormat);
				if(!imgFilePath.exists()){
					imgFilePath.createNewFile();
				}
				imgFile.transferTo(imgFilePath);
			}
			//定制图
			if(imgOrderFile!=null && !imgOrderFile.isEmpty()){
				File previewBackFilePath=new File(updownloadRootDir+dirOrder+resultPath+"@p"+sufFormat);
				if(!previewBackFilePath.exists()){
					previewBackFilePath.createNewFile();
				}
				imgOrderFile.transferTo(previewBackFilePath);
			}
			//设定返回路径
			result=dirOrder+resultPath+"#"+sufFormat;
			if(goodsType.equals("2")){
				//原图
				if(imgFile2!=null && !imgFile2.isEmpty()){
					File imgFilePath2=new File(updownloadRootDir+dirOrder+resultPath+"@b"+sufFormat);
					if(!imgFilePath2.exists()){
						imgFilePath2.createNewFile();
					}
					imgFile2.transferTo(imgFilePath2);
					
					//@b同时生成不带的
					InputStream input=null;
					OutputStream output=null;
					
					File originFile=new File(updownloadRootDir+dirOrder+resultPath+sufFormat);
					if(!originFile.exists()){
						try{
							
							input=new FileInputStream(updownloadRootDir+dirOrder+resultPath+"@b"+sufFormat);
							output=new FileOutputStream(updownloadRootDir+dirOrder+resultPath+sufFormat);
							byte [] buf= new byte[1024];
							int bytesRead;
							while((bytesRead=input.read(buf))>0){
								output.write(buf, 0,bytesRead);
							}
						}finally{
							input.close();
							output.close();
						}
					
						}
					}
				
				//定制图
				if(imgOrderFile2!=null && !imgOrderFile2.isEmpty()){
					File previewBackFilePath=new File(updownloadRootDir+dirOrder+resultPath+"@pb"+sufFormat);
					if(!previewBackFilePath.exists()){
						previewBackFilePath.createNewFile();
					}
					imgOrderFile2.transferTo(previewBackFilePath);
				}
			}
		return result;
	}
	
	//验证是否有文件上传
	public static boolean isFile(HttpServletRequest req){
		CommonsMultipartResolver resolver = new CommonsMultipartResolver(req.getSession().getServletContext());
		if(resolver.isMultipart(req)){
			MultipartHttpServletRequest multipart = (MultipartHttpServletRequest)req;
			if(!multipart.getFileMap().isEmpty())
				return true;
		}
		return false;
	}
	
	/**
	 * 订单批量上传照片书
	 * @param req   http图片请求
	 * @param imgpath   图片根目录
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public static String orderBatchImg(HttpServletRequest req,String imgpath) throws Exception{
		String clientfileName="",filesuffix="",path="",basefileName="",resultImg="",resultSuffix="";
		/**  时间文件  **/
		String sdf=new SimpleDateFormat("yyyyMMdd").format(new Date())+"/";
		path=updownloadRootDir+dirOrder+imgpath+"/"+sdf;
		File file =new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		CommonsMultipartResolver resolver = new CommonsMultipartResolver(req.getSession().getServletContext());
		//是否存在文件上传请求
		if(resolver.isMultipart(req)){
			//转换多文件请求
			MultipartHttpServletRequest multipart = (MultipartHttpServletRequest)req;
			//所有文件名读取到迭代器
			Iterator<String>iterator = multipart.getFileNames();
			//随机名
			String uuid=UUID.randomUUID().toString().replaceAll("-","").substring(0,8);
			int count=1;
			while(iterator.hasNext()){
				MultipartFile Clientfile = multipart.getFile(iterator.next());
				//文件不为空上传
				if(Clientfile !=null && Clientfile.getSize()>0){
					/** 客户端原始文件名 */
					clientfileName = Clientfile.getOriginalFilename();
					/** 文件后缀 **/
					filesuffix = clientfileName.substring(clientfileName.lastIndexOf("."));
					//原图
					basefileName=uuid+"_"+count+filesuffix;
					Clientfile.transferTo(new File(path+basefileName));
					//获取图片宽高
					BufferedImage bufferedImage = ImageIO.read(new File(path+basefileName));   
					int width = bufferedImage.getWidth()/3;   
					int height = bufferedImage.getHeight()/3;   
					 //裁剪图片,压缩图片
				    CommonUtil.cutImage2(width, height,path+basefileName,path+uuid+"_"+count+"@cut"+filesuffix,50.0);
				}	
				count++;
			}
			resultImg=dirOrder+imgpath+"/"+sdf+uuid+"_"+(count-1);
			resultSuffix=filesuffix;
		}
		return resultImg+"#"+resultSuffix;
	}
	
	public static List<Map<String,Object>> getActivityMethod(Map<String,Object>activityMap,Map<String,Object>companyMap){
		List<Map<String,Object>>list=new ArrayList<Map<String,Object>>();
		Map<String,Object>result=null;
		Map<Integer,Double>middle=null;
		if(companyMap==null){
			return null;
		}
		
		
		for(String key : companyMap.keySet()){
			//活动详情
			Map<String,Object>actMap=(Map<String, Object>)companyMap.get(key);
			if(actMap==null){
				continue;
			}
			
			Integer type=actMap.get("type")!=null?(Integer)actMap.get("type"):0;
			String companyId=actMap.get("companyId")!=null?actMap.get("companyId").toString():"0";
			Integer actid=actMap.get("id")!=null?(Integer)actMap.get("id"):0;
			
			String num[];
			String price[];
			String param="";
			boolean flag=false;
			int count=0,maxNum=1,amount=0;
			double calculate=0.00,money=0.00,disCount=0.00,resPrice=0.00;
			switch(type){
			   case 1 : 
				   //获取商品数量和价格
				 result=new HashMap<String,Object>();
				 if(activityMap.get("Num"+type+"_"+companyId)==null || activityMap.get("Price"+type+"_"+companyId)==null){
					 break;
				 }
				 num=activityMap.get("Num"+type+"_"+companyId).toString().split(",");
			   	 price=activityMap.get("Price"+type+"_"+companyId).toString().split(",");
			   	 double desPrice=Double.valueOf(actMap.get("des_price").toString());
			   	 double orgPrice=Double.valueOf(actMap.get("org_price").toString());
			   	 for(int i=0;i<num.length;i++){
			   		 int n=Integer.parseInt(num[i]);
			   		 double p=Double.valueOf(price[i]);
			   		 calculate+=n*p;
			   	 }
			   	 //判断是否优惠
			   	 if(calculate>=orgPrice){
			   		 result.put("isCoupon", 1);
			   		money=calculate-desPrice;
			   		result.put("discount", desPrice);
			   	 }else{
			   		 result.put("isCoupon",0);
			   		 money=calculate;
			   		result.put("discount", 0);
			   	 }
			   	 result.put("desPrice",desPrice);
			   	 result.put("orgPrice",orgPrice);
				 result.put("original",calculate);
				 result.put("nowPrice", money);
				 result.put("about",actMap.get("about"));
				 result.put("type",type);
				 result.put("satisfy",actMap.get("isAbout"));
				 result.put("id",actid);
//				 result.put("desAbout","已减免"+desPrice+"优惠");
				 list.add(result);
				 break;
			   case 2:
				   //获取商品数量和价格
				     result=new HashMap<String,Object>();
				     if(activityMap.get("Num"+type+"_"+companyId)==null || activityMap.get("Price"+type+"_"+companyId)==null){
						 break;
					 }
					 num=activityMap.get("Num"+type+"_"+companyId).toString().split(",");
				   	 price=activityMap.get("Price"+type+"_"+companyId).toString().split(",");
				     param=actMap.get("param1").toString();
				     //优惠时候需要的数量
				     amount=(Integer)actMap.get("num");
				 //冒泡排序
				 if(param.equals("half")){
					 for(int i=0;i<price.length;i++){
						 count+=Integer.parseInt(num[i]);
				   		 for(int j=0;j<price.length;j++){
				   			 if(Double.valueOf(price[i])<Double.valueOf(price[j])){
				   				String temp=price[i];
				   				String temp2=num[i];
				   				
				   				price[i]=price[j];
				   				num[i]=num[j];
				   				
				   				price[j]=temp;
				   				num[j]=temp2;
				   			 }
				   		 }
				   	 }
					 //计算优惠价格，按最便宜的先半价
					 resPrice=0.00;
					 int cc=1;
					 int nnl=count/amount;
					 System.err.println("num.length:"+num.length);
					for(int k=0;k<num.length;k++){
						int size=Integer.parseInt(num[k]);
						for(int p=0;p<size;p++){
							calculate+=Double.valueOf(price[k]);
							//当基数时候多出的那个数不参与打折半价
							if(cc<=nnl){
								resPrice+=Double.valueOf(price[k])/2;
								System.out.println("price="+price[k]);
							}else{
//								//半价时候的处理,只有相同价格的才能半价处理
//								if(k!=0 && price[k-1].equals(price[k]))
//									resPrice+=Double.valueOf(price[k])/2;
//								else if(k==0 && size>1)
//									resPrice+=Double.valueOf(price[k])/2;
//								else
//									resPrice+=Double.valueOf(price[k]);
								System.out.println("price2="+price[k]);
								resPrice+=Double.valueOf(price[k]);
							}
							cc++;
							System.err.println("cc:"+cc);
						}	
					}
					//判断是否优惠
				   	 if(count>=amount){
				   		 result.put("isCoupon", 1);
				   		result.put("discount",calculate-resPrice);
				   	 }else{
				   		 result.put("isCoupon",0);
				   		result.put("discount",0);
				   	 }
					 result.put("original",calculate);
					 result.put("nowPrice", resPrice);
					 //满足条件以后的文案
					 result.put("about",actMap.get("about"));
					 //满足的数量
					 result.put("condition",count/2);
					 result.put("privilege",0.5);
					 result.put("item",2);
					 result.put("type",type);
					 result.put("satisfy",actMap.get("isAbout"));
					 result.put("id",actid);
//					 result.put("desAbout","已享受"+count/2+"商品半价");
					 list.add(result);
					 //第二件免单
				 }else if(param.equals("avoid")){
					 for(int i=0;i<price.length;i++){
						 count+=Integer.parseInt(num[i]);
				   		 for(int j=0;j<price.length;j++){
				   			 if(Double.valueOf(price[i])>Double.valueOf(price[j])){
				   				String temp=price[i];
				   				String temp2=num[i];
				   				
				   				price[i]=price[j];
				   				num[i]=num[j];
				   				
				   				price[j]=temp;
				   				num[j]=temp2;
				   			 }
				   		 }
				   	 }
					 //计算优惠价格，按最便宜的先免单
					 resPrice=0.00;
					 int c=1;
					for(int k=0;k<num.length;k++){
						int size=Integer.parseInt(num[k]);
						for(int p=0;p<size;p++){
							calculate+=Double.valueOf(price[k]);
							if(c>count/2){
//								resPrice+=Double.valueOf(price[k]); 
//								//半价时候的处理,只有相同价格的才能免第二个处理
//								if(k!=0 && price[k-1].equals(price[k]))
//									resPrice+=Double.valueOf(price[k]);
//								else if(k==0 && size>1)
//									resPrice+=Double.valueOf(price[k])/2;
//								else
//									resPrice+=Double.valueOf(price[k]);
								resPrice+=Double.valueOf(price[k])/2;
							}else{
								
							}
							c++;
						}	
					}
					//判断是否优惠
				   	 if(count>=2){
				   		result.put("isCoupon", 1);
				   		result.put("discount",calculate-resPrice);
				   	 }else{
				   		result.put("isCoupon",0);
				   		result.put("discount",0);
				   	 }
					 result.put("original",calculate);
					 result.put("nowPrice", resPrice);
					 result.put("about",actMap.get("about"));
					 result.put("condition",count/2);
					 result.put("item",2);
					 result.put("privilege",0.5);
					 result.put("type",type);
					 result.put("satisfy",actMap.get("isAbout"));
					 result.put("id",actid);
//					 result.put("desAbout","已享受免单"+count/2+"商品");
					 list.add(result);
				 }
				 break;
			   case 4 : 
				    //获取商品数量和价格
				    result=new HashMap<String,Object>();
				    calculate=0.00;
				    if(activityMap.get("Num"+type+"_"+companyId)==null || activityMap.get("Price"+type+"_"+companyId)==null){
						 break;
					 }
				    num=activityMap.get("Num"+type+"_"+companyId).toString().split(",");
				    price=activityMap.get("Price"+type+"_"+companyId).toString().split(",");
				    money=Double.valueOf(actMap.get("money").toString());
				         //冒泡排序
						 for(int i=0;i<price.length;i++){
							 count+=Integer.parseInt(num[i]);
					   		 for(int j=0;j<price.length;j++){
					   			 if(Double.valueOf(price[i])>Double.valueOf(price[j])){
					   				String temp=price[i];
					   				String temp2=num[i];
					   				
					   				price[i]=price[j];
					   				num[i]=num[j];
					   				
					   				price[j]=temp;
					   				num[j]=temp2;
					   			 }
					   		 }
					   	 }
						 //计算优惠价格，按最便宜的作为优惠
						 resPrice=0.00;
						 int c=1;
						for(int k=0;k<num.length;k++){
							int size=Integer.parseInt(num[k]);
							for(int p=0;p<size;p++){
								calculate+=Double.valueOf(price[k]);
								if(c>count/3){
									resPrice=money*(count/3);
								}else{
									resPrice+=Double.valueOf(price[k]);
								}
								c++;
							}	
						}
						//判断是否优惠
					   	 if(count>=3){
					   		 result.put("isCoupon", 1);
					   		 result.put("discount",calculate-resPrice);
					   	 }else{
					   		 result.put("isCoupon",0);
					   		result.put("discount",0);
					   	 }
					   	 result.put("maxCondition",money);
						 result.put("original",calculate);
						 result.put("nowPrice", resPrice);
						 result.put("condition",3);
						 result.put("item",3);
						 result.put("type",type);
						 result.put("about",actMap.get("about"));
						 result.put("satisfy",actMap.get("isAbout"));
						 result.put("id",actid);
//						 result.put("desAbout","已获得三件99元优惠");
						 list.add(result);
						 break;
			   case  8:
				   //获取商品数量和价格
				     result=new HashMap<String,Object>();
				     middle=new HashMap<Integer,Double>();
				     if(activityMap.get("Num"+type+"_"+companyId)==null || activityMap.get("Price"+type+"_"+companyId)==null){
						 break;
					 }
					 num=activityMap.get("Num"+type+"_"+companyId).toString().split(",");
				   	 price=activityMap.get("Price"+type+"_"+companyId).toString().split(",");
				     param=actMap.get("param1").toString();
				     String discount[]=param.split(",");
				     for(int i=0;i<num.length;i++){
				    	 count+=Integer.parseInt(num[i]);
				    	 int n=Integer.parseInt(num[i]);
				   		 double p=Double.valueOf(price[i]);
				   		 calculate+=n*p;
				     }
				     	//获取最大活动件数
				    	 for(int i=0;i<discount.length;i++){
					    	 Integer disNum=Integer.parseInt(discount[i].split("_")[0]);
					    	 Double disRate=Double.valueOf((discount[i].split("_")[1]));
					    	 middle.put(disNum, disRate);
					    	 maxNum=disNum;
				         }
				    //冒泡排序
				    for(int i=0;i<price.length;i++){
				   		 for(int j=0;j<price.length;j++){
				   			 if(Double.valueOf(price[i])>Double.valueOf(price[j])){
				   				String temp=price[i];
				   				String temp2=num[i];
				   				
				   				price[i]=price[j];
				   				num[i]=num[j];
				   				
				   				price[j]=temp;
				   				num[j]=temp2;
				   			 }
				   		 }
				   	 }
					 //计算优惠价格，按最便宜的先半价
				    resPrice=0.00;
				    
					 int b=1;
					for(int k=0;k<num.length;k++){
						int size=Integer.parseInt(num[k]);
						for(int p=0;p<size;p++){
							//计算最大优惠
							if(count>=maxNum){
								resPrice+=Double.valueOf(price[k])*(middle.get(maxNum)==null?1:middle.get(maxNum));
							}else{
								resPrice+=Double.valueOf(price[k]);
							}
							b++;
						}	
					}
					//判断是否优惠
				   	 if(count>=maxNum){
				   		 result.put("isCoupon", 1);
				   		 result.put("discount", calculate-resPrice);
				   	 }else{
				   		 result.put("isCoupon",0);
				   		 result.put("discount",0);
				   	 }
					 result.put("original",calculate);
					 result.put("nowPrice", resPrice);
					 result.put("maxCondition",maxNum);
					//最大活动方案
					 result.put("privilege",Double.valueOf(middle.get(maxNum)==null?"1.00":middle.get(maxNum).toString()));
					 if(count!=0){
						 result.put("minCondition",count%maxNum);
						 //最小活动方案
						 if(count%maxNum!=0)
						 result.put("minPrivilege",middle.get(count%maxNum)==null?1:middle.get(maxNum));
						 else
						 result.put("minPrivilege",0);
					 }
					 result.put("type",type);
					 result.put("item",maxNum);
					 result.put("about",actMap.get("about"));
					 result.put("satisfy",actMap.get("isAbout"));
					 result.put("id",actid);
					 list.add(result);
					 break;
			   default: 	result=new HashMap<String,Object>();
				   				num=activityMap.get("Num"+type+"_"+companyId).toString().split(",");
			   					price=activityMap.get("Price"+type+"_"+companyId).toString().split(",");
			   					for(int i=0;i<num.length;i++){
			   						count+=Integer.parseInt(num[i]);
			   						int n=Integer.parseInt(num[i]);
			   						double p=Double.valueOf(price[i]);
			   						calculate+=n*p;
			   					}
			   					result.put("nowPrice", calculate);
			   					result.put("discount", 0);
			   					list.add(result);
			}
		}
		if(list.isEmpty())
		return null;
		return list;
	}
	
	/**
	 * TODO 裁剪图片
	 * @param width
	 * @param height
	 * @param srcPath
	 * @param newPath
	 */
	public static void cutImage(int width, int height, String srcPath,
			String newPath){
		IMOperation op = new IMOperation();
		op.addImage(srcPath);
		op.resize(width, height, "!");
		
		op.addImage(newPath);
		File dir = new File(newPath).getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		ConvertCmd convert = new ConvertCmd();
		// linux下不要设置此值，不然会报错
		if(!isLinux){convert.setSearchPath(imageMagickPath);}
		try {
			convert.run(op);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IM4JavaException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * TODO 裁剪图片2 裁剪以后再缩放比例
	 * @param width
	 * @param height
	 * @param srcPath
	 * @param newPath
	 * * @param size 清晰度
	 */
	public static void cutImage2(int width, int height, String srcPath,
			String newPath,double size){
		IMOperation op = new IMOperation();
		op.addImage(srcPath);
		op.resize(width, height, "!");
		//压缩图片大小
		op.quality(size);
		
		op.addImage(newPath);
		File dir = new File(newPath).getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		ConvertCmd convert = new ConvertCmd();
		// linux下不要设置此值，不然会报错
		if(!isLinux){convert.setSearchPath(imageMagickPath);}
		try {
			convert.run(op);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IM4JavaException e) {
			e.printStackTrace();
		}
	}
	
    /** 
     * 将图片写入
     * @param img 图片数据流 
     * @param fileName 文件保存时的名称 
     */  
    public static void writeImageToDisk(byte[] img, String fileName){  
        try {  
            File file = new File(fileName);  
            FileOutputStream fops = new FileOutputStream(file);  
            fops.write(img);  
            fops.flush();  
            fops.close();  
            System.out.println("图片已经写入到C盘");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    /** 
     * 根据地址获得数据的字节流 
     * @param strUrl 网络连接地址 
     * @return 
     */  
    public static byte[] getImageFromNetByUrl(String strUrl){  
        try {  
            URL url = new URL(strUrl);  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
            conn.setRequestMethod("GET");  
            conn.setConnectTimeout(5 * 1000);  
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据  
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据  
            return btImg;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
    /** 
     * 从输入流中获取数据 
     * @param inStream 输入流 
     * @return 
     * @throws Exception 
     */  
    public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        while( (len=inStream.read(buffer)) != -1 ){  
            outStream.write(buffer, 0, len);  
        }  
        inStream.close();  
        return outStream.toByteArray();  
    }
    
    /**
     * 
     * TODO 讲中文乱码进行转码 
     * @param s
     * @return
     * @author yuanqi 2017年2月24日 上午9:58:44
     */
    public static String encodeStr(String s){
    	if (s != null) {
    		if(!(java.nio.charset.Charset.forName("GBK").newEncoder().canEncode(s))){  
		    	//如果是中文乱码，则转码
		    	try {
					s = new String(s.getBytes("ISO-8859-1"),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}  
		    }     
		}
    	return s;
    }
    
    public static void main(String[] args) {
    	//获取图片宽高
		try {
			BufferedImage bufferedImage = ImageIO.read(new File("C:/Users/Administrator/Desktop/图片素材/32.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}
	
}

