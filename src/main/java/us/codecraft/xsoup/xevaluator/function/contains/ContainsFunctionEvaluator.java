package us.codecraft.xsoup.xevaluator.function.contains;

import org.jsoup.select.Evaluator;
import us.codecraft.xsoup.constant.ContainsFunctionConstant;
import us.codecraft.xsoup.xevaluator.XPathParser;
import us.codecraft.xsoup.xevaluator.function.custom.CustomFunction;
import us.codecraft.xsoup.xevaluator.function.custom.CustomFunctionFactory;

import java.util.List;


/**
 * 包含函数求值程序
 * @author liu xw
 * @date 2023 07-09
 */
public class ContainsFunctionEvaluator extends CustomFunctionFactory.CustomFunctionFactorySource implements XPathParser.FunctionEvaluator {

    /**
     * 唯一值
     */
    public static final Object CONTAINS_FUNCTION_OBJECT = new Object();

    @Override
    public Evaluator call(String... param) {
        // decideFunction -> 解析使用的函数. context -> 匹配的内容信息
        final String decideFunction = param[0], context = param[1];

        // 获取自定义函数工厂信息
        List<CustomFunction> functionCache = CustomFunctionFactory.getCustomFunction(CONTAINS_FUNCTION_OBJECT);
        if (functionCache.isEmpty()){
            return null;
        }

        CustomFunction executeFunction = null;
        for (CustomFunction customFunction : functionCache) {
            if (customFunction.matching(decideFunction)) {
                executeFunction = customFunction;
                break;
            }
        }
        if (executeFunction == null){
            return null;
        }

        return executeFunction.call(decideFunction, context);
    }


    /**
     * {@link Evaluator.ContainsOwnText}
     */
    public static class ContainsOwnText extends AbstractContainsCustomFunction {


        public ContainsOwnText() {
            super(ContainsFunctionConstant.TEXT_ONE);
        }

        @Override
        protected Evaluator call2(String decideFunction, String context) {
            // 查询文本
            return new Evaluator.ContainsOwnText(context);
        }
    }

    /**
     * {@link Evaluator.ContainsText}
     */
    public static class ContainsAllText extends AbstractContainsCustomFunction {

        public ContainsAllText() {
            super(ContainsFunctionConstant.TEXT_ALL);
        }

        @Override
        protected Evaluator call2(String decideFunction, String context) {
            return new Evaluator.ContainsText(context);
        }
    }

    /**
     * {@link Evaluator.ContainsData}
     */
    @Deprecated
    public static class ContainsAllData extends AbstractContainsCustomFunction {

        public ContainsAllData() {
            super(ContainsFunctionConstant.DATA_ALL);
        }

        @Override
        protected Evaluator call2(String decideFunction, String context) {
            return new Evaluator.ContainsData(context);
        }
    }

    /**
     * {@link Evaluator.AttributeWithValue}
     */
    public static class ContainsAttribute extends AbstractContainsCustomFunction {


        public ContainsAttribute() {
            super(ContainsFunctionConstant.ATTRIBUTE);
        }

        @Override
        protected Evaluator call2(String decideFunction, String context) {
            return new Evaluator.AttributeWithValue(decideFunction, context);
        }
    }

    /**
     * {@link Evaluator.AttributeWithValueNot}
     */
    public static class ContainsAttributeNot extends AbstractContainsCustomFunction {


        public ContainsAttributeNot() {
            super(ContainsFunctionConstant.ATTRIBUTE_NOT);
        }

        @Override
        protected Evaluator call2(String decideFunction, String context) {
            return new Evaluator.AttributeWithValueNot(decideFunction, context);
        }
    }

    /**
     * {@link Evaluator.AttributeWithValueStarting}
     */
    public static class ContainsAttributeStart extends AbstractContainsCustomFunction {


        public ContainsAttributeStart() {
            super(ContainsFunctionConstant.ATTRIBUTE_START);
        }

        @Override
        protected Evaluator call2(String decideFunction, String context) {
            return new Evaluator.AttributeWithValueStarting(decideFunction, context);
        }
    }
}
