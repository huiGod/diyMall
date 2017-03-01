package cn._51app.util;

import java.io.File;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class CreateBarcodeByZxing {
	public static void CreateBarcode(String url,String path) throws Exception {
		int width = 280;
		int height = 280;
		String format = "png";
		Hashtable hints = new Hashtable();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
		bitMatrix=deleteWhite(bitMatrix);
		File outputFile = new File(path);
		if(!outputFile.exists()){
			outputFile.mkdirs();
		}
		MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
	}
	
	public static BitMatrix deleteWhite(BitMatrix matrix){  
	    int[] rec = matrix.getEnclosingRectangle();  
	    int resWidth = rec[2] + 1;  
	    int resHeight = rec[3] + 1;  
	  
	    BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);  
	    resMatrix.clear();  
	    for (int i = 0; i < resWidth; i++) {  
	        for (int j = 0; j < resHeight; j++) {  
	            if (matrix.get(i + rec[0], j + rec[1]))  
	                resMatrix.set(i, j);  
	        }  
	    }  
	    return resMatrix;  
	} 
}
