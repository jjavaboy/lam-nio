package lam.pool.support;
/**
* <p>
* object pool config
* </p>
* @author linanmiao
* @date 2017年3月27日
* @version 1.0
*/
public class SObjectPoolConfig {

	//===============attribute, can be set================
	
	private int maxTotal = 8;
	private int maxIdle = 8;
	private int minIdle = 0;
	private long maxWaitMillis = -1;//wait forever
	private boolean blockWhenExhausted = Boolean.TRUE.booleanValue();
	private boolean testOnCreate = Boolean.FALSE.booleanValue();
	private boolean testOnBorrow = Boolean.FALSE.booleanValue();
	private boolean testOnReturn = Boolean.FALSE.booleanValue();
	private long timeBetweenEvictorRunsMillis = -1;
	private int numTestsPerEvictionRun = 3;
	private boolean testWhileIdle = Boolean.FALSE.booleanValue();
	private long minEvictableIdleTimeMillis = 30 * 60 * 1000;//half of an hour
	private boolean lifo = false;
	
	public SObjectPoolConfig(){}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}
	
	public int getMinIdle() {
		return minIdle;
	}
	
	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
	
	public int getNumTestsPerEvictionRun() {
		return numTestsPerEvictionRun;
	}
	
	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	public boolean isBlockWhenExhausted() {
		return blockWhenExhausted;
	}

	public void setBlockWhenExhausted(boolean blockWhenExhausted) {
		this.blockWhenExhausted = blockWhenExhausted;
	}

	public boolean isTestOnCreate() {
		return testOnCreate;
	}

	public void setTestOnCreate(boolean testOnCreate) {
		this.testOnCreate = testOnCreate;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public long getTimeBetweenEvictorRunsMillis() {
		return timeBetweenEvictorRunsMillis;
	}

	public void setTimeBetweenEvictorRunsMillis(long timeBetweenEvictorRunsMillis) {
		this.timeBetweenEvictorRunsMillis = timeBetweenEvictorRunsMillis;
	}
	
	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}
	
	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public boolean isLifo() {
		return lifo;
	}

	public void setLifo(boolean lifo) {
		this.lifo = lifo;
	}
	
	public long getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}
	
	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}
	
}
