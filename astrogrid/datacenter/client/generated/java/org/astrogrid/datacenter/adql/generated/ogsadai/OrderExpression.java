/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: OrderExpression.java,v 1.2 2004/03/26 16:03:33 eca Exp $
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
 * Class OrderExpression.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class OrderExpression extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _orderList
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfOrder _orderList;


      //----------------/
     //- Constructors -/
    //----------------/

    public OrderExpression() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.OrderExpression()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'orderList'.
     * 
     * @return the value of field 'orderList'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfOrder getOrderList()
    {
        return this._orderList;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfOrder getOrderList() 

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
     * Sets the value of field 'orderList'.
     * 
     * @param orderList the value of field 'orderList'.
     */
    public void setOrderList(org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfOrder orderList)
    {
        this._orderList = orderList;
    } //-- void setOrderList(org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfOrder) 

    /**
     * Method unmarshalOrderExpression
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.OrderExpression unmarshalOrderExpression(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.OrderExpression) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.OrderExpression.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.OrderExpression unmarshalOrderExpression(java.io.Reader) 

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
