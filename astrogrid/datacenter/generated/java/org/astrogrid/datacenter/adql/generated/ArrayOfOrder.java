/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ArrayOfOrder.java,v 1.1 2003/09/10 13:01:27 nw Exp $
 */

package org.astrogrid.datacenter.adql.generated;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class ArrayOfOrder.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/09/10 13:01:27 $
 */
public class ArrayOfOrder extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _orderList
     */
    private java.util.Vector _orderList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ArrayOfOrder() {
        super();
        _orderList = new Vector();
    } //-- org.astrogrid.datacenter.adql.generated.ArrayOfOrder()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addOrder
     * 
     * @param vOrder
     */
    public void addOrder(org.astrogrid.datacenter.adql.generated.Order vOrder)
        throws java.lang.IndexOutOfBoundsException
    {
        _orderList.addElement(vOrder);
    } //-- void addOrder(org.astrogrid.datacenter.adql.generated.Order) 

    /**
     * Method addOrder
     * 
     * @param index
     * @param vOrder
     */
    public void addOrder(int index, org.astrogrid.datacenter.adql.generated.Order vOrder)
        throws java.lang.IndexOutOfBoundsException
    {
        _orderList.insertElementAt(vOrder, index);
    } //-- void addOrder(int, org.astrogrid.datacenter.adql.generated.Order) 

    /**
     * Method enumerateOrder
     */
    public java.util.Enumeration enumerateOrder()
    {
        return _orderList.elements();
    } //-- java.util.Enumeration enumerateOrder() 

    /**
     * Method getOrder
     * 
     * @param index
     */
    public org.astrogrid.datacenter.adql.generated.Order getOrder(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _orderList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.datacenter.adql.generated.Order) _orderList.elementAt(index);
    } //-- org.astrogrid.datacenter.adql.generated.Order getOrder(int) 

    /**
     * Method getOrder
     */
    public org.astrogrid.datacenter.adql.generated.Order[] getOrder()
    {
        int size = _orderList.size();
        org.astrogrid.datacenter.adql.generated.Order[] mArray = new org.astrogrid.datacenter.adql.generated.Order[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.datacenter.adql.generated.Order) _orderList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.datacenter.adql.generated.Order[] getOrder() 

    /**
     * Method getOrderCount
     */
    public int getOrderCount()
    {
        return _orderList.size();
    } //-- int getOrderCount() 

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
     * Method removeAllOrder
     */
    public void removeAllOrder()
    {
        _orderList.removeAllElements();
    } //-- void removeAllOrder() 

    /**
     * Method removeOrder
     * 
     * @param index
     */
    public org.astrogrid.datacenter.adql.generated.Order removeOrder(int index)
    {
        java.lang.Object obj = _orderList.elementAt(index);
        _orderList.removeElementAt(index);
        return (org.astrogrid.datacenter.adql.generated.Order) obj;
    } //-- org.astrogrid.datacenter.adql.generated.Order removeOrder(int) 

    /**
     * Method setOrder
     * 
     * @param index
     * @param vOrder
     */
    public void setOrder(int index, org.astrogrid.datacenter.adql.generated.Order vOrder)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _orderList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _orderList.setElementAt(vOrder, index);
    } //-- void setOrder(int, org.astrogrid.datacenter.adql.generated.Order) 

    /**
     * Method setOrder
     * 
     * @param orderArray
     */
    public void setOrder(org.astrogrid.datacenter.adql.generated.Order[] orderArray)
    {
        //-- copy array
        _orderList.removeAllElements();
        for (int i = 0; i < orderArray.length; i++) {
            _orderList.addElement(orderArray[i]);
        }
    } //-- void setOrder(org.astrogrid.datacenter.adql.generated.Order) 

    /**
     * Method unmarshalArrayOfOrder
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ArrayOfOrder unmarshalArrayOfOrder(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ArrayOfOrder) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ArrayOfOrder.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ArrayOfOrder unmarshalArrayOfOrder(java.io.Reader) 

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
