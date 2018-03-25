package com.gemengine.system.common;

import com.google.inject.Inject;

public class TestSystem extends SystemBase{
	@Inject
	public TestSystem(TestSystemDependency testSystemDependency) {
	}
}
