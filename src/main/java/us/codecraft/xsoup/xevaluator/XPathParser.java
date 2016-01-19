package us.codecraft.xsoup.xevaluator;

import org.jsoup.helper.Validate;
import org.jsoup.select.Evaluator;
import org.jsoup.select.Selector;
import us.codecraft.xsoup.XPathEvaluator;
import us.codecraft.xsoup.XTokenQueue;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser of XPath.
 *
 * @author code4crafter@gmail.com
 */
public class XPathParser {

    private static final String[] COMBINATORS = new String[]{"//", "/", "|"};

    private static final String[] ESCAPED_QUOTES = new String[]{"\\\"", "\\'"};

    private static final String[] QUOTES = new String[]{"\"", "'"};

    private static final String[] HIERARCHY_COMBINATORS = new String[]{"//", "/", "|"};

    private static final Map<String, FunctionEvaluator> FUNCTION_MAPPING = new HashMap<String, FunctionEvaluator>();
    static {
        FUNCTION_MAPPING.put("contains", new FunctionEvaluator() {
            @Override
            public Evaluator call(String... param) {
                Validate.isTrue(param.length == 2, String.format("Error argument of %s", "contains"));
                return new Evaluator.AttributeWithValueContaining(param[0], param[1]);
            }
        });
        FUNCTION_MAPPING.put("starts-with", new FunctionEvaluator() {
            @Override
            public Evaluator call(String... param) {
                Validate.isTrue(param.length == 2, String.format("Error argument of %s", "starts-with"));
                return new Evaluator.AttributeWithValueStarting(param[0], param[1]);
            }
        });
        FUNCTION_MAPPING.put("ends-with", new FunctionEvaluator() {
            @Override
            public Evaluator call(String... param) {
                Validate.isTrue(param.length == 2, String.format("Error argument of %s", "ends-with"));
                return new Evaluator.AttributeWithValueEnding(param[0], param[1]);
            }
        });
    }

