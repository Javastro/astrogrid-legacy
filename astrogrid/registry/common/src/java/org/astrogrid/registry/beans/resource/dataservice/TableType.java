/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: TableType.java,v 1.3 2004/03/05 09:51:59 KevinBenson Exp $
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
 * Class TableType.
 * 
 * @version $Revision: 1.3 $ $Date: 2004/03/05 09:51:59 $
 */
public class TableType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * a name for the role this table plays. Recognized
     *  values include "out", indicating this table is output 
     *  from a query.
     *  
     */
    private java.lang.String _role;

    /**
     * the name of someone or something
     */
    private java.lang.String _name;

    /**
     * An account of the nature of the resource
     *  
     */
    private java.lang.String _description;

    /**
     * a description of a table column.
     *  
     */
    private java.util.ArrayList _columnList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TableType() {
        super();
        _columnList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.dataservice.TableType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addColumn
     * 
     * @param vColumn
     */
    public void addColumn(org.astrogrid.registry.beans.resource.dataservice.ParamType vColumn)
        throws java.lang.IndexOutOfBoundsException
    {
        _columnList.add(vColumn);
    } //-- void addColumn(org.astrogrid.registry.beans.resource.dataservice.ParamType) 

    /**
     * Method addColumn
     * 
     * @param index
     * @param vColumn
     */
    public void addColumn(int index, org.astrogrid.registry.beans.resource.dataservice.ParamType vColumn)
        throws java.lang.IndexOutOfBoundsException
    {
        _columnList.add(index, vColumn);
    } //-- void addColumn(int, org.astrogrid.registry.beans.resource.dataservice.ParamType) 

    /**
     * Method clearColumn
     */
    public void clearColumn()
    {
        _columnList.clear();
    } //-- void clearColumn() 

    /**
     * Method enumerateColumn
     */
    public java.util.Enumeration enumerateColumn()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_columnList.iterator());
    } //-- java.util.Enumeration enumerateColumn() 

    /**
     * Method getColumn
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.dataservice.ParamType getColumn(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _columnList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.dataservice.ParamType) _columnList.get(index);
    } //-- org.astrogrid.registry.beans.resource.dataservice.ParamType getColumn(int) 

    /**
     * Method getColumn
     */
    public org.astrogrid.registry.beans.resource.dataservice.ParamType[] getColumn()
    {
        int size = _columnList.size();
        org.astrogrid.registry.beans.resource.dataservice.ParamType[] mArray = new org.astrogrid.registry.beans.resource.dataservice.ParamType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.dataservice.ParamType) _columnList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.dataservice.ParamType[] getColumn() 

    /**
     * Method getColumnCount
     */
    public int getColumnCount()
    {
        return _columnList.size();
    } //-- int getColumnCount() 

    /**
     * Returns the value of field 'description'. The field
     * 'description' has the following description: An account of
     * the nature of the resource
     *  
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: the name of someone or something
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'role'. The field 'role' has the
     * following description: a name for the role this table plays.
     * Recognized
     *  values include "out", indicating this table is output 
     *  from a query.
     *  
     * 
     * @return the value of field 'role'.
     */
    public java.lang.String getRole()
    {
        return this._role;
    } //-- java.lang.String getRole() 

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
     * Method removeColumn
     * 
     * @param vColumn
     */
    public boolean removeColumn(org.astrogrid.registry.beans.resource.dataservice.ParamType vColumn)
    {
        boolean removed = _columnList.remove(vColumn);
        return removed;
    } //-- boolean removeColumn(org.astrogrid.registry.beans.resource.dataservice.ParamType) 

    /**
     * Method setColumn
     * 
     * @param index
     * @param vColumn
     */
    public void setColumn(int index, org.astrogrid.registry.beans.resource.dataservice.ParamType vColumn)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _columnList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _columnList.set(index, vColumn);
    } //-- void setColumn(int, org.astrogrid.registry.beans.resource.dataservice.ParamType) 

    /**
     * Method setColumn
     * 
     * @param columnArray
     */
    public void setColumn(org.astrogrid.registry.beans.resource.dataservice.ParamType[] columnArray)
    {
        //-- copy array
        _columnList.clear();
        for (int i = 0; i < columnArray.length; i++) {
            _columnList.add(columnArray[i]);
        }
    } //-- void setColumn(org.astrogrid.registry.beans.resource.dataservice.ParamType) 

    /**
     * Sets the value of field 'description'. The field
     * 'description' has the following description: An account of
     * the nature of the resource
     *  
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: the name of someone or something
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'role'. The field 'role' has the
     * following description: a name for the role this table plays.
     * Recognized
     *  values include "out", indicating this table is output 
     *  from a query.
     *  
     * 
     * @param role the value of field 'role'.
     */
    public void setRole(java.lang.String role)
    {
        this._role = role;
    } //-- void setRole(java.lang.String) 

    /**
     * Method unmarshalTableType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.TableType unmarshalTableType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.TableType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.TableType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.TableType unmarshalTableType(java.io.Reader) 

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
