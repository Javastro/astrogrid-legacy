/*
 * $Id: Stopable.java,v 1.2 2008/09/03 14:18:55 pah Exp $
 * 
 * Created on 23 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager;

/**
 * Interface indicating how to shutdown a component.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 8 May 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 * @TODO make this the same as whatever spring assumes to make configuration easier...
 * 
 */
public interface Stopable {

    public void shutdown();
}


/*
 * $Log: Stopable.java,v $
 * Revision 1.2  2008/09/03 14:18:55  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.2  2008/05/08 22:40:53  pah
 * basic UWS working
 *
 * Revision 1.1.2.1  2008/05/01 15:22:48  pah
 * updates to tool
 *
 */
