/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: JobExecutionRecord.java,v 1.16 2004/03/30 22:42:55 pah Exp $
 */

package org.astrogrid.workflow.beans.v1.execution;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * A record of a single execution of a job
 * 
 * @version $Revision: 1.16 $ $Date: 2004/03/30 22:42:55 $
 */
public class JobExecutionRecord extends org.astrogrid.workflow.beans.v1.execution.ExecutionRecordType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _jobId
     */
    private org.astrogrid.workflow.beans.v1.execution.JobURN _jobId;


      //----------------/
     //- Constructors -/
    //----------------/

    public JobExecutionRecord() {
        super();
    } //-- org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord()


      //-----------/
     //- Methods -/
    //-----------/

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
        
        if (obj instanceof JobExecutionRecord) {
        
            JobExecutionRecord temp = (JobExecutionRecord)obj;
            if (this._jobId != null) {
                if (temp._jobId == null) return false;
                else if (!(this._jobId.equals(temp._jobId))) 
                    return false;
            }
            else if (temp._jobId != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'jobId'.
     * 
     * @return the value of field 'jobId'.
     */
    public org.astrogrid.workflow.beans.v1.execution.JobURN getJobId()
    {
        return this._jobId;
    } //-- org.astrogrid.workflow.beans.v1.execution.JobURN getJobId() 

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
     * Sets the value of field 'jobId'.
     * 
     * @param jobId the value of field 'jobId'.
     */
    public void setJobId(org.astrogrid.workflow.beans.v1.execution.JobURN jobId)
    {
        this._jobId = jobId;
    } //-- void setJobId(org.astrogrid.workflow.beans.v1.execution.JobURN) 

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
