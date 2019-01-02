package lam.study.reactive;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import lam.study.reactive.support.Publisher;
import lam.study.reactive.support.Subscriber;
import lam.study.reactive.support.Subscription;

/**
 * Flux: 0...N elements<br/>
 * @author: linanmiao
 */
public class Flux<T> implements Publisher<T> {

    private List<T> data;

    public static <T> Flux<T> just(T... es) {
        Flux<T> flux = new Flux<T>();
        flux.data = Arrays.asList(es);
        return flux;
    }

    public <R> Flux<R> map(Function<? super T, ? extends R> transform) {
        Flux<R> flux = new FluxMap<>(this, transform);
        return flux;
    }

    public void subscribe(Consumer<? super T> onNextConsumer,
                          Consumer<? super Throwable> onErrorConsumer,
                          Runnable onCompleteRunnable) {
        this.subscribe(null, onNextConsumer, onErrorConsumer, onCompleteRunnable);
    }

    public void subscribe(Consumer<? super Subscription> onSubscribeConsumer,
                          Consumer<? super T> onNextConsumer,
                          Consumer<? super Throwable> onErrorConsumer,
                          Runnable onCompleteRunnable) {
        this.subscribe(new SupportSubscriber<T>(onSubscribeConsumer, onNextConsumer, onErrorConsumer, onCompleteRunnable));
    }

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        subscriber.onSubscribe(new SupportSubscription<>(data, subscriber));
    }
}
