/*
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.votech.plastic.PlasticHubListener;

/**
 * The "core" hub application. This interface contains methods that aren't part of the plastic spec, but treat the hub
 * as a PlasticListener implementation in its own right, able to send and receive messages. The methods are mainly
 * convenience methods to make the Workbench UI a bit more friendly.
 * 
 * @author jdt@roe.ac.uk
 * @date 03-Nov-2005
 */
public interface MessengerInternal {
    String getHubPlasticVersion();

    String getHubName();

    String getHubIvorn();

    Map getPlasticVersions();

    Map getNames();

    Map getIvorns();
    
    Map getIconURLs();

    Map echo(String greeting);

    Map sendNoArgMessage(URI message);

    Map sendMessage(URI message, List args);

    URI registerWith(PlasticHubListener hub);
}
