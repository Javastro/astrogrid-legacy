/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ArrayOfTable.java,v 1.1 2003/08/28 15:27:54 nw Exp $
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
 * Class ArrayOfTable.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/08/28 15:27:54 $
 */
public class ArrayOfTable extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _tableList
     */
    private java.util.Vector _tableList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ArrayOfTable() {
        super();
        _tableList = new Vector();
    } //-- org.astrogrid.datacenter.adql.generated.ArrayOfTable()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addTable
     * 
     * @param vTable
     */
    public void addTable(org.astrogrid.datacenter.adql.generated.Table vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableList.addElement(vTable);
    } //-- void addTable(org.astrogrid.datacenter.adql.generated.Table) 

    /**
     * Method addTable
     * 
     * @param index
     * @param vTable
     */
    public void addTable(int index, org.astrogrid.datacenter.adql.generated.Table vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableList.insertElementAt(vTable, index);
    } //-- void addTable(int, org.astrogrid.datacenter.adql.generated.Table) 

    /**
     * Method enumerateTable
     */
    public java.util.Enumeration enumerateTable()
    {
        return _tableList.elements();
    } //-- java.util.Enumeration enumerateTable() 

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
        
        if (obj instanceof ArrayOfTable) {
        
            ArrayOfTable temp = (ArrayOfTable)obj;
            if (this._tableList != null) {
                if (temp._tableList == null) return false;
                else if (!(this._tableList.equals(temp._tableList))) 
                    return false;
            }
            else if (temp._tableList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getTable
     * 
     * @param index
     */
    public org.astrogrid.datacenter.adql.generated.Table getTable(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.datacenter.adql.generated.Table) _tableList.elementAt(index);
    } //-- org.astrogrid.datacenter.adql.generated.Table getTable(int) 

    /**
     * Method getTable
     */
    public org.astrogrid.datacenter.adql.generated.Table[] getTable()
    {
        int size = _tableList.size();
        org.astrogrid.datacenter.adql.generated.Table[] mArray = new org.astrogrid.datacenter.adql.generated.Table[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.datacenter.adql.generated.Table) _tableList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.datacenter.adql.generated.Table[] getTable() 

    /**
     * Method getTableCount
     */
    public int getTableCount()
    {
        return _tableList.size();
    } //-- int getTableCount() 

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
     * Method removeAllTable
     */
    public void removeAllTable()
    {
        _tableList.removeAllElements();
    } //-- void removeAllTable() 

    /**
     * Method removeTable
     * 
     * @param index
     */
    public org.astrogrid.datacenter.adql.generated.Table removeTable(int index)
    {
        java.lang.Object obj = _tableList.elementAt(index);
        _tableList.removeElementAt(index);
        return (org.astrogrid.datacenter.adql.generated.Table) obj;
    } //-- org.astrogrid.datacenter.adql.generated.Table removeTable(int) 

    /**
     * Method setTable
     * 
     * @param index
     * @param vTable
     */
    public void setTable(int index, org.astrogrid.datacenter.adql.generated.Table vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _tableList.setElementAt(vTable, index);
    } //-- void setTable(int, org.astrogrid.datacenter.adql.generated.Table) 

    /**
     * Method setTable
     * 
     * @param tableArray
     */
    public void setTable(org.astrogrid.datacenter.adql.generated.Table[] tableArray)
    {
        //-- copy array
        _tableList.removeAllElements();
        for (int i = 0; i < tableArray.length; i++) {
            _tableList.addElement(tableArray[i]);
        }
    } //-- void setTable(org.astrogrid.datacenter.adql.generated.Table) 

    /**
     * Method unmarshalArrayOfTable
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ArrayOfTable unmarshalArrayOfTable(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ArrayOfTable) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ArrayOfTable.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ArrayOfTable unmarshalArrayOfTable(java.io.Reader) 

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
