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
package org.algorithmx.rules.bind;

import org.algorithmx.rules.error.UnrulyException;

/**
 * Thrown when you attempt to declare an already existing Binding.
 *
 * @author Max Arulananthan
 * @since 1.0
 *
 */
public class BindingAlreadyExistsException extends UnrulyException {

	static final long serialVersionUID = 0L;

	/**
	 * Ctor with the existing binding.
	 * 
	 * @param existingBinding existing binding.
	 */
	public BindingAlreadyExistsException(Binding existingBinding) {
		super(String.format("Binding with name [%s] type [%s] value [%s] already exists.",
				existingBinding.getName(), existingBinding.getType().getTypeName(), existingBinding.getTextValue()));
	}
}
