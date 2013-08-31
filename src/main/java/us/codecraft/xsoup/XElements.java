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

    private String attribute;

    public XElements(Elements elements, String attribute) {
        this.elements = elements;
        this.attribute = attribute;
        initList();
    }

    private void initList() {
        for (Element element : elements) {
            this.add(new XElement(element, attribute));
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
            resultStrings.add(xElement.get());
        }
        return resultStrings;
    }

    @Override
    public String toString() {
        return get();
    }
}
