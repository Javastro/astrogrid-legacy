/*$Id: ParameterType.java,v 1.1 2003/10/12 21:39:34 nw Exp $
 * Created on 02-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap;

/** enumeration of Parameter Types. - types passed into a request.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Oct-2003
 *
 */
public class ParameterType {

    /**
     * all statics here - don't want a public constructor
     */
    private ParameterType() {        
    }
    
    public static final String FLAG = "flag";
    public static final String BOOLEAN = "boolean";
    public static final String INT = "int";
    public static final String FLOAT = "float";
    public static final String STRING = "string";
    // may want to add some more types later - form posted binary data, etc.

}


/* 
$Log: ParameterType.java,v $
Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/