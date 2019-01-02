package lam.study.reactive.support;

/**
 * @author: linanmiao
 */
public interface Subscription {

    public void request(long n);

    public void cancel();

}
