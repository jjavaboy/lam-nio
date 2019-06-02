package lam.study.reactive;

import java.util.function.Consumer;

import lam.study.reactive.support.Subscriber;
import lam.study.reactive.support.Subscription;

/**
 * @author: linanmiao
 */
public class SupportSubscriber<T> implements Subscriber<T> {

    private final Consumer<? super Subscription> onSubscribeConsumer;

    private final Consumer<? super T> onNextConsumer;

    private final Consumer<? super Throwable> onErrorConsumer;

    private final Runnable onCompleteRunnable;

    private Subscription subscription;

    public SupportSubscriber(Consumer<? super Subscription> onSubscribeConsumer,
                             Consumer<? super T> onNextConsumer,
                             Consumer<? super Throwable> onErrorConsumer,
                             Runnable onCompleteRunnable) {
        this.onSubscribeConsumer = onSubscribeConsumer;
        this.onNextConsumer      = onNextConsumer;
        this.onErrorConsumer     = onErrorConsumer;
        this.onCompleteRunnable  = onCompleteRunnable;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        if (this.onSubscribeConsumer == null) {
            subscription.request(Long.MAX_VALUE);
        } else {
            try {
                this.onSubscribeConsumer.accept(subscription);
            } catch (Throwable t) {
                this.subscription.cancel();
                this.onError(t);
            }
        }
    }

    @Override
    public void onNext(T t) {
        try {
            if (this.onNextConsumer != null) {
                this.onNextConsumer.accept(t);
            }
        } catch (Throwable th) {
            this.subscription.cancel();
            this.onError(th);
        }
    }

    @Override
    public void onError(Throwable t) {
        if (this.onErrorConsumer != null) {
            this.onErrorConsumer.accept(t);
        }
    }

    @Override
    public void onComplete() {
        if (this.onCompleteRunnable != null) {
            this.onCompleteRunnable.run();
        }
    }
}
