/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: BinaryExpr.java,v 1.1 2003/08/28 15:27:54 nw Exp $
 */

package org.astrogrid.datacenter.adql.generated;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.datacenter.adql.generated.types.BinaryOperator;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class BinaryExpr.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/08/28 15:27:54 $
 */
public class BinaryExpr extends org.astrogrid.datacenter.adql.generated.ScalarExpression 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _firstExpr
     */
    private org.astrogrid.datacenter.adql.generated.ScalarExpression _firstExpr;

    /**
     * Field _operator
     */
    private org.astrogrid.datacenter.adql.generated.types.BinaryOperator _operator;

    /**
     * Field _secondExpr
     */
    private org.astrogrid.datacenter.adql.generated.ScalarExpression _secondExpr;


      //----------------/
     //- Constructors -/
    //----------------/

    public BinaryExpr() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.BinaryExpr()


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
        
        if (obj instanceof BinaryExpr) {
        
            BinaryExpr temp = (BinaryExpr)obj;
            if (this._firstExpr != null) {
                if (temp._firstExpr == null) return false;
                else if (!(this._firstExpr.equals(temp._firstExpr))) 
                    return false;
            }
            else if (temp._firstExpr != null)
                return false;
            if (this._operator != null) {
                if (temp._operator == null) return false;
                else if (!(this._operator.equals(temp._operator))) 
                    return false;
            }
            else if (temp._operator != null)
                return false;
            if (this._secondExpr != null) {
                if (temp._secondExpr == null) return false;
                else if (!(this._secondExpr.equals(temp._secondExpr))) 
                    return false;
            }
            else if (temp._secondExpr != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

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
     * Returns the value of field 'operator'.
     * 
     * @return the value of field 'operator'.
     */
    public org.astrogrid.datacenter.adql.generated.types.BinaryOperator getOperator()
    {
        return this._operator;
    } //-- org.astrogrid.datacenter.adql.generated.types.BinaryOperator getOperator() 

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
     * Sets the value of field 'firstExpr'.
     * 
     * @param firstExpr the value of field 'firstExpr'.
     */
    public void setFirstExpr(org.astrogrid.datacenter.adql.generated.ScalarExpression firstExpr)
    {
        this._firstExpr = firstExpr;
    } //-- void setFirstExpr(org.astrogrid.datacenter.adql.generated.ScalarExpression) 

    /**
     * Sets the value of field 'operator'.
     * 
     * @param operator the value of field 'operator'.
     */
    public void setOperator(org.astrogrid.datacenter.adql.generated.types.BinaryOperator operator)
    {
        this._operator = operator;
    } //-- void setOperator(org.astrogrid.datacenter.adql.generated.types.BinaryOperator) 

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
     * Method unmarshalBinaryExpr
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.BinaryExpr unmarshalBinaryExpr(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.BinaryExpr) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.BinaryExpr.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.BinaryExpr unmarshalBinaryExpr(java.io.Reader) 

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
