/*$Id: ResponseConvertorType.java,v 1.1 2003/11/11 14:43:33 nw Exp $
 * Created on 02-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap.response;


/** Enumeration of the available response convertors
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Oct-2003
 *
 */
public class ResponseConvertorType {

    /**
     * 
     */
    private ResponseConvertorType() {

    }
    
    public final static String XSLT = "xslt";
    public final static String SCRIPT = "script";
    public final static String REGEXP = "regexp";
    
    /** create appropriate class of convertor, based on type string */
    public static ResponseConvertor createConvertor(String type) {
                 if (ResponseConvertorType.REGEXP.equalsIgnoreCase(type)) {
                     return new RegExpConvertor();
                 }
                 if (ResponseConvertorType.XSLT.equalsIgnoreCase(type)) {
                     return new XsltConvertor();
                 }
                 if (ResponseConvertorType.SCRIPT.equalsIgnoreCase(type)) {
                     return new ScriptConvertor();
                 }
        throw new IllegalArgumentException("Response Object: Unknown Type");
    }

}


/* 
$Log: ResponseConvertorType.java,v $
Revision 1.1  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/