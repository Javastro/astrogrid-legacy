/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: JobExecutionRecord.java,v 1.1 2004/03/02 16:50:20 nw Exp $
 */

package org.astrogrid.workflow.beans.v1.execution;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Date;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * A record of a single execution of a job
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/02 16:50:20 $
 */
public class JobExecutionRecord extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _jobID
     */
    private java.lang.String _jobID;

    /**
     * Field _startTime
     */
    private java.util.Date _startTime;

    /**
     * Field _finishTime
     */
    private java.util.Date _finishTime;

    /**
     * Field _status
     */
    private org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase _status = org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase.valueOf("UNKNOWN");

    /**
     * Field _message
     */
    private org.astrogrid.applications.beans.v1.cea.castor.MessageType _message;


      //----------------/
     //- Constructors -/
    //----------------/

    public JobExecutionRecord() {
        super();
        setStatus(org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase.valueOf("UNKNOWN"));
    } //-- org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'finishTime'.
     * 
     * @return the value of field 'finishTime'.
     */
    public java.util.Date getFinishTime()
    {
        return this._finishTime;
    } //-- java.util.Date getFinishTime() 

    /**
     * Returns the value of field 'jobID'.
     * 
     * @return the value of field 'jobID'.
     */
    public java.lang.String getJobID()
    {
        return this._jobID;
    } //-- java.lang.String getJobID() 

    /**
     * Returns the value of field 'message'.
     * 
     * @return the value of field 'message'.
     */
    public org.astrogrid.applications.beans.v1.cea.castor.MessageType getMessage()
    {
        return this._message;
    } //-- org.astrogrid.applications.beans.v1.cea.castor.MessageType getMessage() 

    /**
     * Returns the value of field 'startTime'.
     * 
     * @return the value of field 'startTime'.
     */
    public java.util.Date getStartTime()
    {
        return this._startTime;
    } //-- java.util.Date getStartTime() 

    /**
     * Returns the value of field 'status'.
     * 
     * @return the value of field 'status'.
     */
    public org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase getStatus()
    {
        return this._status;
    } //-- org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase getStatus() 

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
     * Sets the value of field 'finishTime'.
     * 
     * @param finishTime the value of field 'finishTime'.
     */
    public void setFinishTime(java.util.Date finishTime)
    {
        this._finishTime = finishTime;
    } //-- void setFinishTime(java.util.Date) 

    /**
     * Sets the value of field 'jobID'.
     * 
     * @param jobID the value of field 'jobID'.
     */
    public void setJobID(java.lang.String jobID)
    {
        this._jobID = jobID;
    } //-- void setJobID(java.lang.String) 

    /**
     * Sets the value of field 'message'.
     * 
     * @param message the value of field 'message'.
     */
    public void setMessage(org.astrogrid.applications.beans.v1.cea.castor.MessageType message)
    {
        this._message = message;
    } //-- void setMessage(org.astrogrid.applications.beans.v1.cea.castor.MessageType) 

    /**
     * Sets the value of field 'startTime'.
     * 
     * @param startTime the value of field 'startTime'.
     */
    public void setStartTime(java.util.Date startTime)
    {
        this._startTime = startTime;
    } //-- void setStartTime(java.util.Date) 

    /**
     * Sets the value of field 'status'.
     * 
     * @param status the value of field 'status'.
     */
    public void setStatus(org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase status)
    {
        this._status = status;
    } //-- void setStatus(org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase) 

    /**
     * Method unmarshalJobExecutionRecord
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord unmarshalJobExecutionRecord(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord unmarshalJobExecutionRecord(java.io.Reader) 

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
