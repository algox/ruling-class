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
package org.algorithmx.rules.core.registry;

import org.algorithmx.rules.core.Runnable;
import org.algorithmx.rules.core.UnrulyException;

/**
 * Thrown when you attempt to register an already existing Rule/RuleSet.
 *
 * @author Max Arulananthan
 * @since 1.0
 *
 */
public class AlreadyRegisteredException extends UnrulyException {

	private static final long serialVersionUID = 0L;

	private Runnable runnable;

	/**
	 * Ctor with the existing rule/ruleSet.
	 *
	 * @param name name that is registered under.
	 * @param runnable existing rule/ruleSet.
	 */
	public AlreadyRegisteredException(String name, Runnable runnable) {
		super("Registry already has name [" + name + "] already exists. [" + runnable + "]");
		this.runnable = runnable;
	}

	public Runnable getExistingValue() {
		return runnable;
	}
}
