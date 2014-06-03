package us.codecraft.xsoup.evaluator;

import org.jsoup.nodes.Element;

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
