package us.codecraft.xsoup.xevaluator.function;

import org.jsoup.select.Evaluator;
import org.junit.Test;
import us.codecraft.xsoup.constant.ContainsFunctionConstant;
import us.codecraft.xsoup.xevaluator.function.contains.ContainsFunctionEvaluator;

/**
 * @author liu xw
 * @date 2023 07-10
 */
public class ContainsFunctionEvaluatorTest {

    private ContainsFunctionEvaluator evaluator = new ContainsFunctionEvaluator();

    @Test
    public void testCall() {
        Evaluator call = evaluator.call(ContainsFunctionConstant.DATA_ALL + "test", "test");
        System.out.println(call);
    }
}