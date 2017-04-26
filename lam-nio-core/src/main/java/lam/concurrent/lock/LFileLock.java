package lam.concurrent.lock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import lam.concurrent.lock.support.FileOperate;
import lam.util.FinalizeUtils;

/**
* <p>
* get a file lock
* </p>
* @author linanmiao
* @date 2017年4月26日
* @versio 1.0
*/
public abstract class LFileLock {
	
	protected final File file;
	
	protected final FileOperate fileOperate;
	
	public LFileLock(File file, FileOperate fileOperate){
		this.file = file;
		this.fileOperate = fileOperate;
	}
	
	public boolean lockAndRun(){
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(this.file, this.fileOperate.getMode());
			FileChannel fileChannel = randomAccessFile.getChannel();
			try {
				//lock..
				FileLock fileLock = fileChannel.tryLock();
				if(fileLock == null){
					return false;
				}
				try{
					return execute();
				}finally{
					//unlock..
					fileLock.release();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} finally {
			FinalizeUtils.closeQuietly(randomAccessFile);
		}
	}
	
	protected abstract boolean execute();
	
	public static LFileLock newFileReadWriteLock(File file){
		return null;
		//return new FileLock(file, FileOperate.RW);
	}
	
	public static LFileLock newFileReadWriteLock(String filename) throws IOException{
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
