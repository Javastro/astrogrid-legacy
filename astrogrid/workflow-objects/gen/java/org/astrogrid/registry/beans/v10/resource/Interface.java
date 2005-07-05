/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Interface.java,v 1.2 2005/07/05 08:26:56 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource;

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
 * A description of a service interface.
 *  
 *  Since this type is abstract, one must use an Interface subclass
 *  to describe an actual interface.
 *  
 *  Additional interface subtypes (beyond WebService and
 * WebBrowser) are 
 *  defined in the VODataService schema.
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:56 $
 */
public class Interface extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The URL (or base URL) that a client uses to access the
     *  service. How this URL is to be interpreted and used 
     *  depends on the specific Interface subclass
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.AccessURL _accessURL;


      //----------------/
     //- Constructors -/
    //----------------/

    public Interface() {
        super();
    } //-- org.astrogrid.registry.beans.v10.resource.Interface()


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
        
        if (obj instanceof Interface) {
        
            Interface temp = (Interface)obj;
            if (this._accessURL != null) {
                if (temp._accessURL == null) return false;
                else if (!(this._accessURL.equals(temp._accessURL))) 
                    return false;
            }
            else if (temp._accessURL != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'accessURL'. The field
     * 'accessURL' has the following description: The URL (or base
     * URL) that a client uses to access the
     *  service. How this URL is to be interpreted and used 
     *  depends on the specific Interface subclass
     *  
     * 
     * @return the value of field 'accessURL'.
     */
    public org.astrogrid.registry.beans.v10.resource.AccessURL getAccessURL()
    {
        return this._accessURL;
    } //-- org.astrogrid.registry.beans.v10.resource.AccessURL getAccessURL() 

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
     * Sets the value of field 'accessURL'. The field 'accessURL'
     * has the following description: The URL (or base URL) that a
     * client uses to access the
     *  service. How this URL is to be interpreted and used 
     *  depends on the specific Interface subclass
     *  
     * 
     * @param accessURL the value of field 'accessURL'.
     */
    public void setAccessURL(org.astrogrid.registry.beans.v10.resource.AccessURL accessURL)
    {
        this._accessURL = accessURL;
    } //-- void setAccessURL(org.astrogrid.registry.beans.v10.resource.AccessURL) 

    /**
     * Method unmarshalInterface
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.Interface unmarshalInterface(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.Interface) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.Interface.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.Interface unmarshalInterface(java.io.Reader) 

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
