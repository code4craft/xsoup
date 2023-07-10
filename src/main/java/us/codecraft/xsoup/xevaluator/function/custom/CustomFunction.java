package us.codecraft.xsoup.xevaluator.function.custom;

import org.jsoup.select.Evaluator;
import us.codecraft.xsoup.xevaluator.XPathParser;

import java.util.function.Function;

/**
 * 自定义函数
 * @author liu xw
 * @date 2023 07-09
 */
public interface CustomFunction extends XPathParser.FunctionEvaluator {

    /**
     * 自定义函数匹配方法
     */
   Boolean matching(String function);
}
