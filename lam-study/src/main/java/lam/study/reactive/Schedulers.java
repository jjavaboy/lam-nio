package lam.study.reactive;

import java.util.concurrent.Executors;

/**
 * @author: linanmiao
 */
public class Schedulers {

    private static final Scheduler PARALLEL_SCHEDULER =
            new Scheduler(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

    public static Scheduler parallel() {
        return PARALLEL_SCHEDULER;
    }

}
