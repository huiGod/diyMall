package cn._51app.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn._51app.dao.IEvaluationDao;
import cn._51app.entity.EvaluationInfo;
import cn._51app.entity.Evaluations;
import cn._51app.service.BaseService;
import cn._51app.service.IEvaluationService;
import cn._51app.util.BASE64;
import cn._51app.util.OCSKey;
import cn._51app.util.PropertiesUtil;
import cn._51app.util.ImageTools;

@Service
public class EvaluationService extends BaseService implements IEvaluationService {
	
	@Autowired
	private IEvaluationDao dao;
	
	private final String SYSPATH =PropertiesUtil.getValue("uploadUrl.sys");
	private final String EVAPATH =PropertiesUtil.getValue("evaluation.path");
	private final String EVALNUM =PropertiesUtil.getValue("evaluation.num");
	private final String EVALPICNUM =PropertiesUtil.getValue("evaluation.pic.num");
	private final String updownloadRootDir =PropertiesUtil.getValue("uploadUrl.sys");
	
	/**
	 * @author zhanglz
	 * @return 保存评论
	 */
	@Override
	public boolean saveEvaluation(Evaluations evaluations){
		try {
			Map<String,Object> map = dao.getMobile4Order(evaluations.getOrderNo());
			String mobile = null==map.get("mobile")?"":map.get("mobile").toString();
			String texture_names = null==map.get("texture_names")?"":map.get("texture_names").toString();
			String[] textureArr = texture_names.split("\\|");
			List<EvaluationInfo> list = evaluations.getList();
			for (int i = 0; i < list.size(); i++) {
				String text = "";
				if(textureArr.length>i){
					text = textureArr[i];
				}
				saveEvaluationResolve(list.get(i),mobile,text);
			}
			dao.updateEvaStatus(evaluations.getOrderNo());
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	
	/**
	 * @author zhanglz
	 * @return 保存单个评论
	 */
	private void saveEvaluationResolve(EvaluationInfo info,String mobile,String texture) throws Exception{
		String imgres = "";
		if(!StringUtils.isBlank(info.getImgUrl())){
			String[] imgs = info.getImgUrl().split(",");
			for (int i = 0; i < imgs.length; i++) {
				String fileName=System.currentTimeMillis()+"";
				String pathName=new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
				File file=new File(SYSPATH+EVAPATH+pathName);
				if(!file.exists()){
					file.mkdirs();
				}
				BASE64.uploadPicture(imgs[i], SYSPATH+EVAPATH+pathName+"/"+fileName+"@b.jpg");
				ImageTools.cutImage(180, 180, SYSPATH+EVAPATH+pathName+"/"+fileName+"@b.jpg", SYSPATH+EVAPATH+pathName+"/"+fileName+".jpg");
				imgres+=EVAPATH+pathName+"/"+fileName+".jpg"+",";
			}
			imgres=imgres.substring(0, imgres.length()-1);
		}else{
			imgres=null;
		}
		info.setImgUrl(imgres);
		dao.saveEvaluation(info,mobile,texture);
	}
	
	/**
	 * @author zhanglz
	 * @return 查询评论类型的数量
	 */
	@Override
	public String getEvalTypeNum(int goodsId) throws Exception{
		String cacheKey = OCSKey.DIY_EVALTYPENUM+goodsId;
		super.del(cacheKey);
		Map<String,Object> paramMap =new HashMap<String,Object>();
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime",10);
		paramMap.put("goodsId", goodsId);
		return dao.getEvalTypeNum(paramMap);
	}
	
	/**
	 * @author zhanglz
	 * @return 查询评论的图片
	 */
	@Override
	public String findEvalPic(int page,Integer goodsId) throws Exception{
		page-=1;
		String cacheKey = OCSKey.DIY_EVALPIC+goodsId+page;
		Map<String,Object> paramMap =new HashMap<String,Object>();
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", 10);
		paramMap.put("page", page*(Integer.parseInt(EVALPICNUM)));
		paramMap.put("pagesize", Integer.parseInt(EVALPICNUM));
		paramMap.put("goodsId", goodsId);
		return dao.findEvalPic(paramMap);
	}
	
	/**
	 * @author zhanglz
	 * @return 分页查询评论
	 */
	@Override
	public String findAllEval(int page,int goodsId,Integer evalType) throws Exception{
		page-=1;
		String cacheKey = OCSKey.DIY_EVALPIC+goodsId+page+evalType;
		del(cacheKey);
		Map<String,Object> paramMap =new HashMap<String,Object>();
		paramMap.put("cacheKey", cacheKey);
		paramMap.put("cacheTime", 10);
		paramMap.put("page", page*(Integer.parseInt(EVALNUM)));
		paramMap.put("pagesize", Integer.parseInt(EVALNUM));
		paramMap.put("goodsId", goodsId);
		paramMap.put("evalType", evalType);
		return dao.findAllEval(paramMap);
	}
	
	
	@Override
	public String saveImg(MultipartFile imgFile){
		String resultPath=null;
		try {
			String pathName=new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
			String fileName=System.currentTimeMillis()+"";
			File file=new File(SYSPATH+EVAPATH+pathName);
			String saveUrl=SYSPATH+EVAPATH+pathName+"/"+fileName+".jpg";
			if(!file.exists()){
				file.mkdirs();
			}
			if(imgFile!=null && !imgFile.isEmpty()){
				File imgFilePath=new File(saveUrl);
				if(!imgFilePath.exists()){
					imgFilePath.createNewFile();
				}
				imgFile.transferTo(imgFilePath);
				resultPath = EVAPATH+pathName+"/"+fileName+".jpg";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultPath;
	}
	
	/**
	 * @author zhanglz
	 * @return 保存评论(Android)
	 */
	@Override
	public boolean saveEvaluation2(Evaluations evaluations){
		try {
			Map<String,Object> map = dao.getMobile4Order(evaluations.getOrderNo());
			String mobile = null==map.get("mobile")?"":map.get("mobile").toString();
			String texture_names = null==map.get("texture_names")?"":map.get("texture_names").toString();
			String[] textureArr = texture_names.split("\\|");
			List<EvaluationInfo> list = evaluations.getList();
			for (int i = 0; i < list.size(); i++) {
				String text = "";
				if(textureArr.length>i){
					text = textureArr[i];
				}
				dao.saveEvaluation(list.get(i),mobile,text);
			}
			dao.updateEvaStatus(evaluations.getOrderNo());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
