/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Script.java,v 1.3 2004/07/09 14:44:42 nw Exp $
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
 * script text to be executed
 * 
 * @version $Revision: 1.3 $ $Date: 2004/07/09 14:44:42 $
 */
public class Script extends org.astrogrid.workflow.beans.v1.AbstractActivity 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _scriptingLanguage
     */
    private java.lang.String _scriptingLanguage = "jython-2.1";

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _body
     */
    private java.lang.String _body;

    /**
     * A record of a single execution of a job step
     */
    private java.util.ArrayList _stepExecutionRecordList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Script() {
        super();
        setScriptingLanguage("jython-2.1");
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
            if (this._scriptingLanguage != null) {
                if (temp._scriptingLanguage == null) return false;
                else if (!(this._scriptingLanguage.equals(temp._scriptingLanguage))) 
                    return false;
            }
            else if (temp._scriptingLanguage != null)
                return false;
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
     * Returns the value of field 'body'.
     * 
     * @return the value of field 'body'.
     */
    public java.lang.String getBody()
    {
        return this._body;
    } //-- java.lang.String getBody() 

    /**
     * Returns the value of field 'description'.
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'scriptingLanguage'.
     * 
     * @return the value of field 'scriptingLanguage'.
     */
    public java.lang.String getScriptingLanguage()
    {
        return this._scriptingLanguage;
    } //-- java.lang.String getScriptingLanguage() 

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
     * Sets the value of field 'body'.
     * 
     * @param body the value of field 'body'.
     */
    public void setBody(java.lang.String body)
    {
        this._body = body;
    } //-- void setBody(java.lang.String) 

    /**
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'scriptingLanguage'.
     * 
     * @param scriptingLanguage the value of field
     * 'scriptingLanguage'.
     */
    public void setScriptingLanguage(java.lang.String scriptingLanguage)
    {
        this._scriptingLanguage = scriptingLanguage;
    } //-- void setScriptingLanguage(java.lang.String) 

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
