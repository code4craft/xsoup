package us.codecraft.xsoup.xevaluator.function.contains;

/**
 * @author liu xw
 * @date 2023 07-09
 */

import org.jsoup.select.Evaluator;
import us.codecraft.xsoup.xevaluator.function.custom.AbstractCustomFunction;

public abstract class AbstractContainsCustomFunction extends AbstractCustomFunction {

    @Override
    public Boolean matching(String function) {
        return function.startsWith(this.getCustomFunction());
    }

    public AbstractContainsCustomFunction(String customFunction) {
        super(ContainsFunctionEvaluator.CONTAINS_FUNCTION_OBJECT, customFunction);
    }

    @Override
    public Evaluator call(String... param) {
        if (param == null || param.length < 2){
            return null;
        }
        // decideFunction -> 解析使用的函数. context -> 匹配的内容信息
        String decideFunction = param[0], context = param[1];
        if (decideFunction.startsWith(getCustomFunction())){
            // 截取路由信息
            decideFunction = decideFunction.replaceFirst(getCustomFunction(), "");
        }
        return call2(decideFunction, context);
    }

    protected abstract Evaluator call2(String decideFunction, String context);
}