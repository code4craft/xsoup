package us.codecraft.xsoup;

import junit.framework.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * @author code4crafter@gmail.com
 */
public class XsoupTest {

    private String html = "<html><body><div id='test'><div><a href=\"https://github.com\">github.com</a></div></div></body></html>";

    private String htmlClass = "<html><body><div class='a b c'><div><a href=\"https://github.com\">github.com</a></div></div></body></html>";

    @Test
    public void testSelect() {

        Document document = Jsoup.parse(html);

        String result = Xsoup.select(document, "//a").get();
        Assert.assertEquals("<a href=\"https://github.com\">github.com</a>", result);

        result = Xsoup.compile("//a").evaluate(document).get();
        Assert.assertEquals("<a href=\"https://github.com\">github.com</a>", result);
    }

    @Test
    public void testParent() {

        Document document = Jsoup.parse(html);

        String result = Xsoup.select(document, "/html/body/div/div/a").get();
        Assert.assertEquals("<a href=\"https://github.com\">github.com</a>", result);

        result = Xsoup.select(document, "/html//div/div/a").get();
        Assert.assertEquals("<a href=\"https://github.com\">github.com</a>", result);

        result = Xsoup.select(document, "/html/div/div/a").get();
        Assert.assertNull(result);

    }

    @Test
    public void testAttribute() {

        Document document = Jsoup.parse(html);

        String result = Xsoup.select(document, "//a[@href]").get();
        Assert.assertEquals("<a href=\"https://github.com\">github.com</a>", result);

        result = Xsoup.select(document, "//a[@id]").get();
        Assert.assertNull(result);

        result = Xsoup.select(document, "//div[@id=test]").get();
        Assert.assertEquals("<div id=\"test\">\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>", result);

        result = Xsoup.select(document, "//div[@id='test']").get();
        Assert.assertEquals("<div id=\"test\">\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>", result);
        result = Xsoup.select(document, "//div[@id=\"test\"]").get();
        Assert.assertEquals("<div id=\"test\">\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>", result);
    }

    @Test
    public void testClass() {

        Document document = Jsoup.parse(htmlClass);

        String result = Xsoup.select(document, "//div[@class=a]").get();
        Assert.assertEquals("<div class=\"a b c\">\n" +
                " <div>\n" +
                "  <a href=\"https://github.com\">github.com</a>\n" +
                " </div>\n" +
                "</div>", result);

        result = Xsoup.select(document, "//div[@class=d]").get();
        Assert.assertNull(result);

    }


}
