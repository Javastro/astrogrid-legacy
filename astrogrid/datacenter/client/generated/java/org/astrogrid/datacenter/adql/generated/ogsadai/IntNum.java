/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: IntNum.java,v 1.2 2004/03/26 16:03:33 eca Exp $
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
 * Class IntNum.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class IntNum extends org.astrogrid.datacenter.adql.generated.ogsadai.Number 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _value
     */
    private long _value;

    /**
     * keeps track of state for field: _value
     */
    private boolean _has_value;


      //----------------/
     //- Constructors -/
    //----------------/

    public IntNum() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.IntNum()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'value'.
     * 
     * @return the value of field 'value'.
     */
    public long getValue()
    {
        return this._value;
    } //-- long getValue() 

    /**
     * Method hasValue
     */
    public boolean hasValue()
    {
        return this._has_value;
    } //-- boolean hasValue() 

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
     * Sets the value of field 'value'.
     * 
     * @param value the value of field 'value'.
     */
    public void setValue(long value)
    {
        this._value = value;
        this._has_value = true;
    } //-- void setValue(long) 

    /**
     * Method unmarshalIntNum
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.IntNum unmarshalIntNum(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.IntNum) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.IntNum.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.IntNum unmarshalIntNum(java.io.Reader) 

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
