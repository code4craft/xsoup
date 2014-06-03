package us.codecraft.xsoup.w3c;

import org.jsoup.Jsoup;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import us.codecraft.xsoup.Xsoup;

import javax.xml.xpath.*;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author code4crafer@gmail.com
 */
public class W3cEvaluatorTest {

    private String html = "<html><body><div id='test'>aaa<div><a href=\"https://github.com\">github.com</a></div></div></body></html>";

    private String htmlClass = "<html><body><div class='a b c'><div><a href=\"https://github.com\">github.com</a></div></div><div>b</div></body></html>";

    @Test
    public void testSelect() throws XPathExpressionException {

        String html = "<html><div><a href='https://github.com'>github.com</a></div>" +
                "<table><tr><td>a</td><td>b</td></tr></table></html>";

        org.w3c.dom.Document document = Xsoup.convertDocument(Jsoup.parse(html));

        assertThat(getStringValue(document, "//div/a/@href")).isEqualTo("https://github.com");

        List<String> nodeListValue = getNodeListValue(document, "//tr/td");
        assertThat(nodeListValue.get(0)).isEqualTo("<td>a</td>");
        assertThat(nodeListValue.get(1)).isEqualTo("<td>b</td>");
    }

    private String getStringValue(org.w3c.dom.Document document, String expression) throws XPathExpressionException {
        XPathExpression xPathExpression = newXPathExpression(expression);
        return xPathExpression.evaluate(document);
    }

    private XPathExpression newXPathExpression(String expression) throws XPathExpressionException {
        XPathExpression xPathExpression;
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath target = xPathfactory.newXPath();
        xPathExpression = target.compile(expression);
        return xPathExpression;
    }

    private String getNodeValue(org.w3c.dom.Document document, String expression) throws XPathExpressionException {
        XPathExpression xPathExpression = newXPathExpression(expression);
        Object evaluate = xPathExpression.evaluate(document, XPathConstants.NODE);
        if (evaluate == null) {
            return null;
        }
        Node node = (Node) evaluate;
        return node.getNodeValue();
    }

