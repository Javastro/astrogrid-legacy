/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Function.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.datacenter.adql.generated.ogsadai.types.AggregateFunction;
import org.astrogrid.datacenter.adql.generated.ogsadai.types.MathFunction;
import org.astrogrid.datacenter.adql.generated.ogsadai.types.TrigonometricFunction;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Function.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class Function extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _aggregateFunction
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.types.AggregateFunction _aggregateFunction;

    /**
     * Field _mathFunction
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.types.MathFunction _mathFunction;

    /**
     * Field _trigonometricFunction
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.types.TrigonometricFunction _trigonometricFunction;


      //----------------/
     //- Constructors -/
    //----------------/

    public Function() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Function()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'aggregateFunction'.
     * 
     * @return the value of field 'aggregateFunction'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.types.AggregateFunction getAggregateFunction()
    {
        return this._aggregateFunction;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.AggregateFunction getAggregateFunction() 

    /**
     * Returns the value of field 'mathFunction'.
     * 
     * @return the value of field 'mathFunction'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.types.MathFunction getMathFunction()
    {
        return this._mathFunction;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.MathFunction getMathFunction() 

    /**
     * Returns the value of field 'trigonometricFunction'.
     * 
     * @return the value of field 'trigonometricFunction'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.types.TrigonometricFunction getTrigonometricFunction()
    {
        return this._trigonometricFunction;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.TrigonometricFunction getTrigonometricFunction() 

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
     * Sets the value of field 'aggregateFunction'.
     * 
     * @param aggregateFunction the value of field
     * 'aggregateFunction'.
     */
    public void setAggregateFunction(org.astrogrid.datacenter.adql.generated.ogsadai.types.AggregateFunction aggregateFunction)
    {
        this._aggregateFunction = aggregateFunction;
    } //-- void setAggregateFunction(org.astrogrid.datacenter.adql.generated.ogsadai.types.AggregateFunction) 

    /**
     * Sets the value of field 'mathFunction'.
     * 
     * @param mathFunction the value of field 'mathFunction'.
     */
    public void setMathFunction(org.astrogrid.datacenter.adql.generated.ogsadai.types.MathFunction mathFunction)
    {
        this._mathFunction = mathFunction;
    } //-- void setMathFunction(org.astrogrid.datacenter.adql.generated.ogsadai.types.MathFunction) 

    /**
     * Sets the value of field 'trigonometricFunction'.
     * 
     * @param trigonometricFunction the value of field
     * 'trigonometricFunction'.
     */
    public void setTrigonometricFunction(org.astrogrid.datacenter.adql.generated.ogsadai.types.TrigonometricFunction trigonometricFunction)
    {
        this._trigonometricFunction = trigonometricFunction;
    } //-- void setTrigonometricFunction(org.astrogrid.datacenter.adql.generated.ogsadai.types.TrigonometricFunction) 

    /**
     * Method unmarshalFunction
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.Function unmarshalFunction(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.Function) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.Function.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Function unmarshalFunction(java.io.Reader) 

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
