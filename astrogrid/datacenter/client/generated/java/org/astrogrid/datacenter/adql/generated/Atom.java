/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Atom.java,v 1.4 2003/11/19 15:26:41 nw Exp $
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
 * Class Atom.
 * 
 * @version $Revision: 1.4 $ $Date: 2003/11/19 15:26:41 $
 */
public class Atom extends org.astrogrid.datacenter.adql.AbstractQOM implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _stringLiteral
     */
    private org.astrogrid.datacenter.adql.generated.StringLiteral _stringLiteral;

    /**
     * Field _numberLiteral
     */
    private org.astrogrid.datacenter.adql.generated.NumberLiteral _numberLiteral;


      //----------------/
     //- Constructors -/
    //----------------/

    public Atom() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.Atom()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'numberLiteral'.
     * 
     * @return the value of field 'numberLiteral'.
     */
    public org.astrogrid.datacenter.adql.generated.NumberLiteral getNumberLiteral()
    {
        return this._numberLiteral;
    } //-- org.astrogrid.datacenter.adql.generated.NumberLiteral getNumberLiteral() 

    /**
     * Returns the value of field 'stringLiteral'.
     * 
     * @return the value of field 'stringLiteral'.
     */
    public org.astrogrid.datacenter.adql.generated.StringLiteral getStringLiteral()
    {
        return this._stringLiteral;
    } //-- org.astrogrid.datacenter.adql.generated.StringLiteral getStringLiteral() 

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
    public void setNumberLiteral(org.astrogrid.datacenter.adql.generated.NumberLiteral numberLiteral)
    {
        this._numberLiteral = numberLiteral;
    } //-- void setNumberLiteral(org.astrogrid.datacenter.adql.generated.NumberLiteral) 

    /**
     * Sets the value of field 'stringLiteral'.
     * 
     * @param stringLiteral the value of field 'stringLiteral'.
     */
    public void setStringLiteral(org.astrogrid.datacenter.adql.generated.StringLiteral stringLiteral)
    {
        this._stringLiteral = stringLiteral;
    } //-- void setStringLiteral(org.astrogrid.datacenter.adql.generated.StringLiteral) 

    /**
     * Method unmarshalAtom
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.Atom unmarshalAtom(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.Atom) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.Atom.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.Atom unmarshalAtom(java.io.Reader) 

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
