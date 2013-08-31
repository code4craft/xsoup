package us.codecraft.xsoup;

import org.jsoup.nodes.Element;

/**
 * @author code4crafter@gmail.com
 */
public class XElement {

    private Element element;

    private String attribute;

    public XElement(Element element, String attribute) {
        this.element = element;
        this.attribute = attribute;
    }

    public String get(){
        return get(attribute);
    }

    public String get(String attribute){
        if (attribute == null) {
            return element.toString();
        } else {
            return element.attr(attribute);
        }
    }

    public String toString() {
         return get();
    }

}
