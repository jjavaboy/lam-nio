package lam.concurrent.lock.support;
/**
* <p>
* file operate
* </p>
* @author linanmiao
* @date 2017年4月26日
* @versio 1.0
* @see java.io.RandomAccessFile.RandomAccessFile(File, String)
*/
public enum FileOperate {
	/**
	 * Open for reading only. Invoking any of the write methods of 
	 * the resulting object will cause an java.io.IOException to be thrown. 
	 */
	R("r"),
	
	/**
	 * Open for reading and writing. If the file does not already exist 
	 * then an attempt will be made to create it. 
	 */
	RW("rw"),
	
	/**
	 * Open for reading and writing, as with "rw", and also require that 
	 * very update to the file's content or metadata be written synchronously to the underlying storage device. 
	 */
	RWS("rws"),
	
	/**
	 * Open for reading and writing, as with "rw", and also require that 
	 * every update to the file's content be written synchronously to the underlying storage device. 
	 */
	RWD("rwd");
	
	private String mode;
	
	private FileOperate(String mode){
		this.mode = mode;
	}
	
	public String getMode() {
		return mode;
	}

}
