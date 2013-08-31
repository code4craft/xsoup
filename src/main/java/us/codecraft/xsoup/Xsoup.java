package us.codecraft.xsoup;

import org.jsoup.nodes.Element;

/**
 * @author code4crafter@gmail.com
 */
public class Xsoup {

    public static XElements select(Element element, String xpathStr) {
        return new XPathParser().parse(xpathStr).evaluate(element);
    }

}
