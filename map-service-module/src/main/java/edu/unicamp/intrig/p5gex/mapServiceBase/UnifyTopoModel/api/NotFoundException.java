package edu.unicamp.intrig.p5gex.mapServiceBase.UnifyTopoModel.api;

public class NotFoundException extends ApiException {
    private int code;
    public NotFoundException (int code, String msg) {
        super(code, msg);
        this.code = code;
    }
}
