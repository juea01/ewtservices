package com.eworldtrade.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class ServicesHelper {
	
	static Properties pro = initialiseProperties();
	
	private static  Properties initialiseProperties() {
		try {
		InputStream settingsStream = ServicesHelper.class.getClassLoader().getResourceAsStream("setting.properties");
		Properties pro = new Properties();
		pro.load(settingsStream);
		return pro;
		} catch (Exception exc){
			exc.printStackTrace();
		}
		return null;
	}
	
	public static String getUploadFileLocation() { 
		return pro.getProperty("uploadImageFileLocation");
	}
	
	public static String getUrl() {
		return pro.getProperty("url");
	}

	public static void createFolderIfNotExists(String dirName)throws Exception {
		File theDir = new File(dirName);
		if(!theDir.exists()) {
			theDir.mkdir();
		}
	}
	
	public static void saveToFile(InputStream inStream, String target) throws IOException {
		FileOutputStream out = null;
		int read = 0;
		byte[] bytes = new byte[1024];
		
		out = new FileOutputStream(new File(target));
		while ((read = inStream.read(bytes)) != -1) {
			out.write(bytes,0,read);
		}
		out.flush();
		out.close();
	}
	
public static String convertToStringDate(Date date) {
	SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd");
	return mdyFormat.format(date);
}

	
}
