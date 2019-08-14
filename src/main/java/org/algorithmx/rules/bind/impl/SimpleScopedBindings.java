package org.algorithmx.rules.bind.impl;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.bind.ScopedBindings;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class SimpleScopedBindings implements ScopedBindings {

    private final Stack<Bindings> scopes = new Stack<>();

    public SimpleScopedBindings() {
        super();
        init();
    }

    @Override
    public Bindings getCurrentScope() {
        return scopes.peek();
    }

    @Override
    public Iterable<Bindings> getScopes() {
        return scopes;
    }

    @Override
    public Iterable<Bindings> getReverseScopes() {
        List<Bindings> result = scopes.subList(0, scopes.size());
        Collections.reverse(result);
        return result;
    }

    @Override
    public Bindings newScope() {
        Bindings result = createScope();
        scopes.push(result);
        return result;
    }

    @Override
    public Bindings endScope() {
        return scopes.pop();
    }

    @Override
    public void clear() {
        scopes.clear();
        init();
    }

    protected void init() {
        scopes.push(createScope());
    }

    protected Bindings createScope() {
        return new SimpleBindings();
    }
}
