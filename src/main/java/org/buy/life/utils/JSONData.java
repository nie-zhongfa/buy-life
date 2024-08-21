package org.buy.life.utils;

import lombok.Data;

@Data
public class JSONData<T> {
    private T data;
    private String code;
    private String msg;

    /**
     * 若没有数据返回，默认状态码为0，提示信息为：操作成功！
     */
    public JSONData() {
        this.code = "0";
        this.msg = "success";
    }

    /**
     * 若没有数据返回，可以人为指定状态码和提示信息
     * @param code
     * @param msg
     */
    public JSONData(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 有数据返回时，状态码为0，默认提示信息为：操作成功！
     * @param data
     */
    public JSONData(T data) {
        this.data = data;
        this.code = "0";
        this.msg = "success";
    }

    /**
     * 有数据返回，状态码为0，人为指定提示信息
     * @param data
     * @param msg
     */
    public JSONData(T data, String msg) {
        this.data = data;
        this.code = "0";
        this.msg = msg;
    }

    public static <T> JSONData<T> success() {
        return new JSONData<>();
    }

    public static <T> JSONData<T> success(T data) {
        return new JSONData(data);
    }
    // 省略get和set方法
}