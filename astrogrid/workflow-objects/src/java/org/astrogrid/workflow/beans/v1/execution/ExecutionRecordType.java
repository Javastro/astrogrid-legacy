/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ExecutionRecordType.java,v 1.13 2004/04/05 15:18:00 nw Exp $
 */

package org.astrogrid.workflow.beans.v1.execution;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class ExecutionRecordType.
 * 
 * @version $Revision: 1.13 $ $Date: 2004/04/05 15:18:00 $
 */
public class ExecutionRecordType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

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
    private org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase _status = org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase.valueOf("PENDING");

    /**
     * Field _messageList
     */
    private java.util.ArrayList _messageList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ExecutionRecordType() {
        super();
        setStatus(org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase.valueOf("PENDING"));
        _messageList = new ArrayList();
    } //-- org.astrogrid.workflow.beans.v1.execution.ExecutionRecordType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addMessage
     * 
     * @param vMessage
     */
    public void addMessage(org.astrogrid.applications.beans.v1.cea.castor.MessageType vMessage)
        throws java.lang.IndexOutOfBoundsException
    {
        _messageList.add(vMessage);
    } //-- void addMessage(org.astrogrid.applications.beans.v1.cea.castor.MessageType) 

    /**
     * Method addMessage
     * 
     * @param index
     * @param vMessage
     */
    public void addMessage(int index, org.astrogrid.applications.beans.v1.cea.castor.MessageType vMessage)
        throws java.lang.IndexOutOfBoundsException
    {
        _messageList.add(index, vMessage);
    } //-- void addMessage(int, org.astrogrid.applications.beans.v1.cea.castor.MessageType) 

    /**
     * Method clearMessage
     */
    public void clearMessage()
    {
        _messageList.clear();
    } //-- void clearMessage() 

    /**
     * Method enumerateMessage
     */
    public java.util.Enumeration enumerateMessage()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_messageList.iterator());
    } //-- java.util.Enumeration enumerateMessage() 

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
        
        if (obj instanceof ExecutionRecordType) {
        
            ExecutionRecordType temp = (ExecutionRecordType)obj;
            if (this._startTime != null) {
                if (temp._startTime == null) return false;
                else if (!(this._startTime.equals(temp._startTime))) 
                    return false;
            }
            else if (temp._startTime != null)
                return false;
            if (this._finishTime != null) {
                if (temp._finishTime == null) return false;
                else if (!(this._finishTime.equals(temp._finishTime))) 
                    return false;
            }
            else if (temp._finishTime != null)
                return false;
            if (this._status != null) {
                if (temp._status == null) return false;
                else if (!(this._status.equals(temp._status))) 
                    return false;
            }
            else if (temp._status != null)
                return false;
            if (this._messageList != null) {
                if (temp._messageList == null) return false;
                else if (!(this._messageList.equals(temp._messageList))) 
                    return false;
            }
            else if (temp._messageList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

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
     * Method getMessage
     * 
     * @param index
     */
    public org.astrogrid.applications.beans.v1.cea.castor.MessageType getMessage(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _messageList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.applications.beans.v1.cea.castor.MessageType) _messageList.get(index);
    } //-- org.astrogrid.applications.beans.v1.cea.castor.MessageType getMessage(int) 

    /**
     * Method getMessage
     */
    public org.astrogrid.applications.beans.v1.cea.castor.MessageType[] getMessage()
    {
        int size = _messageList.size();
        org.astrogrid.applications.beans.v1.cea.castor.MessageType[] mArray = new org.astrogrid.applications.beans.v1.cea.castor.MessageType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.applications.beans.v1.cea.castor.MessageType) _messageList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.applications.beans.v1.cea.castor.MessageType[] getMessage() 

    /**
     * Method getMessageCount
     */
    public int getMessageCount()
    {
        return _messageList.size();
    } //-- int getMessageCount() 

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
     * Method removeMessage
     * 
     * @param vMessage
     */
    public boolean removeMessage(org.astrogrid.applications.beans.v1.cea.castor.MessageType vMessage)
    {
        boolean removed = _messageList.remove(vMessage);
        return removed;
    } //-- boolean removeMessage(org.astrogrid.applications.beans.v1.cea.castor.MessageType) 

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
     * Method setMessage
     * 
     * @param index
     * @param vMessage
     */
    public void setMessage(int index, org.astrogrid.applications.beans.v1.cea.castor.MessageType vMessage)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _messageList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _messageList.set(index, vMessage);
    } //-- void setMessage(int, org.astrogrid.applications.beans.v1.cea.castor.MessageType) 

    /**
     * Method setMessage
     * 
     * @param messageArray
     */
    public void setMessage(org.astrogrid.applications.beans.v1.cea.castor.MessageType[] messageArray)
    {
        //-- copy array
        _messageList.clear();
        for (int i = 0; i < messageArray.length; i++) {
            _messageList.add(messageArray[i]);
        }
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
     * Method unmarshalExecutionRecordType
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.execution.ExecutionRecordType unmarshalExecutionRecordType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.execution.ExecutionRecordType) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.execution.ExecutionRecordType.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.execution.ExecutionRecordType unmarshalExecutionRecordType(java.io.Reader) 

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
