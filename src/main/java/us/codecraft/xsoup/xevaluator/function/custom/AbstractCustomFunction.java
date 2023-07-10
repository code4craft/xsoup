package us.codecraft.xsoup.xevaluator.function.custom;

import lombok.Getter;

/**
 * TODO 可以优化call方法
 * @author liu xw
 * @date 2023 07-09
 */
public abstract class AbstractCustomFunction implements CustomFunction {

    /**
     * 用来绑定同一个类型的object
     */
    @Getter
    private final Object FUNCTION_OBJEzCT;

    /**
     * 自定义函数路由信息
     */
    @Getter
    private final String customFunction;

    public AbstractCustomFunction(Object obj, String customFunction) {
        this.FUNCTION_OBJEzCT = obj;
        this.customFunction = customFunction;
    }
}
