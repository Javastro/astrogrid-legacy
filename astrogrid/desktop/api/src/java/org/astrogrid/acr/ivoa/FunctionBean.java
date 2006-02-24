/*$Id: FunctionBean.java,v 1.1 2006/02/24 12:17:52 nw Exp $
 * Created on 22-Feb-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.ivoa;

import org.astrogrid.acr.astrogrid.ParameterBean;

/**
 * description of one ADQL function
 * @since 1.9
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Feb-2006
 *
 */
public class FunctionBean {

    /** Construct a new FunctionBean
     * @param name
     * @param description
     * @param parameters
     */
    public FunctionBean(String name, String description, ParameterBean[] parameters) {
        super();
        this.name = name;
        this.description = description;
        this.parameters = parameters;
    }

    
    private final String name;
    private final String description;
    private final ParameterBean[] parameters;
    /** description of this function */
    public String getDescription() {
        return this.description;
    }
    /** name of this function */
    public String getName() {
        return this.name;
    }
    /** descriptin of parameters for this function 
     * @return an array of {@link ParameterBean}, where only name, description and type will be specified
     * */
    public ParameterBean[] getParameters() {
        return this.parameters;
    }
    

}


/* 
$Log: FunctionBean.java,v $
Revision 1.1  2006/02/24 12:17:52  nw
added interfaces for skynode
 
*/