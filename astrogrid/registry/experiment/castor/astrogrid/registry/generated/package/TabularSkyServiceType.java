/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: TabularSkyServiceType.java,v 1.1 2004/03/03 03:40:59 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package;

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
 * Class TabularSkyServiceType.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:59 $
 */
public class TabularSkyServiceType extends org.astrogrid.registry.generated.package.SkyServiceType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * a description of a table and its columns.
     *  
     */
    private java.util.Vector _tableList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TabularSkyServiceType() {
        super();
        _tableList = new Vector();
    } //-- org.astrogrid.registry.generated.package.TabularSkyServiceType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addTable
     * 
     * @param vTable
     */
    public void addTable(org.astrogrid.registry.generated.package.Table vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableList.addElement(vTable);
    } //-- void addTable(org.astrogrid.registry.generated.package.Table) 

    /**
     * Method addTable
     * 
     * @param index
     * @param vTable
     */
    public void addTable(int index, org.astrogrid.registry.generated.package.Table vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableList.insertElementAt(vTable, index);
    } //-- void addTable(int, org.astrogrid.registry.generated.package.Table) 

    /**
     * Method enumerateTable
     */
    public java.util.Enumeration enumerateTable()
    {
        return _tableList.elements();
    } //-- java.util.Enumeration enumerateTable() 

    /**
     * Method getTable
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.Table getTable(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.Table) _tableList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.Table getTable(int) 

    /**
     * Method getTable
     */
    public org.astrogrid.registry.generated.package.Table[] getTable()
    {
        int size = _tableList.size();
        org.astrogrid.registry.generated.package.Table[] mArray = new org.astrogrid.registry.generated.package.Table[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.Table) _tableList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.Table[] getTable() 

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
    public org.astrogrid.registry.generated.package.Table removeTable(int index)
    {
        java.lang.Object obj = _tableList.elementAt(index);
        _tableList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.Table) obj;
    } //-- org.astrogrid.registry.generated.package.Table removeTable(int) 

    /**
     * Method setTable
     * 
     * @param index
     * @param vTable
     */
    public void setTable(int index, org.astrogrid.registry.generated.package.Table vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _tableList.setElementAt(vTable, index);
    } //-- void setTable(int, org.astrogrid.registry.generated.package.Table) 

    /**
     * Method setTable
     * 
     * @param tableArray
     */
    public void setTable(org.astrogrid.registry.generated.package.Table[] tableArray)
    {
        //-- copy array
        _tableList.removeAllElements();
        for (int i = 0; i < tableArray.length; i++) {
            _tableList.addElement(tableArray[i]);
        }
    } //-- void setTable(org.astrogrid.registry.generated.package.Table) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.TabularSkyServiceType) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.TabularSkyServiceType.class, reader);
    } //-- java.lang.Object unmarshal(java.io.Reader) 

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
