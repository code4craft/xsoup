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

    @Override
    public String get(ElementOperator elementOperator){
        if (elementOperator == null) {
            return element.toString();
        } else {
            return elementOperator.operate(element);
        }
    }

    @Override
    public String get(String attribute){
       return get(new ElementOperator.AttributeGetter(attribute));
    }

    public String toString() {
         return get();
    }

    @Override
    public Element getElement() {
        return element;
    }
}
