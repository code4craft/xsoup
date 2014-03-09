package us.codecraft.xsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * @author code4crafter@gmail.com
 */
public class XsoupTest {

    private String html = "<html><body><div id='test'>aaa<div><a href=\"https://github.com\">github.com</a></div></div></body></html>";

    private String htmlClass = "<html><body><div class='a b c'><div><a href=\"https://github.com\">github.com</a></div></div><div>b</div></body></html>";

    @Test
    public void testSelect() {

        String html = "<html><div><a href='https://github.com'>github.com</a></div>" +
                "<table><tr><td>a</td><td>b</td></tr></table></html>";

        Document document = Jsoup.parse(html);

        String result = Xsoup.compile("//a/@href").evaluate(document).get();
        assertEquals("https://github.com", result);

        List<String> list = Xsoup.compile("//tr/td/text()").evaluate(document).list();
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
    }

    @Test
    public void testParent() {

        Document document = Jsoup.parse(html);

        String result = Xsoup.select(document, "/html/body/div/div/a").get();
        assertEquals("<a href=\"https://github.com\">github.com</a>", result);

        result = Xsoup.select(document, "/html//div/div/a").get();
        assertEquals("<a href=\"https://github.com\">github.com</a>", result);

        result = Xsoup.select(document, "/html/div/div/a").get();
        assertNull(result);

    }

    @Test
    public void testByAttribute() {

        Document document = Jsoup.parse(html);

        String result = Xsoup.select(document, "//a[@href]").get();
        assertEquals("<a href=\"https://github.com\">github.com</a>", result);

        result = Xsoup.select(document, "//a[@id]").get();
        assertNull(result);

        result = Xsoup.select(document, "//div[@id=test]").get();
        String expectedDiv = "<div id=\"test\">\n" +
                " aaa\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>";
        assertEquals(expectedDiv, result);

        result = Xsoup.select(document, "//div[@id='test']").get();
        assertEquals(expectedDiv, result);
        result = Xsoup.select(document, "//div[@id=\"test\"]").get();
        assertEquals(expectedDiv, result);
    }

    @Test
    public void testClass() {

        Document document = Jsoup.parse(htmlClass);

        String result = Xsoup.select(document, "//div[@class=a]").get();
        assertEquals("<div class=\"a b c\">\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>", result);

        result = Xsoup.select(document, "//div[@class=d]").get();
        assertNull(result);

    }

    @Test
    public void testNth() {

        Document document = Jsoup.parse(htmlClass);

        String result = Xsoup.select(document, "//body/div[1]").get();
        assertEquals("<div class=\"a b c\">\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>", result);

        result = Xsoup.select(document, "//body/div[2]").get();
        assertEquals("<div>\n" +
                " b\n" +
                "</div>", result);

    }

    @Test
    public void testAttribute() {

        Document document = Jsoup.parse(htmlClass);

        String result = Xsoup.select(document, "//a/@href").get();
        assertEquals("https://github.com", result);

        result = Xsoup.select(document, "//a/text()").get();
        assertEquals("github.com", result);

        result = Xsoup.select(document, "//div[@class=a]/html()").get();
        assertEquals("<div>\n" +
                " <a href=\"https://github.com\">github.com</a>\n" +
                "</div>", result);

    }

    @Test
    public void testWildcard() {

        Document document = Jsoup.parse(htmlClass);

        String result = Xsoup.select(document, "//*[@href]/@href").get();
        assertEquals("https://github.com", result);

        result = Xsoup.select(document, "//*[@class=a]/html()").get();
        assertEquals("<div>\n" +
                " <a href=\"https://github.com\">github.com</a>\n" +
                "</div>", result);

        List<String> list = Xsoup.select(document, "//*[@*]/html()").list();
        assertEquals("<div>\n" +
                " <a href=\"https://github.com\">github.com</a>\n" +
                "</div>", list.get(0));
        assertEquals("github.com", list.get(1));
    }

    @Test
    public void testFuzzyValueMatch() {

        Document document = Jsoup.parse(html);

        String result = Xsoup.select(document, "//*[@id~=te]/text()").get();
        assertEquals("aaa", result);
        result = Xsoup.select(document, "//*[@id$=st]/text()").get();
        assertEquals("aaa", result);
        result = Xsoup.select(document, "//*[@id*=es]/text()").get();
        assertEquals("aaa", result);
        result = Xsoup.select(document, "//*[@id~='tes[t]+']/text()").get();
        assertEquals("aaa", result);

        result = Xsoup.select(document, "//*[@id~=te]/allText()").get();
        assertEquals("aaa github.com", result);
    }

    @Test
    public void testLogicOperation() {

        Document document = Jsoup.parse(html);

        String result = Xsoup.select(document, "//*[@id=te or @id=test]/text()").get();
        assertEquals("aaa", result);

        result = Xsoup.select(document, "//*[@id=test or @id=te]/text()").get();
        assertEquals("aaa", result);

        result = Xsoup.select(document, "//*[@id=te and @id=test]/text()").get();
        assertNull(result);

        result = Xsoup.select(document, "//*[(@id=te or @id=test) and @id=test]/text()").get();
        assertEquals("aaa", result);

        result = Xsoup.select(document, "//*[@id=te or (@id=test and @id=id)]/text()").get();
        assertNull(result);
    }

    @Test
    public void testRegex() {

        Document document = Jsoup.parse(html);

        String result = Xsoup.select(document, "//*[@id~=te]/regex('gi\\w+ub')").get();
        assertEquals("github", result);

        result = Xsoup.select(document, "//a/regex('@href','.*gi\\w+ub.*')").get();
        assertEquals("https://github.com", result);

        result = Xsoup.select(document, "//a/regex('@href','.*(gi\\w+ub).*',1").get();
        assertEquals("github", result);
    }

    @Test
    public void testContains() {

        Document document = Jsoup.parse(html);

        String result = Xsoup.select(document, "//div[contains(@id,'te')]").get();
        assertEquals("<div id=\"test\">\n" +
                " aaa\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>", result);

    }

}
