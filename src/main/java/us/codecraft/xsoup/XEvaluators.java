package us.codecraft.xsoup;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;

/**
 * Evaluators in Xsoup.
 * @author code4crafter@gmail.com
 */
public abstract class XEvaluators {

    public static class HasAnyAttribute extends Evaluator {

        @Override
        public boolean matches(Element root, Element element) {
            return element.attributes().size() > 0;
        }
    }

    public static class IsNthOfType extends Evaluator.CssNthEvaluator {
        public IsNthOfType(int a, int b) {
            super(a,b);
        }

        protected int calculatePosition(Element root, Element element) {
            int pos = 0;
            Elements family = element.parent().children();
            for (int i = 0; i < family.size(); i++) {
                if (family.get(i).tag().equals(element.tag())) pos++;
                if (family.get(i) == element) break;
            }
            return pos;
        }

        @Override
        protected String getPseudoClass() {
            return "nth-of-type";
        }
    }
}
