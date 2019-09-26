package org.algorithmx.rules.model;

import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.spring.util.Assert;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ActionExecution implements Comparable<ActionExecution> {

    // Make sure we don't hold onto the actual action definition
    private final WeakReference<ActionDefinition> actionDefinition;
    private final Map<String, String> params = new LinkedHashMap<>();
    private final Date time = new Date();
    private WeakReference<Exception> error;

    public ActionExecution(ActionDefinition actionDefinition, Binding<Object>...bindings) {
        super();
        Assert.notNull(actionDefinition, "actionDefinition cannot be null.");
        this.actionDefinition = new WeakReference<>(actionDefinition);
        add(bindings);
    }

    public ActionExecution(ActionDefinition actionDefinition, Exception error, Binding<Object>...bindings) {
        super();
        Assert.notNull(actionDefinition, "actionDefinition cannot be null.");
        this.error = new WeakReference<>(error);
        this.actionDefinition = new WeakReference<>(actionDefinition);
        add(bindings);
    }

    /**
     * Returns the associated Action Definition. Could be null.
     *
     * @return action definition (if avail).
     */
    public ActionDefinition getActionDefinition() {
        return actionDefinition.get();
    }

    /**
     * Retrieves all the action parameters.
     *
     * @return rule parameters.
     */
    public Map<String, String> getParams() {
        return params;
    }

    /**
     * Return the exception that occurred during the execution of the rule/
     * @return exception.
     */
    public Exception getError() {
        return error.get();
    }

    /**
     * Time when the Rule condition was run.
     *
     * @return time of Rule execution.
     */
    public Date getTime() {
        return time;
    }

    /**
     * Did an error happen during the execution.
     *
     * @return Rule execution error?
     */
    public boolean isError() {
        return error != null;
    }

    @Override
    public int compareTo(ActionExecution o) {
        return time.compareTo(o.getTime());
    }

    /**
     * Adds all the associated action parameters.
     *
     * @param bindings action parameters.
     */
    private void add(Binding<Object>...bindings) {
        if (bindings == null || bindings.length == 0) return;
        Arrays.stream(bindings).forEach(this::add);
    }

    /**
     * Adds a action parameter.
     *
     * @param binding action parameter.
     */
    private void add(Binding<Object> binding) {
        if (binding == null) return;
        params.put(binding.getName(), binding.getValue() != null ? binding.getValue().toString() : null);
    }

    @Override
    public String toString() {
        return "ActionExecution{" +
                "actionDefinition=" + actionDefinition +
                ", params=" + params +
                ", time=" + time +
                ", error=" + error +
                '}';
    }
}
