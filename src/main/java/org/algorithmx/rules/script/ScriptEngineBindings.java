package org.algorithmx.rules.script;

import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.lib.spring.util.Assert;

import javax.script.Bindings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScriptEngineBindings implements Bindings {

    private final org.algorithmx.rules.bind.Bindings target;
    private final Map<String,Object> internal = new HashMap<>();

    public ScriptEngineBindings(org.algorithmx.rules.bind.Bindings target) {
        super();
        this.target = target;
        this.put("bindings", target);
    }

    @Override
    public boolean containsKey(Object key) {
        Assert.notNull(key, "key cannot be null");
        return internal.containsKey(key) || getTarget().contains(key.toString());
    }

    @Override
    public Object get(Object key) {
        Assert.notNull(key, "key cannot be null");
        if (internal.containsKey(key)) return internal.get(key);
        Binding binding = getTarget().getBinding(key.toString());
        return binding != null ? binding.getValue() : null;
    }

    @Override
    public int size() {
        return internal.size() + getTarget().size();
    }

    @Override
    public boolean isEmpty() {
        return internal.isEmpty() && getTarget().isEmpty();
    }

    @Override
    public boolean containsValue(Object value) {
        boolean result = false;

        if (internal.containsValue(value)) return true;

        for (Binding binding : getTarget()) {
            if (value == null && binding.getValue() == null
                    || value != null && value.equals(binding.getValue())) {
                result = true;
                break;
            }
        }

        return result;
    }

    @Override
    public Set<String> keySet() {
        Set<String> result = new HashSet<>(internal.keySet());
        result.addAll(getTarget().asMap().keySet());
        return result;
    }

    @Override
    public Collection<Object> values() {
        List<Object> result = new ArrayList<>(internal.values());
        result.addAll(getTarget().asMap().values());
        return result;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        Set<Entry<String, Object>> result = new HashSet<>(internal.entrySet());

        for (Entry<String, ?> entry : getTarget().asMap().entrySet()) {
            result.add((Entry<String, Object>) entry);
        }

        return result;
    }

    public org.algorithmx.rules.bind.Bindings getTarget() {
        return target;
    }

    @Override
    public Object put(String name, Object value) {
        return internal.put(name, value);
    }

    @Override
    public void putAll(Map<? extends String, ?> toMerge) {
        internal.putAll(toMerge);
    }

    @Override
    public Object remove(Object key) {
        return internal.remove(key);
    }

    @Override
    public void clear() {
        internal.clear();
    }
}
