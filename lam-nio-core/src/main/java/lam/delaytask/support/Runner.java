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
	
	public Segment getSegment(int index);
	
	public void doTask();
	
	public boolean isClose();

}
