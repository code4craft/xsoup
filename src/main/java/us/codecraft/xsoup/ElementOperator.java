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

    public abstract String operate(Element element);

    public static class AttributeGetter extends ElementOperator {

        private String attribute;

        public AttributeGetter(String attribute) {
            this.attribute = attribute;
        }

        @Override
        public String operate(Element element) {
            return element.attr(attribute);
        }

        @Override
        public String toString() {
            return "@" + attribute;
        }
    }

    public static class AllText extends ElementOperator {

        @Override
        public String operate(Element element) {
            return element.text();
        }

        @Override
        public String toString() {
            return "allText()";
        }
    }

    public static class Html extends ElementOperator {

        @Override
        public String operate(Element element) {
            return element.html();
        }

        @Override
        public String toString() {
            return "html()";
        }
    }

    public static class OuterHtml extends ElementOperator {

        @Override
        public String operate(Element element) {
            return element.outerHtml();
        }

        @Override
        public String toString() {
            return "outerHtml()";
        }
    }

    public static class TidyText extends ElementOperator {

        @Override
        public String operate(Element element) {
            return new HtmlToPlainText().getPlainText(element);
        }

        @Override
        public String toString() {
            return "tidyText()";
        }
    }

    public static class GroupedText extends ElementOperator {

        private int group;

        public GroupedText(int group) {
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

        @Override
        public String toString() {
            return String.format("text(%d)", group);
        }
    }

    /**
     * usage:
     * <p/>
     * regex('.*')
     * regex(@attr,'.*')
     * regex(@attr,'.*',group)
     */
    public static class Regex extends ElementOperator {

        private Pattern pattern;

        private String attribute;

        private int group;

        public Regex(String expr) {
            this.pattern = Pattern.compile(expr);
        }

        public Regex(String expr, String attribute) {
            this.attribute = attribute;
            this.pattern = Pattern.compile(expr);
        }

        public Regex(String expr, String attribute, int group) {
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

        @Override
        public String toString() {
            return String.format("regex(%s%s%s)",
                    attribute != null ? "@" + attribute + "," : "", pattern.toString(),
                    group != 0 ? "," + group : "");
        }
    }
}
