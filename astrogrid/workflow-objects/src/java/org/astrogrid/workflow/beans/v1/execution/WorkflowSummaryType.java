/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: WorkflowSummaryType.java,v 1.3 2005/01/23 12:52:25 jdt Exp $
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
 * summary record for a single execution of a job
 * 
 * @version $Revision: 1.3 $ $Date: 2005/01/23 12:52:25 $
 */
public class WorkflowSummaryType extends org.astrogrid.workflow.beans.v1.execution.JobExecutionRecordType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _workflowName
     */
    private java.lang.String _workflowName;

    /**
     * Field _description
     */
    private java.lang.String _description;


      //----------------/
     //- Constructors -/
    //----------------/

    public WorkflowSummaryType() {
        super();
    } //-- org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType()


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
        
        if (obj instanceof WorkflowSummaryType) {
        
            WorkflowSummaryType temp = (WorkflowSummaryType)obj;
            if (this._workflowName != null) {
                if (temp._workflowName == null) return false;
                else if (!(this._workflowName.equals(temp._workflowName))) 
                    return false;
            }
            else if (temp._workflowName != null)
                return false;
            if (this._description != null) {
                if (temp._description == null) return false;
                else if (!(this._description.equals(temp._description))) 
                    return false;
            }
            else if (temp._description != null)
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
     * Returns the value of field 'workflowName'.
     * 
     * @return the value of field 'workflowName'.
     */
    public java.lang.String getWorkflowName()
    {
        return this._workflowName;
    } //-- java.lang.String getWorkflowName() 

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
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'workflowName'.
     * 
     * @param workflowName the value of field 'workflowName'.
     */
    public void setWorkflowName(java.lang.String workflowName)
    {
        this._workflowName = workflowName;
    } //-- void setWorkflowName(java.lang.String) 

    /**
     * Method unmarshalWorkflowSummaryType
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType unmarshalWorkflowSummaryType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType unmarshalWorkflowSummaryType(java.io.Reader) 

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
