package lam.webflux.dispatch;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @description: Dispatcher
 * @author: linanmiao
 * @date: 2018/12/31 16:52
 * @version: 1.0
 */
public interface Dispatcher {

    public Mono<ServerResponse> dispatch(ServerRequest serverRequest);

}
