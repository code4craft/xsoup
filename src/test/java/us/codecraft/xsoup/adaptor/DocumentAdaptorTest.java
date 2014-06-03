package us.codecraft.xsoup.adaptor;

import org.jsoup.Jsoup;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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
            XPathExpression compile = target.compile("//div[@id='test']");
            String evaluate = compile.evaluate(document);
            System.out.println(evaluate);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }

    }
}
