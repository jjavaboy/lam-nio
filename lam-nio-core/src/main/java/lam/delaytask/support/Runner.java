package lam.delaytask.support;
/**
* <p>
* This interface defines some method about task:<br/>
* </p>
* @author linanmiao
* @date 2017年4月12日
* @version 1.0
*/
public interface Runner {
	
	public boolean addTask(Segment.Task task);
	
	public Segment nextSegment();
	
	/**
	 * transfer second to segment slot of the runner
	 */
	public int second2SegmentSlot(int second);
	
	public int getCurrentIndex();
	
	public Segment getSegment(int index);
	
	public boolean remove(Segment segment);
	
	public void doTask();
	
	public boolean isClose();

}
