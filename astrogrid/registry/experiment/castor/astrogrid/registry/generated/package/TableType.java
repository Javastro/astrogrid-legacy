/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: TableType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
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
 * Class TableType.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class TableType implements java.io.Serializable {


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
    private java.util.Vector _columnList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TableType() {
        super();
        _columnList = new Vector();
    } //-- org.astrogrid.registry.generated.package.TableType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addColumn
     * 
     * @param vColumn
     */
    public void addColumn(org.astrogrid.registry.generated.package.Column vColumn)
        throws java.lang.IndexOutOfBoundsException
    {
        _columnList.addElement(vColumn);
    } //-- void addColumn(org.astrogrid.registry.generated.package.Column) 

    /**
     * Method addColumn
     * 
     * @param index
     * @param vColumn
     */
    public void addColumn(int index, org.astrogrid.registry.generated.package.Column vColumn)
        throws java.lang.IndexOutOfBoundsException
    {
        _columnList.insertElementAt(vColumn, index);
    } //-- void addColumn(int, org.astrogrid.registry.generated.package.Column) 

    /**
     * Method enumerateColumn
     */
    public java.util.Enumeration enumerateColumn()
    {
        return _columnList.elements();
    } //-- java.util.Enumeration enumerateColumn() 

    /**
     * Method getColumn
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.Column getColumn(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _columnList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.Column) _columnList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.Column getColumn(int) 

    /**
     * Method getColumn
     */
    public org.astrogrid.registry.generated.package.Column[] getColumn()
    {
        int size = _columnList.size();
        org.astrogrid.registry.generated.package.Column[] mArray = new org.astrogrid.registry.generated.package.Column[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.Column) _columnList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.Column[] getColumn() 

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
     * Method removeAllColumn
     */
    public void removeAllColumn()
    {
        _columnList.removeAllElements();
    } //-- void removeAllColumn() 

    /**
     * Method removeColumn
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.Column removeColumn(int index)
    {
        java.lang.Object obj = _columnList.elementAt(index);
        _columnList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.Column) obj;
    } //-- org.astrogrid.registry.generated.package.Column removeColumn(int) 

    /**
     * Method setColumn
     * 
     * @param index
     * @param vColumn
     */
    public void setColumn(int index, org.astrogrid.registry.generated.package.Column vColumn)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _columnList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _columnList.setElementAt(vColumn, index);
    } //-- void setColumn(int, org.astrogrid.registry.generated.package.Column) 

    /**
     * Method setColumn
     * 
     * @param columnArray
     */
    public void setColumn(org.astrogrid.registry.generated.package.Column[] columnArray)
    {
        //-- copy array
        _columnList.removeAllElements();
        for (int i = 0; i < columnArray.length; i++) {
            _columnList.addElement(columnArray[i]);
        }
    } //-- void setColumn(org.astrogrid.registry.generated.package.Column) 

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
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.TableType) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.TableType.class, reader);
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
