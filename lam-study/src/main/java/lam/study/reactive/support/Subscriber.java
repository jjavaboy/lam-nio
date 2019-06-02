package lam.study.reactive.support;

/**
 * @author: linanmiao
 */
public interface Subscriber<T> {

    public void onSubscribe(Subscription subscription);

    public void onNext(T t);

    public void onError(Throwable t);

    public void onComplete();

}
