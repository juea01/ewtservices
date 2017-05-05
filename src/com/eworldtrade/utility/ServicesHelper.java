package com.eworldtrade.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ServicesHelper {

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

	
}
