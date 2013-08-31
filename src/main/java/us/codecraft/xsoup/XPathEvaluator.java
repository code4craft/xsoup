package us.codecraft.xsoup;


import org.jsoup.nodes.Element;
import org.jsoup.select.Collector;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;

/**
 * @author code4crafter@gmail.com
 */
public class XPathEvaluator {

    private Evaluator evaluator;

    private String attribute;

    public XPathEvaluator(Evaluator evaluator, String attribute) {
        this.evaluator = evaluator;
        this.attribute = attribute;
    }

    public XElements evaluate(Element element) {
        Elements elements = Collector.collect(evaluator, element);
        return new XElements(elements,attribute);
    }

}
