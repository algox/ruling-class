package org.algorithmx.rules.util.reflect;

import org.algorithmx.rules.core.Identifiable;

import java.util.ArrayList;
import java.util.List;

public class TestObjectVisitor extends ObjectVisitorTemplate {

    private List<String> idList = new ArrayList<>();

    public TestObjectVisitor() {
        super();
    }

    @Override
    public boolean visitObjectStart(Object target) {
        if (target instanceof Identifiable) idList.add(((Identifiable) target).getName());
        return true;
    }

    public List<String> getIdList() {
        return idList;
    }
}
