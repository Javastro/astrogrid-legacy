/*$Id: Checkable.java,v 1.1 2003/11/11 14:43:33 nw Exp $
 * Created on 10-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Nov-2003
 *
 */
public interface Checkable {
    public abstract Throwable getError();
    public abstract boolean hasError();
}

/* 
$Log: Checkable.java,v $
Revision 1.1  2003/11/11 14:43:33  nw
added unit tests.
basic working version
 
*/