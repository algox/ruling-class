/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2021, Algorithmx Inc.
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
package org.algorithmx.rules.event;

import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.Date;

public class ExecutionEvent<T> implements Comparable<ExecutionEvent> {

    private final EventType eventType;
    private final T data;
    private final Date time;

    public ExecutionEvent(EventType eventType, T data) {
        this(eventType, data, new Date());
    }

    public ExecutionEvent(EventType eventType, T data, Date time) {
        super();
        Assert.notNull(eventType, "eventType cannot be null.");
        Assert.notNull(time, "time cannot be null.");
        this.eventType = eventType;
        this.data = data;
        this.time = time;
    }

    @Override
    public int compareTo(ExecutionEvent o) {
        return time.compareTo(o.time);
    }

    public boolean isError() {
        return data instanceof Exception;
    }

    public EventType getEventType() {
        return eventType;
    }

    public T getData() {
        return data;
    }

    public Date getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "RuleEvent{" +
                "eventType=" + eventType +
                ", data=" + data +
                ", time=" + time +
                '}';
    }
}
