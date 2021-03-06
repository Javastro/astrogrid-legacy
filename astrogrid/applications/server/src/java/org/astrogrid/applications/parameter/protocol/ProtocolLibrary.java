/*$Id: ProtocolLibrary.java,v 1.5 2008/09/04 19:10:53 pah Exp $
 * Created on 16-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.parameter.protocol;

import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.security.SecurityGuard;

import java.net.URI;
import java.net.URISyntaxException;

/** A library of protocol-handling code, for working with External Values
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public interface ProtocolLibrary {
    /** build an external value for a particular indirect parameter value
     * 
     * @param pval parameter to build external value for
     * @param secGuard TODO
     * @return an external value for this parameter.
     * @throws InaccessibleExternalValueException - if the external value cannot be gained - e.g. cannot resolve, no path
     * @throws UnrecognizedProtocolException - if the indirection uri format is not recognized.
     */
    ExternalValue getExternalValue(ParameterValue pval, SecurityGuard secGuard) throws InaccessibleExternalValueException, UnrecognizedProtocolException;
    
    /** build an external value, direct from a URI
     * @param location location to build external value for
     * @param secGuard TODO
     * @return an external value for this location
     * @throws InaccessibleExternalValueException - if the external value cannot be accessed - e.g. cannot resolve, 
     * @throws UnrecognizedProtocolException - if the uri protocol / scheme is not recognized */
    ExternalValue getExternalValue(URI location, SecurityGuard secGuard) throws InaccessibleExternalValueException, UnrecognizedProtocolException;

    /** build an external value, direct from a URI String
     * @param location String representation of URI location to build external value for
     * @param secGuard TODO
     * @return an external value for this location
     * @throws InaccessibleExternalValueException - if the external value cannot be accessed - e.g. cannot resolve, 
     * @throws UnrecognizedProtocolException - if the uri protocol / scheme is not recognized */
    ExternalValue getExternalValue(String location, SecurityGuard secGuard) throws InaccessibleExternalValueException, UnrecognizedProtocolException, URISyntaxException;

    
    
    /** list the protocols supported in this library */
    String[] listSupportedProtocols();
    
    /** return true if this library supports the protcol in question */
    boolean isProtocolSupported(String protocol);
    
    
}