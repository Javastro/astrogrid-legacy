/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Alias.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

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
 * Class Alias.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class Alias extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _negate
     */
    private boolean _negate;

    /**
     * keeps track of state for field: _negate
     */
    private boolean _has_negate;

    /**
     * Field _name
     */
    private java.lang.String _name;


      //----------------/
     //- Constructors -/
    //----------------/

    public Alias() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Alias()


      //-----------/
     //- Methods -/
    //-----------/

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
     * Returns the value of field 'negate'.
     * 
     * @return the value of field 'negate'.
     */
    public boolean getNegate()
    {
        return this._negate;
    } //-- boolean getNegate() 

    /**
     * Method hasNegate
     */
    public boolean hasNegate()
    {
        return this._has_negate;
    } //-- boolean hasNegate() 

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
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'negate'.
     * 
     * @param negate the value of field 'negate'.
     */
    public void setNegate(boolean negate)
    {
        this._negate = negate;
        this._has_negate = true;
    } //-- void setNegate(boolean) 

    /**
     * Method unmarshalAlias
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.Alias unmarshalAlias(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.Alias) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.Alias.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Alias unmarshalAlias(java.io.Reader) 

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
