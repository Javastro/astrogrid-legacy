/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: StringLiteral.java,v 1.6 2003/11/21 17:30:19 nw Exp $
 */

package org.astrogrid.datacenter.adql.generated;

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
 * Class StringLiteral.
 * 
 * @version $Revision: 1.6 $ $Date: 2003/11/21 17:30:19 $
 */
public class StringLiteral extends org.astrogrid.datacenter.adql.generated.Literal 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _value
     */
    private org.astrogrid.datacenter.adql.generated.ArrayOfString _value;


      //----------------/
     //- Constructors -/
    //----------------/

    public StringLiteral() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.StringLiteral()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'value'.
     * 
     * @return the value of field 'value'.
     */
    public org.astrogrid.datacenter.adql.generated.ArrayOfString getValue()
    {
        return this._value;
    } //-- org.astrogrid.datacenter.adql.generated.ArrayOfString getValue() 

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
    public void setValue(org.astrogrid.datacenter.adql.generated.ArrayOfString value)
    {
        this._value = value;
    } //-- void setValue(org.astrogrid.datacenter.adql.generated.ArrayOfString) 

    /**
     * Method unmarshalStringLiteral
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.StringLiteral unmarshalStringLiteral(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.StringLiteral) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.StringLiteral.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.StringLiteral unmarshalStringLiteral(java.io.Reader) 

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
