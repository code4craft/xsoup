package us.codecraft.xsoup.xevaluator;


import org.jsoup.nodes.Element;
import org.jsoup.select.Collector;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import us.codecraft.xsoup.XElements;
import us.codecraft.xsoup.XPathEvaluator;

/**
 * @author code4crafter@gmail.com
 */
public class DefaultXPathEvaluator implements XPathEvaluator {

    private Evaluator evaluator;

    private ElementOperator elementOperator;

    public DefaultXPathEvaluator(Evaluator evaluator, ElementOperator elementOperator) {
        this.evaluator = evaluator;
        this.elementOperator = elementOperator;
    }

    @Override
    public XElements evaluate(Element element) {
        Elements elements;
        if (evaluator != null) {
            elements = Collector.collect(evaluator, element);
        } else {
            elements = new Elements();
            elements.add(element);
        }
        return new DefaultXElements(elements, elementOperator);
    }

    @Override
    public boolean hasAttribute() {
        return elementOperator != null;
    }

    public Evaluator getEvaluator() {
        return evaluator;
    }

    public String getAttribute() {
        if (elementOperator == null) {
            return null;
        }
        return elementOperator.toString();
    }

    public ElementOperator getElementOperator() {
        return elementOperator;
    }
}
