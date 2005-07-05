/*
 * $Id: TestServiceImplementation.java,v 1.2 2005/07/05 08:27:01 clq2 Exp $
 * 
 * Created on Mar 2, 2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.ws;

/**
 * @author Paul Harrison (pharriso@eso.org) Mar 2, 2005
 * @version $Name:  $
 * @since iteration9
 */
public class TestServiceImplementation implements TestService {

    /* (non-Javadoc)
     * @see org.astrogrid.applications.ws.TestService#addNumbers(int, int)
     */
    public int addNumbers(int a, int b) {
        return a+b;
    }

    /* (non-Javadoc)
     * @see org.astrogrid.applications.ws.TestService#multiplyNumbers(float, float)
     */
    public float multiplyNumbers(float a, float b) {
      return a*b;
     }

}
