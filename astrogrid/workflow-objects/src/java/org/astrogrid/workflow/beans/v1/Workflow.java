/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Workflow.java,v 1.1 2004/02/20 18:36:39 nw Exp $
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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Workflow.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/02/20 18:36:39 $
 */
public class Workflow extends org.astrogrid.common.bean.BaseBean 
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
     * Field _startDate
     */
    private java.util.Date _startDate;

    /**
     * Field _completeDate
     */
    private java.util.Date _completeDate;

    /**
     * Field _jobURN
     */
    private java.lang.String _jobURN;

    /**
     * Field _status
     */
    private org.astrogrid.workflow.beans.v1.types.Status _status = org.astrogrid.workflow.beans.v1.types.Status.valueOf("INITIALIZED");

    /**
     * Field _community
     */
    private org.astrogrid.workflow.beans.v1.Community _community;

    /**
     * Field _sequence
     */
    private org.astrogrid.workflow.beans.v1.Sequence _sequence;

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

    public Workflow() {
        super();
        setStatus(org.astrogrid.workflow.beans.v1.types.Status.valueOf("INITIALIZED"));
    } //-- org.astrogrid.workflow.beans.v1.Workflow()


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
     * Returns the value of field 'community'.
     * 
     * @return the value of field 'community'.
     */
    public org.astrogrid.workflow.beans.v1.Community getCommunity()
    {
        return this._community;
    } //-- org.astrogrid.workflow.beans.v1.Community getCommunity() 

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
     * Returns the value of field 'jobURN'.
     * 
     * @return the value of field 'jobURN'.
     */
    public java.lang.String getJobURN()
    {
        return this._jobURN;
    } //-- java.lang.String getJobURN() 

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
     * Returns the value of field 'sequence'.
     * 
     * @return the value of field 'sequence'.
     */
    public org.astrogrid.workflow.beans.v1.Sequence getSequence()
    {
        return this._sequence;
    } //-- org.astrogrid.workflow.beans.v1.Sequence getSequence() 

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
     * Sets the value of field 'community'.
     * 
     * @param community the value of field 'community'.
     */
    public void setCommunity(org.astrogrid.workflow.beans.v1.Community community)
    {
        this._community = community;
    } //-- void setCommunity(org.astrogrid.workflow.beans.v1.Community) 

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
     * Sets the value of field 'jobURN'.
     * 
     * @param jobURN the value of field 'jobURN'.
     */
    public void setJobURN(java.lang.String jobURN)
    {
        this._jobURN = jobURN;
    } //-- void setJobURN(java.lang.String) 

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
     * Sets the value of field 'sequence'.
     * 
     * @param sequence the value of field 'sequence'.
     */
    public void setSequence(org.astrogrid.workflow.beans.v1.Sequence sequence)
    {
        this._sequence = sequence;
    } //-- void setSequence(org.astrogrid.workflow.beans.v1.Sequence) 

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
     * Method unmarshalWorkflow
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.Workflow unmarshalWorkflow(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.Workflow) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.Workflow.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.Workflow unmarshalWorkflow(java.io.Reader) 

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
