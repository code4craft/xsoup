package us.codecraft.xsoup;

import org.jsoup.helper.Validate;
import org.jsoup.select.Evaluator;
import org.jsoup.select.Selector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser of XPath.
 *
 * @author code4crafter@gmail.com
 */
public class XPathParser {

    private String[] combinators = new String[]{"//", "/", "|"};

    private XTokenQueue tq;
    private String query;
    private List<Evaluator> evals = new ArrayList<Evaluator>();
    private ElementOperator elementOperator;
    private boolean noEvalAllow = false;

    public XPathParser(String xpathStr) {
        this.query = xpathStr;
        this.tq = new XTokenQueue(xpathStr);
    }

    public XPathEvaluator parse() {

        while (!tq.isEmpty()) {
            Validate.isFalse(noEvalAllow, "XPath error! No operator allowed after attribute or function!" + tq);
            if (tq.matchesAny(combinators)) {
                combinator(tq.consumeAny(combinators));
            } else {
                findElements();
            }
        }
        if (noEvalAllow) {
            return new XPathEvaluator(null, elementOperator);
        }

        if (evals.size() == 1)
            return new XPathEvaluator(evals.get(0), elementOperator);

        return new XPathEvaluator(new CombiningEvaluator.And(evals), elementOperator);
    }

    private void combinator(String combinator) {
        Evaluator currentEval;
        if (evals.size() == 0) {
            currentEval = new StructuralEvaluator.Root();
        } else if (evals.size() == 1) {
            currentEval = evals.get(0);
        } else {
            currentEval = new CombiningEvaluator.And(evals);
        }
        evals.clear();
        String subQuery = consumeSubQuery();
        XPathEvaluator newEval = parse(subQuery);
        if (newEval.getAttribute() != null) {
            elementOperator = newEval.getElementOperator();
        }
        // attribute expr does not return Evaluator
        if (newEval.getEvaluator() != null) {
            if (combinator.equals("//")) {
                currentEval = new CombiningEvaluator.And(newEval.getEvaluator(), new StructuralEvaluator.Parent(currentEval));
            } else if (combinator.equals("/")) {
                currentEval = new CombiningEvaluator.And(newEval.getEvaluator(), new StructuralEvaluator.ImmediateParent(currentEval));
            } else if (combinator.equals("|")) {
                currentEval = new CombiningEvaluator.Or(newEval.getEvaluator(), new StructuralEvaluator.ImmediateParent(currentEval));
            }
        }
        evals.add(currentEval);

    }

    private String consumeSubQuery() {
        StringBuilder sq = new StringBuilder();
        while (!tq.isEmpty()) {
            if (tq.matches("("))
                sq.append("(").append(tq.chompBalanced('(', ')')).append(")");
            else if (tq.matches("["))
                sq.append("[").append(tq.chompBalanced('[', ']')).append("]");
            else if (tq.matchesAny(combinators))
                break;
            else
                sq.append(tq.consume());
        }
        return sq.toString();
    }

    private void findElements() {
        if (tq.matches("@")) {
            consumeAttribute();
        } else if (tq.matches("*")) {
            allElements();
        } else if (tq.matchesRegex("\\w+\\(.*\\)")) {
            consumeFunction();
        } else if (tq.matchesWord()) {
            byTag();
        } else if (tq.matches("[@")) {
            byAttribute();
        } else if (tq.matchesRegex("\\[\\d+\\]")) {
            byNth();
        } else {
            // unhandled
            throw new Selector.SelectorParseException("Could not parse query '%s': unexpected token at '%s'", query, tq.remainder());
        }

    }

    private void allElements() {
        tq.consume();
        evals.add(new Evaluator.AllElements());
    }

    private void byNth() {
        String nth = tq.chompBalanced('[', ']');
        evals.add(new Evaluator.IsNthOfType(0, Integer.parseInt(nth)));
    }

    private void consumeAttribute() {
        tq.consume("@");
        elementOperator = new ElementOperator.AttributeGetter(tq.remainder());
        noEvalAllow = true;
    }

    private Pattern patternForText = Pattern.compile("text\\((\\d*)\\)");

