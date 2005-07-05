/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Script.java,v 1.2 2005/07/05 08:26:55 clq2 Exp $
 */

package org.astrogrid.workflow.beans.v1;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * a step in the workflow - execute some script statements.
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:55 $
 */
public class Script extends org.astrogrid.workflow.beans.v1.AbstractActivity 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * optional description of this step
     */
    private java.lang.String _description;

    /**
     * script statements to execute. By default, groovy language is
     * expected
     */
    private java.lang.String _body;

    /**
     * records of execution of this script step
     */
    private java.util.ArrayList _stepExecutionRecordList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Script() {
        super();
        _stepExecutionRecordList = new ArrayList();
    } //-- org.astrogrid.workflow.beans.v1.Script()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addStepExecutionRecord
     * 
     * @param vStepExecutionRecord
     */
    public void addStepExecutionRecord(org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord vStepExecutionRecord)
        throws java.lang.IndexOutOfBoundsException
    {
        _stepExecutionRecordList.add(vStepExecutionRecord);
    } //-- void addStepExecutionRecord(org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord) 

    /**
     * Method addStepExecutionRecord
     * 
     * @param index
     * @param vStepExecutionRecord
     */
    public void addStepExecutionRecord(int index, org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord vStepExecutionRecord)
        throws java.lang.IndexOutOfBoundsException
    {
        _stepExecutionRecordList.add(index, vStepExecutionRecord);
    } //-- void addStepExecutionRecord(int, org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord) 

    /**
     * Method clearStepExecutionRecord
     */
    public void clearStepExecutionRecord()
    {
        _stepExecutionRecordList.clear();
    } //-- void clearStepExecutionRecord() 

    /**
     * Method enumerateStepExecutionRecord
     */
    public java.util.Enumeration enumerateStepExecutionRecord()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_stepExecutionRecordList.iterator());
    } //-- java.util.Enumeration enumerateStepExecutionRecord() 

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
        
        if (obj instanceof Script) {
        
            Script temp = (Script)obj;
            if (this._description != null) {
                if (temp._description == null) return false;
                else if (!(this._description.equals(temp._description))) 
                    return false;
            }
            else if (temp._description != null)
                return false;
            if (this._body != null) {
                if (temp._body == null) return false;
                else if (!(this._body.equals(temp._body))) 
                    return false;
            }
            else if (temp._body != null)
                return false;
            if (this._stepExecutionRecordList != null) {
                if (temp._stepExecutionRecordList == null) return false;
                else if (!(this._stepExecutionRecordList.equals(temp._stepExecutionRecordList))) 
                    return false;
            }
            else if (temp._stepExecutionRecordList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'body'. The field 'body' has the
     * following description: script statements to execute. By
     * default, groovy language is expected
     * 
     * @return the value of field 'body'.
     */
    public java.lang.String getBody()
    {
        return this._body;
    } //-- java.lang.String getBody() 

    /**
     * Returns the value of field 'description'. The field
     * 'description' has the following description: optional
     * description of this step
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Method getStepExecutionRecord
     * 
     * @param index
     */
    public org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord getStepExecutionRecord(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _stepExecutionRecordList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord) _stepExecutionRecordList.get(index);
    } //-- org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord getStepExecutionRecord(int) 

    /**
     * Method getStepExecutionRecord
     */
    public org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord[] getStepExecutionRecord()
    {
        int size = _stepExecutionRecordList.size();
        org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord[] mArray = new org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord) _stepExecutionRecordList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord[] getStepExecutionRecord() 

    /**
     * Method getStepExecutionRecordCount
     */
    public int getStepExecutionRecordCount()
    {
        return _stepExecutionRecordList.size();
    } //-- int getStepExecutionRecordCount() 

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
     * Method removeStepExecutionRecord
     * 
     * @param vStepExecutionRecord
     */
    public boolean removeStepExecutionRecord(org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord vStepExecutionRecord)
    {
        boolean removed = _stepExecutionRecordList.remove(vStepExecutionRecord);
        return removed;
    } //-- boolean removeStepExecutionRecord(org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord) 

    /**
     * Sets the value of field 'body'. The field 'body' has the
     * following description: script statements to execute. By
     * default, groovy language is expected
     * 
     * @param body the value of field 'body'.
     */
    public void setBody(java.lang.String body)
    {
        this._body = body;
    } //-- void setBody(java.lang.String) 

    /**
     * Sets the value of field 'description'. The field
     * 'description' has the following description: optional
     * description of this step
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Method setStepExecutionRecord
     * 
     * @param index
     * @param vStepExecutionRecord
     */
    public void setStepExecutionRecord(int index, org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord vStepExecutionRecord)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _stepExecutionRecordList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _stepExecutionRecordList.set(index, vStepExecutionRecord);
    } //-- void setStepExecutionRecord(int, org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord) 

    /**
     * Method setStepExecutionRecord
     * 
     * @param stepExecutionRecordArray
     */
    public void setStepExecutionRecord(org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord[] stepExecutionRecordArray)
    {
        //-- copy array
        _stepExecutionRecordList.clear();
        for (int i = 0; i < stepExecutionRecordArray.length; i++) {
            _stepExecutionRecordList.add(stepExecutionRecordArray[i]);
        }
    } //-- void setStepExecutionRecord(org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord) 

    /**
     * Method unmarshalScript
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.Script unmarshalScript(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.Script) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.Script.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.Script unmarshalScript(java.io.Reader) 

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
