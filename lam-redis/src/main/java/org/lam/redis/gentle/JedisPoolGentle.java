package org.lam.redis.gentle;

import java.net.URI;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;

/**
 * jedis ~ version 2.9.0
 * @author linanmiao
 * @version 1.0.0
 * @since 1.0.0
 */
public class JedisPoolGentle extends JedisPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisPoolGentle.class);

    private List<JedisCloseable> closeableList;

    public JedisPoolGentle() {
        super();
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, String host) {
        super(poolConfig, host);
    }

    public JedisPoolGentle(String host, int port) {
        super(host, port);
    }

    public JedisPoolGentle(String host) {
        super(host);
    }

    public JedisPoolGentle(String host, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(host, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPoolGentle(URI uri) {
        super(uri);
    }

    public JedisPoolGentle(URI uri, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(uri, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPoolGentle(URI uri, int timeout) {
        super(uri, timeout);
    }

    public JedisPoolGentle(URI uri, int timeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(uri, timeout, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password) {
        super(poolConfig, host, port, timeout, password);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, boolean ssl) {
        super(poolConfig, host, port, timeout, password, ssl);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, host, port, timeout, password, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, String host, int port) {
        super(poolConfig, host, port);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, String host, int port, boolean ssl) {
        super(poolConfig, host, port, ssl);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, String host, int port, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, host, port, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, String host, int port, int timeout) {
        super(poolConfig, host, port, timeout);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, boolean ssl) {
        super(poolConfig, host, port, timeout, ssl);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, host, port, timeout, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database) {
        super(poolConfig, host, port, timeout, password, database);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database, boolean ssl) {
        super(poolConfig, host, port, timeout, password, database, ssl);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, host, port, timeout, password, database, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database, String clientName) {
        super(poolConfig, host, port, timeout, password, database, clientName);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database, String clientName, boolean ssl) {
        super(poolConfig, host, port, timeout, password, database, clientName, ssl);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, host, port, timeout, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, String host, int port, int connectionTimeout, int soTimeout, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, host, port, connectionTimeout, soTimeout, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, URI uri) {
        super(poolConfig, uri);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, URI uri, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, uri, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, URI uri, int timeout) {
        super(poolConfig, uri, timeout);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, URI uri, int timeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, uri, timeout, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, URI uri, int connectionTimeout, int soTimeout) {
        super(poolConfig, uri, connectionTimeout, soTimeout);
    }

    public JedisPoolGentle(GenericObjectPoolConfig poolConfig, URI uri, int connectionTimeout, int soTimeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(poolConfig, uri, connectionTimeout, soTimeout, sslSocketFactory, sslParameters, hostnameVerifier);
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
                } catch (Exception e) {
                    LOGGER.error("close {} failed", closeable, e);
                }
            }
        }
        super.destroy();
    }
}
