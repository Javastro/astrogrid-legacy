/*$Id: ICardinality.java,v 1.2 2004/02/25 10:57:43 nw Exp $
 * Created on 24-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2004
 *
 */
public interface ICardinality {
    /**
     * Getter for maximum cardinality.
     * 
     * @return maximum cardinality as an integer
      */
    public abstract int getMaximum();
    /**
     * Getter for minimum cardinality.
     * 
     * @return minimum cardinality as an integer
      */
    public abstract int getMinimum();
    /**
     * Tester for presence of unlimited maximum cardinality.
     * 
     * @return boolean true for unlimited maximum cardinality
     */
    public abstract boolean isUnlimited();
}
/* 
$Log: ICardinality.java,v $
Revision 1.2  2004/02/25 10:57:43  nw
merged in branch nww-itn05-bz#140 (refactor in preparation for changing object model)

Revision 1.1.2.1  2004/02/24 15:35:46  nw
extracted public interface from each implementation class.
altered types to reference interface rather than implementation whever possible.
added factory and manager facade at the front
 
*/