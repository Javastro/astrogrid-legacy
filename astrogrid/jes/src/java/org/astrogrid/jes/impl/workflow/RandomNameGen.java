/*$Id: RandomNameGen.java,v 1.2 2005/04/25 12:13:54 clq2 Exp $
 * Created on 11-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.impl.workflow;

import org.astrogrid.common.namegen.NameGen;

import java.util.Random;

/** NameGen implementation that preserves old behaviour - random numbers and system time.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Apr-2005
 *
 */
public class RandomNameGen implements NameGen {

    /** Construct a new RandomNameGen
     * 
     */
    public RandomNameGen() {
        super();
    }
    private static Random rand = new Random();

    /**
     * @see org.astrogrid.jes.impl.workflow.NameGen#next()
     */
    public String next() {
        StringBuffer result = new StringBuffer();
         result.append( System.currentTimeMillis()) 
        .append( ':' )
        .append(Math.abs( rand.nextInt()));
         return result.toString();
    }

}


/* 
$Log: RandomNameGen.java,v $
Revision 1.2  2005/04/25 12:13:54  clq2
jes-nww-776-again

Revision 1.1.2.1  2005/04/11 13:55:57  nw
started using common-namegen
 
*/