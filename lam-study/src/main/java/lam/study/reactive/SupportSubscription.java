package lam.study.reactive;

import java.util.List;

import lam.study.reactive.support.Subscriber;
import lam.study.reactive.support.Subscription;

/**
 * @author: linanmiao
 */
public class SupportSubscription<T> implements Subscription {

    private List<T> data;

    private volatile boolean cancelled = false;

    private Subscriber<? super T> subscriber;

    public SupportSubscription(List<T> data, Subscriber<? super T> subscriber) {
        this.data = data;
        this.subscriber = subscriber;
    }

    @Override
    public void request(long n) {
        final int size = data.size();
        n = n > size ? size : n;
        for (int i = 0; i < n && !cancelled; i++) {
            subscriber.onNext(data.get(i));
        }
        subscriber.onComplete();
    }

    @Override
    public void cancel() {
        this.cancelled = true;
    }
}
