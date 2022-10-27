package org.rone.web.model.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 返回的数据结构
 * @author rone
 */
public class Result<T> {

    /**
     * {@link ResultCodeEnum}
     * @ApiModelProperty("状态值，0：成功；1：出错") 声明了swagger2文档中该参数的说明
     */
    @ApiModelProperty("返回码，详见 ResultCodeEnum ")
    private Integer code;

    /**
     * 在此说明错误的具体信息
     */
    @ApiModelProperty("在此说明错误的具体信息")
    private String message;

    /**
     * 返回的具体数据
     */
    @ApiModelProperty("返回的具体数据")
    private T data;

    public static Result success() {
        return new Result(ResultCodeEnum.SUCCESS.getCode(), "", null);
    }

    public static Result success(String message) {
        return new Result(ResultCodeEnum.SUCCESS.getCode(), message, null);
    }

    public static <T> Result success(String message, T data) {
        return new Result(ResultCodeEnum.SUCCESS.getCode(), message, data);
    }

    public static <T> Result success(T data) {
        return new Result(ResultCodeEnum.SUCCESS.getCode(),"",data);
    }

    public static Result fault(String message) {
        return new Result(ResultCodeEnum.ERROR.getCode(), message, null);
    }

    public static <T> Result fault(String message, T data) {
        return new Result(ResultCodeEnum.ERROR.getCode(), message, data);
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    /**
     * 返回码
     */
    public enum ResultCodeEnum {

        SUCCESS(0, "成功"),
        ERROR(1, "错误");

        private Integer code;
        private String message;

        ResultCodeEnum(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
