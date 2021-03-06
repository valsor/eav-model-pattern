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
package pl.softech.eav.domain.value;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;

import pl.softech.eav.domain.dictionary.DictionaryEntry;

/**
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class ValueFactory {

	public AbstractValue<?> create(Object value) {

		checkNotNull(value);
		
		final AbstractValue<?>[] bag = new AbstractValue<?>[1];
		ValueTypeVisitor visitor = new ValueTypeVisitorAdapter() {

			@Override
			public void visit(String value) {
				bag[0] = new StringValue(value.toString());
			}

			@Override
			public void visit(DictionaryEntry value) {
				bag[0] = new DictionaryEntryValue(value);
			}

			@Override
			public void visit(Date value) {
				bag[0] = new DateValue(value);
			}

			@Override
			public void visit(Boolean value) {
				bag[0] = new BooleanValue(value);
			}

			@Override
			protected void visitAnyDouble(Double value) {
				bag[0] = new DoubleValue(value);
			}

			@Override
			protected void visitAnyInteger(Integer value) {
				bag[0] = new IntegerValue(value);
			}

		};

		accept(value, visitor);

		checkNotNull(bag[0]);
		return bag[0];

	}

	private void accept(Object value, ValueTypeVisitor visitor) {

		if (value instanceof Boolean || value.getClass() == boolean.class) {
			visitor.visit((Boolean) value);
		} else if (value instanceof Date) {
			visitor.visit((Date) value);
		} else if (value instanceof DictionaryEntry) {
			visitor.visit((DictionaryEntry) value);
		} else if (value instanceof Double || value.getClass() == double.class) {
			visitor.visit((Double) value);
		} else if (value instanceof Float || value.getClass() == float.class) {
			visitor.visit((Float) value);
		} else if (value instanceof Integer || value.getClass() == int.class) {
			visitor.visit((Integer) value);
		} else if (value instanceof Long || value.getClass() == long.class) {
			visitor.visit((Long) value);
		} else if (value instanceof Short || value.getClass() == short.class) {
			visitor.visit((Short) value);
		} else if (value instanceof String) {
			visitor.visit((String) value);
		}

	}

	private interface ValueTypeVisitor {

		void visit(Boolean value);

		void visit(Date value);

		void visit(DictionaryEntry value);

		void visit(Double value);

		void visit(Float value);

		void visit(Integer value);

		void visit(Long value);

		void visit(Short value);

		void visit(String value);
	}

	private abstract class ValueTypeVisitorAdapter implements ValueTypeVisitor {

		@Override
		public void visit(Double value) {
			visitAnyDouble(value);
		}

		@Override
		public void visit(Float value) {
			visitAnyDouble(value.doubleValue());
		}

		@Override
		public void visit(Integer value) {
			visitAnyInteger(value);
		}

		@Override
		public void visit(Long value) {
			visitAnyInteger(value.intValue());

		}

		@Override
		public void visit(Short value) {
			visitAnyInteger(value.intValue());

		}

		protected void visitAnyInteger(Integer value) {

		}

		protected void visitAnyDouble(Double value) {

		}

	}

}
