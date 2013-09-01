package us.codecraft.xsoup;

import org.jsoup.nodes.Element;
import org.jsoup.select.Evaluator;

/**
 * @author code4crafter@gmail.com
 */
public abstract class XEvaluators {

    public static class HasAnyAttribute extends Evaluator {

        @Override
        public boolean matches(Element root, Element element) {
            return element.attributes().size() > 0;
        }
    }
}
