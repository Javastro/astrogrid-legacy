/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AtomExpr.java,v 1.2 2004/03/26 16:03:33 eca Exp $
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
 * Class AtomExpr.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class AtomExpr extends org.astrogrid.datacenter.adql.generated.ogsadai.ScalarExpression 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _value
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.Atom _value;


      //----------------/
     //- Constructors -/
    //----------------/

    public AtomExpr() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AtomExpr()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'value'.
     * 
     * @return the value of field 'value'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.Atom getValue()
    {
        return this._value;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Atom getValue() 

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
    public void setValue(org.astrogrid.datacenter.adql.generated.ogsadai.Atom value)
    {
        this._value = value;
    } //-- void setValue(org.astrogrid.datacenter.adql.generated.ogsadai.Atom) 

    /**
     * Method unmarshalAtomExpr
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.AtomExpr unmarshalAtomExpr(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.AtomExpr) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.AtomExpr.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AtomExpr unmarshalAtomExpr(java.io.Reader) 

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
