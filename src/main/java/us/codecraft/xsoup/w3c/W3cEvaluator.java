package us.codecraft.xsoup.w3c;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import us.codecraft.xsoup.XElements;
import us.codecraft.xsoup.XPathEvaluator;

import javax.xml.xpath.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafer@gmail.com
 */
public class W3cEvaluator implements XPathEvaluator {

    private XPathExpression xPathExpression;

    public W3cEvaluator(String xpathExpression) {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath target = xPathfactory.newXPath();
        try {
            this.xPathExpression = target.compile(xpathExpression);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException("XPath parse error " + xpathExpression, e);
        }
    }

    @Override
    public XElements evaluate(Element element) {
        try {
            NodeList nodeList = (NodeList) xPathExpression.evaluate(NodeAdaptors.getElement(element), XPathConstants.NODESET);
            List<String> values = new ArrayList<String>(nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                values.add(node.getNodeValue());
            }
            return new ListBasedXElements(values);
        } catch (XPathExpressionException e) {
            throw new RuntimeException("XPath evaluate error " + xPathExpression, e);
        }
    }

    @Override
    public XElements evaluate(String html) {
        return evaluate(Jsoup.parse(html));
    }

    @Override
    public boolean hasAttribute() {
        throw new UnsupportedOperationException();
    }
}
