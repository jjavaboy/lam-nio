package org.lam.hongbao.core.model.http;

import java.io.Serializable;

import org.lam.hongbao.core.constant.Status;

import com.google.gson.Gson;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年1月20日
* @version 1.0
*/
public class ResponseData implements Serializable{

	private static final long serialVersionUID = -1864901711551866592L;
	
	private Gson gson = new Gson();

	private int status;
	
	private Object data;

	public ResponseData(){}
	
	public ResponseData(int status, Object data){
		this.status = status;
		this.data = data;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public static ResponseData crateOK(){
		return new ResponseData(Status.HttpResponse.OK.getValue(), Status.HttpResponse.OK.getDescribe());
	}
	
	public String toString(){
		return toJson();
	}
	
	public String toJson(){
		return gson.toJson(this);
	}
	
}
