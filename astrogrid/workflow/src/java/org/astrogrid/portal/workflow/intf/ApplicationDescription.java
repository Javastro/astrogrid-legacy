/*$Id: ApplicationDescription.java,v 1.1 2004/03/09 15:31:21 nw Exp $
 * Created on 09-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;

import org.astrogrid.applications.beans.v1.ApplicationBase;
import org.astrogrid.applications.beans.v1.InterfacesType;
import org.astrogrid.applications.beans.v1.Parameters;
import org.astrogrid.workflow.beans.v1.Tool;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

import java.io.IOException;
import java.io.Writer;

/** Description of an application - wraps castor object returned from registry. 
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Mar-2004
 *
 */
public class ApplicationDescription extends ApplicationBase {
    /** Construct a new ApplicationDescription
     * 
     */
    public ApplicationDescription(ApplicationBase app) {
        this.app = app;
    }
    protected final ApplicationBase app;
    
    /** construct a new, initialized tool from this descriptor 
     * @todo implement and test*/
    public Tool createToolFromDescription() {
        Tool t = new Tool();
        return t;
    }
    
    /** verify that a tool object matches what is expected by the application (as defined by this descriptor) 
     * @todo implement*/
    public boolean isValid(Tool t) {
        return true;
    }
    
    
    /**
     * @see org.astrogrid.applications.beans.v1.ApplicationBase#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return app.equals(obj);
    }

    /**
     * @see org.astrogrid.applications.beans.v1.ApplicationBase#getInstanceClass()
     */
    public String getInstanceClass() {
        return app.getInstanceClass();
    }

    /**
     * @see org.astrogrid.applications.beans.v1.ApplicationBase#getInterfaces()
     */
    public InterfacesType getInterfaces() {
        return app.getInterfaces();
    }

    /**
     * @see org.astrogrid.applications.beans.v1.ApplicationBase#getName()
     */
    public String getName() {
        return app.getName();
    }

    /**
     * @see org.astrogrid.applications.beans.v1.ApplicationBase#getParameters()
     */
    public Parameters getParameters() {
        return app.getParameters();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return app.hashCode();
    }

    /**
     * @see org.astrogrid.applications.beans.v1.ApplicationBase#isValid()
     */
    public boolean isValid() {
        return app.isValid();
    }

    /**
     * @see org.astrogrid.applications.beans.v1.ApplicationBase#marshal(java.io.Writer)
     */
    public void marshal(Writer arg0) throws MarshalException, ValidationException {
        app.marshal(arg0);
    }

    /**
     * @see org.astrogrid.applications.beans.v1.ApplicationBase#marshal(org.xml.sax.ContentHandler)
     */
    public void marshal(ContentHandler arg0) throws IOException, MarshalException, ValidationException {
        app.marshal(arg0);
    }

    /**
     * @see org.astrogrid.applications.beans.v1.ApplicationBase#setInstanceClass(java.lang.String)
     */
    public void setInstanceClass(String arg0) {
        app.setInstanceClass(arg0);
    }

    /**
     * @see org.astrogrid.applications.beans.v1.ApplicationBase#setInterfaces(org.astrogrid.applications.beans.v1.InterfacesType)
     */
    public void setInterfaces(InterfacesType arg0) {
        app.setInterfaces(arg0);
    }

    /**
     * @see org.astrogrid.applications.beans.v1.ApplicationBase#setName(java.lang.String)
     */
    public void setName(String arg0) {
        app.setName(arg0);
    }

    /**
     * @see org.astrogrid.applications.beans.v1.ApplicationBase#setParameters(org.astrogrid.applications.beans.v1.Parameters)
     */
    public void setParameters(Parameters arg0) {
        app.setParameters(arg0);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return app.toString();
    }

    /**
     * @see org.astrogrid.applications.beans.v1.ApplicationBase#validate()
     */
    public void validate() throws ValidationException {
        app.validate();
    }

}


/* 
$Log: ApplicationDescription.java,v $
Revision 1.1  2004/03/09 15:31:21  nw
represents the initerface to an application
 
*/