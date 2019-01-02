package lam.study.reactive.support;

/**
 * @author: linanmiao
 */
public interface Processor<S, P> extends Subscriber<S>, Publisher<P> {

}
