package org.algorithmx.rulii.test.validation;

import org.algorithmx.rulii.core.action.ActionBuilder;
import org.algorithmx.rulii.core.condition.ConditionBuilder;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.context.RuleContextBuilder;
import org.algorithmx.rulii.core.rule.RuleBuilder;
import org.algorithmx.rulii.core.ruleset.RuleSet;
import org.algorithmx.rulii.core.ruleset.RuleSetBuilder;
import org.algorithmx.rulii.validation.RuleViolationBuilder;
import org.algorithmx.rulii.validation.RuleViolations;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.beans.BeanHolder;
import org.algorithmx.rulii.validation.beans.BeanValidator;
import org.algorithmx.rulii.validation.rules.blank.BlankValidationRule;
import org.junit.Test;

public class BeanValidationRuleBuilderTest {

    public BeanValidationRuleBuilderTest() {
        super();
    }

    @Test
    public void test1() {
        Person person = TestData.createPerson1();
        RuleContext context = RuleContextBuilder.empty();
        context.getRuleRegistry().register(createAddressRuleSet());

        BeanValidator validator = new BeanValidator();
        validator.validateBean(context, person, new BeanHolder(person, null));
    }

    private RuleSet createAddressRuleSet() {
        RuleSet result = RuleSetBuilder.with("addressRules")
                .rule(RuleBuilder
                        .name("rule1")
                        .given(ConditionBuilder.build((String streetNumber) -> streetNumber.length() > 1))
                        .otherwise(ActionBuilder.build((String streetNumber, RuleViolations violations) -> {
                            RuleViolationBuilder builder = RuleViolationBuilder
                                    .with("MinStreetNumberRule")
                                    .errorCode("error.101")
                                    .param("streetNumber", streetNumber);
                            violations.add(builder.build());
                        }))
                        .build())
                .rule(RuleBuilder
                        .name("rule2")
                        .given(ConditionBuilder.build((String city, String state) -> city != null && state != null))
                        .otherwise(ActionBuilder.build((String city, String state, RuleViolations violations) -> {
                            violations.add(RuleViolationBuilder
                                    .with("CityStateCannotBeNullRule")
                                    .errorCode("error.102")
                                    .param("city", city)
                                    .param("state", state)
                                    .build());
                        }))
                        .build())
                .rule(RuleBuilder.with(new BlankValidationRule("country", "error.102",
                        Severity.ERROR, "Country must be blank"))
                        .build())
                .build();

        return result;
    }

    /*@Test
    public void test2() {
        BeanValidationRules rules = BeanValidationRuleBuilder.with(Person.class)
                .loadAnnotatedFields()
                .loadAnnotatedMethods()
                .build();
        System.err.println(rules);
    }

    @Test
    public void test3() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("field4");

        MergedAnnotations.from(field, MergedAnnotations.SearchStrategy.DIRECT).stream(ValidationRule.class)
                .forEach(a -> System.err.println(a.getMetaSource().synthesize()));
    }

    @Test
    public void test4() throws NoSuchMethodException {
        Method method = Person.class.getDeclaredMethod("add", Car.class);

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            MergedAnnotations.from(method, parameterAnnotations[i], RepeatableContainers.standardRepeatables())
                    .stream(ValidationRule.class).forEach(a -> System.err.println(a.getMetaSource().synthesize()));
        }
    }*/
}
