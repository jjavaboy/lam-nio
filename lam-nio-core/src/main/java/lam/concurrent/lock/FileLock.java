package lam.concurrent.lock;

import java.io.File;
import java.io.IOException;

import lam.concurrent.lock.support.FileOperate;

/**
* <p>
* get a file lock
* </p>
* @author linanmiao
* @date 2017年4月26日
* @versio 1.0
*/
public abstract class FileLock {

	protected String filename;
	
	protected final File file;
	
	protected final FileOperate fileOperate;
	
	public FileLock(File file, FileOperate fileOperate){
		this.file = file;
		this.fileOperate = fileOperate;
	}
	
	public boolean lockAndRun(){
		//lock..
		return execute();
		//unlock..
	}
	
	protected abstract boolean execute();
	
	public static FileLock newFileReadWriteLock(File file){
		return null;
		//return new FileLock(file, FileOperate.RW);
	}
	
	public static FileLock newFileReadWriteLock(String filename) throws IOException{
		File file = new File(filename);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw e;
			}
		}
		return newFileReadWriteLock(file);
	}
	
}
