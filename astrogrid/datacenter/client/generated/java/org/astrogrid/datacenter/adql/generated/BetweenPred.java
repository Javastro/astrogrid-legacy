/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: BetweenPred.java,v 1.11 2003/12/01 16:30:01 nw Exp $
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
 * Class BetweenPred.
 * 
 * @version $Revision: 1.11 $ $Date: 2003/12/01 16:30:01 $
 */
public class BetweenPred extends org.astrogrid.datacenter.adql.generated.Predicate 
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
     * Field _firstExpr
     */
    private org.astrogrid.datacenter.adql.generated.ScalarExpression _firstExpr;

    /**
     * Field _secondExpr
     */
    private org.astrogrid.datacenter.adql.generated.ScalarExpression _secondExpr;


      //----------------/
     //- Constructors -/
    //----------------/

    public BetweenPred() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.BetweenPred()


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
     * Returns the value of field 'firstExpr'.
     * 
     * @return the value of field 'firstExpr'.
     */
    public org.astrogrid.datacenter.adql.generated.ScalarExpression getFirstExpr()
    {
        return this._firstExpr;
    } //-- org.astrogrid.datacenter.adql.generated.ScalarExpression getFirstExpr() 

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
     * Returns the value of field 'secondExpr'.
     * 
     * @return the value of field 'secondExpr'.
     */
    public org.astrogrid.datacenter.adql.generated.ScalarExpression getSecondExpr()
    {
        return this._secondExpr;
    } //-- org.astrogrid.datacenter.adql.generated.ScalarExpression getSecondExpr() 

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
     * Sets the value of field 'firstExpr'.
     * 
     * @param firstExpr the value of field 'firstExpr'.
     */
    public void setFirstExpr(org.astrogrid.datacenter.adql.generated.ScalarExpression firstExpr)
    {
        this._firstExpr = firstExpr;
    } //-- void setFirstExpr(org.astrogrid.datacenter.adql.generated.ScalarExpression) 

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
     * Sets the value of field 'secondExpr'.
     * 
     * @param secondExpr the value of field 'secondExpr'.
     */
    public void setSecondExpr(org.astrogrid.datacenter.adql.generated.ScalarExpression secondExpr)
    {
        this._secondExpr = secondExpr;
    } //-- void setSecondExpr(org.astrogrid.datacenter.adql.generated.ScalarExpression) 

    /**
     * Method unmarshalBetweenPred
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.BetweenPred unmarshalBetweenPred(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.BetweenPred) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.BetweenPred.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.BetweenPred unmarshalBetweenPred(java.io.Reader) 

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
