package org.astrogrid.datacenter.config;

import junit.framework.TestCase;

/**
 * Abstract JUnit test case for ConfigurableTest
 * - extend to test implementations of Configurable.
 */

public abstract class ConfigurableTestSpec extends TestCase {
	//declare reusable objects to be used across multiple tests
	public ConfigurableTestSpec(String name) {
		super(name);
	}


    /** extenders to implement this method */
    protected abstract Configurable createConfigurable();

    protected Configuration var1  = new Configuration() {
    public String getProperty(String s,String k) {
        return "";
    }
};

	public void testConfigurationNull() {

		//insert code testing basic functionality
        Configurable var0 = createConfigurable();
		var1 = var0.getConfiguration();
        assertNull(var1);
        var0.setConfiguration(null);
        var1 = var0.getConfiguration();
        assertNull(var1);

	}
	public void testConfigurationVal() {
        Configurable var0 = createConfigurable();
        var0.setConfiguration(var1);
		Configuration var2 = var0.getConfiguration();
        assertEquals(var1,var2);
        
	}
}