/*
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.astrogrid.acr.ACRException;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;

/**
 * @author jdt@roe.ac.uk
 * @date 22-Nov-2005
 */
public interface PlasticHubListenerInternal extends PlasticHubListener{//NWW - needs to extend public interface, otherwise won't get found

    /**
     * Instruct the hub to open a web page listing details of all the registered apps.
     * This is meant to be a bit more human friendly than the other info methods.
     * @throws IOException 
     * @throws ACRException 
     *
     */
    void prettyPrintRegisteredApps() throws IOException, ACRException;
    /**
     * Switch off registration/unregistration notifications via jdic
     * @param enable true to enable, falst to disable
     */
    void setNotificationsEnabled(boolean enable);
    
    /**
     * Used by the AR for internal registration - perhaps we might give ourselves some special 
     * privs.
     * @param name
     * @param supportedOperations
     * @param caller
     */
    URI registerSelf(String name, List supportedOperations, PlasticListener caller);
}
