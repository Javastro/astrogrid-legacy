/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ParameterValue.java,v 1.36 2007/01/04 16:26:33 clq2 Exp $
 */

package org.astrogrid.applications.beans.v1.parameters;

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
 * Defines what a parameterValue can contain - for the instance
 * document
 * 
 * @version $Revision: 1.36 $ $Date: 2007/01/04 16:26:33 $
 */
public class ParameterValue extends org.astrogrid.common.bean.BaseBean 
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
     * Field _encoding
     */
    private java.lang.String _encoding;

    /**
     * Field _indirect
     */
    private boolean _indirect = false;

    /**
     * keeps track of state for field: _indirect
     */
    private boolean _has_indirect;

    /**
     * Field _value
     */
    private java.lang.String _value;


      //----------------/
     //- Constructors -/
    //----------------/

    public ParameterValue() {
        super();
    } //-- org.astrogrid.applications.beans.v1.parameters.ParameterValue()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteIndirect
     */
    public void deleteIndirect()
    {
        this._has_indirect= false;
    } //-- void deleteIndirect() 

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
        
        if (obj instanceof ParameterValue) {
        
            ParameterValue temp = (ParameterValue)obj;
            if (this._name != null) {
                if (temp._name == null) return false;
                else if (!(this._name.equals(temp._name))) 
                    return false;
            }
            else if (temp._name != null)
                return false;
            if (this._encoding != null) {
                if (temp._encoding == null) return false;
                else if (!(this._encoding.equals(temp._encoding))) 
                    return false;
            }
            else if (temp._encoding != null)
                return false;
            if (this._indirect != temp._indirect)
                return false;
            if (this._has_indirect != temp._has_indirect)
                return false;
            if (this._value != null) {
                if (temp._value == null) return false;
                else if (!(this._value.equals(temp._value))) 
                    return false;
            }
            else if (temp._value != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'encoding'.
     * 
     * @return the value of field 'encoding'.
     */
    public java.lang.String getEncoding()
    {
        return this._encoding;
    } //-- java.lang.String getEncoding() 

    /**
     * Returns the value of field 'indirect'.
     * 
     * @return the value of field 'indirect'.
     */
    public boolean getIndirect()
    {
        return this._indirect;
    } //-- boolean getIndirect() 

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
     * Returns the value of field 'value'.
     * 
     * @return the value of field 'value'.
     */
    public java.lang.String getValue()
    {
        return this._value;
    } //-- java.lang.String getValue() 

    /**
     * Method hasIndirect
     */
    public boolean hasIndirect()
    {
        return this._has_indirect;
    } //-- boolean hasIndirect() 

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
     * Sets the value of field 'encoding'.
     * 
     * @param encoding the value of field 'encoding'.
     */
    public void setEncoding(java.lang.String encoding)
    {
        this._encoding = encoding;
    } //-- void setEncoding(java.lang.String) 

    /**
     * Sets the value of field 'indirect'.
     * 
     * @param indirect the value of field 'indirect'.
     */
    public void setIndirect(boolean indirect)
    {
        this._indirect = indirect;
        this._has_indirect = true;
    } //-- void setIndirect(boolean) 

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
     * Sets the value of field 'value'.
     * 
     * @param value the value of field 'value'.
     */
    public void setValue(java.lang.String value)
    {
        this._value = value;
    } //-- void setValue(java.lang.String) 

    /**
     * Method unmarshalParameterValue
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.parameters.ParameterValue unmarshalParameterValue(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.parameters.ParameterValue) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.parameters.ParameterValue.class, reader);
    } //-- org.astrogrid.applications.beans.v1.parameters.ParameterValue unmarshalParameterValue(java.io.Reader) 

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
