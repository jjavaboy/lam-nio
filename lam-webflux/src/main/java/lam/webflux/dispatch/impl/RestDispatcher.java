package lam.webflux.dispatch.impl;

import lam.webflux.dispatch.Dispatcher;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @description: RestDispatcher
 * @author: linanmiao
 * @date: 2018/12/31 16:53
 * @version: 1.0
 */
@Service
public class RestDispatcher implements Dispatcher {

    private static final Logger LOGGER = Logger.getLogger(RestDispatcher.class);

    @Autowired
    private ReactorResourceFactory resourceFactory;

    @Override
    public Mono<ServerResponse> dispatch(ServerRequest serverRequest) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 0);
        map.put("message", "success");
        map.put("data", new HashMap<String, Object>());
        LOGGER.info("[dispatch] " + serverRequest + ", response:" +  map);

        WebClient webClient = WebClient.builder().clientConnector(new ReactorClientHttpConnector(resourceFactory, mapper()))
                //.baseUrl("http://www.baidu.com").build();
                .baseUrl("http://localhost:8090/").build();

        return webClient.method(serverRequest.method())
                .uri(serverRequest.path())
                .headers((HttpHeaders httpHeaders) -> {
                    httpHeaders.clear();
                    httpHeaders.putAll(serverRequest.headers().asHttpHeaders());
                })
                .exchange()
                .flatMap((ClientResponse clientResponse) -> {
                    return ServerResponse.status(clientResponse.statusCode())
                            .headers((HttpHeaders httpHeaders) -> {
                                httpHeaders.clear();
                                httpHeaders.putAll(clientResponse.headers().asHttpHeaders());
                            }).body(clientResponse.bodyToMono(String.class), String.class);
                });
    }

    private Function<HttpClient, HttpClient> mapper() {
        return (HttpClient httpClient) -> {

            return httpClient;
        };
    }
}
