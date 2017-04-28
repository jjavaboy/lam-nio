package lam.design.pattern.chainofresponsibility.rh;

import java.util.Objects;

import lam.log.Console;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年4月27日
* @versio 1.0
*/
public abstract class AbstractHandler {
	
	protected AbstractRequest.Level level;
	
	protected AbstractHandler next;
	
	public AbstractHandler(AbstractRequest.Level level){
		Objects.requireNonNull(level, "level of request cann't be null");
		this.level = level;
	}
	
	public final void run(AbstractRequest request){
		if(request == null){
			Console.println("request is null");
		}
		if(this.level == request.level){
			this.handle(request);
		}else{
			if(this.next != null){
				Console.println(getClass().getSimpleName() + " pass level:" + request.level);
				this.next.run(request);
			}else{
				Console.println(getClass().getSimpleName() + " next is null, teminated.");
			}
		}
	}
	
	public void setNext(AbstractHandler next){
		Objects.requireNonNull(next, "next handler cann't be null");
		this.next = next;
	}
	
	protected abstract void handle(AbstractRequest request);

}
