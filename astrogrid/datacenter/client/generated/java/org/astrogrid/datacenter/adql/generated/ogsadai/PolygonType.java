/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: PolygonType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
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
 * Class PolygonType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class PolygonType extends org.astrogrid.datacenter.adql.generated.ogsadai.ShapeType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _vertexList
     */
    private java.util.Vector _vertexList;


      //----------------/
     //- Constructors -/
    //----------------/

    public PolygonType() {
        super();
        _vertexList = new Vector();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.PolygonType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addVertex
     * 
     * @param vVertex
     */
    public void addVertex(org.astrogrid.datacenter.adql.generated.ogsadai.VertexType vVertex)
        throws java.lang.IndexOutOfBoundsException
    {
        _vertexList.addElement(vVertex);
    } //-- void addVertex(org.astrogrid.datacenter.adql.generated.ogsadai.VertexType) 

    /**
     * Method addVertex
     * 
     * @param index
     * @param vVertex
     */
    public void addVertex(int index, org.astrogrid.datacenter.adql.generated.ogsadai.VertexType vVertex)
        throws java.lang.IndexOutOfBoundsException
    {
        _vertexList.insertElementAt(vVertex, index);
    } //-- void addVertex(int, org.astrogrid.datacenter.adql.generated.ogsadai.VertexType) 

    /**
     * Method enumerateVertex
     */
    public java.util.Enumeration enumerateVertex()
    {
        return _vertexList.elements();
    } //-- java.util.Enumeration enumerateVertex() 

    /**
     * Method getVertex
     * 
     * @param index
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.VertexType getVertex(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _vertexList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.datacenter.adql.generated.ogsadai.VertexType) _vertexList.elementAt(index);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.VertexType getVertex(int) 

    /**
     * Method getVertex
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.VertexType[] getVertex()
    {
        int size = _vertexList.size();
        org.astrogrid.datacenter.adql.generated.ogsadai.VertexType[] mArray = new org.astrogrid.datacenter.adql.generated.ogsadai.VertexType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.datacenter.adql.generated.ogsadai.VertexType) _vertexList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.VertexType[] getVertex() 

    /**
     * Method getVertexCount
     */
    public int getVertexCount()
    {
        return _vertexList.size();
    } //-- int getVertexCount() 

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
     * Method removeAllVertex
     */
    public void removeAllVertex()
    {
        _vertexList.removeAllElements();
    } //-- void removeAllVertex() 

    /**
     * Method removeVertex
     * 
     * @param index
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.VertexType removeVertex(int index)
    {
        java.lang.Object obj = _vertexList.elementAt(index);
        _vertexList.removeElementAt(index);
        return (org.astrogrid.datacenter.adql.generated.ogsadai.VertexType) obj;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.VertexType removeVertex(int) 

    /**
     * Method setVertex
     * 
     * @param index
     * @param vVertex
     */
    public void setVertex(int index, org.astrogrid.datacenter.adql.generated.ogsadai.VertexType vVertex)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _vertexList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _vertexList.setElementAt(vVertex, index);
    } //-- void setVertex(int, org.astrogrid.datacenter.adql.generated.ogsadai.VertexType) 

    /**
     * Method setVertex
     * 
     * @param vertexArray
     */
    public void setVertex(org.astrogrid.datacenter.adql.generated.ogsadai.VertexType[] vertexArray)
    {
        //-- copy array
        _vertexList.removeAllElements();
        for (int i = 0; i < vertexArray.length; i++) {
            _vertexList.addElement(vertexArray[i]);
        }
    } //-- void setVertex(org.astrogrid.datacenter.adql.generated.ogsadai.VertexType) 

    /**
     * Method unmarshalPolygonType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.PolygonType unmarshalPolygonType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.PolygonType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.PolygonType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.PolygonType unmarshalPolygonType(java.io.Reader) 

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
