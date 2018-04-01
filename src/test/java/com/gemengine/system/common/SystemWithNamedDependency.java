package com.gemengine.system.common;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class SystemWithNamedDependency extends SystemBase {
	private final int testNumber;

	@Inject
	public SystemWithNamedDependency(@Named("testNumber") int testNumber) {
		this.testNumber = testNumber;
	}

	public int getTestNumber() {
		return testNumber;
	}
}
