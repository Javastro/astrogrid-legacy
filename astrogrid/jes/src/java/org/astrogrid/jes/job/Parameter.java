/*$Id: Parameter.java,v 1.7 2004/02/27 00:46:03 nw Exp $
 * Created on 09-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.job;



/**
 * Interface defining what controller requires from a parameter.
 * absolutely nothing in this interface is used outside the o-model implementation. not accessed by the controller at all !
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Feb-2004
 */
public interface Parameter {
    //public abstract void setName(String name);
    //public abstract String getName();
   // public abstract String getType();
    //public abstract String getContents();
    //public abstract String getLocation();
    //public abstract void setContents(String string);
    //public abstract void setLocation(String url);
    /**
       */
   // public abstract void setType(String string);
    //public abstract void setParent(Tool parent);
    //public abstract Tool getParent();
   // public abstract boolean isRemoteReference();
  //  public abstract boolean isInStream();
}
/* 
$Log: Parameter.java,v $
Revision 1.7  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.5.2.3  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.5.2.2  2004/02/12 01:16:08  nw
analyzed code, stripped interfaces of all unused methods.

Revision 1.5.2.1  2004/02/09 12:39:06  nw
isolated existing datamodel.
refactored to extract interfaces from all current datamodel classes in org.astrogrid.jes.job.
moved implementation of interfaces to org.astrogrid.jes.impl
refactored so services reference interface rather than current implementation
 
*/