package us.codecraft.xsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import us.codecraft.xsoup.nodes.ElementConverter;
import us.codecraft.xsoup.nodes.XPathEvaluator;
import us.codecraft.xsoup.nodes.XPathParser;
import us.codecraft.xsoup.evalutor.XElements;

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

    public static org.w3c.dom.Element convert(Element element){
        return new ElementConverter(element).convertToW3CNode();
    }

}
