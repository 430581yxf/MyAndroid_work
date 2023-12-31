package com.example.myapplicationui.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;
//返回数据实体类主要用来解析返回数据，因为返回数据的多样性所以统一一下格式
@Data
public class Res<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <T> Res<T> success(T object) {
        Res<T> r = new Res<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> Res<T> error(String msg) {
        Res r = new Res();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public Res<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
