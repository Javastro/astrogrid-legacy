/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: MessageType.java,v 1.1 2004/03/02 16:50:20 nw Exp $
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
 * @version $Revision: 1.1 $ $Date: 2004/03/02 16:50:20 $
 */
public class MessageType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private java.lang.String _content = "";

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
        setContent("");
    } //-- org.astrogrid.applications.beans.v1.cea.castor.MessageType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
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
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
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
