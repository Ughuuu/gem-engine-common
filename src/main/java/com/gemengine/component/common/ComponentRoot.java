package com.gemengine.component.common;

public class ComponentRoot extends BaseComponent{
	private final static ComponentRoot instance = new ComponentRoot(true);
	private ComponentRoot(boolean enable) {
		super(enable);
	}
	
	public static ComponentRoot getInstance() {
		return instance;
	}
}
