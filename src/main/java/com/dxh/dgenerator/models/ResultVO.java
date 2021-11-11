package com.dxh.dgenerator.models;

import lombok.Data;

/**
 * TODO
 *
 * @author xuhong.ding
 * @since 2021/1/26 15:11
 */
@Data
public class ResultVO<T> {
    /**
     * 状态码，比如1000代表响应成功
     */
    private int code;
    /**
     * 响应信息，用来说明响应情况
     */
    private String msg;
    /**
     * 响应的具体数据
     */
    private T data;

    public ResultVO(T data) {
        this(ResultCode.SUCCESS, data);
    }

    public static ResultVO<String> fail(String msg){
        return new ResultVO<String>(ResultCode.FAILED,msg);
    }

    public ResultVO(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
        this.data = data;
    }

}
