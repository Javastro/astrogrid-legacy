/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: VOResource.java,v 1.14 2007/01/04 16:26:23 clq2 Exp $
 */

package org.astrogrid.registry.beans.resource;

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
 * A description of a single VO Resource
 *  
 *  This element is used as a general container for a single
 * resource
 *  descriptions and can be used as a root element. 
 *  
 * 
 * @version $Revision: 1.14 $ $Date: 2007/01/04 16:26:23 $
 */
public class VOResource extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Any entity that is describable and identifiable by a IVOA
     * Identifier.
     *  
     */
    private org.astrogrid.registry.beans.resource.ResourceType _resource;


      //----------------/
     //- Constructors -/
    //----------------/

    public VOResource() {
        super();
    } //-- org.astrogrid.registry.beans.resource.VOResource()


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
        
        if (obj instanceof VOResource) {
        
            VOResource temp = (VOResource)obj;
            if (this._resource != null) {
                if (temp._resource == null) return false;
                else if (!(this._resource.equals(temp._resource))) 
                    return false;
            }
            else if (temp._resource != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'resource'. The field 'resource'
     * has the following description: Any entity that is
     * describable and identifiable by a IVOA Identifier.
     *  
     * 
     * @return the value of field 'resource'.
     */
    public org.astrogrid.registry.beans.resource.ResourceType getResource()
    {
        return this._resource;
    } //-- org.astrogrid.registry.beans.resource.ResourceType getResource() 

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
     * Sets the value of field 'resource'. The field 'resource' has
     * the following description: Any entity that is describable
     * and identifiable by a IVOA Identifier.
     *  
     * 
     * @param resource the value of field 'resource'.
     */
    public void setResource(org.astrogrid.registry.beans.resource.ResourceType resource)
    {
        this._resource = resource;
    } //-- void setResource(org.astrogrid.registry.beans.resource.ResourceType) 

    /**
     * Method unmarshalVOResource
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.VOResource unmarshalVOResource(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.VOResource) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VOResource.class, reader);
    } //-- org.astrogrid.registry.beans.resource.VOResource unmarshalVOResource(java.io.Reader) 

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
