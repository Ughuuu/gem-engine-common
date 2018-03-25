package com.gemengine.system.common;

import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.spi.InstanceBinding;

public class SystemInjectionModule extends AbstractModule{
	private final List<SystemBase> systemList;
	
	public SystemInjectionModule(List<SystemBase> systemList) {
		this.systemList = systemList;
	}

	@Override
	protected void configure() {
		for (SystemBase system : systemList) {
			makeMapping(genericCast(system.getClass()), system);
		}
	}

	private <T> void makeMapping(Class<T> systemClass, T system) {
		bind(systemClass).toInstance(system);
	}

	@SuppressWarnings("unchecked")
	private <T> Class<T> genericCast(Class<?> cls) {
		return (Class<T>) cls;
	}
}
