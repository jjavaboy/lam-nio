package lam.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

/**
* <p>
* operate file 
* </p>
* @author linanmiao
* @date 2017年5月12日
* @version 1.0
*/
public class FileUtil {
	
	public static boolean storeToFile(Properties properties, String filename){
		return storeToFile(properties, filename == null ? null : new File(filename));
	}
	
	public static boolean storeToFile(Properties properties, File file){
		OutputStreamWriter outputStreamWriter = null;
		try {
			outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
			for(Object key : properties.keySet()){
				outputStreamWriter.write(String.format("%s=%s", key, properties.getProperty(String.valueOf(key))));
				outputStreamWriter.write("\n");
			}
			outputStreamWriter.flush();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			FinalizeUtils.closeQuietly(outputStreamWriter);
		}
		return false;
	}
	
	public static Properties getProperties(String filename){
		Properties p = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = FileUtil.class.getResourceAsStream(filename);
			p.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if(inputStream != null)
				FinalizeUtils.closeQuietly(inputStream);
		}
		return p;
	}

}