    private void consumeFunction() {
        String remainder = tq.remainder();
        functionText(remainder);
        if (elementOperator == null) {
            functionRegex(remainder);
        }
        if (elementOperator == null) {
            if (remainder.equals("allText()") || remainder.equals("text()")) {
                elementOperator = new ElementOperator.AllText();
            } else if (remainder.equals("tidyText()")) {
                elementOperator = new ElementOperator.TidyText();
            } else if (remainder.equals("html()")) {
                elementOperator = new ElementOperator.Html();
            } else if (remainder.equals("outerHtml()")) {
                elementOperator = new ElementOperator.OuterHtml();
            } else {
                throw new IllegalArgumentException("Unsupported function " + remainder);
            }
        }
        noEvalAllow = true;
    }

    private void functionRegex(String remainder) {
        if (remainder.startsWith("regex(")) {
            Validate.isTrue(remainder.endsWith(")"), "Unclosed bracket for function! " + remainder);
            List<String> params = XTokenQueue.trimQuotes(XTokenQueue.parseFuncionParams(remainder.substring("regex(".length(), remainder.length() - 1)));
            if (params.size() == 1) {
                elementOperator = new ElementOperator.Regex(params.get(0));
            } else if (params.size() == 2) {
                if (params.get(0).startsWith("@")) {
                    elementOperator = new ElementOperator.Regex(params.get(1), params.get(0).substring(1));
                } else {
                    elementOperator = new ElementOperator.Regex(params.get(0), null, Integer.parseInt(params.get(1)));
                }
            } else if (params.size() == 3) {
                elementOperator = new ElementOperator.Regex(params.get(1), params.get(0).substring(1), Integer.parseInt(params.get(2)));
            } else {
                throw new Selector.SelectorParseException("Unknown usage for regex()" + remainder);
            }
        }
    }

    private void functionText(String remainder) {
        Matcher matcher = patternForText.matcher(remainder);
        if (matcher.matches()) {
            int attributeGroup;
            String group = matcher.group(1);
            if (group.equals("")) {
                attributeGroup = 0;
            } else {
                attributeGroup = Integer.parseInt(group);
            }
            elementOperator = new ElementOperator.GroupedText(attributeGroup);
        }
    }

    private void byTag() {
        String tagName = tq.consumeElementSelector();
        Validate.notEmpty(tagName);

        // namespaces: if element name is "abc:def", selector must be "abc|def", so flip:
        if (tagName.contains("|"))
            tagName = tagName.replace("|", ":");

        evals.add(new Evaluator.Tag(tagName.trim().toLowerCase()));
    }

    private void byAttribute() {
        XTokenQueue cq = new XTokenQueue(tq.chompBalanced('[', ']')); // content queue
        cq.matchChomp("@");
        String key = cq.consumeToAny("=", "!=", "^=", "$=", "*=", "~="); // eq, not, start, end, contain, match, (no val)
        Validate.notEmpty(key);
        cq.consumeWhitespace();

        if (cq.isEmpty()) {
            if ("*".equals(key)) {
                evals.add(new XEvaluators.HasAnyAttribute());
            } else {
                evals.add(new Evaluator.Attribute(key));
            }
        } else {
            if (cq.matchChomp("=")) {
                //to support select one class out of all
                if (key.equals("class")) {
                    String className = XTokenQueue.trimQuotes(cq.remainder());
                    if (!className.contains(" ")) {
                        evals.add(new Evaluator.Class(className));
                    } else {
                        evals.add(new Evaluator.AttributeWithValue(key, className));
                    }
                } else {
                    evals.add(new Evaluator.AttributeWithValue(key, XTokenQueue.trimQuotes(cq.remainder())));
                }
            } else if (cq.matchChomp("!="))
                evals.add(new Evaluator.AttributeWithValueNot(key, XTokenQueue.trimQuotes(cq.remainder())));

            else if (cq.matchChomp("^="))
                evals.add(new Evaluator.AttributeWithValueStarting(key, XTokenQueue.trimQuotes(cq.remainder())));

            else if (cq.matchChomp("$="))
                evals.add(new Evaluator.AttributeWithValueEnding(key, XTokenQueue.trimQuotes(cq.remainder())));

            else if (cq.matchChomp("*="))
                evals.add(new Evaluator.AttributeWithValueContaining(key, XTokenQueue.trimQuotes(cq.remainder())));

            else if (cq.matchChomp("~="))
                evals.add(new Evaluator.AttributeWithValueMatching(key, Pattern.compile(XTokenQueue.trimQuotes(cq.remainder()))));
            else
                throw new Selector.SelectorParseException("Could not parse attribute query '%s': unexpected token at '%s'", query, cq.remainder());
        }
    }

    public static XPathEvaluator parse(String xpathStr) {
        XPathParser xPathParser = new XPathParser(xpathStr);
        return xPathParser.parse();
    }

}
