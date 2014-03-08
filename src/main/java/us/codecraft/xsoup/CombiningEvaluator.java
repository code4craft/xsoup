package us.codecraft.xsoup;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Evaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Base combining (and, or) evaluator.
 *
 * Copy from {@link org.jsoup.select.CombiningEvaluator} because it is package visible
 *
 * @see org.jsoup.select.CombiningEvaluator
 */
abstract class CombiningEvaluator extends Evaluator {
    final List<Evaluator> evaluators;

    CombiningEvaluator() {
        super();
        evaluators = new ArrayList<Evaluator>();
    }

    CombiningEvaluator(Collection<Evaluator> evaluators) {
        this();
        this.evaluators.addAll(evaluators);
    }

    Evaluator rightMostEvaluator() {
        return evaluators.size() > 0 ? evaluators.get(evaluators.size() - 1) : null;
    }
    
    void replaceRightMostEvaluator(Evaluator replacement) {
        evaluators.set(evaluators.size() - 1, replacement);
    }

    static final class And extends CombiningEvaluator {
        And(Collection<Evaluator> evaluators) {
            super(evaluators);
        }

        And(Evaluator... evaluators) {
            this(Arrays.asList(evaluators));
        }

        @Override
        public boolean matches(Element root, Element node) {
            for (int i = 0; i < evaluators.size(); i++) {
                Evaluator s = evaluators.get(i);
                if (!s.matches(root, node))
                    return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return StringUtil.join(evaluators, " ");
        }
    }

    static final class Or extends CombiningEvaluator {

        Or(Collection<Evaluator> evaluators) {
            super();
            this.evaluators.addAll(evaluators);
        }

        Or(Evaluator... evaluators) {
            this(Arrays.asList(evaluators));
        }

        Or() {
            super();
        }

        public void add(Evaluator e) {
            evaluators.add(e);
        }

        @Override
        public boolean matches(Element root, Element node) {
            for (int i = 0; i < evaluators.size(); i++) {
                Evaluator s = evaluators.get(i);
                if (s.matches(root, node))
                    return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return String.format(":or%s", evaluators);
        }
    }
}
