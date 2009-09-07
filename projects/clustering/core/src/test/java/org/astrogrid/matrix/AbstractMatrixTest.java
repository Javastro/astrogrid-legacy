/*
 * $Id: AbstractMatrixTest.java,v 1.1 2009/09/07 16:06:11 pah Exp $
 * 
 * Created on 11 Dec 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.matrix;

import static org.junit.Assert.fail;

public abstract class AbstractMatrixTest {

    public AbstractMatrixTest() {
        super();
    }

    protected void assertResult(Matrix result, Matrix expected, String func) {
        expected.add(-1.0, result);
        double norm = expected.norm(Matrix.Norm.Maxvalue);
        if(Math.abs(norm)> 1e-8){
            fail(func+ " function does not return correct value - diff="+norm);
        }
    }

}

/*
 * $Log: AbstractMatrixTest.java,v $
 * Revision 1.1  2009/09/07 16:06:11  pah
 * initial transcription of the core
 *
 */
