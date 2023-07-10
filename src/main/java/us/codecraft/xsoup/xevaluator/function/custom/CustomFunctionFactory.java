package us.codecraft.xsoup.xevaluator.function.custom;

import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义函数工厂
 * @author liu xw
 * @date 2023 07-09
 */
public class CustomFunctionFactory {

    /**
     * 缓存自定义函数信息
     */
    private static final Map<Object, List<CustomFunction>> customFunctionCache = new ConcurrentHashMap<>();

    /**
     * 获取自定义函数信息
     */
    public static CustomFunction getCustomFunction(String decideFunction){
        return null;
    }


    /**
     * 获取自定义函数信息
     */
    public static List<CustomFunction> getCustomFunction(Object keyObj){
        return customFunctionCache.getOrDefault(keyObj, Collections.emptyList());
    }

    public static void putCustomFunction(Object obj, CustomFunction function){
        if (customFunctionCache.containsKey(obj)){
            customFunctionCache.get(obj).add(function);
        }
        synchronized (obj){
            if (customFunctionCache.containsKey(obj)){
                customFunctionCache.get(obj).add(function);
                return;
            }
            List<CustomFunction> functionList = new ArrayList<>();
            functionList.add(function);
            customFunctionCache.put(obj, functionList);
        }

    }

    /**
     * 自定义工厂来源
     */
    public static class CustomFunctionFactorySource{
        @SneakyThrows
        public CustomFunctionFactorySource() {
            for (Class<?> declaredClass : this.getClass().getDeclaredClasses()) {
                if (AbstractCustomFunction.class.isAssignableFrom(declaredClass)){
                    AbstractCustomFunction function = (AbstractCustomFunction) declaredClass.getConstructor().newInstance();
                    putCustomFunction(function.getFUNCTION_OBJEzCT(), function);
                }
            }
        }
    }
}
