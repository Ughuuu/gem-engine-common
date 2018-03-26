package com.gemengine.system.common;

import org.junit.Test;
import static org.junit.Assert.*;

public class SystemTest {
    @Test public void addAndRemoveTestSystem() {
    	SystemManager systemManager = new SystemManager();
    	systemManager.putSystemType(TestSystem.class);
    	systemManager.instantiateSystems();
    	
    	systemManager.doOperation((systemBase) ->
    	assertEquals("System should be TestSystem", TestSystem.class, systemBase.getClass()));
    	systemManager.removeSystemType(TestSystem.class);
    	systemManager.instantiateSystems();
    	
    	systemManager.doOperation((systemBase) ->
    	assertNotEquals("System should be TestSystem", TestSystem.class, systemBase.getClass()));
    	systemManager.putSystemType(TestSystem.class);
    	systemManager.instantiateSystems();
    	
    	systemManager.doOperation((systemBase) ->
    	assertEquals("System should be TestSystem", TestSystem.class, systemBase.getClass()));
    }
}
