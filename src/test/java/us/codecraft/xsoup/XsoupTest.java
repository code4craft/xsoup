package us.codecraft.xsoup;

import junit.framework.Assert;
import org.jsoup.Jsoup;
import org.junit.Test;

/**
 * @author code4crafter@gmail.com
 */
public class XsoupTest {

    @Test
    public void testSelect() {
        String html = "<html><div><a href='https://github.com'>github.com</a></div></html>";

        String result = Xsoup.select(Jsoup.parse(html), "//a/@href").get();
        Assert.assertEquals("https://github.com", result);

        result = Xsoup.compile("//a/@href").evaluate(html).get();
        Assert.assertEquals("https://github.com", result);
    }
}
