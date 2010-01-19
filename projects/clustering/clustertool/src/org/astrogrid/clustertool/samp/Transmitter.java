/*
 * $$Id: Transmitter.java,v 1.1 2010/01/19 21:25:14 pah Exp $$
 *
 * Created on 28-Aug-2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid
 * Software License, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */ 
package org.astrogrid.clustertool.samp;

import javax.swing.Action;
import javax.swing.JMenu;

/**
 * Interface for an action which notionally sends some information from
 * this application to one or more other applications.
 *
 * @author   Mark Taylor
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) Sep 15, 2009
 * @since    4 Sep 2008
 */
public interface Transmitter {

    /**
     * Returns an action which sends the information to all appropriate
     * recipients.
     *
     * @return  broadcast action
     */
    Action getBroadcastAction();

    /**
     * Returns a per-application menu which allows sending the information
     * to any single one of the appropriate recipients.
     *
     * @return   send menu
     */
    JMenu createSendMenu();

    /**
     * Sets whether the send actions controlled by this transmitter should
     * be enabled or not.  This is an AND-like restriction - the actions
     * may still be disabled for other reasons (e.g. no hub connection).
     *
     * @param  isEnabled  true iff actions may be invoked
     */
    void setEnabled( boolean isEnabled );
}
