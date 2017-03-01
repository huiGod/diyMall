package cn._51app.util;

import java.io.File;
import java.io.IOException;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

/**
 * ImageMagick和im4java处理图片
 * 
 * @author jonny
 */

public class ImageTools {

	/**
	 * 
	 * ImageMagick的路径
	 */

	public static String imageMagickPath = "D:/Program Files/ImageMagick-6.9.1-Q16";
	public static boolean isLinux = true;
	static {
		/**
		 * 
		 * 获取ImageMagick的路径
		 */
		// Properties prop = new PropertiesFile().getPropertiesFile();
		// linux下不要设置此值，不然会报错
		// imageMagickPath = prop.getProperty("imageMagickPath");
		String os = System.getProperty("os.name");  
		if(os.toLowerCase().startsWith("win")){  
			isLinux = false;
		} 
	}

	/**
	 * 
	 * 根据坐标裁剪图片
	 * 
	 * 
	 * 
	 * @param srcPath
	 *            要裁剪图片的路径
	 * 
	 * @param newPath
	 *            裁剪图片后的路径
	 * 
	 * @param x
	 *            起始横坐标
	 * 
	 * @param y
	 *            起始纵坐标
	 * 
	 * @param x1
	 *            结束横坐标
	 * 
	 * @param y1
	 *            结束纵坐标
	 */

	public static void cutImage(String srcPath, String newPath, int x, int y,
			int x1,
			int y1) throws Exception {
		int width = x1 - x;
		int height = y1 - y;
		IMOperation op = new IMOperation();
		op.addImage(srcPath);

		/**
		 * 
		 * width：裁剪的宽度
		 * 
		 * height：裁剪的高度
		 * 
		 * x：裁剪的横坐标
		 * 
		 * y：裁剪的纵坐标
		 */
		op.crop(width, height, x, y);
		op.addImage(newPath);
		ConvertCmd convert = new ConvertCmd();
		// linux下不要设置此值，不然会报错
		if(!isLinux){
			if(!isLinux){convert.setSearchPath(imageMagickPath);}
		}
		convert.run(op);

	}

	/**
	 * 
	 * 根据尺寸缩放图片
	 * 
	 * @param width
	 *            缩放后的图片宽度
	 * 
	 * @param height
	 *            缩放后的图片高度
	 * 
	 * @param srcPath
	 *            源图片路径
	 * 
	 * @param newPath
	 *            缩放后图片的路径
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
	 * 
	 * 根据宽度缩放图片
	 * 
	 * @param width
	 *            缩放后的图片宽度
	 * 
	 * @param srcPath
	 *            源图片路径
	 * 
	 * @param newPath
	 *            缩放后图片的路径
	 */

	public static void cutImage(int width, String srcPath, String newPath)
			throws Exception {
		IMOperation op = new IMOperation();
		op.addImage(srcPath);
		op.resize(width, null);
		op.addImage(newPath);
		File dir = new File(newPath).getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		ConvertCmd convert = new ConvertCmd();
		// linux下不要设置此值，不然会报错
		if(!isLinux){convert.setSearchPath(imageMagickPath);}
		convert.run(op);
	}

	/**
	 * 
	 * 给图片加水印
	 * 
	 * @param srcPath
	 *            源图片路径
	 */

	public static void addImgText(String srcPath) throws Exception {
		IMOperation op = new IMOperation();
		op.font("宋体").gravity("southeast").pointsize(18).fill("#BCBFC8").draw(
				"text 5,5 i4.cn");
		op.addImage();
		op.addImage();
		ConvertCmd convert = new ConvertCmd();
		// linux下不要设置此值，不然会报错
		if(!isLinux){convert.setSearchPath(imageMagickPath);}
		convert.run(op, srcPath, srcPath);

	}
}
