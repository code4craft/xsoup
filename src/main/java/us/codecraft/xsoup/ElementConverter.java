package us.codecraft.xsoup;

import org.jsoup.nodes.Element;
import org.w3c.dom.Node;

/**
 * @author code4crafer@gmail.com
 */
public class ElementConverter {

    private Element element;

    public ElementConverter(Element element) {
        this.element = element;
    }

    public org.w3c.dom.Element convertToW3CNode(){
        return null;
    }
}
