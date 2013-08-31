package us.codecraft.xsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 * @author code4crafter@gmail.com
 */
public class Xsoup {

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
