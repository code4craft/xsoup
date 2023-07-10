package us.codecraft.xsoup.xevaluator.function;

import org.jsoup.helper.Validate;
import org.jsoup.select.Evaluator;
import us.codecraft.xsoup.XTokenQueue;
import us.codecraft.xsoup.xevaluator.XPathParser;
import us.codecraft.xsoup.xevaluator.function.contains.ContainsFunctionEvaluator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liu xw
 * @date 2023 07-09
 */
public class FunctionFactory {

    /**
     * 工厂缓存信息
     */
    private static final Map<String, XPathParser.FunctionEvaluator> cache = new ConcurrentHashMap<>();


    static {
        // 新的 - 包含方法
        cache.put("contains", new ContainsFunctionEvaluator());


        // 旧的保留
        cache.put("starts-with", new XPathParser.FunctionEvaluator() {
            @Override
            public Evaluator call(String... param) {
                Validate.isTrue(param.length == 2, String.format("Error argument of %s", "starts-with"));
                // decideFunction -> 解析使用的函数. context -> 匹配的内容信息
                String decideFunction = param[0], context = param[1];
                if (decideFunction.startsWith("@")) {
                    decideFunction = decideFunction.replaceFirst("@", "");
                }
                return new Evaluator.AttributeWithValueStarting(decideFunction, context);
            }
        });
        cache.put("ends-with", new XPathParser.FunctionEvaluator() {
            @Override
            public Evaluator call(String... param) {
                Validate.isTrue(param.length == 2, String.format("Error argument of %s", "ends-with"));
                String decideFunction = param[0], context = param[1];
                if (decideFunction.startsWith("@")) {
                    decideFunction = decideFunction.replaceFirst("@", "");
                }
                return new Evaluator.AttributeWithValueEnding(decideFunction, context);
            }
        });
    }

    /**
     * 获取函数信息
     * @param queue
     * @return
     */
    public static XPathParser.FunctionEvaluator getFunction(XTokenQueue queue){
        // 判断是否允许使用函数
        for (String function : cache.keySet()) {
            if (queue.matchChomp(function)) {
                // 允许使用函数
                return cache.get(function);
            }
        }
        return null;
    }

}
