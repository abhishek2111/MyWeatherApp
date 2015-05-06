package com.example.newweatherapp.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;

public class RawCache {

	public static final String PREFIX = "Abhi_Maha";
	public static final String SEPERATOR = "_";
	public static final long RETENTION_TIME = 1000 * 60 * 10; // 10 min

	public static File getRoot(Context context) {
		return context.getFilesDir();
	}

	public static String generateKey(String type) {
		return PREFIX + SEPERATOR + type;
	}

	public static void cache(Context context, String type, String data) {
		if (context != null && data != null && !"".equals(data)) {
			File file = null;
			FileWriter writer = null;

			try {
				file = new File(getRoot(context), generateKey(type));
				writer = new FileWriter(file);
				writer.write(data);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}

	}
	
	public static String fromCache(Context context, String type){
		String data = null;
		
		if(context != null){
			File file = null;
			FileReader reader = null;
			BufferedReader buffer = null;
			
		
			try {
				file = new File(getRoot(context), generateKey(type));
				reader = new FileReader(file);
				buffer = new BufferedReader(reader);
				data = buffer.readLine();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					buffer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		return data;
	}
	
	public static boolean isInCache(Context context , String type){
		boolean isinCache = false;
		
		if(context!= null){
			File file = new File(getRoot(context), generateKey(type));
			
			if(file!=null && file.exists()){
				if(!Utilities.isConnected(context) || System.currentTimeMillis() - file.lastModified() < RETENTION_TIME) {
					isinCache = true;
				}else{
					 file.delete();
				}
			}
		}
		return isinCache;
	}

}
