package org.astrogrid.registry.common;

/**
 * Astrogrid Interface type for the FileStore web service.
 * @author Kevin Benson
 *
 */
public class CommunityInterfaceType implements InterfaceType {
    
    private static final String INTERFACE_NAME = "CommunityServerKind";
    
    public String getInterfaceType() {
        return INTERFACE_NAME;
    }
    
}