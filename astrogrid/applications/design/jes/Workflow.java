/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Workflow.java,v 1.1 2004/08/10 21:09:29 pah Exp $
 */

package jes;

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
import org.astrogrid.workflow.beans.v1.Sequence;

/**
 * Representation of a workflow document, conforming to the schema
 *  defined for namespace
 * http://www.astrogrid.org/schema/AGWorkflow/v1
 *  
 * 
 * @version $Revision: 1.1 $ $Date: 2004/08/10 21:09:29 $
 */
public class Workflow extends org.astrogrid.common.bean.BaseBean 
implements Serializable
{
      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

//-- org.astrogrid.workflow.beans.v1.Workflow()


      //-----------/
     //- Methods -/
    //-----------/

//-- boolean equals(java.lang.Object) 


    /**
     * Returns the value of field 'credentials'.
     * 
     * @return the value of field 'credentials'.
     */
    public jes.Credentials getCredentials()
    {
        return this._credentials;
    } //-- org.astrogrid.community.beans.v1.Credentials getCredentials() 

    /**
     * Returns the value of field 'defaultScriptingLanguage'.
     * 
     * @return the value of field 'defaultScriptingLanguage'.
     */
    public String getDefaultScriptingLanguage()
    {
        return this._defaultScriptingLanguage;
    } //-- java.lang.String getDefaultScriptingLanguage() 

    /**
     * Returns the value of field 'description'.
     * 
     * @return the value of field 'description'.
     */
    public String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'id'.
     * 
     * @return the value of field 'id'.
     */
    public String getId()
    {
        return this._id;
    } //-- java.lang.String getId() 

    /**
     * Returns the value of field 'jobExecutionRecord'. The field
     * 'jobExecutionRecord' has the following description: A record
     * of a single execution of a job
     * 
     * @return the value of field 'jobExecutionRecord'.
     */
    public jes.JobExecutionRecord getJobExecutionRecord()
    {
        return this._jobExecutionRecord;
    } //-- org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord getJobExecutionRecord() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'sequence'. The field 'sequence'
     * has the following description: a collection of activities to
     * be performed sequentially
     * 
     * @return the value of field 'sequence'.
     */
    public jes.Sequence getSequence()
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
    }

 //-- boolean isValid() 


//-- void marshal(java.io.Writer) 

//-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'credentials'.
     * 
     * @param credentials the value of field 'credentials'.
     */
    public void setCredentials(jes.Credentials credentials)
    {
        this._credentials = credentials;
    } //-- void setCredentials(org.astrogrid.community.beans.v1.Credentials) 

    /**
     * Sets the value of field 'defaultScriptingLanguage'.
     * 
     * @param defaultScriptingLanguage the value of field
     * 'defaultScriptingLanguage'.
     */
    public void setDefaultScriptingLanguage(String defaultScriptingLanguage)
    {
        this._defaultScriptingLanguage = defaultScriptingLanguage;
    } //-- void setDefaultScriptingLanguage(java.lang.String) 

    /**
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(String id)
    {
        this._id = id;
    } //-- void setId(java.lang.String) 

    /**
     * Sets the value of field 'jobExecutionRecord'. The field
     * 'jobExecutionRecord' has the following description: A record
     * of a single execution of a job
     * 
     * @param jobExecutionRecord the value of field
     * 'jobExecutionRecord'.
     */
    public void setJobExecutionRecord(jes.JobExecutionRecord jobExecutionRecord)
    {
        this._jobExecutionRecord = jobExecutionRecord;
    } //-- void setJobExecutionRecord(org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'sequence'. The field 'sequence' has
     * the following description: a collection of activities to be
     * performed sequentially
     * 
     * @param sequence the value of field 'sequence'.
     */
    public void setSequence(jes.Sequence sequence)
    {
        this._sequence = sequence;
    }

    /**
     * @supplierCardinality 1
     * @clientRole uses*/
    private jes.Credentials lnkCredentials;

    /**
     * @link aggregation
     * @supplierCardinality 0..* 
     */
    private jes.JobExecutionRecord lnkJobExecutionRecord;

    /**
     * @link aggregationByValue
     * @supplierCardinality 1 
     */
    private jes.Sequence lnkSequence;
 //-- void setSequence(org.astrogrid.workflow.beans.v1.Sequence) 


//-- org.astrogrid.workflow.beans.v1.Workflow unmarshalWorkflow(java.io.Reader) 

//-- void validate() 

}
