/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Atom.java,v 1.2 2004/03/26 16:03:33 eca Exp $
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
 * Class Atom.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class Atom extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _stringLiteral
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.StringLiteral _stringLiteral;

    /**
     * Field _numberLiteral
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.NumberLiteral _numberLiteral;


      //----------------/
     //- Constructors -/
    //----------------/

    public Atom() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Atom()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'numberLiteral'.
     * 
     * @return the value of field 'numberLiteral'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.NumberLiteral getNumberLiteral()
    {
        return this._numberLiteral;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.NumberLiteral getNumberLiteral() 

    /**
     * Returns the value of field 'stringLiteral'.
     * 
     * @return the value of field 'stringLiteral'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.StringLiteral getStringLiteral()
    {
        return this._stringLiteral;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.StringLiteral getStringLiteral() 

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
     * Sets the value of field 'numberLiteral'.
     * 
     * @param numberLiteral the value of field 'numberLiteral'.
     */
    public void setNumberLiteral(org.astrogrid.datacenter.adql.generated.ogsadai.NumberLiteral numberLiteral)
    {
        this._numberLiteral = numberLiteral;
    } //-- void setNumberLiteral(org.astrogrid.datacenter.adql.generated.ogsadai.NumberLiteral) 

    /**
     * Sets the value of field 'stringLiteral'.
     * 
     * @param stringLiteral the value of field 'stringLiteral'.
     */
    public void setStringLiteral(org.astrogrid.datacenter.adql.generated.ogsadai.StringLiteral stringLiteral)
    {
        this._stringLiteral = stringLiteral;
    } //-- void setStringLiteral(org.astrogrid.datacenter.adql.generated.ogsadai.StringLiteral) 

    /**
     * Method unmarshalAtom
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.Atom unmarshalAtom(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.Atom) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.Atom.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Atom unmarshalAtom(java.io.Reader) 

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
