/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ConvexHullType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
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
 * Class ConvexHullType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class ConvexHullType extends org.astrogrid.datacenter.adql.generated.ogsadai.ShapeType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _pointList
     */
    private java.util.Vector _pointList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ConvexHullType() {
        super();
        _pointList = new Vector();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ConvexHullType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addPoint
     * 
     * @param vPoint
     */
    public void addPoint(CoordsType vPoint)
        throws java.lang.IndexOutOfBoundsException
    {
        _pointList.addElement(vPoint);
    } //-- void addPoint(CoordsType) 

    /**
     * Method addPoint
     * 
     * @param index
     * @param vPoint
     */
    public void addPoint(int index, CoordsType vPoint)
        throws java.lang.IndexOutOfBoundsException
    {
        _pointList.insertElementAt(vPoint, index);
    } //-- void addPoint(int, CoordsType) 

    /**
     * Method enumeratePoint
     */
    public java.util.Enumeration enumeratePoint()
    {
        return _pointList.elements();
    } //-- java.util.Enumeration enumeratePoint() 

    /**
     * Method getPoint
     * 
     * @param index
     */
    public CoordsType getPoint(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _pointList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (CoordsType) _pointList.elementAt(index);
    } //-- CoordsType getPoint(int) 

    /**
     * Method getPoint
     */
    public CoordsType[] getPoint()
    {
        int size = _pointList.size();
        CoordsType[] mArray = new CoordsType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (CoordsType) _pointList.elementAt(index);
        }
        return mArray;
    } //-- CoordsType[] getPoint() 

    /**
     * Method getPointCount
     */
    public int getPointCount()
    {
        return _pointList.size();
    } //-- int getPointCount() 

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
     * Method removeAllPoint
     */
    public void removeAllPoint()
    {
        _pointList.removeAllElements();
    } //-- void removeAllPoint() 

    /**
     * Method removePoint
     * 
     * @param index
     */
    public CoordsType removePoint(int index)
    {
        java.lang.Object obj = _pointList.elementAt(index);
        _pointList.removeElementAt(index);
        return (CoordsType) obj;
    } //-- CoordsType removePoint(int) 

    /**
     * Method setPoint
     * 
     * @param index
     * @param vPoint
     */
    public void setPoint(int index, CoordsType vPoint)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _pointList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _pointList.setElementAt(vPoint, index);
    } //-- void setPoint(int, CoordsType) 

    /**
     * Method setPoint
     * 
     * @param pointArray
     */
    public void setPoint(CoordsType[] pointArray)
    {
        //-- copy array
        _pointList.removeAllElements();
        for (int i = 0; i < pointArray.length; i++) {
            _pointList.addElement(pointArray[i]);
        }
    } //-- void setPoint(CoordsType) 

    /**
     * Method unmarshalConvexHullType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.ConvexHullType unmarshalConvexHullType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.ConvexHullType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.ConvexHullType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ConvexHullType unmarshalConvexHullType(java.io.Reader) 

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
