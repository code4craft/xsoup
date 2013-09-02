package us.codecraft.xsoup;

import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Operate on element to get XPath result.
 *
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
            if (expr.equals("allText()")) {
                return element.text();
            } else if (expr.equals("tidyText()")) {
                return new HtmlToPlainText().getPlainText(element);
            } else if (expr.equals("html()")) {
                return element.html();
            } else if (expr.equals("outerHtml()")) {
                return element.outerHtml();
            } else {
                throw new IllegalArgumentException("Unsupported function " + expr);
            }
        }
    }

    public static class Text extends Function {

        private int group;

        public Text(String expr, int group) {
            super(expr);
            this.group = group;
        }

        @Override
        public String operate(Element element) {
            int index = 0;
            StringBuilder accum = new StringBuilder();
            for (Node node : element.childNodes()) {
                if (node instanceof TextNode) {
                    TextNode textNode = (TextNode) node;
                    if (group == 0) {
                        accum.append(textNode.text());
                    } else if (++index == group) {
                        return textNode.text();
                    }
                }
            }
            return accum.toString();
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

        private int group;

        public Regex(String expr) {
            super(expr);
            this.pattern = Pattern.compile(expr);
        }

        public Regex(String expr, String attribute) {
            super("@" + attribute + ":" + expr);
            this.attribute = attribute;
            this.pattern = Pattern.compile(expr);
        }

        public Regex(String expr, String attribute, int group) {
            super("@" + attribute + ":" + expr + "[" + group + "]");
            this.attribute = attribute;
            this.pattern = Pattern.compile(expr);
            this.group = group;
        }

        @Override
        public String operate(Element element) {
            Matcher matcher = pattern.matcher(getSource(element));
            if (matcher.find()) {
                return matcher.group(group);
            }
            return null;
        }

        protected String getSource(Element element) {
            if (attribute == null) {
                return element.outerHtml();
            } else {
                String attr = element.attr(attribute);
                Validate.notNull(attr, "Attribute " + attribute + " of " + element + " is not exist!");
                return attr;
            }
        }

    }
}
