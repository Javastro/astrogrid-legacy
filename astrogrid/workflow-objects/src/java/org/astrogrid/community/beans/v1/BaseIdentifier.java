/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: BaseIdentifier.java,v 1.10 2004/03/10 13:58:29 pah Exp $
 */

package org.astrogrid.community.beans.v1;

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
 * The identifier base class. Contains the name and the community
 * 
 * @version $Revision: 1.10 $ $Date: 2004/03/10 13:58:29 $
 */
public class BaseIdentifier extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _community
     */
    private java.lang.String _community;


      //----------------/
     //- Constructors -/
    //----------------/

    public BaseIdentifier() {
        super();
    } //-- org.astrogrid.community.beans.v1.BaseIdentifier()


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
        
        if (obj instanceof BaseIdentifier) {
        
            BaseIdentifier temp = (BaseIdentifier)obj;
            if (this._name != null) {
                if (temp._name == null) return false;
                else if (!(this._name.equals(temp._name))) 
                    return false;
            }
            else if (temp._name != null)
                return false;
            if (this._community != null) {
                if (temp._community == null) return false;
                else if (!(this._community.equals(temp._community))) 
                    return false;
            }
            else if (temp._community != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'community'.
     * 
     * @return the value of field 'community'.
     */
    public java.lang.String getCommunity()
    {
        return this._community;
    } //-- java.lang.String getCommunity() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

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
     * Sets the value of field 'community'.
     * 
     * @param community the value of field 'community'.
     */
    public void setCommunity(java.lang.String community)
    {
        this._community = community;
    } //-- void setCommunity(java.lang.String) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Method unmarshalBaseIdentifier
     * 
     * @param reader
     */
    public static org.astrogrid.community.beans.v1.BaseIdentifier unmarshalBaseIdentifier(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.community.beans.v1.BaseIdentifier) Unmarshaller.unmarshal(org.astrogrid.community.beans.v1.BaseIdentifier.class, reader);
    } //-- org.astrogrid.community.beans.v1.BaseIdentifier unmarshalBaseIdentifier(java.io.Reader) 

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
