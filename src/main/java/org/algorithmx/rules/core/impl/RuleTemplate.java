/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 2019, algorithmx.org (dev@algorithmx.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.Action;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.core.Then;
import org.algorithmx.rules.util.ActionUtils;

import java.util.LinkedList;

public abstract class RuleTemplate implements Rule {

    private final LinkedList<Action> actions = new LinkedList();

    protected RuleTemplate() {
        super();
    }

    @Override
    public Action[] getActions() {
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public Rule then(Action action) {
        actions.add(action);
        return this;
    }

    @Override
    public Rule then(Then action) {
        return then(action, null);
    }

    @Override
    public Rule then(Then action, String description) {
        actions.add(ActionUtils.create(action, description, getTarget()));
        return this;
    }

    @Override
    public Rule then(Then.Then0 arg) {
        return then(arg, null);
    }

    @Override
    public <A> Rule then(Then.Then1<A> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B> Rule then(Then.Then2<A, B> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C> Rule then(Then.Then3<A, B, C> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D> Rule then(Then.Then4<A, B, C, D> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E> Rule then(Then.Then5<A, B, C, D, E> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E, F> Rule then(Then.Then6<A, B, C, D, E, F> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E, F, G> Rule then(Then.Then7<A, B, C, D, E, F, G> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E, F, G, H> Rule then(Then.Then8<A, B, C, D, E, F, G, H> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E, F, G, H, I> Rule then(Then.Then9<A, B, C, D, E, F, G, H, I> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E, F, G, H, I, J> Rule then(Then.Then10<A, B, C, D, E, F, G, H, I, J> arg) {
        return then(arg, null);
    }
}
