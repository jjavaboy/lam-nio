package lam.study.reactive;

import java.util.function.Consumer;

/**
 * @author: linanmiao
 */
public class FluxMain {

    public static void main(String[] args) {
        Flux.just(1, 2, 3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer i) {
                        System.out.println("onNextConsumer" + " element:" + i);
                    }
                },
               new Consumer<Throwable>() {
                   @Override
                   public void accept(Throwable t) {
                       System.out.println("onErrorConsumer" + " throwable:" + t.getMessage());
                   }
               },
                () -> {
                    System.out.println("onCompleteRunnable" + " Complete!");
                });
    }

}
