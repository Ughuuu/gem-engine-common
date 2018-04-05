package com.gemengine.component.common;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

/**
 * The basic ComponentBase type. This needs to be extended by components that
 * will be used in the engine. Components hold data only, and the logic is
 * performed by {@link com.gemengine.system.common.SystemBase} or others that
 * extend that.
 *
 */
public abstract class BaseComponent {
	private final SetMultimap<Class<? extends BaseComponent>, BaseComponent> children = HashMultimap.create();
	private boolean enable;
	private BaseComponent parent;
	private final static ComponentRoot ROOT_COMPONENT = ComponentRoot.getInstance();

	protected ComponentBase(boolean enable) {
		this.enable = enable;
		parent = ROOT_COMPONENT;
		if (ROOT_COMPONENT != parent) {
			ROOT_COMPONENT.addChild(this);
		}
	}

	public void addChild(BaseComponent child) {
		child.parent.children.remove(child.getClass(), child);
		children.put(child.getClass(), child);
		child.parent = this;
	}

	public BaseComponent getChild(Class<? extends BaseComponent> childType) {
		Iterator<BaseComponent> componentIterator = children.get(childType).iterator();
		if (componentIterator.hasNext()) {
			return componentIterator.next();
		}
		return null;
	}

	public Collection<BaseComponent> getChildren() {
		return children.values();
	}

	public Set<BaseComponent> getChildren(Class<? extends BaseComponent> childType) {
		return children.get(childType);
	}

	public BaseComponent getParent() {
		return parent;
	}

	public boolean isEnable() {
		return enable;
	}

	public void remove() {
		parent.children.remove(this.getClass(), this);
		getChildren().stream().forEach(component -> component.parent = ROOT_COMPONENT);
	}

	public void removeChild(BaseComponent child) {
		children.remove(child.getClass(), child);
		child.setParent(ROOT_COMPONENT);
	}

	public void removeChildren(Class<? extends BaseComponent> childType) {
		Set<BaseComponent> typeChildren = children.get(childType);
		children.remove(childType, children);
		typeChildren.stream().forEach(child -> child.setParent(ROOT_COMPONENT));
	}

	public void removeParent() {
		parent.removeChild(this);
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setParent(BaseComponent parent) {
		if (parent == null) {
			ROOT_COMPONENT.addChild(this);
		} else {
			parent.addChild(this);
		}
	}
}
