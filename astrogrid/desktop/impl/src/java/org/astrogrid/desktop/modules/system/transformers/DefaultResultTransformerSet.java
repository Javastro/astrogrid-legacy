/*$Id: DefaultResultTransformerSet.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 09-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.transformers;


import org.apache.commons.collections.Transformer;

/** 
 *  - default - configured to return strings.
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Feb-2005
 *
 */
public class DefaultResultTransformerSet implements ResultTransformerSet {

    /** Construct a new TransformerSet
     * 
     */
    protected DefaultResultTransformerSet() {
        super();
        this.htmlTransformer = DefaultHtmlTransformer.getInstance();
        this.plainTransformer = DefaultPlainTransformer.getInstance();
        this.xmlTransformer = DefaultXmlTransformer.getInstance();
        this.xmlrpcTransformer = TypeStructureTransformer.getInstance();
    }

    /** transformer to apply to computed value to produce html response */
    protected Transformer htmlTransformer;
    /** transformer to apply to computed value to produce plain text response */
    protected Transformer plainTransformer;
    /** transformer to apply to computed value to produce xml response */
    protected Transformer xmlTransformer;
    /** transformer to apply to compute value to produce response suiutable for returning in xmlrpc. */
    protected Transformer xmlrpcTransformer;

    public Transformer getHtmlTransformer() {
        return this.htmlTransformer;
    }

    protected void setHtmlTransformer(Transformer htmlTransformer) {
        this.htmlTransformer = htmlTransformer;
    }

    public Transformer getPlainTransformer() {
        return this.plainTransformer;
    }

    protected void setPlainTransformer(Transformer plainTransformer) {
        this.plainTransformer = plainTransformer;
    }

    public Transformer getXmlrpcTransformer() {
        return this.xmlrpcTransformer;
    }

    protected void setXmlrpcTransformer(Transformer xmlrpcTransformer) {
        this.xmlrpcTransformer = xmlrpcTransformer;
    }

    public Transformer getXmlTransformer() {
        return this.xmlTransformer;
    }

    protected void setXmlTransformer(Transformer xmlTransformer) {
        this.xmlTransformer = xmlTransformer;
    }

 
    
    public static ResultTransformerSet getInstance() {
        return theInstance;
    }
    
    private static ResultTransformerSet theInstance = new DefaultResultTransformerSet(); 

}


/* 
$Log: DefaultResultTransformerSet.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/