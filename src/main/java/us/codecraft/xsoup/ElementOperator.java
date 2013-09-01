package us.codecraft.xsoup;

import org.jsoup.nodes.Element;

import java.util.regex.Pattern;

/**
 * @author code4crafter@gmail.com
 */
public abstract class ElementOperator {

    public ElementOperator(String expr) {
        this.expr = expr;
    }

    protected String expr;

    public abstract String operate(Element element);

    @Override
    public String toString() {
        return expr;
    }

    public static class AttributeGetter extends ElementOperator {

        private String attribute;

        public AttributeGetter(String expr) {
            super(expr);
            if (expr.startsWith("@")) {
                attribute = expr.substring(1);
            }
        }

        @Override
        public String operate(Element element) {
            return element.attr(attribute);
        }
    }

    public static class Function extends ElementOperator {

        public Function(String expr) {
            super(expr);
        }

        @Override
        public String operate(Element element) {
            if (expr.equals("text()")) {
                return element.text();
            } else if (expr.equals("html()")) {
                return element.html();
            } else {
                throw new IllegalArgumentException("Unsupported function " + expr);
            }
        }
    }

    /**
     * usage:
     * <p/>
     * regex('.*')
     * regex(@attr,'.*')
     * regex(@attr,'.*',group)
     */
    public static class Regex extends Function {

        private Pattern pattern;

        private String attribute;

        public Regex(String expr) {
            super(expr);
        }

        protected void parse(String expr) {
            //todo
        }
    }
}
