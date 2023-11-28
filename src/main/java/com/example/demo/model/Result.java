package com.example.demo.model;



public class Result {

    // 正确返回代码
    private static final int SUCCESS_CODE = 200;
    // 错误返回代码
    private static final int ERROR_CODE = -200;

    // 状态代码
    private int code;
    // 信息
    private String message;
    // 内容
    private Object data;

    public static int getSuccessCode() {
        return SUCCESS_CODE;
    }

    public static int getErrorCode() {
        return ERROR_CODE;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result() {
    }

    public static Result ok(Object data) {
        return ok("OK", data);
    }

    public static Result ok(String msg, Object data) {
        return new Result(SUCCESS_CODE, msg, data);
    }

    public static Result error(Object data) {
        return error("ERROR", data);
    }

    public static Result error(String msg, Object data) {
        return new Result(ERROR_CODE, msg, data);
    }

}
