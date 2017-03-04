package lam.dubbo.extension;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年3月5日
* @versio 1.0
*/
@DubboAdaptive
public class DubboAdaptiveCompiler implements DubboCompiler {

    private static volatile String DEFAULT_COMPILER;

    public static void setDefaultCompiler(String compiler) {
        DEFAULT_COMPILER = compiler;
    }

    public Class<?> compile(String code, ClassLoader classLoader) {
        DubboCompiler compiler;
        DubboExtensionLoader<DubboCompiler> loader = DubboExtensionLoader.getExtensionLoader(DubboCompiler.class);
        String name = DEFAULT_COMPILER; // copy reference
        if (name != null && name.length() > 0) {
            compiler = loader.getExtension(name);
        } else {
            compiler = loader.getDefaultExtension();
        }
        return compiler.compile(code, classLoader);
    }

}