/*$Id: ReturnDoc.java,v 1.1 2005/02/21 11:25:07 nw Exp $
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

import org.astrogrid.desktop.service.conversion.DefaultHtmlTransformer;
import org.astrogrid.desktop.service.conversion.DefaultPlainTransformer;
import org.astrogrid.desktop.service.conversion.DefaultXmlTransformer;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.NOPTransformer;

/** Documentation annotation for a return value.
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *
 */
public class ReturnDoc {

    /** Construct a new ReturnDoc
     * 
     */
    public ReturnDoc(String description) {
        super();
        this.description = description;
        this.rts = new ResultTransformerSet();
    }
    
    protected String description;
    protected ResultTransformerSet rts;
    
    
    
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Return: ");
        buffer.append(description);
        return buffer.toString();
    }
    public ResultTransformerSet getRts() {
        return this.rts;
    }
    public void setRts(ResultTransformerSet rts) {
        this.rts = rts;
    }
    // delegate methods fgor result transformer set.
    public void setHtmlTransformer(Transformer htmlTransformer) {
        this.rts.setHtmlTransformer(htmlTransformer);
    }
    public void setPlainTransformer(Transformer plainTransformer) {
        this.rts.setPlainTransformer(plainTransformer);
    }
    public void setXmlrpcTransformer(Transformer xmlrpcTransformer) {
        this.rts.setXmlrpcTransformer(xmlrpcTransformer);
    }
    public void setXmlrpcType(String xmlrpcType) {
        this.rts.setXmlrpcType(xmlrpcType);
    }
    public void setXmlTransformer(Transformer xmlTransformer) {
        this.rts.setXmlTransformer(xmlTransformer);
    }
}


/* 
$Log: ReturnDoc.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/