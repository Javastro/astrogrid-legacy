/*
 * $Id: TestService.java,v 1.2 2005/07/05 08:27:01 clq2 Exp $
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
 * This is the interface for a simple test service.
 * @author Paul Harrison (pharriso@eso.org) Mar 2, 2005
 * @version $Name:  $
 * @since iteration9
 */
public interface TestService {
    
    int addNumbers(int a, int b);
    float multiplyNumbers(float a, float b);

}
