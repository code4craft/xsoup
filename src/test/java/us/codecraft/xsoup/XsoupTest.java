package us.codecraft.xsoup;

import junit.framework.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * @author code4crafter@gmail.com
 */
public class XsoupTest {

    @Test
    public void testSelect() {
        String html = "<html><div><a href=\"https://github.com\">github.com</a></div></html>";

        Document document = Jsoup.parse(html);

        String result = Xsoup.select(document, "//a").get();
        Assert.assertEquals("<a href=\"https://github.com\">github.com</a>", result);

        result = Xsoup.compile("//a").evaluate(document).get();
        Assert.assertEquals("<a href=\"https://github.com\">github.com</a>", result);
    }
}
