package com.gemengine.system.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.spi.InstanceBinding;

/**
 * 
 * 
 * @author Dragos
 *
 */
public class SystemManager {
	private final Logger logger = LoggerFactory.getLogger(SystemManager.class);
	private final Set<Class<? extends SystemBase>> systemTypesSet = new HashSet<>();
	private final List<SystemBase> systemArray = new ArrayList<>();
	private Injector injector;

	public void putSystemType(Class<? extends SystemBase> systemType) {
		systemTypesSet.add(systemType);
	}
	
	public void removeSystemType(Class<? extends SystemBase> systemType) {
		systemTypesSet.remove(systemType);
	}
	
	public void doOperation(Consumer<SystemBase> consumer) {
		for (SystemBase system : systemArray) {
			consumer.accept(system);
        }
	}
	
	public void instantiateSystems() {
		injector = Guice.createInjector(new SystemInjectionModule(systemArray));
		final Map<Class<?>, SystemBase> systemMap = new HashMap<>();
		for(Class<? extends SystemBase> systemType : systemTypesSet) {
			try {
				if(!systemMap.containsKey(systemType)) {
					injector.getInstance(systemType);
				}
				addInstances(systemMap);
			} catch(Exception exception) {
				logger.error("The following system {} failed to instantiate", systemType, exception);
			}
		}
		systemArray.clear();
		systemArray.addAll(systemMap.values());
		Collections.sort(systemArray);
	}

	private void addInstances(Map<Class<?>, SystemBase> systemMap) {
		for (Entry<Key<?>, Binding<?>> entry : injector.getAllBindings().entrySet()) {
			Class<?> systemType = entry.getKey().getTypeLiteral().getRawType();
			final Binding<?> value = entry.getValue();
			if ((value instanceof InstanceBinding) &&
					extendsType(systemType, SystemBase.class)) {
				InstanceBinding<?> instanceBinding = (InstanceBinding<?>) value;
				SystemBase systemInstance = (SystemBase) instanceBinding.getInstance();
				systemMap.put(systemType, systemInstance);
			}
		}
	}

	private static boolean extendsType(Class<?> type, Class<?> extendsType) {
		if (type == null || type.equals(Object.class)) {
			return false;
		}
		return type.equals(extendsType) || extendsType(type.getSuperclass(), extendsType);
	}
}
