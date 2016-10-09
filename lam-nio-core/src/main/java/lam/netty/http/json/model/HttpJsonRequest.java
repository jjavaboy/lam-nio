package lam.netty.http.json.model;

import io.netty.handler.codec.http.FullHttpRequest;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月9日
* @versio 1.0
*/
public class HttpJsonRequest {
	
	private FullHttpRequest request;
	private Object body;
	
	public HttpJsonRequest(FullHttpRequest request, Object body) {
		this.request = request;
		this.body = body;
	}

	public FullHttpRequest getRequest() {
		return request;
	}

	public void setRequest(FullHttpRequest request) {
		this.request = request;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
	
}
