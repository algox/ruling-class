/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2019, Live Software & Consultants Inc (rules@algorithmx.org)
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
package org.algorithmx.rules.core;

/**
 * Available Values of the Rule Execution State.
 *
 * RUNNING - Rules are executing.
 * FINISHED - Rules have successfully finished execution.
 * STOPPED - Stop condition was met and the execution is halted.
 * ERROR - There was an exception during the execution and rules have stopped executing.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public enum RuleExecutionState {

    RUNNING, STOPPED, FINISHED, ERROR;

    /**
     * Determines whether the rules are running.
     *
     * @return true if state is RUNNING.
     */
    public boolean isRunning() {
        return this == RUNNING;
    }

    /**
     * Determines whether the rules have stopped executing.
     *
     * @return true if state is FINISHED or ERROR.
     */
    public boolean isStopped() {
        return this == STOPPED;
    }

    /**
     * Determines whether the state is ERROR.
     *
     * @return true if state is ERROR.
     */
    public boolean isError() {
        return this == ERROR;
    }

    /**
     * Determines whether the state is FINISHED.
     *
     * @return true if state is FINISHED.
     */
    public boolean isCompletedSuccessfully() {
        return this == FINISHED;
    }
}
