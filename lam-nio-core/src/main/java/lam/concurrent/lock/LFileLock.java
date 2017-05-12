package lam.concurrent.lock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

import lam.concurrent.lock.support.FileOperate;
import lam.log.Console;
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
	
	private static final int DEFAULT_RETRY = 3;
	
	protected int retry = DEFAULT_RETRY;//default time
	
	protected final File file;
	
	protected final FileOperate fileOperate;
	
	/**
	 * retry 3 times by default
	 */
	public LFileLock(String file, FileOperate fileOperate){
		this(file, fileOperate, DEFAULT_RETRY);
	}
	
	public LFileLock(String file, FileOperate fileOperate, int retry){
		this(file == null ? null : new File(file), fileOperate, retry);
	}
	
	/**
	 * retry 3 times by default
	 */
	public LFileLock(File file, FileOperate fileOperate){
		this(file, fileOperate, DEFAULT_RETRY);
	}
	
	public LFileLock(File file, FileOperate fileOperate, int retry){
		this.file = file;
		this.fileOperate = fileOperate;
		this.retry = retry;
	}
	
	public boolean lockAndRun(){
		RandomAccessFile randomAccessFile = null;
		try {
			if(!this.file.exists()){
				this.file.createNewFile();
			}
			randomAccessFile = new RandomAccessFile(this.file, this.fileOperate.getMode());
			FileChannel fileChannel = randomAccessFile.getChannel();
			try {
				//lock..
				FileLock fileLock = fileChannel.tryLock();
				if(fileLock == null){
					throw new OverlappingFileLockException();
				}
				try{
					boolean result = false;
					while(!(result = execute()) && --retry > 1);
					return result;
				}finally{
					//unlock..
					fileLock.release();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} catch (OverlappingFileLockException e0){
				Console.println(this.file.getAbsolutePath() + " has been locked by other thread.");
				throw e0;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
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
