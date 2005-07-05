/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: WorkflowSummaryList.java,v 1.2 2005/07/05 08:27:00 clq2 Exp $
 */

package org.astrogrid.workflow.beans.v1.execution;

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
 * A summary of an execution of a job
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:27:00 $
 */
public class WorkflowSummaryList extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _itemList
     */
    private java.util.ArrayList _itemList;


      //----------------/
     //- Constructors -/
    //----------------/

    public WorkflowSummaryList() {
        super();
        _itemList = new ArrayList();
    } //-- org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryList()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addItem
     * 
     * @param vItem
     */
    public void addItem(org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _itemList.add(vItem);
    } //-- void addItem(org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType) 

    /**
     * Method addItem
     * 
     * @param index
     * @param vItem
     */
    public void addItem(int index, org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _itemList.add(index, vItem);
    } //-- void addItem(int, org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType) 

    /**
     * Method clearItem
     */
    public void clearItem()
    {
        _itemList.clear();
    } //-- void clearItem() 

    /**
     * Method enumerateItem
     */
    public java.util.Enumeration enumerateItem()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_itemList.iterator());
    } //-- java.util.Enumeration enumerateItem() 

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
        
        if (obj instanceof WorkflowSummaryList) {
        
            WorkflowSummaryList temp = (WorkflowSummaryList)obj;
            if (this._itemList != null) {
                if (temp._itemList == null) return false;
                else if (!(this._itemList.equals(temp._itemList))) 
                    return false;
            }
            else if (temp._itemList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getItem
     * 
     * @param index
     */
    public org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType getItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _itemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType) _itemList.get(index);
    } //-- org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType getItem(int) 

    /**
     * Method getItem
     */
    public org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType[] getItem()
    {
        int size = _itemList.size();
        org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType[] mArray = new org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType) _itemList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType[] getItem() 

    /**
     * Method getItemCount
     */
    public int getItemCount()
    {
        return _itemList.size();
    } //-- int getItemCount() 

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
     * Method removeItem
     * 
     * @param vItem
     */
    public boolean removeItem(org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType vItem)
    {
        boolean removed = _itemList.remove(vItem);
        return removed;
    } //-- boolean removeItem(org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType) 

    /**
     * Method setItem
     * 
     * @param index
     * @param vItem
     */
    public void setItem(int index, org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _itemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _itemList.set(index, vItem);
    } //-- void setItem(int, org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType) 

    /**
     * Method setItem
     * 
     * @param itemArray
     */
    public void setItem(org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType[] itemArray)
    {
        //-- copy array
        _itemList.clear();
        for (int i = 0; i < itemArray.length; i++) {
            _itemList.add(itemArray[i]);
        }
    } //-- void setItem(org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType) 

    /**
     * Method unmarshalWorkflowSummaryList
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryList unmarshalWorkflowSummaryList(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryList) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryList.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryList unmarshalWorkflowSummaryList(java.io.Reader) 

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
