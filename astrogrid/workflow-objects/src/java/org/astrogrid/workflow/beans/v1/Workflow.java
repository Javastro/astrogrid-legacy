/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Workflow.java,v 1.6 2004/03/03 19:05:19 pah Exp $
 */

package org.astrogrid.workflow.beans.v1;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * General representation of a workflow
 * 
 * @version $Revision: 1.6 $ $Date: 2004/03/03 19:05:19 $
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
     * Field _activities
     */
    private org.astrogrid.workflow.beans.v1.Activities _activities;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _credentials
     */
    private org.astrogrid.community.beans.v1.Credentials _credentials;

    /**
     * A record of a single execution of a job
     */
    private org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord _jobExecutionRecord;


      //----------------/
     //- Constructors -/
    //----------------/

    public Workflow() {
        super();
    } //-- org.astrogrid.workflow.beans.v1.Workflow()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'activities'.
     * 
     * @return the value of field 'activities'.
     */
    public org.astrogrid.workflow.beans.v1.Activities getActivities()
    {
        return this._activities;
    } //-- org.astrogrid.workflow.beans.v1.Activities getActivities() 

    /**
     * Returns the value of field 'credentials'.
     * 
     * @return the value of field 'credentials'.
     */
    public org.astrogrid.community.beans.v1.Credentials getCredentials()
    {
        return this._credentials;
    } //-- org.astrogrid.community.beans.v1.Credentials getCredentials() 

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
     * Returns the value of field 'jobExecutionRecord'. The field
     * 'jobExecutionRecord' has the following description: A record
     * of a single execution of a job
     * 
     * @return the value of field 'jobExecutionRecord'.
     */
    public org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord getJobExecutionRecord()
    {
        return this._jobExecutionRecord;
    } //-- org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord getJobExecutionRecord() 

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
     * Sets the value of field 'activities'.
     * 
     * @param activities the value of field 'activities'.
     */
    public void setActivities(org.astrogrid.workflow.beans.v1.Activities activities)
    {
        this._activities = activities;
    } //-- void setActivities(org.astrogrid.workflow.beans.v1.Activities) 

    /**
     * Sets the value of field 'credentials'.
     * 
     * @param credentials the value of field 'credentials'.
     */
    public void setCredentials(org.astrogrid.community.beans.v1.Credentials credentials)
    {
        this._credentials = credentials;
    } //-- void setCredentials(org.astrogrid.community.beans.v1.Credentials) 

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
     * Sets the value of field 'jobExecutionRecord'. The field
     * 'jobExecutionRecord' has the following description: A record
     * of a single execution of a job
     * 
     * @param jobExecutionRecord the value of field
     * 'jobExecutionRecord'.
     */
    public void setJobExecutionRecord(org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord jobExecutionRecord)
    {
        this._jobExecutionRecord = jobExecutionRecord;
    } //-- void setJobExecutionRecord(org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord) 

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
