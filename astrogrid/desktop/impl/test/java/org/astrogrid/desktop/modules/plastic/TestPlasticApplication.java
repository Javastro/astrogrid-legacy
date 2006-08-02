/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.List;

import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.incoming.handlers.MessageHandler;

public interface TestPlasticApplication {
    /**
     * Keys into the properties file to define application metadata
     */
    public static final String NAME="NAME";
    public static final String DESC="DESC";
    public static final String IVORN="IVORN";
    public static final String LOGOURL="LOGOURL";
    public static final String VERSION="VERSION";
    /**
     * Different clients will have slightly different registration methods,
     * so we leave the registration to them.
     * @param hub
     * @return
     */
    public URI registerWith(PlasticHubListener hub, String name);
    public List getMessages();
    public void addHandler(MessageHandler h) ;
}