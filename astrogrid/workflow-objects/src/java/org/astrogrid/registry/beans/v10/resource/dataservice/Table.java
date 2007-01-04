/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Table.java,v 1.2 2007/01/04 16:26:25 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.dataservice;

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
 * Class Table.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:25 $
 */
public class Table extends org.astrogrid.common.bean.BaseBean 
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
     * the name of the table 
     *  
     */
    private java.lang.String _name;

    /**
     * a description of the tables contents
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

    public Table() {
        super();
        _columnList = new ArrayList();
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Table()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addColumn
     * 
     * @param vColumn
     */
    public void addColumn(org.astrogrid.registry.beans.v10.resource.dataservice.Param vColumn)
        throws java.lang.IndexOutOfBoundsException
    {
        _columnList.add(vColumn);
    } //-- void addColumn(org.astrogrid.registry.beans.v10.resource.dataservice.Param) 

    /**
     * Method addColumn
     * 
     * @param index
     * @param vColumn
     */
    public void addColumn(int index, org.astrogrid.registry.beans.v10.resource.dataservice.Param vColumn)
        throws java.lang.IndexOutOfBoundsException
    {
        _columnList.add(index, vColumn);
    } //-- void addColumn(int, org.astrogrid.registry.beans.v10.resource.dataservice.Param) 

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
        
        if (obj instanceof Table) {
        
            Table temp = (Table)obj;
            if (this._role != null) {
                if (temp._role == null) return false;
                else if (!(this._role.equals(temp._role))) 
                    return false;
            }
            else if (temp._role != null)
                return false;
            if (this._name != null) {
                if (temp._name == null) return false;
                else if (!(this._name.equals(temp._name))) 
                    return false;
            }
            else if (temp._name != null)
                return false;
            if (this._description != null) {
                if (temp._description == null) return false;
                else if (!(this._description.equals(temp._description))) 
                    return false;
            }
            else if (temp._description != null)
                return false;
            if (this._columnList != null) {
                if (temp._columnList == null) return false;
                else if (!(this._columnList.equals(temp._columnList))) 
                    return false;
            }
            else if (temp._columnList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getColumn
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.Param getColumn(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _columnList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.v10.resource.dataservice.Param) _columnList.get(index);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Param getColumn(int) 

    /**
     * Method getColumn
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.Param[] getColumn()
    {
        int size = _columnList.size();
        org.astrogrid.registry.beans.v10.resource.dataservice.Param[] mArray = new org.astrogrid.registry.beans.v10.resource.dataservice.Param[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.v10.resource.dataservice.Param) _columnList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Param[] getColumn() 

    /**
     * Method getColumnCount
     */
    public int getColumnCount()
    {
        return _columnList.size();
    } //-- int getColumnCount() 

    /**
     * Returns the value of field 'description'. The field
     * 'description' has the following description: a description
     * of the tables contents
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
     * following description: the name of the table 
     *  
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
    public boolean removeColumn(org.astrogrid.registry.beans.v10.resource.dataservice.Param vColumn)
    {
        boolean removed = _columnList.remove(vColumn);
        return removed;
    } //-- boolean removeColumn(org.astrogrid.registry.beans.v10.resource.dataservice.Param) 

    /**
     * Method setColumn
     * 
     * @param index
     * @param vColumn
     */
    public void setColumn(int index, org.astrogrid.registry.beans.v10.resource.dataservice.Param vColumn)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _columnList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _columnList.set(index, vColumn);
    } //-- void setColumn(int, org.astrogrid.registry.beans.v10.resource.dataservice.Param) 

    /**
     * Method setColumn
     * 
     * @param columnArray
     */
    public void setColumn(org.astrogrid.registry.beans.v10.resource.dataservice.Param[] columnArray)
    {
        //-- copy array
        _columnList.clear();
        for (int i = 0; i < columnArray.length; i++) {
            _columnList.add(columnArray[i]);
        }
    } //-- void setColumn(org.astrogrid.registry.beans.v10.resource.dataservice.Param) 

    /**
     * Sets the value of field 'description'. The field
     * 'description' has the following description: a description
     * of the tables contents
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
     * following description: the name of the table 
     *  
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
     * Method unmarshalTable
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.dataservice.Table unmarshalTable(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.dataservice.Table) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.dataservice.Table.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Table unmarshalTable(java.io.Reader) 

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
