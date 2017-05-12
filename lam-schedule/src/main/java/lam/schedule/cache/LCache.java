package lam.schedule.cache;
/**
* <p>
* cache
* </p>
* @author linanmiao
* @date 2017年5月11日
* @version 1.0
*/
public interface LCache {
	
	public String get(String key);
	
	public void set(String key, String value);
	
	public void remove(String key);
	
	public boolean contains(String key);
	
	public void init();

	public void reload();
	
	public void clear();
	
}
