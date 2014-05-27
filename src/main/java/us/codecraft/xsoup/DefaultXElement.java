package us.codecraft.xsoup;

import org.jsoup.nodes.Element;

/**
 * XPath result.
 *
 * @author code4crafter@gmail.com
 */
public class DefaultXElement implements XElement {

    private Element element;

    private ElementOperator elementOperator;

    public DefaultXElement(Element element, ElementOperator elementOperator) {
        this.element = element;
        this.elementOperator = elementOperator;
    }

    @Override
    public String get(){
        return get(elementOperator);
    }

    protected String get(ElementOperator elementOperator){
        if (elementOperator == null) {
            return element.toString();
        } else {
            return elementOperator.operate(element);
        }
    }

    public String toString() {
         return get();
    }

    @Override
    public Element getElement() {
        return element;
    }

}
