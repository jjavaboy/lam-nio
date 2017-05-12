package lam.schedule.cache.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.concurrent.lock.LFileLock;
import lam.concurrent.lock.support.FileOperate;
import lam.schedule.cache.LCache;
import lam.util.FileUtil;
import lam.util.FinalizeUtils;

/**
* <p>
* file cache
* </p>
* @author linanmiao
* @date 2017年5月11日
* @version 1.0
*/
public class FileCache implements LCache{
	
	private static Logger logger = LoggerFactory.getLogger(FileCache.class);
	
	private File file;
	
	private Properties properties;
	
	private String lockFile;
	
	public FileCache(File file){
		Objects.requireNonNull(file, "file can't be null");
		this.file = file;
		this.lockFile = this.file.getAbsolutePath() + ".lock";
		properties = new Properties();
	}
	
	public FileCache(String filename){
		this(filename == null ? null : new File(filename));
	}

	@Override
	public void init() {
		final File f = this.file;
		boolean rs = new LFileLock(this.lockFile, FileOperate.RW){
			@Override
			protected boolean execute() {
				FileInputStream fileInputStream = null;
				try {
					if(!f.exists()){
						f.createNewFile();
					}
					fileInputStream = new FileInputStream(f);
					properties.load(fileInputStream);
					return true;
				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					FinalizeUtils.closeQuietly(fileInputStream);
				}
			}
		}.lockAndRun();
		logger.info("init, load file:{}, rs:{}", f, rs);
		
		for(Object key : properties.keySet()){
			logger.info(String.format("%s=%s", key, properties.getProperty(String.valueOf(key))));
		}
	}

	@Override
	public void reload() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		if(properties != null){
			properties.clear();
		}
	}

	@Override
	public String get(String key) {
		return properties.getProperty(key);
	}

	@Override
	public void set(String key, String value) {
		properties.setProperty(key, value);
		restore();
	}
	
	@Override
	public void remove(String key) {
		properties.remove(key);
		restore();
	}
	
	@Override
	public boolean contains(String key) {
		return properties.containsKey(key);
	}
	
	private void restore(){
		final Properties p = this.properties;
		final File f = this.file;
		boolean rs = new LFileLock(this.lockFile, FileOperate.RW){
			@Override
			protected boolean execute() {
				if(f.exists()){
					f.delete();
					try {
						f.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				}
				return FileUtil.storeToFile(p, f);
			}
		}.lockAndRun();
		logger.info("restore properties to file:{} ==>> result:{}", this.file.getAbsolutePath(), rs);
	}

}
