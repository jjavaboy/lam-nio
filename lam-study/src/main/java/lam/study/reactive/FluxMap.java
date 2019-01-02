package lam.study.reactive;

import java.util.function.Function;

import lam.study.reactive.support.Subscriber;
import lam.study.reactive.support.Subscription;

/**
 * @author: linanmiao
 */
public class FluxMap<K, E> extends Flux<E>{

    private Flux<K> source;

    private Function<? super K, ? extends E> transform;

    public FluxMap(Flux<K> source, Function<? super K, ? extends E> transform) {
        this.source    = source;
        this.transform = transform;
    }

    @Override
    public void subscribe(Subscriber<? super E> subscriber) {
        super.subscribe(subscriber);
    }

    /*private class MapSubscriber<K, E> extends Subscriber<K> {

        private Subscriber<? super E> downStreamSubscriber;

        private Function<? super K, ? extends E> transform;

        public MapSubscriber(Subscriber<? super E> downStreamSubscriber, Function<? super K, ? extends E> transform) {
            this.downStreamSubscriber = downStreamSubscriber;
            this.transform            = transform;
        }

        @Override
        public void onSubscribe(Subscription subscription) {
            this.downStreamSubscriber.onSubscribe(subscription);
        }

        @Override
        public void onNext(K k) {
            this.downStreamSubscriber.onNext(transform.apply(k));
        }

        @Override
        public void onError(Throwable t) {
            this.downStreamSubscriber.onError(t);
        }

        @Override
        public void onComplete() {
            this.downStreamSubscriber.onComplete();
        }
    }*/
}
