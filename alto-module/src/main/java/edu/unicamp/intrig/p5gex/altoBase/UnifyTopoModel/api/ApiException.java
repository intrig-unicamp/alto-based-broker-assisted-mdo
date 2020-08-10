package edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.api;

public class ApiException extends Exception{
	private int code;
	public ApiException (int code, String msg) {
		super(msg);
		this.code = code;
	}
}
