package us.codecraft.xsoup.w3c;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import us.codecraft.xsoup.XElements;
import us.codecraft.xsoup.XPathEvaluator;
import us.codecraft.xsoup.Xsoup;
import us.codecraft.xsoup.xevaluator.XPathParser;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author code4crafter@gmail.com
 */
public class W3cXsoupTest {

    private String html = "<html><body><div id='test'>aaa<div><a href=\"https://github.com\">github.com</a></div></div></body></html>";

    private String htmlClass = "<html><body><div class='a b c'><div><a href=\"https://github.com\">github.com</a></div></div><div>b</div></body></html>";

    @Test
    public void testSelect() {

        String html = "<html><div><a href='https://github.com'>github.com</a></div>" +
                "<table><tr><td>a</td><td>b</td></tr></table></html>";

        Document document = Jsoup.parse(html);

        String result = Xsoup.compileW3c("//div/a").evaluate(document).get();
        assertThat(result).isEqualTo("https://github.com");

        XPathEvaluator xPathEvaluator = Xsoup.compileW3c("//tr/td/text()");
        List<String> list = xPathEvaluator.evaluate(document).list();
        assertThat(list).contains("a","b");
        assertThat(xPathEvaluator.hasAttribute()).isTrue();

    }

    @Test
    public void testParent() {

        Document document = Jsoup.parse(html);

        String result = Xsoup.selectW3c(document, "/html/body/div/div/a").get();
        assertThat(result).isEqualTo("<a href=\"https://github.com\">github.com</a>");

        result = Xsoup.selectW3c(document, "/html//div/div/a").get();
        assertThat(result).isEqualTo("<a href=\"https://github.com\">github.com</a>");

        result = Xsoup.selectW3c(document, "/html/div/div/a").get();
        assertThat(result).isNull();

    }

    @Test
    public void testByAttribute() {

        Document document = Jsoup.parse(html);

        XPathEvaluator xPathEvaluator = new W3cEvaluator("//a[@href]");
        assertThat(xPathEvaluator.hasAttribute()).isFalse();
        XElements select = xPathEvaluator.evaluate(document);
        assertThat(select.get()).isEqualTo("<a href=\"https://github.com\">github.com</a>");

        String result = Xsoup.selectW3c(document, "//a[@id]").get();
        assertThat(result).isNull();

        result = Xsoup.selectW3c(document, "//div[@id=test]").get();
        String expectedDiv = "<div id=\"test\">\n" +
                " aaa\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>";
        assertThat(result).isEqualTo(expectedDiv);

        result = Xsoup.selectW3c(document, "//div[@id='test']").get();
        assertThat(result).isEqualTo(expectedDiv);
        result = Xsoup.selectW3c(document, "//div[@id=\"test\"]").get();
        assertThat(result).isEqualTo(expectedDiv);
    }

    @Test
    public void testClass() {

        Document document = Jsoup.parse(htmlClass);

        String result = Xsoup.selectW3c(document, "//div[@class='a b c']").get();
        assertThat(result).isEqualTo("<div class=\"a b c\">\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>");

        result = Xsoup.selectW3c(document, "//div[@class='d']").get();
        assertThat(result).isNull();

    }

    @Test
    public void testNth() {

        Document document = Jsoup.parse(htmlClass);

        String result = Xsoup.selectW3c(document, "//body/div[1]").get();
        assertThat(result).isEqualTo("<div class=\"a b c\">\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>");

        result = Xsoup.selectW3c(document, "//body/div[2]").get();
        assertThat(result).isEqualTo("<div>\n" +
                " b\n" +
                "</div>");

        String htmlSVG = "<div><svg>1</svg><svg>2</svg></div>";
        result = Xsoup.selectW3c(htmlSVG, "//div/svg[1]/text()").get();
        assertThat(result).isEqualTo("1");
        result = Xsoup.selectW3c(htmlSVG, "//div/svg[2]/text()").get();
        assertThat(result).isEqualTo("2");
    }

