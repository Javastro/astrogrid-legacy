package org.astrogrid.registry.common;

/**
 * Astrogrid Interface type for the CEA web service.
 * @author Kevin Benson
 *
 */
public class CEAInterfaceType implements InterfaceType {
    
    private static final String INTERFACE_NAME = "CeaService";
    
    public String getInterfaceType() {
        return INTERFACE_NAME;
    }
}