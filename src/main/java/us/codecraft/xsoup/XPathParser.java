package us.codecraft.xsoup;

import org.jsoup.select.Evaluator;

/**
 * @author code4crafter@gmail.com
 */
public class XPathParser {

    public XPathEvaluator parse(String xpathStr) {
        //todo: parse

        return new XPathEvaluator(new Evaluator.AllElements(), null);
    }

}
