package lam.netty.http.json.model;

import io.netty.handler.codec.http.FullHttpResponse;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月10日
* @versio 1.0
*/
public class HttpJsonResponse {
	
	private FullHttpResponse response;
	
	private Object body;
	
	public HttpJsonResponse(){}
	
	public HttpJsonResponse(FullHttpResponse response, Object body){
		this.response = response;
		this.body = body;
	}

	public FullHttpResponse getResponse() {
		return response;
	}

	public void setResponse(FullHttpResponse response) {
		this.response = response;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
	
}
