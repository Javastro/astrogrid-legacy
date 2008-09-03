/*
 * $Id: Identify.java,v 1.2 2008/09/03 14:18:43 pah Exp $
 * 
 * Created on 13 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

/**
 * An object can identify itself. This is used principally in the {@link MapDecoratedList}.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 19 Apr 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public interface Identify {

    public String getId();
}


/*
 * $Log: Identify.java,v $
 * Revision 1.2  2008/09/03 14:18:43  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.2  2008/04/23 14:14:30  pah
 * ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749
 *
 * Revision 1.1.2.1  2008/03/19 23:10:53  pah
 * First stage of refactoring done - code compiles again - not all unit tests passed
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
