package org.votech.plastic;

import java.util.Vector;

/**
 * The interface that java-rmi Plastic-compatible applications should support.
 * 
 * @see <a href="http://plastic.sourceforge.net/">http://plastic.sourceforge.net</a>
 * @author jdt@roe.ac.uk
 * @date 11-Oct-2005
 * @version 0.1 
 * FIXME check that the xml on the wire is correct (maybe add in logging permanently?) 
 * FIXME R interface
 */
public interface PlasticListener {
    /**
     * The current version of Plastic defined by this interface.
     */
    String CURRENT_VERSION = "0.1";

    /**
     * Request that the application perform an action based on a message.
     * 
     * @param sender the ID of the originating application
     * @param message the string representing the action.
     * @param args any argumentss to pass
     * @return any return value of the action
     * @see <a href="http://plastic.sourceforge.net">http://plastic.sourceforge.net</a>
     */
    public Object perform(String sender, String message, Vector args);

}
