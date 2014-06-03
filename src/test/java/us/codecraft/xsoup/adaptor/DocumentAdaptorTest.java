package us.codecraft.xsoup.adaptor;

import org.jsoup.Jsoup;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.*;
import java.io.StringReader;

/**
 * @author code4crafer@gmail.com
 */
public class DocumentAdaptorTest {

    private String html = "<html><body><div id='test'>aaa<div><a href=\"https://github.com\">github.com</a></div></div></body></html>";

    @Test
    public void testDocumentAdaptor() throws Exception {
        Document document = new DocumentAdaptor(Jsoup.parse(html));
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath target = xPathfactory.newXPath();
        try {
            XPathExpression xPathExpression = target.compile("//html");
            NodeList list = (NodeList) xPathExpression.evaluate(new InputSource(new StringReader(html)), XPathConstants.NODESET);
            System.out.println(list.getLength());

            for (int i=0;i<list.getLength();i++){
                System.out.println(list.item(i));
            }
            Object evaluate = xPathExpression.evaluate(document, XPathConstants.NODESET);
            System.out.println(evaluate);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }

    }
}
