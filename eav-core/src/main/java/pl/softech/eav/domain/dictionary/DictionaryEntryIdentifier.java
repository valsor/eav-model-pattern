/*
 * Copyright 2013 Sławomir Śledź <slawomir.sledz@sof-tech.pl>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.softech.eav.domain.dictionary;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Embeddable;

import pl.softech.eav.domain.AbstractValueObject;
import pl.softech.eav.domain.TextMedium;

/**
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
@Embeddable
public class DictionaryEntryIdentifier extends AbstractValueObject {

	public static final String IDENTIFIER_PROPERTY = "identifier";

	@TextMedium
	private String identifier;

	protected DictionaryEntryIdentifier() {
	}

	public DictionaryEntryIdentifier(String identifier) {
		this.identifier = checkNotNull(identifier, ARG_NOT_NULL_CHECK, "identifier");
	}

	public String getIdentifier() {
		return identifier;
	}

}
