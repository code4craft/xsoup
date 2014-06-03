package us.codecraft.xsoup.xevaluator;

import org.jsoup.nodes.Element;
import us.codecraft.xsoup.XPathEvaluator;

/**
 * @author code4crafer@gmail.com
 */
public interface XEvaluatorPathEvaluator extends XPathEvaluator {

    XEvaluatorElements evaluate(Element element);

}
