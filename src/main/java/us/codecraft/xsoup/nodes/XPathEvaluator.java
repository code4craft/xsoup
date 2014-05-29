package us.codecraft.xsoup.nodes;

import org.jsoup.nodes.Element;
import us.codecraft.xsoup.evalutor.XElements;

/**
 * @author code4crafter@gmail.com
 */
public interface XPathEvaluator {

    XElements evaluate(Element element);

    XElements evaluate(String html);

    boolean hasAttribute();

}
