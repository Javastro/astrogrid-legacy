/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: MessageType.java,v 1.9 2004/03/09 14:19:25 nw Exp $
 */

package org.astrogrid.applications.beans.v1.cea.castor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Date;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * A log entry recorded during execution
 * 
 * @version $Revision: 1.9 $ $Date: 2004/03/09 14:19:25 $
 */
public class MessageType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _content
     */
    private java.lang.String _content;

    /**
     * Field _source
     */
    private java.lang.String _source;

    /**
     * Field _timestamp
     */
    private java.util.Date _timestamp;

    /**
     * Field _level
     */
    private org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel _level;

    /**
     * Field _phase
     */
    private org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase _phase;


      //----------------/
     //- Constructors -/
    //----------------/

    public MessageType() {
        super();
    } //-- org.astrogrid.applications.beans.v1.cea.castor.MessageType()


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
        
        if (obj instanceof MessageType) {
        
            MessageType temp = (MessageType)obj;
            if (this._content != null) {
                if (temp._content == null) return false;
                else if (!(this._content.equals(temp._content))) 
                    return false;
            }
            else if (temp._content != null)
                return false;
            if (this._source != null) {
                if (temp._source == null) return false;
                else if (!(this._source.equals(temp._source))) 
                    return false;
            }
            else if (temp._source != null)
                return false;
            if (this._timestamp != null) {
                if (temp._timestamp == null) return false;
                else if (!(this._timestamp.equals(temp._timestamp))) 
                    return false;
            }
            else if (temp._timestamp != null)
                return false;
            if (this._level != null) {
                if (temp._level == null) return false;
                else if (!(this._level.equals(temp._level))) 
                    return false;
            }
            else if (temp._level != null)
                return false;
            if (this._phase != null) {
                if (temp._phase == null) return false;
                else if (!(this._phase.equals(temp._phase))) 
                    return false;
            }
            else if (temp._phase != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'content'.
     * 
     * @return the value of field 'content'.
     */
    public java.lang.String getContent()
    {
        return this._content;
    } //-- java.lang.String getContent() 

    /**
     * Returns the value of field 'level'.
     * 
     * @return the value of field 'level'.
     */
    public org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel getLevel()
    {
        return this._level;
    } //-- org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel getLevel() 

    /**
     * Returns the value of field 'phase'.
     * 
     * @return the value of field 'phase'.
     */
    public org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase getPhase()
    {
        return this._phase;
    } //-- org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase getPhase() 

    /**
     * Returns the value of field 'source'.
     * 
     * @return the value of field 'source'.
     */
    public java.lang.String getSource()
    {
        return this._source;
    } //-- java.lang.String getSource() 

    /**
     * Returns the value of field 'timestamp'.
     * 
     * @return the value of field 'timestamp'.
     */
    public java.util.Date getTimestamp()
    {
        return this._timestamp;
    } //-- java.util.Date getTimestamp() 

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
     * Sets the value of field 'content'.
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(java.lang.String content)
    {
        this._content = content;
    } //-- void setContent(java.lang.String) 

    /**
     * Sets the value of field 'level'.
     * 
     * @param level the value of field 'level'.
     */
    public void setLevel(org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel level)
    {
        this._level = level;
    } //-- void setLevel(org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel) 

    /**
     * Sets the value of field 'phase'.
     * 
     * @param phase the value of field 'phase'.
     */
    public void setPhase(org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase phase)
    {
        this._phase = phase;
    } //-- void setPhase(org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase) 

    /**
     * Sets the value of field 'source'.
     * 
     * @param source the value of field 'source'.
     */
    public void setSource(java.lang.String source)
    {
        this._source = source;
    } //-- void setSource(java.lang.String) 

    /**
     * Sets the value of field 'timestamp'.
     * 
     * @param timestamp the value of field 'timestamp'.
     */
    public void setTimestamp(java.util.Date timestamp)
    {
        this._timestamp = timestamp;
    } //-- void setTimestamp(java.util.Date) 

    /**
     * Method unmarshalMessageType
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.cea.castor.MessageType unmarshalMessageType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.cea.castor.MessageType) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.cea.castor.MessageType.class, reader);
    } //-- org.astrogrid.applications.beans.v1.cea.castor.MessageType unmarshalMessageType(java.io.Reader) 

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
