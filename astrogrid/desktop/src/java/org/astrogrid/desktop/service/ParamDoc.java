/*$Id: ParamDoc.java,v 1.1 2005/02/21 11:25:07 nw Exp $
 * Created on 31-Jan-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.service;

import org.astrogrid.desktop.service.conversion.DefaultConverter;

import org.apache.commons.beanutils.Converter;

/** Documentation annotation for a parameter
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *
 */
public class ParamDoc {

    /** Construct a new ParamDoc
     * 
     */
    public ParamDoc(String name,String description) {
        this.name = name;
        this.description = description;
        this.converter = DefaultConverter.getInstance();
        this.xmlrpcType = "string";
    }
    protected String name;
    /** simple type used in xmlrpc - one of  'int', 'boolean' , 'string', 'double', 'dateTime.iso8601', 'base64', 'struct', 'array' */
    protected String xmlrpcType;
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    protected String description;
    /** object used to convert parameter from input format (probaby string) to required type for calling implementation method.*/
    protected Converter converter;
    
    public Converter getConverter() {
        return this.converter;
    }
    public void setConverter(Converter transformer) {
        this.converter = transformer;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Parameter: ");
        buffer.append(name);
        buffer.append(" - ");
        buffer.append(description);
        return buffer.toString();
    }
    public String getXmlrpcType() {
        return this.xmlrpcType;
    }
    public void setXmlrpcType(String xmlrpcType) {
        this.xmlrpcType = xmlrpcType;
    }
}


/* 
$Log: ParamDoc.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/