    private static final String OR_COMBINATOR = "|";

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
            if (tq.matchChomp(OR_COMBINATOR)) {
                tq.consumeWhitespace();
                return combineXPathEvaluator(tq.remainder());
            } else if (tq.matchesAny(HIERARCHY_COMBINATORS)) {
                combinator(tq.consumeAny(HIERARCHY_COMBINATORS));
            } else {
                findElements();
            }
            tq.consumeWhitespace();
        }
        return collectXPathEvaluator();
    }

    private XPathEvaluator combineXPathEvaluator(String subQuery) {
        XPathEvaluator xPathEvaluator = collectXPathEvaluator();
        return new CombingXPathEvaluator(xPathEvaluator, parse(subQuery));
    }

    private XPathEvaluator collectXPathEvaluator() {
        if (noEvalAllow) {
            return new DefaultXPathEvaluator(null, elementOperator);
        }

        if (evals.size() == 1)
            return new DefaultXPathEvaluator(evals.get(0), elementOperator);

        return new DefaultXPathEvaluator(new CombiningEvaluator.And(evals), elementOperator);
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
        XPathEvaluator tmpEval = parse(subQuery);
        if (!(tmpEval instanceof DefaultXPathEvaluator)) {
            throw new IllegalArgumentException(String.format("Error XPath in %s", subQuery));
        }
        DefaultXPathEvaluator newEval = (DefaultXPathEvaluator) tmpEval;
        if (newEval.getElementOperator() != null) {
            elementOperator = newEval.getElementOperator();
        }
        // attribute expr does not return Evaluator
        if (newEval.getEvaluator() != null) {
            if (combinator.equals("//")) {
                currentEval = new CombiningEvaluator.And(newEval.getEvaluator(), new StructuralEvaluator.Parent(currentEval));
            } else if (combinator.equals("/")) {
                currentEval = new CombiningEvaluator.And(newEval.getEvaluator(), new StructuralEvaluator.ImmediateParent(currentEval));
            }
        }
        evals.add(currentEval);

    }

    private String consumeSubQuery() {
        StringBuilder sq = new StringBuilder();
        while (!tq.isEmpty()) {
            tq.consumeWhitespace();
            if (tq.matches("("))
                sq.append("(").append(tq.chompBalanced('(', ')')).append(")");
            else if (tq.matches("["))
                sq.append("[").append(tq.chompBalanced('[', ']')).append("]");
            else if (tq.matchesAny(ESCAPED_QUOTES))
                sq.append(tq.consumeAny(ESCAPED_QUOTES));
            else if (tq.matchesAny(QUOTES))
                sq.append(tq.chompBalancedQuotes());
            else if (tq.matchesAny(COMBINATORS))
                break;
            else if (!tq.isEmpty()) {
                sq.append(tq.consume());
            }
        }
        return sq.toString();
    }

    private void findElements() {
        if (tq.matches("@")) {
            consumeAttribute();
        } else if (tq.matches("*")) {
            allElements();
        } else if (tq.matchesRegex("\\w+\\(.*\\).*")) {
            consumeOperatorFunction();
        } else if (tq.matchesWord()) {
            byTag();
        } else if (tq.matchesRegex("\\[\\d+\\]")) {
            byNth();
        } else if (tq.matches("[")) {
            evals.add(consumePredicates(tq.chompBalanced('[', ']')));
        } else {
            // unhandled
            throw new Selector.SelectorParseException("Could not parse query '%s': unexpected token at '%s'", query, tq.remainder());
        }

    }

    /**
     * EvaluatorStack for logic calculate.
     * Priority: AND &gt; OR, Regardless of bracket.
     * <br>
     * Calculate AND immediately.
     * Store evaluator with OR, until there are two evaluator in stack, then calculate it.
     */
    static class EvaluatorStack extends Stack<Evaluator> {

        public void calc(Evaluator evaluator, Operation operation) {
            if (size() == 0) {
                push(evaluator);
            } else {
                if (operation == Operation.AND) {
                    evaluator = new CombiningEvaluator.And(pop(), evaluator);
                } else {
                    mergeOr();
                }
                push(evaluator);
            }
        }

        public void mergeOr() {
            if (size() >= 2) {
                Evaluator pop1 = pop();
                Evaluator pop2 = pop();
                Evaluator tempEvaluator = new CombiningEvaluator.Or(pop2, pop1);
                push(tempEvaluator);
            }
        }
    }

    interface FunctionEvaluator {
        Evaluator call(String... param);
    }

    enum Operation {
        AND, OR;
    }

    private Evaluator consumePredicates(String queue) {
        XTokenQueue predicatesQueue = new XTokenQueue(queue);
        EvaluatorStack evaluatorStack = new EvaluatorStack();
        Operation currentOperation = null;
        predicatesQueue.consumeWhitespace();
        while (!predicatesQueue.isEmpty()) {
            if (predicatesQueue.matchChomp("and")) {
                currentOperation = Operation.AND;
            } else if (predicatesQueue.matchChomp("or")) {
                currentOperation = Operation.OR;
            } else {
                if (currentOperation == null && evaluatorStack.size() > 0) {
                    throw new IllegalArgumentException(String.format("Need AND/OR between two predicate! %s", predicatesQueue.remainder()));
                }
                Evaluator evaluator;
                if (predicatesQueue.matches("(")) {
                    evaluator = consumePredicates(predicatesQueue.chompBalanced('(', ')'));
                } else if (predicatesQueue.matches("@")) {
                    evaluator = byAttribute(predicatesQueue);
                } else if (predicatesQueue.matchesRegex("\\w+.*")) {
                    evaluator = byFunction(predicatesQueue);
                } else {
                    throw new Selector.SelectorParseException("Could not parse query '%s': unexpected token at '%s'", query, predicatesQueue.remainder());
                }
                evaluatorStack.calc(evaluator, currentOperation);
                //consume operator
                currentOperation = null;
            }
            predicatesQueue.consumeWhitespace();
        }
        evaluatorStack.mergeOr();
        return evaluatorStack.peek();
    }

    private Evaluator byFunction(XTokenQueue predicatesQueue) {
        for (Map.Entry<String, FunctionEvaluator> entry : FUNCTION_MAPPING.entrySet()) {
            if (predicatesQueue.matchChomp(entry.getKey())) {
                String paramString = predicatesQueue.chompBalanced('(', ')');
                List<String> params = XTokenQueue.trimQuotes(XTokenQueue.parseFuncionParams(paramString));

                if (params.get(0).startsWith("@")) {
                    params.set(0, params.get(0).substring(1));
                    return entry.getValue().call(params.toArray(new String[0]));
                } else {
                    return null;
                }
            }
        }

        throw new Selector.SelectorParseException("Could not parse query '%s': unexpected token at '%s'", query, predicatesQueue.remainder());
    }

    private void allElements() {
        tq.consume();
        evals.add(new Evaluator.AllElements());
    }

    private void byNth() {
        String nth = tq.chompBalanced('[', ']');
        evals.add(new XEvaluators.IsNthOfType(0, Integer.parseInt(nth)));
    }

    private void consumeAttribute() {
        tq.consume("@");
        elementOperator = new ElementOperator.AttributeGetter(tq.remainder());
        noEvalAllow = true;
    }

    private Pattern patternForText = Pattern.compile("text\\((\\d*)\\)");

    private void consumeOperatorFunction() {
        String remainder = consumeSubQuery();
        if (remainder.startsWith("text(")) {
            functionText(remainder);
        } else if (remainder.startsWith("regex(")) {
            functionRegex(remainder);
        } else if (remainder.equals("allText()")) {
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
        if (elementOperator != null) {
            noEvalAllow = true;
        }
    }

    private void functionRegex(String remainder) {
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

    private Evaluator byAttribute(XTokenQueue cq) {
        cq.matchChomp("@");
        String key = cq.consumeToAny("=", "!=", "^=", "$=", "*=", "~="); // eq, not, start, end, contain, match, (no val)
        Validate.notEmpty(key);
        cq.consumeWhitespace();
        Evaluator evaluator;
        if (cq.isEmpty()) {
            if ("*".equals(key)) {
                evaluator = new XEvaluators.HasAnyAttribute();
            } else {
                evaluator = new Evaluator.Attribute(key);
            }
        } else {
            if (cq.matchChomp("=")) {
                String value = chompEqualValue(cq);
                //to support select one class out of all
                if (key.equals("class")) {
                    String className = XTokenQueue.trimQuotes(value);
                    if (!className.contains(" ")) {
                        evaluator = new Evaluator.Class(className);
                    } else {
                        evaluator = new Evaluator.AttributeWithValue(key, className);
                    }
                } else {
                    evaluator = new Evaluator.AttributeWithValue(key, XTokenQueue.trimQuotes(value));
                }
            } else if (cq.matchChomp("!="))
                evaluator = new Evaluator.AttributeWithValueNot(key, XTokenQueue.trimQuotes(chompEqualValue(cq)));

            else if (cq.matchChomp("^="))
                evaluator = new Evaluator.AttributeWithValueStarting(key, XTokenQueue.trimQuotes(chompEqualValue(cq)));

            else if (cq.matchChomp("$="))
                evaluator = new Evaluator.AttributeWithValueEnding(key, XTokenQueue.trimQuotes(chompEqualValue(cq)));

            else if (cq.matchChomp("*="))
                evaluator = new Evaluator.AttributeWithValueContaining(key, XTokenQueue.trimQuotes(chompEqualValue(cq)));

            else if (cq.matchChomp("~="))
                evaluator = new Evaluator.AttributeWithValueMatching(key, Pattern.compile(XTokenQueue.trimQuotes(chompEqualValue(cq))));
            else
                throw new Selector.SelectorParseException("Could not parse attribute query '%s': unexpected token at '%s'", query, chompEqualValue(cq));
        }
        return evaluator;
    }

    private String chompEqualValue(XTokenQueue cq) {
        String value;
        if (cq.matchChomp("'")) {
            value = cq.chompTo("'");
        } else if (cq.matchChomp("\"")) {
            value = cq.chompTo("\"");
        } else if (cq.containsAny(" ")) {
            value = cq.chompTo(" ");
        } else {
            value = cq.remainder();
        }
        return value;
    }

    public static XPathEvaluator parse(String xpathStr) {
        XPathParser xPathParser = new XPathParser(xpathStr);
        return xPathParser.parse();
    }

}