    private List<String> getNodeListValue(org.w3c.dom.Document document, String expression) throws XPathExpressionException {
        XPathExpression xPathExpression = newXPathExpression(expression);
        Object evaluate = xPathExpression.evaluate(document, XPathConstants.NODESET);
        if (evaluate == null) {
            return null;
        }
        NodeList nodeList = (NodeList) evaluate;
        List<String> nodeStrings = new ArrayList<String>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            nodeStrings.add(nodeList.item(i).getNodeValue());
        }
        return nodeStrings;
    }

    @Test
    public void testParent() throws XPathExpressionException {

        org.w3c.dom.Document document = Xsoup.convertDocument(Jsoup.parse(html));

        assertThat(getNodeValue(document, "/html/body/div/div/a")).isEqualTo("<a href=\"https://github.com\">github.com</a>");
        assertThat(getNodeValue(document, "/html//div/div/a")).isEqualTo("<a href=\"https://github.com\">github.com</a>");

        assertThat(getNodeValue(document, "/html/div/div/a")).isNull();

    }

    @Test
    public void testByAttribute() throws XPathExpressionException {

        org.w3c.dom.Document document = Xsoup.convertDocument(Jsoup.parse(html));

        assertThat(getNodeValue(document, "//a[@href]")).isEqualTo("<a href=\"https://github.com\">github.com</a>");

        assertThat(getNodeValue(document, "//a[@id]")).isNull();

        String expectedDiv = "<div id=\"test\">\n" +
                " aaa\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>";


        //TODO: illegal
        //assertThat(getNodeValue(document,"//div[@id=test]")).isEqualTo(expectedDiv);

        assertThat(getNodeValue(document, "//div[@id='test']")).isEqualTo(expectedDiv);
        assertThat(getNodeValue(document, "//div[@id=\"test\"]")).isEqualTo(expectedDiv);
    }

    @Test
    public void testClass() throws XPathExpressionException {

        org.w3c.dom.Document document = Xsoup.convertDocument(Jsoup.parse(htmlClass));


        assertThat(getNodeListValue(document,"//div[@class='a b c']").get(0)).isEqualTo("<div class=\"a b c\">\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>");

        assertThat(getNodeListValue(document, "//div[@class='b']")).isNullOrEmpty();

        assertThat(getNodeListValue(document, "//div[@class='d']")).isNullOrEmpty();


    }

    @Test
    public void testNth() throws XPathExpressionException {

        org.w3c.dom.Document document = Xsoup.convertDocument(Jsoup.parse(htmlClass));

        assertThat(getNodeValue(document, "//body/div[1]")).isEqualTo("<div class=\"a b c\">\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>");

        assertThat(getNodeValue(document, "//body/div[2]")).isEqualTo("<div>\n" +
                " b\n" +
                "</div>");

        String htmlSVG = "<div><svg>1</svg><svg>2</svg></div>";

        document = Xsoup.convertDocument(Jsoup.parse(htmlSVG));
        assertThat(getNodeValue(document, "//div/svg[1]")).isEqualTo("<svg>\n" +
                " 1\n" +
                "</svg>");
        assertThat(getNodeValue(document, "//div/svg[2]")).isEqualTo("<svg>\n" +
                " 2\n" +
                "</svg>");
    }

    @Test
    public void testAttribute() throws XPathExpressionException {

        org.w3c.dom.Document document = Xsoup.convertDocument(Jsoup.parse(htmlClass));

        assertThat(getStringValue(document,"//a/@href")).isEqualTo("https://github.com");

        //TODO:  not support
        //assertThat(getStringValue(document,"//a/text()")).isEqualTo("github.com");

        //TODO:  not support
        //assertThat(getStringValue(document,"//div[@class=a]/html()")).isEqualTo("<div>\n" +
        //        " <a href=\"https://github.com\">github.com</a>\n" +
        //        "</div>");

    }

    @Test
    public void testLogicOperation() throws XPathExpressionException {

        org.w3c.dom.Document document = Xsoup.convertDocument(Jsoup.parse(html));

        String expectedDiv = "<div id=\"test\">\n" +
                " aaa\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>";

        assertThat(getNodeValue(document, "//*[@id='te' or @id='test']")).isEqualTo(expectedDiv);

        assertThat(getNodeValue(document, "//*[@id='te' and @id='test']")).isNullOrEmpty();

        assertThat(getNodeValue(document, "//*[@id='te' and @id='test']")).isNullOrEmpty();

        assertThat(getNodeValue(document,"//*[(@id='te' or @id='test') and @id='test']")).isEqualTo(expectedDiv);

        assertThat(getNodeValue(document,"//*[@id='te' or (@id='test' and @id='id')]")).isNull();
    }

    @Test
    public void testContains() throws XPathExpressionException {

        org.w3c.dom.Document document = Xsoup.convertDocument(Jsoup.parse(html));

        assertThat(getNodeValue(document,"//div[contains(@id,'te')]")).isEqualTo("<div id=\"test\">\n" +
                " aaa\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>");

    }

    @Test
    public void testCombingXPath() throws XPathExpressionException {

        String html2 = "<html><div id='test2'>aa<a href='https://github.com'>github.com</a></div>";

        String expectedDiv1 = "<div id=\"test\">\n" +
                " aaa\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>";

        String expectedDiv2 = "<div id=\"test2\">\n" +
                " aa\n" +
                " <a href=\"https://github.com\">github.com</a>\n" +
                "</div>";

        org.w3c.dom.Document document = Xsoup.convertDocument(Jsoup.parse(html));

        assertThat(getNodeValue(document, "//div[@id='test'] | //div[@id='test2']")).isEqualTo(expectedDiv1);

        document = Xsoup.convertDocument(Jsoup.parse(html2));

        assertThat(getNodeValue(document, "//div[@id='test'] | //div[@id='test2']")).isEqualTo(expectedDiv2);

        document = Xsoup.convertDocument(Jsoup.parse(html+html2));

        assertThat(getNodeListValue(document, "//div[@id='test'] | //div[@id='test2']")).contains(expectedDiv1,expectedDiv2);

    }

}
