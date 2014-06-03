package us.codecraft.xsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import us.codecraft.xsoup.xevaluator.XPathEvaluator;
import us.codecraft.xsoup.xevaluator.XPathParser;

/**
 * @author code4crafter@gmail.com
 */
public class W3cXsoup {

    public static XElements select(Element element, String xpathStr) {
        return XPathParser.parse(xpathStr).evaluate(element);
    }

    public static XElements select(String html, String xpathStr) {
        return XPathParser.parse(xpathStr).evaluate(Jsoup.parse(html));
    }

    public static XPathEvaluator compile(String xpathStr) {
        return XPathParser.parse(xpathStr);
    }

}
