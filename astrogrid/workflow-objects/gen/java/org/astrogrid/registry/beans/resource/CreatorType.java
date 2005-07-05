/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CreatorType.java,v 1.2 2005/07/05 08:26:56 clq2 Exp $
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
 * Class CreatorType.
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:56 $
 */
public class CreatorType extends org.astrogrid.registry.beans.resource.NameReferenceType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * URL pointing to a graphical logo, which may be used to help 
     *  identify the information source
     *  
     */
    private java.lang.String _logo;


      //----------------/
     //- Constructors -/
    //----------------/

    public CreatorType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.CreatorType()


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
        
        if (obj instanceof CreatorType) {
        
            CreatorType temp = (CreatorType)obj;
            if (this._logo != null) {
                if (temp._logo == null) return false;
                else if (!(this._logo.equals(temp._logo))) 
                    return false;
            }
            else if (temp._logo != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'logo'. The field 'logo' has the
     * following description: URL pointing to a graphical logo,
     * which may be used to help 
     *  identify the information source
     *  
     * 
     * @return the value of field 'logo'.
     */
    public java.lang.String getLogo()
    {
        return this._logo;
    } //-- java.lang.String getLogo() 

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
     * Sets the value of field 'logo'. The field 'logo' has the
     * following description: URL pointing to a graphical logo,
     * which may be used to help 
     *  identify the information source
     *  
     * 
     * @param logo the value of field 'logo'.
     */
    public void setLogo(java.lang.String logo)
    {
        this._logo = logo;
    } //-- void setLogo(java.lang.String) 

    /**
     * Method unmarshalCreatorType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.CreatorType unmarshalCreatorType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.CreatorType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.CreatorType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.CreatorType unmarshalCreatorType(java.io.Reader) 

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
