package lam.study.function;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author: linanmiao
 */
public class MyMain {

    public static void main(String[] args) {
        doFunction((Integer i) -> {
            Long r0 = 23L;
            System.out.println("apply Integer:" + i + ", Long:" + r0);
            return r0;
        }, 1);

        doConsumer((Long l) -> {
            System.out.println("accept Long:" + l);
        }, 3L);

        doPredicate((Long lo) -> {
            boolean r = false;
            System.out.println("test Long:" + lo + ", result:" + r);
            return r;
        }, 2L);

        doSupplier(() -> {
            Integer i = 10;
            System.out.println("get Integer:" + i);
            return i;
        });

        // BiXxxxx====

        Byte b = doBiFunction((Long lo, Integer in) -> {
            Byte by = (byte)1;
            System.out.println("bi apply, Long:" + lo + ", Integer:" + in + ", Byte:" + by);
            return by;
        }, 2L, 0);

        doBiConsumer((Long lo, Integer in) -> {
            System.out.println("bi consumer, Long:" + lo + ", Integer:" + in);
        }, 3L, 1);

        doBiPredicate((Long lo, Integer in) -> {
            boolean b0 = true;
            System.out.println("bi test, Long:" + lo + ", Integer:" + in + ", Boolean:" + b0);
            return b0;
        }, 4L, 2);
    }

    public static <T, R> R doFunction(Function<T, R> f, T t) {
        R r = f.apply(t);
        return r;
    }

    public static <T> void doConsumer(Consumer<T> c, T t) {
        c.accept(t);
    }

    public static <T> boolean doPredicate(Predicate<T> p, T t) {
        return p.test(t);
    }

    public static <T> T doSupplier(Supplier<T> s) {
        return s.get();
    }

    // BiXxxx==========

    public static <T, U, R> R doBiFunction(BiFunction<T, U, R> b, T t, U u) {
        R r = b.apply(t, u);
        return r;
    }

    public static <T, U> void doBiConsumer(BiConsumer<T, U> c, T t, U u) {
        c.accept(t, u);
    }

    public static <T, U> boolean doBiPredicate(BiPredicate<T, U> b, T t, U u) {
        return b.test(t, u);
    }

}
