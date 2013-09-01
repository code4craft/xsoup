package us.codecraft.xsoup;

import org.jsoup.helper.Validate;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author code4crafter@gmail.com
 */
public class XPathParser {

    private String[] combinators = new String[]{"/", "(", ")", "[", "]", "\"", "'", "=", "<", ">"};

    private TokenQueue tq;
    private String query;
    private List<Evaluator> evals = new ArrayList<Evaluator>();

    public XPathParser(String xpathStr) {
        this.query = xpathStr;
        this.tq = new TokenQueue(xpathStr);
    }

    public XPathEvaluator parse() {
        //todo: parse

        while (!tq.isEmpty()) {
            // hierarchy and extras
            boolean seenWhite = tq.consumeWhitespace();

            if (tq.matchesAny(combinators)) {
                combinator(tq.consume());
            } else if (seenWhite) {
                combinator(' ');
            } else { // E.class, E#id, E[attr] etc. AND
                findElements(); // take next el, #. etc off queue
            }
        }

        if (evals.size() == 1)
            return new XPathEvaluator(evals.get(0), null);

        return new XPathEvaluator(new CombiningEvaluator.And(evals), "href");
    }

    private void combinator(char combinator) {
        tq.consumeWhitespace();
        String subQuery = consumeSubQuery(); // support multi > childs

        Evaluator rootEval; // the new topmost evaluator
        Evaluator currentEval; // the evaluator the new eval will be combined to. could be root, or rightmost or.
        Evaluator newEval = parse(subQuery).getEvaluator(); // the evaluator to add into target evaluator
        boolean replaceRightMost = false;

        if (evals.size() == 1) {
            rootEval = currentEval = evals.get(0);
            // make sure OR (,) has precedence:
            if (rootEval instanceof CombiningEvaluator.Or && combinator != ',') {
                currentEval = ((CombiningEvaluator.Or) currentEval).rightMostEvaluator();
                replaceRightMost = true;
            }
        }
        else {
            rootEval = currentEval = new CombiningEvaluator.And(evals);
        }
        evals.clear();

        // for most combinators: change the current eval into an AND of the current eval and the new eval
        if (combinator == '>')
            currentEval = new CombiningEvaluator.And(newEval, new StructuralEvaluator.ImmediateParent(currentEval));
        else if (combinator == ' ')
            currentEval = new CombiningEvaluator.And(newEval, new StructuralEvaluator.Parent(currentEval));
        else if (combinator == '+')
            currentEval = new CombiningEvaluator.And(newEval, new StructuralEvaluator.ImmediatePreviousSibling(currentEval));
        else if (combinator == '~')
            currentEval = new CombiningEvaluator.And(newEval, new StructuralEvaluator.PreviousSibling(currentEval));
        else if (combinator == ',') { // group or.
            CombiningEvaluator.Or or;
            if (currentEval instanceof CombiningEvaluator.Or) {
                or = (CombiningEvaluator.Or) currentEval;
                or.add(newEval);
            } else {
                or = new CombiningEvaluator.Or();
                or.add(currentEval);
                or.add(newEval);
            }
            currentEval = or;
        }
        else
            throw new Selector.SelectorParseException("Unknown combinator: " + combinator);

        if (replaceRightMost)
            ((CombiningEvaluator.Or) rootEval).replaceRightMostEvaluator(currentEval);
        else rootEval = currentEval;
        evals.add(rootEval);
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
        if (tq.matchChomp("#")) {

        } else // unhandled
            throw new Selector.SelectorParseException("Could not parse query '%s': unexpected token at '%s'", query, tq.remainder());

    }

    private void byId() {
        String id = tq.consumeCssIdentifier();
        Validate.notEmpty(id);
        evals.add(new Evaluator.Id(id));
    }

    private void byClass() {
        String className = tq.consumeCssIdentifier();
        Validate.notEmpty(className);
        evals.add(new Evaluator.Class(className.trim().toLowerCase()));
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
        TokenQueue cq = new TokenQueue(tq.chompBalanced('[', ']')); // content queue
        String key = cq.consumeToAny("=", "!=", "^=", "$=", "*=", "~="); // eq, not, start, end, contain, match, (no val)
        Validate.notEmpty(key);
        cq.consumeWhitespace();

        if (cq.isEmpty()) {
            if (key.startsWith("^"))
                evals.add(new Evaluator.AttributeStarting(key.substring(1)));
            else
                evals.add(new Evaluator.Attribute(key));
        } else {
            if (cq.matchChomp("="))
                evals.add(new Evaluator.AttributeWithValue(key, cq.remainder()));

            else if (cq.matchChomp("!="))
                evals.add(new Evaluator.AttributeWithValueNot(key, cq.remainder()));

            else if (cq.matchChomp("^="))
                evals.add(new Evaluator.AttributeWithValueStarting(key, cq.remainder()));

            else if (cq.matchChomp("$="))
                evals.add(new Evaluator.AttributeWithValueEnding(key, cq.remainder()));

            else if (cq.matchChomp("*="))
                evals.add(new Evaluator.AttributeWithValueContaining(key, cq.remainder()));

            else if (cq.matchChomp("~="))
                evals.add(new Evaluator.AttributeWithValueMatching(key, Pattern.compile(cq.remainder())));
            else
                throw new Selector.SelectorParseException("Could not parse attribute query '%s': unexpected token at '%s'", query, cq.remainder());
        }
    }

    public static XPathEvaluator parse(String xpathStr) {
        XPathParser xPathParser = new XPathParser(xpathStr);
        return xPathParser.parse();
    }

}
