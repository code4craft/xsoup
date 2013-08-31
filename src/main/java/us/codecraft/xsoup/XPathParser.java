package us.codecraft.xsoup;

import org.jsoup.parser.TokenQueue;
import org.jsoup.select.Evaluator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafter@gmail.com
 */
public class XPathParser {

    private TokenQueue tq;
    private String query;
    private List<Evaluator> evals = new ArrayList<Evaluator>();

    public XPathParser(String xpathStr) {
        this.query = xpathStr;
        this.tq = new TokenQueue(xpathStr);
    }

    public XPathEvaluator parse() {
        //todo: parse

        return new XPathEvaluator(new Evaluator.Tag("a"), "href");
    }

    public static XPathEvaluator parse(String xpathStr) {
        XPathParser xPathParser = new XPathParser(xpathStr);
        return xPathParser.parse();
    }

}
