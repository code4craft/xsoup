package us.codecraft.xsoup.xevaluator;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.xsoup.XElement;

import java.util.ArrayList;
import java.util.List;

/**
 * XPath results.
 *
 * @author code4crafter@gmail.com
 */
public class DefaultXEvaluatorElements extends ArrayList<XElement> implements XEvaluatorElements {

    private Elements elements;

    private ElementOperator elementOperator;

    public DefaultXEvaluatorElements(Elements elements, ElementOperator elementOperator) {
        this.elements = elements;
        this.elementOperator = elementOperator;
        initList();
    }

    private void initList() {
        for (Element element : elements) {
            this.add(new DefaultXEvaluatorElement(element, elementOperator));
        }
    }

    @Override
    public String get() {
        if (size() < 1) {
            return null;
        } else {
            return get(0).get();
        }
    }

    @Override
    public List<String> list() {
        List<String> resultStrings = new ArrayList<String>();
        for (XElement xElement : this) {
            String text = xElement.get();
            if (text != null) {
                resultStrings.add(text);
            }
        }
        return resultStrings;
    }

    @Override
    public String toString() {
        return get();
    }

    @Override
    public Elements getElements() {
        return elements;
    }

}
