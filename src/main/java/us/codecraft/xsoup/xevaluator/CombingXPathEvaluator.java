package us.codecraft.xsoup.xevaluator;

import org.jsoup.nodes.Element;
import us.codecraft.xsoup.XPathEvaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author code4crafter@gmail.com
 */
public class CombingXPathEvaluator implements XEvaluatorPathEvaluator {

    private List<XEvaluatorPathEvaluator> xPathEvaluators;

    public CombingXPathEvaluator(List<XEvaluatorPathEvaluator> xPathEvaluators) {
        this.xPathEvaluators = xPathEvaluators;
    }

    public CombingXPathEvaluator(XEvaluatorPathEvaluator... xPathEvaluators) {
        this.xPathEvaluators = Arrays.asList(xPathEvaluators);
    }

    @Override
    public XEvaluatorElements evaluate(Element element) {
        List<XEvaluatorElements> xElementses = new ArrayList<XEvaluatorElements>();
        for (XEvaluatorPathEvaluator xPathEvaluator : xPathEvaluators) {
            xElementses.add(xPathEvaluator.evaluate(element));
        }
        return new CombiningDefaultXElements(xElementses);
    }

    @Override
    public boolean hasAttribute() {
        for (XPathEvaluator xPathEvaluator : xPathEvaluators) {
            if (xPathEvaluator.hasAttribute()){
                return true;
            }
        }
        return false;
    }
}
