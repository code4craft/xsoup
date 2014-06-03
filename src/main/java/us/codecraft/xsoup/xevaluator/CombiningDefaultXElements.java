package us.codecraft.xsoup.xevaluator;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author code4crafter@gmail.com
 */
public class CombiningDefaultXElements implements XEvaluatorElements {

    private List<XEvaluatorElements> elementsList;

    public CombiningDefaultXElements(List<XEvaluatorElements> elementsList) {
        this.elementsList = elementsList;
    }

    public CombiningDefaultXElements(XEvaluatorElements... elementsList) {
        this.elementsList = Arrays.asList(elementsList);
    }

    @Override
    public String get() {
        for (XEvaluatorElements xElements : elementsList) {
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
        for (XEvaluatorElements xElements : elementsList) {
            results.addAll(xElements.list());
        }
        return results;
    }

    public Elements getElements() {
        Elements elements = new Elements();
        for (XEvaluatorElements xElements : elementsList) {
            elements.addAll(xElements.getElements());
        }
        return elements;
    }

}
