/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Step.java,v 1.14 2004/03/10 17:13:59 pah Exp $
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
import org.astrogrid.workflow.beans.v1.types.JoinType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * an activity to be performed
 * 
 * @version $Revision: 1.14 $ $Date: 2004/03/10 17:13:59 $
 */
public class Step extends org.astrogrid.workflow.beans.v1.AbstractActivity 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _joinCondition
     */
    private org.astrogrid.workflow.beans.v1.types.JoinType _joinCondition = org.astrogrid.workflow.beans.v1.types.JoinType.valueOf("any");

    /**
     * Field _stepNumber
     */
    private int _stepNumber;

    /**
     * keeps track of state for field: _stepNumber
     */
    private boolean _has_stepNumber;

    /**
     * Field _sequenceNumber
     */
    private int _sequenceNumber;

    /**
     * keeps track of state for field: _sequenceNumber
     */
    private boolean _has_sequenceNumber;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * the CEA definition of the tool to be run 
     */
    private org.astrogrid.workflow.beans.v1.Tool _tool;

    /**
     * A record of a single execution of a job step
     */
    private java.util.ArrayList _stepExecutionRecordList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Step() {
        super();
        setJoinCondition(org.astrogrid.workflow.beans.v1.types.JoinType.valueOf("any"));
        _stepExecutionRecordList = new ArrayList();
    } //-- org.astrogrid.workflow.beans.v1.Step()


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
     * Method deleteSequenceNumber
     */
    public void deleteSequenceNumber()
    {
        this._has_sequenceNumber= false;
    } //-- void deleteSequenceNumber() 

    /**
     * Method deleteStepNumber
     */
    public void deleteStepNumber()
    {
        this._has_stepNumber= false;
    } //-- void deleteStepNumber() 

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
        
        if (obj instanceof Step) {
        
            Step temp = (Step)obj;
            if (this._name != null) {
                if (temp._name == null) return false;
                else if (!(this._name.equals(temp._name))) 
                    return false;
            }
            else if (temp._name != null)
                return false;
            if (this._joinCondition != null) {
                if (temp._joinCondition == null) return false;
                else if (!(this._joinCondition.equals(temp._joinCondition))) 
                    return false;
            }
            else if (temp._joinCondition != null)
                return false;
            if (this._stepNumber != temp._stepNumber)
                return false;
            if (this._has_stepNumber != temp._has_stepNumber)
                return false;
            if (this._sequenceNumber != temp._sequenceNumber)
                return false;
            if (this._has_sequenceNumber != temp._has_sequenceNumber)
                return false;
            if (this._description != null) {
                if (temp._description == null) return false;
                else if (!(this._description.equals(temp._description))) 
                    return false;
            }
            else if (temp._description != null)
                return false;
            if (this._tool != null) {
                if (temp._tool == null) return false;
                else if (!(this._tool.equals(temp._tool))) 
                    return false;
            }
            else if (temp._tool != null)
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
     * Returns the value of field 'description'.
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'joinCondition'.
     * 
     * @return the value of field 'joinCondition'.
     */
    public org.astrogrid.workflow.beans.v1.types.JoinType getJoinCondition()
    {
        return this._joinCondition;
    } //-- org.astrogrid.workflow.beans.v1.types.JoinType getJoinCondition() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'sequenceNumber'.
     * 
     * @return the value of field 'sequenceNumber'.
     */
    public int getSequenceNumber()
    {
        return this._sequenceNumber;
    } //-- int getSequenceNumber() 

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
     * Returns the value of field 'stepNumber'.
     * 
     * @return the value of field 'stepNumber'.
     */
    public int getStepNumber()
    {
        return this._stepNumber;
    } //-- int getStepNumber() 

    /**
     * Returns the value of field 'tool'. The field 'tool' has the
     * following description: the CEA definition of the tool to be
     * run 
     * 
     * @return the value of field 'tool'.
     */
    public org.astrogrid.workflow.beans.v1.Tool getTool()
    {
        return this._tool;
    } //-- org.astrogrid.workflow.beans.v1.Tool getTool() 

    /**
     * Method hasSequenceNumber
     */
    public boolean hasSequenceNumber()
    {
        return this._has_sequenceNumber;
    } //-- boolean hasSequenceNumber() 

    /**
     * Method hasStepNumber
     */
    public boolean hasStepNumber()
    {
        return this._has_stepNumber;
    } //-- boolean hasStepNumber() 

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
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'joinCondition'.
     * 
     * @param joinCondition the value of field 'joinCondition'.
     */
    public void setJoinCondition(org.astrogrid.workflow.beans.v1.types.JoinType joinCondition)
    {
        this._joinCondition = joinCondition;
    } //-- void setJoinCondition(org.astrogrid.workflow.beans.v1.types.JoinType) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'sequenceNumber'.
     * 
     * @param sequenceNumber the value of field 'sequenceNumber'.
     */
    public void setSequenceNumber(int sequenceNumber)
    {
        this._sequenceNumber = sequenceNumber;
        this._has_sequenceNumber = true;
    } //-- void setSequenceNumber(int) 

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
     * Sets the value of field 'stepNumber'.
     * 
     * @param stepNumber the value of field 'stepNumber'.
     */
    public void setStepNumber(int stepNumber)
    {
        this._stepNumber = stepNumber;
        this._has_stepNumber = true;
    } //-- void setStepNumber(int) 

    /**
     * Sets the value of field 'tool'. The field 'tool' has the
     * following description: the CEA definition of the tool to be
     * run 
     * 
     * @param tool the value of field 'tool'.
     */
    public void setTool(org.astrogrid.workflow.beans.v1.Tool tool)
    {
        this._tool = tool;
    } //-- void setTool(org.astrogrid.workflow.beans.v1.Tool) 

    /**
     * Method unmarshalStep
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.Step unmarshalStep(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.Step) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.Step.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.Step unmarshalStep(java.io.Reader) 

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
