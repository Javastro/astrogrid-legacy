package org.astrogrid.registry.common;

/**
 * Astrogrid Interface type for the Myspace web service.
 * @author Kevin Benson
 *
 */
public class MyspaceInterfaceType implements InterfaceType {
    
    private static final String INTERFACE_NAME = "Myspace";
    
    public String getInterfaceType() {
        return INTERFACE_NAME;
    }
}