/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: GLUService.java,v 1.2 2007/01/04 16:26:26 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.dataservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * A web-based service that is described in a GLU registry.
 *  
 *  The AccessURL element gives the URL to the GLU record
 * describing 
 *  the service.
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:26 $
 */
public class GLUService extends org.astrogrid.registry.beans.v10.resource.Interface 
implements java.io.Serializable
{


      //----------------/
     //- Constructors -/
    //----------------/

    public GLUService() {
        super();
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.GLUService()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Note: hashCode() has not been overriden
     * 
     * @param obj
     */
    public boolean equals(java.lang.Object obj)
    {
        if ( this == obj )
            return true;
        
        if (super.equals(obj)==false)
            return false;
        
        if (obj instanceof GLUService) {
        
            GLUService temp = (GLUService)obj;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method isValid
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Method unmarshalGLUService
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.dataservice.GLUService unmarshalGLUService(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.dataservice.GLUService) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.dataservice.GLUService.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.GLUService unmarshalGLUService(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
