package lam.dao.id.model;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月10日
* @version 1.0
*/
public class Sequence implements Serializable{

	private static final long serialVersionUID = -1140960513897234827L;
	
	public static final int DEFAULT_STEP = 50;

	/**
	 * 名称，一般是表名或者Model名
	 */
	private String name;
	
	/**
	 * 步长
	 */
	private int step;
	
	/**
	 * 旧的值（含）
	 */
	private long oldValue;
	
	/**
	 * 最新的值（不含）
	 */
	private long newValue;
	
	private Date createdTime;
	
	private Date updatedTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public long getOldValue() {
		return oldValue;
	}

	public void setOldValue(long oldValue) {
		this.oldValue = oldValue;
	}

	public long getNewValue() {
		return newValue;
	}

	public void setNewValue(long newValue) {
		this.newValue = newValue;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
}
