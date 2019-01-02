package lam.study.reactive.support;

/**
 * @author: linanmiao
 */
public interface Publisher<T> {

    public void subscribe(Subscriber<? super T> subscriber);

}
