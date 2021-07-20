package utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonUtils {
    //根据泛型返回解析制定的类型
    public static <T> T fromToJson(String json, Type listType){
        Gson gson = new Gson();
        T t = null;
        t = gson.fromJson(json,listType);
        return t;
    }
    
    
}
