/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: TabularSkyServiceType.java,v 1.4 2004/03/09 09:45:23 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.dataservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class TabularSkyServiceType.
 * 
 * @version $Revision: 1.4 $ $Date: 2004/03/09 09:45:23 $
 */
public class TabularSkyServiceType extends org.astrogrid.registry.beans.resource.dataservice.SkyServiceType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * a description of a table and its columns.
     *  
     */
    private java.util.ArrayList _tableList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TabularSkyServiceType() {
        super();
        _tableList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.dataservice.TabularSkyServiceType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addTable
     * 
     * @param vTable
     */
    public void addTable(org.astrogrid.registry.beans.resource.dataservice.TableType vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableList.add(vTable);
    } //-- void addTable(org.astrogrid.registry.beans.resource.dataservice.TableType) 

    /**
     * Method addTable
     * 
     * @param index
     * @param vTable
     */
    public void addTable(int index, org.astrogrid.registry.beans.resource.dataservice.TableType vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableList.add(index, vTable);
    } //-- void addTable(int, org.astrogrid.registry.beans.resource.dataservice.TableType) 

    /**
     * Method clearTable
     */
    public void clearTable()
    {
        _tableList.clear();
    } //-- void clearTable() 

    /**
     * Method enumerateTable
     */
    public java.util.Enumeration enumerateTable()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_tableList.iterator());
    } //-- java.util.Enumeration enumerateTable() 

    /**
     * Method getTable
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.dataservice.TableType getTable(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.dataservice.TableType) _tableList.get(index);
    } //-- org.astrogrid.registry.beans.resource.dataservice.TableType getTable(int) 

    /**
     * Method getTable
     */
    public org.astrogrid.registry.beans.resource.dataservice.TableType[] getTable()
    {
        int size = _tableList.size();
        org.astrogrid.registry.beans.resource.dataservice.TableType[] mArray = new org.astrogrid.registry.beans.resource.dataservice.TableType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.dataservice.TableType) _tableList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.dataservice.TableType[] getTable() 

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
     * Method removeTable
     * 
     * @param vTable
     */
    public boolean removeTable(org.astrogrid.registry.beans.resource.dataservice.TableType vTable)
    {
        boolean removed = _tableList.remove(vTable);
        return removed;
    } //-- boolean removeTable(org.astrogrid.registry.beans.resource.dataservice.TableType) 

    /**
     * Method setTable
     * 
     * @param index
     * @param vTable
     */
    public void setTable(int index, org.astrogrid.registry.beans.resource.dataservice.TableType vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _tableList.set(index, vTable);
    } //-- void setTable(int, org.astrogrid.registry.beans.resource.dataservice.TableType) 

    /**
     * Method setTable
     * 
     * @param tableArray
     */
    public void setTable(org.astrogrid.registry.beans.resource.dataservice.TableType[] tableArray)
    {
        //-- copy array
        _tableList.clear();
        for (int i = 0; i < tableArray.length; i++) {
            _tableList.add(tableArray[i]);
        }
    } //-- void setTable(org.astrogrid.registry.beans.resource.dataservice.TableType) 

    /**
     * Method unmarshalTabularSkyServiceType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.TabularSkyServiceType unmarshalTabularSkyServiceType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.TabularSkyServiceType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.TabularSkyServiceType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.TabularSkyServiceType unmarshalTabularSkyServiceType(java.io.Reader) 

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
