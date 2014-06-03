package us.codecraft.xsoup.evaluator;

import org.jsoup.nodes.Element;
import us.codecraft.xsoup.nodes.XElements;

/**
 * @author code4crafter@gmail.com
 */
public interface XPathEvaluator {

    XElements evaluate(Element element);

    XElements evaluate(String html);

    boolean hasAttribute();

}
