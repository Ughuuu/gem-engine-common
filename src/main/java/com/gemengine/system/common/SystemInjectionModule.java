package com.gemengine.system.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class SystemInjectionModule extends AbstractModule {
	private final Map<String, Object> properties;
	private final Collection<Class<? extends SystemBase>> systemList;

	public SystemInjectionModule() {
		this(new ArrayList<>(), new HashMap<>());
	}

	public SystemInjectionModule(Collection<Class<? extends SystemBase>> systemList, Map<String, Object> properties) {
		this.systemList = systemList;
		this.properties = properties;
	}

	@Override
	protected void configure() {
		for (Class<? extends Object> systemClass : systemList) {
			bind(systemClass).asEagerSingleton();
		}
		for (Map.Entry<String, Object> propertyEntry : properties.entrySet()) {
			setNamedBinding(propertyEntry.getKey(), propertyEntry.getValue());
		}
	}

	@SuppressWarnings("unchecked")
	private <T> void setNamedBinding(String key, T object) {
		bind((Class<T>) object.getClass()).annotatedWith(Names.named(key)).toInstance(object);
	}
}
