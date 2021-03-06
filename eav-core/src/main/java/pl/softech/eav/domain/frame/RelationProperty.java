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
package pl.softech.eav.domain.frame;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import pl.softech.eav.domain.object.MyObject;
import pl.softech.eav.domain.relation.Relation;
import pl.softech.eav.domain.relation.RelationConfiguration;
import pl.softech.eav.domain.relation.RelationConfigurationRepository;
import pl.softech.eav.domain.relation.RelationIdentifier;

import com.google.common.collect.ImmutableSet;

/**
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class RelationProperty implements Property {

	private final MyObject object;

	private final RelationConfigurationRepository relationConfigurationRepository;

	private final String relationIdentifier;

	private final FrameFactory frameFactory;

	RelationProperty(MyObject object, String relationIdentifier, RelationConfigurationRepository relationConfigurationRepository,
			FrameFactory frameFactory) {
		this.object = object;
		this.relationIdentifier = relationIdentifier;
		this.relationConfigurationRepository = relationConfigurationRepository;
		this.frameFactory = frameFactory;
	}

	@Override
	public void setValue(Method method, Object arg) {
		RelationConfiguration conf = relationConfigurationRepository.findByIdentifier(new RelationIdentifier(relationIdentifier));

		if (arg == null) {
			object.updateRelation(conf, null);
			return;
		}

		addValue(method, arg);

	}

	@Override
	public void addValue(Method method, Object arg) {

		checkNotNull(arg);

		RelationConfiguration conf = relationConfigurationRepository.findByIdentifier(new RelationIdentifier(relationIdentifier));

		if (arg instanceof MyObject) {
			object.addRelation(conf, (MyObject) arg);
			return;
		}

		checkState(arg instanceof MyObjectProxy);

		object.addRelation(conf, ((MyObjectProxy) arg).getTarget());

	}

	private Object getValues(String attIdentifier, Method method) throws Exception {

		ImmutableSet<Relation> relations = object.getRelationsByIdentifier(new RelationIdentifier(relationIdentifier));

		if (relations == null) {
			return null;
		}
		
		Collection<Object> coll = ReflectionUtils.newCollection(method.getReturnType());

		ParameterizedType pt = (ParameterizedType) method.getGenericReturnType();
		
		Class<?> parameterizedClazz = (Class<?>) pt.getActualTypeArguments()[0];
		
		boolean frame = !parameterizedClazz.equals(MyObject.class);

		for (Relation relation : relations) {

			if (frame) {
				coll.add(frameFactory.frame(parameterizedClazz, relation.getTarget()));
			} else {
				coll.add(relation.getTarget());
			}

		}

		return coll;
	}

	@Override
	public Object getValue(Method method) {

		Relation relation = object.getRelationByIdentifier(new RelationIdentifier(relationIdentifier));

		if (relation == null) {
			return null;
		}

		Class<?> returnType = method.getReturnType();

		if (returnType.equals(MyObject.class)) {
			return relation.getTarget();
		}

		return frameFactory.frame(returnType, relation.getTarget());
	}

	@Override
	public Object getValues(Method method) {
		try {
			return getValues(relationIdentifier, method);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
