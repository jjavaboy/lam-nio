package lam.dubbo.extension;
/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年3月4日
* @versio 1.0
*/
@DubboSPI("javassist")
public interface DubboCompiler {
	
	Class<?> compile(String code, ClassLoader classLoader);

}
