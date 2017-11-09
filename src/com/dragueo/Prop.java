package com.dragueo;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/*
 * http://www.mkyong.com/java/java-properties-file-examples/
 */

public class Prop {
	private static Properties ret = null;
	
	public static void saveProp(Properties prop) {	
		OutputStream output = null;
		
		try {
			output = new FileOutputStream("config.properties");
			
			prop.store(output, null);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Properties loadProp() {
		if(ret != null) {
			return ret;
		}
		
		ret = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream("config.properties");
			ret.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return ret;
	}
	
	public static Properties reloadProp() {
		ret = null;
		return loadProp();
	}
}
