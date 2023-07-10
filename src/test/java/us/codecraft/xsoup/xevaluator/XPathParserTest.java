package us.codecraft.xsoup.xevaluator;

import junit.framework.TestCase;
import org.junit.Test;
import us.codecraft.xsoup.XPathEvaluator;

/**
 * @author liu xw
 * @date 2023 07-10
 */
public class XPathParserTest {



    @Test
    public void testParse() {
        String xpathStr = "/span[contains(:text:one, 'test')]";
        XPathEvaluator parse = XPathParser.parse(xpathStr);
        System.out.println(parse);
    }
}