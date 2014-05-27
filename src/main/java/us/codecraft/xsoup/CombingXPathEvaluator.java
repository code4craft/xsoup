package us.codecraft.xsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author code4crafter@gmail.com
 */
public class CombingXPathEvaluator implements XPathEvaluator {

    private List<XPathEvaluator> xPathEvaluators;

    public CombingXPathEvaluator(List<XPathEvaluator> xPathEvaluators) {
        this.xPathEvaluators = xPathEvaluators;
    }

    public CombingXPathEvaluator(XPathEvaluator... xPathEvaluators) {
        this.xPathEvaluators = Arrays.asList(xPathEvaluators);
    }

    @Override
    public XElements evaluate(Element element) {
        List<XElements> xElementses = new ArrayList<XElements>();
        for (XPathEvaluator xPathEvaluator : xPathEvaluators) {
            xElementses.add(xPathEvaluator.evaluate(element));
        }
        return new CombiningXElements(xElementses);
    }

    @Override
    public XElements evaluate(String html) {
        Document document = Jsoup.parse(html);
        return evaluate(document);
    }

    @Override
    public boolean hasAttribute() {
        for (XPathEvaluator xPathEvaluator : xPathEvaluators) {
            if (xPathEvaluator.hasAttribute()){
                return true;
            }
        }
        return false;
    }
}
