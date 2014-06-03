package us.codecraft.xsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import us.codecraft.xsoup.w3c.W3cEvaluator;
import us.codecraft.xsoup.xevaluator.XPathParser;

/**
 * @author code4crafter@gmail.com
 */
public class Xsoup {

    /*-------------     XEvaluator         --------------- */

    public static XElements select(Element element, String xpathStr) {
        return XPathParser.parse(xpathStr).evaluate(element);
    }

    public static XElements select(String html, String xpathStr) {
        return XPathParser.parse(xpathStr).evaluate(Jsoup.parse(html));
    }

    public static XPathEvaluator compile(String xpathStr) {
        return XPathParser.parse(xpathStr);
    }

    /*-------------     W3cAdaptor         --------------- */

    public static XElements selectW3c(Element element, String xpathStr) {
        return compileW3c(xpathStr).evaluate(element);
    }

    public static XElements selectW3c(String html, String xpathStr) {
        return compileW3c(xpathStr).evaluate(Jsoup.parse(html));
    }

    public static XPathEvaluator compileW3c(String xpathStr) {
        return new W3cEvaluator(xpathStr);
    }

}
