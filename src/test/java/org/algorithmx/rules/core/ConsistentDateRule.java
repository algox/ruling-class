package org.algorithmx.rules.core;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.annotation.Then;

import java.util.Date;

@Rule
@Description("This Rule will validate that the first date is before the second.")
public class ConsistentDateRule {

    public ConsistentDateRule() {
        super();
    }

    @Given // Condition
    public boolean isValid(Date date1, Date date2) {
        return date1.compareTo(date2) < 0;
    }

    @Then // Action
    public void then() {
        System.out.println("Your dates are consistent.");
    }

    @Otherwise() // Else Action
    public void otherwise(Date date1, Date date2) {
        System.out.println("Inconsistent dates. Date 1[" + date1 + "] Date2 [" + date2 + "]");
    }

    /*public static void main(String[] args) {
        org.algorithmx.rules.core.rule.Rule rule = RuleBuilder
                .given(ConditionBuilder.build((Date date1, Date date2) -> date1.compareTo(date2) < 0))
                .then(ActionBuilder.build(() -> System.out.println("Your dates are consistent.")))
                .otherwise(ActionBuilder.build((Date date1, Date date2) -> System.out.println("Inconsistent dates. Date 1[" + date1 + "] Date2 [" + date2 + "]")))
                .given(ConditionBuilder.build((Date date1, Date date2) -> date1.compareTo(date2) < 0))
                .name("ConsistentDateRule")
                .description("This Rule will validate that the first date is before the second.")
                .build();

        rule.run(date1 -> new Date(), date2 -> new Date());
    }*/
}
