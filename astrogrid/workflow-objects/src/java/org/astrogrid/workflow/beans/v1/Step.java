/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Step.java,v 1.2 2004/03/02 14:09:49 pah Exp $
 */

package org.astrogrid.workflow.beans.v1;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Date;
import org.astrogrid.workflow.beans.v1.types.Status;
import org.astrogrid.workflow.beans.v1.types.StepJoinConditionType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Step.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/02 14:09:49 $
 */
public class Step extends org.astrogrid.common.bean.BaseBean 
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
    private org.astrogrid.workflow.beans.v1.types.StepJoinConditionType _joinCondition = org.astrogrid.workflow.beans.v1.types.StepJoinConditionType.valueOf("any");

    /**
     * Field _startDate
     */
    private java.util.Date _startDate;

    /**
     * Field _completeDate
     */
    private java.util.Date _completeDate;

    /**
     * Field _status
     */
    private org.astrogrid.workflow.beans.v1.types.Status _status = org.astrogrid.workflow.beans.v1.types.Status.valueOf("INITIALIZED");

    /**
     * Field _stepNumber
     */
    private short _stepNumber;

    /**
     * keeps track of state for field: _stepNumber
     */
    private boolean _has_stepNumber;

    /**
     * Field _sequenceNumber
     */
    private short _sequenceNumber;

    /**
     * keeps track of state for field: _sequenceNumber
     */
    private boolean _has_sequenceNumber;

    /**
     * Field _tool
     */
    private org.astrogrid.workflow.beans.v1.Tool _tool;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _comment
     */
    private java.lang.String _comment;


      //----------------/
     //- Constructors -/
    //----------------/

    public Step() {
        super();
        setJoinCondition(org.astrogrid.workflow.beans.v1.types.StepJoinConditionType.valueOf("any"));
        setStatus(org.astrogrid.workflow.beans.v1.types.Status.valueOf("INITIALIZED"));
    } //-- org.astrogrid.workflow.beans.v1.Step()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'comment'.
     * 
     * @return the value of field 'comment'.
     */
    public java.lang.String getComment()
    {
        return this._comment;
    } //-- java.lang.String getComment() 

    /**
     * Returns the value of field 'completeDate'.
     * 
     * @return the value of field 'completeDate'.
     */
    public java.util.Date getCompleteDate()
    {
        return this._completeDate;
    } //-- java.util.Date getCompleteDate() 

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
    public org.astrogrid.workflow.beans.v1.types.StepJoinConditionType getJoinCondition()
    {
        return this._joinCondition;
    } //-- org.astrogrid.workflow.beans.v1.types.StepJoinConditionType getJoinCondition() 

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
    public short getSequenceNumber()
    {
        return this._sequenceNumber;
    } //-- short getSequenceNumber() 

    /**
     * Returns the value of field 'startDate'.
     * 
     * @return the value of field 'startDate'.
     */
    public java.util.Date getStartDate()
    {
        return this._startDate;
    } //-- java.util.Date getStartDate() 

    /**
     * Returns the value of field 'status'.
     * 
     * @return the value of field 'status'.
     */
    public org.astrogrid.workflow.beans.v1.types.Status getStatus()
    {
        return this._status;
    } //-- org.astrogrid.workflow.beans.v1.types.Status getStatus() 

    /**
     * Returns the value of field 'stepNumber'.
     * 
     * @return the value of field 'stepNumber'.
     */
    public short getStepNumber()
    {
        return this._stepNumber;
    } //-- short getStepNumber() 

    /**
     * Returns the value of field 'tool'.
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
     * Sets the value of field 'comment'.
     * 
     * @param comment the value of field 'comment'.
     */
    public void setComment(java.lang.String comment)
    {
        this._comment = comment;
    } //-- void setComment(java.lang.String) 

    /**
     * Sets the value of field 'completeDate'.
     * 
     * @param completeDate the value of field 'completeDate'.
     */
    public void setCompleteDate(java.util.Date completeDate)
    {
        this._completeDate = completeDate;
    } //-- void setCompleteDate(java.util.Date) 

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
    public void setJoinCondition(org.astrogrid.workflow.beans.v1.types.StepJoinConditionType joinCondition)
    {
        this._joinCondition = joinCondition;
    } //-- void setJoinCondition(org.astrogrid.workflow.beans.v1.types.StepJoinConditionType) 

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
    public void setSequenceNumber(short sequenceNumber)
    {
        this._sequenceNumber = sequenceNumber;
        this._has_sequenceNumber = true;
    } //-- void setSequenceNumber(short) 

    /**
     * Sets the value of field 'startDate'.
     * 
     * @param startDate the value of field 'startDate'.
     */
    public void setStartDate(java.util.Date startDate)
    {
        this._startDate = startDate;
    } //-- void setStartDate(java.util.Date) 

    /**
     * Sets the value of field 'status'.
     * 
     * @param status the value of field 'status'.
     */
    public void setStatus(org.astrogrid.workflow.beans.v1.types.Status status)
    {
        this._status = status;
    } //-- void setStatus(org.astrogrid.workflow.beans.v1.types.Status) 

    /**
     * Sets the value of field 'stepNumber'.
     * 
     * @param stepNumber the value of field 'stepNumber'.
     */
    public void setStepNumber(short stepNumber)
    {
        this._stepNumber = stepNumber;
        this._has_stepNumber = true;
    } //-- void setStepNumber(short) 

    /**
     * Sets the value of field 'tool'.
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
