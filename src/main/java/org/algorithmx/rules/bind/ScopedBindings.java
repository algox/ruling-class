package org.algorithmx.rules.bind;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface ScopedBindings extends Bindings {

    Bindings getCurrentScope();

    Iterable<Bindings> getScopes();

    Iterable<Bindings> getReverseScopes();

    Bindings newScope();

    Bindings endScope();

    @Override
    default <T> Binding<T> getBinding(String name) {
        Binding<T> result = null;

        for (Bindings scope : getScopes()) {
            result = scope.getBinding(name);
            if (result != null) break;
        }

        return result;
    }

    @Override
    default <T> Binding<T> getBinding(String name, TypeReference<T> type) {
        Binding<T> result = null;

        for (Bindings scope : getScopes()) {
            result = scope.getBinding(name, type);
            if (result != null) break;
        }

        return result;
    }

    @Override
    default <T> Set<Binding<T>> getBindings(TypeReference<T> type) {
        Set<Binding<T>> result = new HashSet<>();

        for (Bindings scope : getReverseScopes()) {
            result.addAll(scope.getBindings(type));
        }

        return result;
    }

    @Override
    default Map<String, ?> asMap() {
        Map<String, Object> result = new HashMap<>();

        for (Iterator<Binding<?>> it = iterator(); it.hasNext();) {
            Binding<?> binding = it.next();
            result.put(binding.getName(), binding.getValue());
        }

        return result;
    }

    @Override
    default int size() {
        int result = 0;

        for (Bindings scope : getScopes()) {
            result += scope.size();
        }

        return result;
    }

    default Iterator<Binding<?>> iterator() {
        Set<Binding<?>> result = new HashSet<>();

        for (Bindings scope : getReverseScopes()) {
            for (Iterator<Binding<?>> it = scope.iterator(); it.hasNext();) {
                result.add(it.next());
            }
        }

        return result.iterator();
    }

    @Override
    default <T> Bindings bind(String name, TypeReference<T> type, T initialValue, Predicate<T> validationCheck, boolean mutable) throws BindingAlreadyExistsException, InvalidBindingException {
        return getCurrentScope().bind(name, type, initialValue, validationCheck, mutable);
    }

    @Override
    default <T> Bindings bind(Binding<T> binding) {
        return getCurrentScope().bind(binding);
    }

    @Override
    default <T> Bindings bind(Collection<Binding<T>> bindings) {
        return getCurrentScope().bind(bindings);
    }

    @Override
    default <T> Bindings bind(String name, Supplier<T> valueSupplier, TypeReference<T> type) throws BindingAlreadyExistsException {
        return getCurrentScope().bind(name, valueSupplier, type);
    }
}
