package us.codecraft.xsoup;

import org.jsoup.nodes.Element;

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
            if (expr.startsWith("@")){
                attribute = expr.substring(1);
            }
        }

        @Override
        public String operate(Element element) {
            return element.attr(attribute);
        }
    }
}
