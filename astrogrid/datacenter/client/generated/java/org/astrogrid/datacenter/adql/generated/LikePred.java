/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: LikePred.java,v 1.3 2003/11/18 14:21:03 nw Exp $
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
 * Class LikePred.
 * 
 * @version $Revision: 1.3 $ $Date: 2003/11/18 14:21:03 $
 */
public class LikePred extends org.astrogrid.datacenter.adql.generated.Predicate 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _expr
     */
    private org.astrogrid.datacenter.adql.generated.ScalarExpression _expr;

    /**
     * Field _negate
     */
    private boolean _negate;

    /**
     * keeps track of state for field: _negate
     */
    private boolean _has_negate;

    /**
     * Field _value
     */
    private org.astrogrid.datacenter.adql.generated.Atom _value;


      //----------------/
     //- Constructors -/
    //----------------/

    public LikePred() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.LikePred()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'expr'.
     * 
     * @return the value of field 'expr'.
     */
    public org.astrogrid.datacenter.adql.generated.ScalarExpression getExpr()
    {
        return this._expr;
    } //-- org.astrogrid.datacenter.adql.generated.ScalarExpression getExpr() 

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
     * Returns the value of field 'value'.
     * 
     * @return the value of field 'value'.
     */
    public org.astrogrid.datacenter.adql.generated.Atom getValue()
    {
        return this._value;
    } //-- org.astrogrid.datacenter.adql.generated.Atom getValue() 

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
     * Sets the value of field 'expr'.
     * 
     * @param expr the value of field 'expr'.
     */
    public void setExpr(org.astrogrid.datacenter.adql.generated.ScalarExpression expr)
    {
        this._expr = expr;
    } //-- void setExpr(org.astrogrid.datacenter.adql.generated.ScalarExpression) 

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
     * Sets the value of field 'value'.
     * 
     * @param value the value of field 'value'.
     */
    public void setValue(org.astrogrid.datacenter.adql.generated.Atom value)
    {
        this._value = value;
    } //-- void setValue(org.astrogrid.datacenter.adql.generated.Atom) 

    /**
     * Method unmarshalLikePred
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.LikePred unmarshalLikePred(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.LikePred) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.LikePred.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.LikePred unmarshalLikePred(java.io.Reader) 

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
