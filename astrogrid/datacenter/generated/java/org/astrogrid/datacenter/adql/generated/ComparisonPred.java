/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ComparisonPred.java,v 1.3 2003/09/16 13:23:24 nw Exp $
 */

package org.astrogrid.datacenter.adql.generated;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.datacenter.adql.generated.types.Comparison;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class ComparisonPred.
 * 
 * @version $Revision: 1.3 $ $Date: 2003/09/16 13:23:24 $
 */
public class ComparisonPred extends org.astrogrid.datacenter.adql.generated.Predicate 
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
     * Field _compare
     */
    private org.astrogrid.datacenter.adql.generated.types.Comparison _compare;

    /**
     * Field _secondExpr
     */
    private org.astrogrid.datacenter.adql.generated.ScalarExpression _secondExpr;


      //----------------/
     //- Constructors -/
    //----------------/

    public ComparisonPred() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ComparisonPred()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'compare'.
     * 
     * @return the value of field 'compare'.
     */
    public org.astrogrid.datacenter.adql.generated.types.Comparison getCompare()
    {
        return this._compare;
    } //-- org.astrogrid.datacenter.adql.generated.types.Comparison getCompare() 

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
     * Sets the value of field 'compare'.
     * 
     * @param compare the value of field 'compare'.
     */
    public void setCompare(org.astrogrid.datacenter.adql.generated.types.Comparison compare)
    {
        this._compare = compare;
    } //-- void setCompare(org.astrogrid.datacenter.adql.generated.types.Comparison) 

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
     * Sets the value of field 'secondExpr'.
     * 
     * @param secondExpr the value of field 'secondExpr'.
     */
    public void setSecondExpr(org.astrogrid.datacenter.adql.generated.ScalarExpression secondExpr)
    {
        this._secondExpr = secondExpr;
    } //-- void setSecondExpr(org.astrogrid.datacenter.adql.generated.ScalarExpression) 

    /**
     * Method unmarshalComparisonPred
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ComparisonPred unmarshalComparisonPred(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ComparisonPred) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ComparisonPred.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ComparisonPred unmarshalComparisonPred(java.io.Reader) 

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
