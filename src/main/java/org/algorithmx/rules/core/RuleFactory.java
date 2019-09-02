package org.algorithmx.rules.core;

import org.algorithmx.rules.core.impl.DefaultObjectFactory;
import org.algorithmx.rules.core.impl.DefaultRuleFactory;
import org.algorithmx.rules.core.impl.DefaultRuleSet;
import org.algorithmx.rules.model.ActionDefinition;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.util.ActionUtils;

import java.util.Arrays;

import static org.algorithmx.rules.util.RuleUtils.load;

public interface RuleFactory {

    static RuleFactory defaultFactory() {
        return new DefaultRuleFactory(new DefaultObjectFactory());
    }

    Rule rule(RuleDefinition ruleDefinition);

    default RuleSet rules(String name) {
        return new DefaultRuleSet(name, null);
    }

    default RuleSet rules(String name, String description) {
        return new DefaultRuleSet(name, description);
    }

    default Rule rule(Class<?> rulingClass) {
        Rule result = rule(RuleDefinition.load(rulingClass));
        ActionDefinition[] actions = ActionDefinition.load(rulingClass);

        if (actions != null) {
            Arrays.sort(actions);
            Arrays.stream(actions).forEach(action -> result.then(ActionUtils.create(action, result.getTarget())));
        }

        return result;
    }

    default Rule rule(Condition condition) {
        return rule(load(condition, null, null));
    }

    default Rule rule(Condition.Condition0 arg) {
        return rule((Condition) arg);
    }

    default <A> Rule rule(Condition.Condition1<A> arg) {
        return rule((Condition) arg);
    }

    default <A, B> Rule rule(Condition.Condition2<A, B> arg) {
        return rule((Condition) arg);
    }

    default <A, B, C> Rule rule(Condition.Condition3<A, B, C> arg) {
        return rule((Condition) arg);
    }

    default <A, B, C, D> Rule rule(Condition.Condition4<A, B, C, D> arg) {
        return rule((Condition) arg);
    }

    default <A, B, C, D, E> Rule rule(Condition.Condition5<A, B, C, D, E> arg) {
        return rule((Condition) arg);
    }

    default <A, B, C, D, E, F> Rule rule(Condition.Condition6<A, B, C, D, E, F> arg) {
        return rule((Condition) arg);
    }

    default <A, B, C, D, E, F, G> Rule rule(Condition.Condition7<A, B, C, D, E, F, G> arg) {
        return rule((Condition) arg);
    }

    default <A, B, C, D, E, F, G, H> Rule rule(Condition.Condition8<A, B, C, D, E, F, G, H> arg) {
        return rule((Condition) arg);
    }

    default <A, B, C, D, E, F, G, H, I> Rule rule(Condition.Condition9<A, B, C, D, E, F, G, H, I> arg) {
        return rule((Condition) arg);
    }

    default <A, B, C, D, E, F, G, H, I, J> Rule rule(Condition.Condition10<A, B, C, D, E, F, G, H, I, J> arg) {
        return rule((Condition) arg);
    }

    default Rule rule(String name, Condition condition) {
        return rule(load(condition, name, null));
    }

    default Rule rule(String name, Condition condition, String description) {
        return rule(load(condition, name, description));
    }
}
