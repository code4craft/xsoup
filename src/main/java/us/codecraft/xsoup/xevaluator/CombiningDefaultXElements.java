package us.codecraft.xsoup.xevaluator;

import org.jsoup.select.Elements;
import us.codecraft.xsoup.XElements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author code4crafter@gmail.com
 */
public class CombiningDefaultXElements implements XElements {

    private List<XElements> elementsList;

    public CombiningDefaultXElements(List<XElements> elementsList) {
        this.elementsList = elementsList;
    }

    public CombiningDefaultXElements(XElements... elementsList) {
        this.elementsList = Arrays.asList(elementsList);
    }

    @Override
    public String get() {
        for (XElements xElements : elementsList) {
            String result = xElements.get();
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public List<String> list() {
        List<String> results = new ArrayList<String>();
        for (XElements xElements : elementsList) {
            results.addAll(xElements.list());
        }
        return results;
    }

    public Elements getElements() {
        Elements elements = new Elements();
        for (XElements xElements : elementsList) {
            elements.addAll(xElements.getElements());
        }
        return elements;
    }

}
