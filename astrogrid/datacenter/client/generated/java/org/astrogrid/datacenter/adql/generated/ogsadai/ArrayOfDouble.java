/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ArrayOfDouble.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

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
 * Class ArrayOfDouble.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class ArrayOfDouble extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _doubleList
     */
    private java.util.Vector _doubleList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ArrayOfDouble() {
        super();
        _doubleList = new Vector();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method add_double
     * 
     * @param v_double
     */
    public void add_double(double v_double)
        throws java.lang.IndexOutOfBoundsException
    {
        _doubleList.addElement(new Double(v_double));
    } //-- void add_double(double) 

    /**
     * Method add_double
     * 
     * @param index
     * @param v_double
     */
    public void add_double(int index, double v_double)
        throws java.lang.IndexOutOfBoundsException
    {
        _doubleList.insertElementAt(new Double(v_double), index);
    } //-- void add_double(int, double) 

    /**
     * Method enumerate_double
     */
    public java.util.Enumeration enumerate_double()
    {
        return _doubleList.elements();
    } //-- java.util.Enumeration enumerate_double() 

    /**
     * Method get_double
     * 
     * @param index
     */
    public double get_double(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _doubleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return ((Double)_doubleList.elementAt(index)).doubleValue();
    } //-- double get_double(int) 

    /**
     * Method get_double
     */
    public double[] get_double()
    {
        int size = _doubleList.size();
        double[] mArray = new double[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = ((Double)_doubleList.elementAt(index)).doubleValue();
        }
        return mArray;
    } //-- double[] get_double() 

    /**
     * Method get_doubleCount
     */
    public int get_doubleCount()
    {
        return _doubleList.size();
    } //-- int get_doubleCount() 

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
     * Method removeAll_double
     */
    public void removeAll_double()
    {
        _doubleList.removeAllElements();
    } //-- void removeAll_double() 

    /**
     * Method remove_double
     * 
     * @param index
     */
    public double remove_double(int index)
    {
        java.lang.Object obj = _doubleList.elementAt(index);
        _doubleList.removeElementAt(index);
        return ((Double)obj).doubleValue();
    } //-- double remove_double(int) 

    /**
     * Method set_double
     * 
     * @param index
     * @param v_double
     */
    public void set_double(int index, double v_double)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _doubleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _doubleList.setElementAt(new Double(v_double), index);
    } //-- void set_double(int, double) 

    /**
     * Method set_double
     * 
     * @param _doubleArray
     */
    public void set_double(double[] _doubleArray)
    {
        //-- copy array
        _doubleList.removeAllElements();
        for (int i = 0; i < _doubleArray.length; i++) {
            _doubleList.addElement(new Double(_doubleArray[i]));
        }
    } //-- void set_double(double) 

    /**
     * Method unmarshalArrayOfDouble
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble unmarshalArrayOfDouble(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble unmarshalArrayOfDouble(java.io.Reader) 

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
