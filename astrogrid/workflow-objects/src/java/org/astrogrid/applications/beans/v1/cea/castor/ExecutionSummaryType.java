/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ExecutionSummaryType.java,v 1.13 2007/01/04 16:26:32 clq2 Exp $
 */

package org.astrogrid.applications.beans.v1.cea.castor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class ExecutionSummaryType.
 * 
 * @version $Revision: 1.13 $ $Date: 2007/01/04 16:26:32 $
 */
public class ExecutionSummaryType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _applicationName
     */
    private java.lang.String _applicationName;

    /**
     * Field _executionId
     */
    private java.lang.String _executionId;

    /**
     * Field _inputList
     */
    private org.astrogrid.applications.beans.v1.cea.castor.InputListType _inputList;

    /**
     * Field _resultList
     */
    private org.astrogrid.applications.beans.v1.cea.castor.ResultListType _resultList;

    /**
     * Field _status
     */
    private org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase _status;


      //----------------/
     //- Constructors -/
    //----------------/

    public ExecutionSummaryType() {
        super();
    } //-- org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType()


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
        
        if (obj instanceof ExecutionSummaryType) {
        
            ExecutionSummaryType temp = (ExecutionSummaryType)obj;
            if (this._applicationName != null) {
                if (temp._applicationName == null) return false;
                else if (!(this._applicationName.equals(temp._applicationName))) 
                    return false;
            }
            else if (temp._applicationName != null)
                return false;
            if (this._executionId != null) {
                if (temp._executionId == null) return false;
                else if (!(this._executionId.equals(temp._executionId))) 
                    return false;
            }
            else if (temp._executionId != null)
                return false;
            if (this._inputList != null) {
                if (temp._inputList == null) return false;
                else if (!(this._inputList.equals(temp._inputList))) 
                    return false;
            }
            else if (temp._inputList != null)
                return false;
            if (this._resultList != null) {
                if (temp._resultList == null) return false;
                else if (!(this._resultList.equals(temp._resultList))) 
                    return false;
            }
            else if (temp._resultList != null)
                return false;
            if (this._status != null) {
                if (temp._status == null) return false;
                else if (!(this._status.equals(temp._status))) 
                    return false;
            }
            else if (temp._status != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'applicationName'.
     * 
     * @return the value of field 'applicationName'.
     */
    public java.lang.String getApplicationName()
    {
        return this._applicationName;
    } //-- java.lang.String getApplicationName() 

    /**
     * Returns the value of field 'executionId'.
     * 
     * @return the value of field 'executionId'.
     */
    public java.lang.String getExecutionId()
    {
        return this._executionId;
    } //-- java.lang.String getExecutionId() 

    /**
     * Returns the value of field 'inputList'.
     * 
     * @return the value of field 'inputList'.
     */
    public org.astrogrid.applications.beans.v1.cea.castor.InputListType getInputList()
    {
        return this._inputList;
    } //-- org.astrogrid.applications.beans.v1.cea.castor.InputListType getInputList() 

    /**
     * Returns the value of field 'resultList'.
     * 
     * @return the value of field 'resultList'.
     */
    public org.astrogrid.applications.beans.v1.cea.castor.ResultListType getResultList()
    {
        return this._resultList;
    } //-- org.astrogrid.applications.beans.v1.cea.castor.ResultListType getResultList() 

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
     * Sets the value of field 'applicationName'.
     * 
     * @param applicationName the value of field 'applicationName'.
     */
    public void setApplicationName(java.lang.String applicationName)
    {
        this._applicationName = applicationName;
    } //-- void setApplicationName(java.lang.String) 

    /**
     * Sets the value of field 'executionId'.
     * 
     * @param executionId the value of field 'executionId'.
     */
    public void setExecutionId(java.lang.String executionId)
    {
        this._executionId = executionId;
    } //-- void setExecutionId(java.lang.String) 

    /**
     * Sets the value of field 'inputList'.
     * 
     * @param inputList the value of field 'inputList'.
     */
    public void setInputList(org.astrogrid.applications.beans.v1.cea.castor.InputListType inputList)
    {
        this._inputList = inputList;
    } //-- void setInputList(org.astrogrid.applications.beans.v1.cea.castor.InputListType) 

    /**
     * Sets the value of field 'resultList'.
     * 
     * @param resultList the value of field 'resultList'.
     */
    public void setResultList(org.astrogrid.applications.beans.v1.cea.castor.ResultListType resultList)
    {
        this._resultList = resultList;
    } //-- void setResultList(org.astrogrid.applications.beans.v1.cea.castor.ResultListType) 

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
     * Method unmarshalExecutionSummaryType
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType unmarshalExecutionSummaryType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType.class, reader);
    } //-- org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType unmarshalExecutionSummaryType(java.io.Reader) 

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
