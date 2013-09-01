package us.codecraft.xsoup;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafter@gmail.com
 */
public class XElements extends ArrayList<XElement> {

    private Elements elements;

    private ElementOperator elementOperator;

    public XElements(Elements elements, ElementOperator elementOperator) {
        this.elements = elements;
        this.elementOperator = elementOperator;
        initList();
    }

    private void initList() {
        for (Element element : elements) {
            this.add(new XElement(element, elementOperator));
        }
    }

    public String get() {
        if (size() < 1) {
            return null;
        } else {
            return get(0).get();
        }
    }

    public List<String> list() {
        List<String> resultStrings = new ArrayList<String>();
        for (XElement xElement : this) {
            String text = xElement.get();
            if (text!=null){
                resultStrings.add(text);
            }
        }
        return resultStrings;
    }

    @Override
    public String toString() {
        return get();
    }
}
