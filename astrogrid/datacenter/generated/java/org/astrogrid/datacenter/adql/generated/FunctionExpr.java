/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: FunctionExpr.java,v 1.3 2003/09/16 13:23:24 nw Exp $
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
 * Class FunctionExpr.
 * 
 * @version $Revision: 1.3 $ $Date: 2003/09/16 13:23:24 $
 */
public class FunctionExpr extends org.astrogrid.datacenter.adql.generated.ScalarExpression 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _allExpressionsFunction
     */
    private org.astrogrid.datacenter.adql.generated.AllExpressionsFunction _allExpressionsFunction;

    /**
     * Field _expressionFunction
     */
    private org.astrogrid.datacenter.adql.generated.ExpressionFunction _expressionFunction;

    /**
     * Field _mutipleColumnsFunction
     */
    private org.astrogrid.datacenter.adql.generated.MutipleColumnsFunction _mutipleColumnsFunction;

    /**
     * Field _distinctColumnFunction
     */
    private org.astrogrid.datacenter.adql.generated.DistinctColumnFunction _distinctColumnFunction;


      //----------------/
     //- Constructors -/
    //----------------/

    public FunctionExpr() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.FunctionExpr()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'allExpressionsFunction'.
     * 
     * @return the value of field 'allExpressionsFunction'.
     */
    public org.astrogrid.datacenter.adql.generated.AllExpressionsFunction getAllExpressionsFunction()
    {
        return this._allExpressionsFunction;
    } //-- org.astrogrid.datacenter.adql.generated.AllExpressionsFunction getAllExpressionsFunction() 

    /**
     * Returns the value of field 'distinctColumnFunction'.
     * 
     * @return the value of field 'distinctColumnFunction'.
     */
    public org.astrogrid.datacenter.adql.generated.DistinctColumnFunction getDistinctColumnFunction()
    {
        return this._distinctColumnFunction;
    } //-- org.astrogrid.datacenter.adql.generated.DistinctColumnFunction getDistinctColumnFunction() 

    /**
     * Returns the value of field 'expressionFunction'.
     * 
     * @return the value of field 'expressionFunction'.
     */
    public org.astrogrid.datacenter.adql.generated.ExpressionFunction getExpressionFunction()
    {
        return this._expressionFunction;
    } //-- org.astrogrid.datacenter.adql.generated.ExpressionFunction getExpressionFunction() 

    /**
     * Returns the value of field 'mutipleColumnsFunction'.
     * 
     * @return the value of field 'mutipleColumnsFunction'.
     */
    public org.astrogrid.datacenter.adql.generated.MutipleColumnsFunction getMutipleColumnsFunction()
    {
        return this._mutipleColumnsFunction;
    } //-- org.astrogrid.datacenter.adql.generated.MutipleColumnsFunction getMutipleColumnsFunction() 

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
     * Sets the value of field 'allExpressionsFunction'.
     * 
     * @param allExpressionsFunction the value of field
     * 'allExpressionsFunction'.
     */
    public void setAllExpressionsFunction(org.astrogrid.datacenter.adql.generated.AllExpressionsFunction allExpressionsFunction)
    {
        this._allExpressionsFunction = allExpressionsFunction;
    } //-- void setAllExpressionsFunction(org.astrogrid.datacenter.adql.generated.AllExpressionsFunction) 

    /**
     * Sets the value of field 'distinctColumnFunction'.
     * 
     * @param distinctColumnFunction the value of field
     * 'distinctColumnFunction'.
     */
    public void setDistinctColumnFunction(org.astrogrid.datacenter.adql.generated.DistinctColumnFunction distinctColumnFunction)
    {
        this._distinctColumnFunction = distinctColumnFunction;
    } //-- void setDistinctColumnFunction(org.astrogrid.datacenter.adql.generated.DistinctColumnFunction) 

    /**
     * Sets the value of field 'expressionFunction'.
     * 
     * @param expressionFunction the value of field
     * 'expressionFunction'.
     */
    public void setExpressionFunction(org.astrogrid.datacenter.adql.generated.ExpressionFunction expressionFunction)
    {
        this._expressionFunction = expressionFunction;
    } //-- void setExpressionFunction(org.astrogrid.datacenter.adql.generated.ExpressionFunction) 

    /**
     * Sets the value of field 'mutipleColumnsFunction'.
     * 
     * @param mutipleColumnsFunction the value of field
     * 'mutipleColumnsFunction'.
     */
    public void setMutipleColumnsFunction(org.astrogrid.datacenter.adql.generated.MutipleColumnsFunction mutipleColumnsFunction)
    {
        this._mutipleColumnsFunction = mutipleColumnsFunction;
    } //-- void setMutipleColumnsFunction(org.astrogrid.datacenter.adql.generated.MutipleColumnsFunction) 

    /**
     * Method unmarshalFunctionExpr
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.FunctionExpr unmarshalFunctionExpr(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.FunctionExpr) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.FunctionExpr.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.FunctionExpr unmarshalFunctionExpr(java.io.Reader) 

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
