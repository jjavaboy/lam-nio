package org.lam.redis.gentle;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author linanmiao
 * @version 1.0.0
 * @since 1.0.0
 */
public class JedisCloseable implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisCloseable.class);

    private static final long CLOSE_TIMEOUT_MILLIS = 30000L;

    private List<Closeable> closeables;

    public List<Closeable> getCloseables() {
        return closeables;
    }

    public void setCloseables(final List<Closeable> closeables) {
        this.closeables = closeables;
    }

    public void close() throws IOException {
        LOGGER.info("[close] Closeable size: {} start==>>", this.sizeOf(this.closeables));
        if (this.closeables != null) {
            Field field = null;
            final String executorServiceField = "executorService";
            try {
                field = FieldUtils.getField(Closeable.class, executorServiceField, true);
            } catch (final Exception e) {
                LOGGER.error("[close] failed", e);
            }
            if (field == null) {
                LOGGER.info("[close] field:{} of executorService is null", executorServiceField);
                return ;
            }
            final Iterator<Closeable> iter = this.closeables.iterator();
            while (iter.hasNext()) {
                final Closeable closeable = iter.next();
                try {
                    final ExecutorService executorService = (ExecutorService) FieldUtils.readField(field, closeable, true);
                    if (executorService.isTerminated()) {
                        LOGGER.info("[close] executorService:{} has been terminated before, remove this executorService now.", closeable);
                        iter.remove();
                    } else {
                        executorService.shutdown();
                    }
                } catch (final Exception e) {
                    LOGGER.error("[close] failed", e);
                }
            }
            long endMillis = System.currentTimeMillis() + CLOSE_TIMEOUT_MILLIS;
            final Iterator<Closeable> iter1 = this.closeables.iterator();
            while (iter1.hasNext()) {
                final Closeable closeable = iter1.next();
                try {
                    final ExecutorService executorService = (ExecutorService) FieldUtils.readField(field, closeable, true);
                    if (executorService.isTerminated()) {
                        LOGGER.info("[close] executorService:{} has been terminated before do nothing.", executorService);
                        iter1.remove();
                        continue ;
                    }
                    endMillis = endMillis - System.currentTimeMillis();
                    if (endMillis <= 0) {
                        executorService.shutdownNow();
                        LOGGER.info("[close] executorService:{} shutdown time has run out, so shutdownNow.", executorService);
                        iter1.remove();
                        continue ;
                    }
                    executorService.shutdown();
                    if (executorService.isTerminated()) {
                        LOGGER.info("[close] executorService:{} terminate successful.", executorService);
                        iter1.remove();
                        continue ;
                    }
                    final boolean result = executorService.awaitTermination(endMillis, TimeUnit.MILLISECONDS);
                    LOGGER.info("[close] executorService:{} try to terminate in {} ms, result:{}.", executorService, endMillis, result);
                    if (!result) {
                        LOGGER.info("[close] executorService:{} shutdown time has run out before, so shutdownNow.", executorService);
                        executorService.shutdownNow();
                    }
                    iter1.remove();
                } catch (final Exception e) {
                    LOGGER.error("[close] failed", e);
                }
            }
        }
        LOGGER.info("[close] end==>>");
    }

    private int sizeOf(Collection<?> c) {
        return c == null ? 0 : c.size();
    }
}
