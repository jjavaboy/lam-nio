package org.lam.redis.gentle;

import java.util.List;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisSentinelPool;

/**
 * jedis ~ verson 2.9.0
 * @author linanmiao
 * @version 1.0.0
 * @since 1.0.0
 */
public class JedisSentinelPoolGentle extends JedisSentinelPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisSentinelPoolGentle.class);

    private List<JedisCloseable> closeableList;

    public JedisSentinelPoolGentle(final String masterName, final Set<String> sentinels, final GenericObjectPoolConfig poolConfig) {
        super(masterName, sentinels, poolConfig);
    }

    public JedisSentinelPoolGentle(final String masterName, final Set<String> sentinels) {
        super(masterName, sentinels);
    }

    public JedisSentinelPoolGentle(final String masterName, final Set<String> sentinels, final String password) {
        super(masterName, sentinels, password);
    }

    public JedisSentinelPoolGentle(final String masterName, final Set<String> sentinels, final GenericObjectPoolConfig poolConfig, final int timeout, final String password) {
        super(masterName, sentinels, poolConfig, timeout, password);
    }

    public JedisSentinelPoolGentle(final String masterName, final Set<String> sentinels, final GenericObjectPoolConfig poolConfig, final int timeout) {
        super(masterName, sentinels, poolConfig, timeout);
    }

    public JedisSentinelPoolGentle(final String masterName, final Set<String> sentinels, final GenericObjectPoolConfig poolConfig, final String password) {
        super(masterName, sentinels, poolConfig, password);
    }

    public JedisSentinelPoolGentle(final String masterName, final Set<String> sentinels, final GenericObjectPoolConfig poolConfig, final int timeout, final String password, final int database) {
        super(masterName, sentinels, poolConfig, timeout, password, database);
    }

    public JedisSentinelPoolGentle(final String masterName, final Set<String> sentinels, final GenericObjectPoolConfig poolConfig, final int timeout, final String password, final int database, final String clientName) {
        super(masterName, sentinels, poolConfig, timeout, password, database, clientName);
    }

    public JedisSentinelPoolGentle(final String masterName, final Set<String> sentinels, final GenericObjectPoolConfig poolConfig, final int timeout, final int soTimeout, final String password, final int database) {
        super(masterName, sentinels, poolConfig, timeout, soTimeout, password, database);
    }

    public JedisSentinelPoolGentle(final String masterName, final Set<String> sentinels, final GenericObjectPoolConfig poolConfig, final int connectionTimeout, final int soTimeout, final String password, final int database, final String clientName) {
        super(masterName, sentinels, poolConfig, connectionTimeout, soTimeout, password, database, clientName);
    }

    @Override
    public void close() {
        this.destroy();
    }

    @Override
    public void destroy() {
        if (this.closeableList != null && !this.closeableList.isEmpty()) {
            for (final JedisCloseable closeable : this.closeableList) {
                try {
                    closeable.close();
                } catch (final Exception e) {
                    LOGGER.error("close {} failed", closeable, e);
                }
            }
        }
        super.destroy();
    }
}
