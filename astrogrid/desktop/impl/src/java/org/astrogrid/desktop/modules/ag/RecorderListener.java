/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.util.EventListener;

import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.Folder;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.MessageContainer;

/** listens to events from the message recorder */

public interface RecorderListener extends EventListener {
    public void messageReceived(Folder f,MessageContainer msg);
}