    @Test
    public void testAttribute() {

        Document document = Jsoup.parse(htmlClass);

        String result = Xsoup.selectW3c(document, "//a/@href").get();
        assertThat(result).isEqualTo("https://github.com");

        result = Xsoup.selectW3c(document, "//a/text()").get();
        assertThat(result).isEqualTo("github.com");

        result = Xsoup.selectW3c(document, "//div[@class=a]/html()").get();
        assertThat(result).isEqualTo("<div>\n" +
                " <a href=\"https://github.com\">github.com</a>\n" +
                "</div>");

    }

    @Test
    public void testWildcard() {

        Document document = Jsoup.parse(htmlClass);

        String result = Xsoup.selectW3c(document, "//*[@href]/@href").get();
        assertThat(result).isEqualTo("https://github.com");

        result = Xsoup.selectW3c(document, "//*[@class=a]/html()").get();
        assertThat(result).isEqualTo("<div>\n" +
                " <a href=\"https://github.com\">github.com</a>\n" +
                "</div>");

        List<String> list = Xsoup.selectW3c(document, "//*[@*]/html()").list();
        assertThat(list.get(0)).isEqualTo("<div>\n" +
                " <a href=\"https://github.com\">github.com</a>\n" +
                "</div>");
        assertThat(list.get(1)).isEqualTo("github.com");
    }

    @Test
    public void testLogicOperation() {

        Document document = Jsoup.parse(html);

        String result = Xsoup.selectW3c(document, "//*[@id=te or @id=test]/text()").get();
        assertThat(result).isEqualTo("aaa");

        result = Xsoup.selectW3c(document, "//*[@id=test or @id=te]/text()").get();
        assertThat(result).isEqualTo("aaa");

        result = Xsoup.selectW3c(document, "//*[@id=te and @id=test]/text()").get();
        assertThat(result).isNull();

        result = Xsoup.selectW3c(document, "//*[(@id=te or @id=test) and @id=test]/text()").get();
        assertThat(result).isEqualTo("aaa");

        result = Xsoup.selectW3c(document, "//*[@id=te or (@id=test and @id=id)]/text()").get();
        assertThat(result).isNull();
    }

    @Test
    public void testContains() {

        Document document = Jsoup.parse(html);

        String result = Xsoup.selectW3c(document, "//div[contains(@id,'te')]").get();
        assertThat(result).isEqualTo("<div id=\"test\">\n" +
                " aaa\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>");

    }

    @Test
    public void testCombingXPath() {

        String html2 = "<html><div id='test2'>aa<a href='https://github.com'>github.com</a></div>";

        Document document = Jsoup.parse(html);

        XPathEvaluator xPathEvaluator = XPathParser.parse("//div[@id='test']/text() | //div[@id='test2']/text()");
        assertThat(xPathEvaluator.hasAttribute()).isTrue();
        XElements select = xPathEvaluator.evaluate(document);
        assertThat(select.get()).isEqualTo("aaa");


        select = Xsoup.selectW3c(html2, "//div[@id='test']/text() | //div[@id='test2']/text()");
        assertThat(select.get()).isEqualTo("aa");

        select = Xsoup.selectW3c(html + html2, "//div[@id='test']/text() | //div[@id='test2']/text()");
        assertThat(select.list()).contains("aaa", "aa");

        xPathEvaluator = XPathParser.parse("//div[@id='test'] | //div[@id='test2']");
        assertThat(xPathEvaluator.hasAttribute()).isFalse();
    }

    @Test
    public void testSeparatorInQuotes() {

        String html2 = "<html><div id='test2'>/list/12345<a href='https://github.com'>github.com</a></div>";

        Document document = Jsoup.parse(html2);

        String result = Xsoup.selectW3c(document, "//div[@id='test2']/regex(\"/list/(\\d+)\",1)").get();
        assertThat(result).isEqualTo("12345");

    }

}
