/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Workflow.java,v 1.31 2004/09/09 10:41:47 pah Exp $
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
 * Base element of an Astrogird workflow document, conforming to
 * the schema
 *  defined for namespace
 * http://www.astrogrid.org/schema/AGWorkflow/v1
 *  
 * 
 * @version $Revision: 1.31 $ $Date: 2004/09/09 10:41:47 $
 */
public class Workflow extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * descriptive name for this workflow
     */
    private java.lang.String _name;

    /**
     * used within the job execution system.
     */
    private java.lang.String _id;

    /**
     * sequence of activities to perform
     */
    private org.astrogrid.workflow.beans.v1.Sequence _sequence;

    /**
     * An optional textual description
     */
    private java.lang.String _description;

    /**
     * Field _credentials
     */
    private org.astrogrid.community.beans.v1.Credentials _credentials;

    /**
     * Details of the execution of this workflow
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
        
        if (obj instanceof Workflow) {
        
            Workflow temp = (Workflow)obj;
            if (this._name != null) {
                if (temp._name == null) return false;
                else if (!(this._name.equals(temp._name))) 
                    return false;
            }
            else if (temp._name != null)
                return false;
            if (this._id != null) {
                if (temp._id == null) return false;
                else if (!(this._id.equals(temp._id))) 
                    return false;
            }
            else if (temp._id != null)
                return false;
            if (this._sequence != null) {
                if (temp._sequence == null) return false;
                else if (!(this._sequence.equals(temp._sequence))) 
                    return false;
            }
            else if (temp._sequence != null)
                return false;
            if (this._description != null) {
                if (temp._description == null) return false;
                else if (!(this._description.equals(temp._description))) 
                    return false;
            }
            else if (temp._description != null)
                return false;
            if (this._credentials != null) {
                if (temp._credentials == null) return false;
                else if (!(this._credentials.equals(temp._credentials))) 
                    return false;
            }
            else if (temp._credentials != null)
                return false;
            if (this._jobExecutionRecord != null) {
                if (temp._jobExecutionRecord == null) return false;
                else if (!(this._jobExecutionRecord.equals(temp._jobExecutionRecord))) 
                    return false;
            }
            else if (temp._jobExecutionRecord != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

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
     * Returns the value of field 'description'. The field
     * 'description' has the following description: An optional
     * textual description
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'id'. The field 'id' has the
     * following description: used within the job execution system.
     * 
     * @return the value of field 'id'.
     */
    public java.lang.String getId()
    {
        return this._id;
    } //-- java.lang.String getId() 

    /**
     * Returns the value of field 'jobExecutionRecord'. The field
     * 'jobExecutionRecord' has the following description: Details
     * of the execution of this workflow
     * 
     * @return the value of field 'jobExecutionRecord'.
     */
    public org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord getJobExecutionRecord()
    {
        return this._jobExecutionRecord;
    } //-- org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord getJobExecutionRecord() 

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: descriptive name for this workflow
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'sequence'. The field 'sequence'
     * has the following description: sequence of activities to
     * perform
     * 
     * @return the value of field 'sequence'.
     */
    public org.astrogrid.workflow.beans.v1.Sequence getSequence()
    {
        return this._sequence;
    } //-- org.astrogrid.workflow.beans.v1.Sequence getSequence() 

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
     * Sets the value of field 'credentials'.
     * 
     * @param credentials the value of field 'credentials'.
     */
    public void setCredentials(org.astrogrid.community.beans.v1.Credentials credentials)
    {
        this._credentials = credentials;
    } //-- void setCredentials(org.astrogrid.community.beans.v1.Credentials) 

    /**
     * Sets the value of field 'description'. The field
     * 'description' has the following description: An optional
     * textual description
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'id'. The field 'id' has the
     * following description: used within the job execution system.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(java.lang.String id)
    {
        this._id = id;
    } //-- void setId(java.lang.String) 

    /**
     * Sets the value of field 'jobExecutionRecord'. The field
     * 'jobExecutionRecord' has the following description: Details
     * of the execution of this workflow
     * 
     * @param jobExecutionRecord the value of field
     * 'jobExecutionRecord'.
     */
    public void setJobExecutionRecord(org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord jobExecutionRecord)
    {
        this._jobExecutionRecord = jobExecutionRecord;
    } //-- void setJobExecutionRecord(org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord) 

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: descriptive name for this workflow
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'sequence'. The field 'sequence' has
     * the following description: sequence of activities to perform
     * 
     * @param sequence the value of field 'sequence'.
     */
    public void setSequence(org.astrogrid.workflow.beans.v1.Sequence sequence)
    {
        this._sequence = sequence;
    } //-- void setSequence(org.astrogrid.workflow.beans.v1.Sequence) 

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
