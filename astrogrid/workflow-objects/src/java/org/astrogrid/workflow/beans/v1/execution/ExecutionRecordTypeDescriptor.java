/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ExecutionRecordTypeDescriptor.java,v 1.8 2004/03/10 17:13:59 pah Exp $
 */

package org.astrogrid.workflow.beans.v1.execution;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;

/**
 * Class ExecutionRecordTypeDescriptor.
 * 
 * @version $Revision: 1.8 $ $Date: 2004/03/10 17:13:59 $
 */
public class ExecutionRecordTypeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field nsPrefix
     */
    private java.lang.String nsPrefix;

    /**
     * Field nsURI
     */
    private java.lang.String nsURI;

    /**
     * Field xmlName
     */
    private java.lang.String xmlName;

    /**
     * Field identity
     */
    private org.exolab.castor.xml.XMLFieldDescriptor identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public ExecutionRecordTypeDescriptor() {
        super();
        nsURI = "http://www.astrogrid.org/schema/ExecutionRecord/v1";
        xmlName = "execution-record-type";
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.xml.XMLFieldHandler              handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- _startTime
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.util.Date.class, "_startTime", "startTime", org.exolab.castor.xml.NodeType.Attribute);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ExecutionRecordType target = (ExecutionRecordType) object;
                return target.getStartTime();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ExecutionRecordType target = (ExecutionRecordType) object;
                    target.setStartTime( (java.util.Date) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new java.util.Date();
            }
        } );
        desc.setHandler( new org.exolab.castor.xml.handlers.DateFieldHandler(handler));
        desc.setImmutable(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _startTime
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _finishTime
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.util.Date.class, "_finishTime", "finishTime", org.exolab.castor.xml.NodeType.Attribute);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ExecutionRecordType target = (ExecutionRecordType) object;
                return target.getFinishTime();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ExecutionRecordType target = (ExecutionRecordType) object;
                    target.setFinishTime( (java.util.Date) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new java.util.Date();
            }
        } );
        desc.setHandler( new org.exolab.castor.xml.handlers.DateFieldHandler(handler));
        desc.setImmutable(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _finishTime
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _status
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase.class, "_status", "status", org.exolab.castor.xml.NodeType.Attribute);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ExecutionRecordType target = (ExecutionRecordType) object;
                return target.getStatus();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ExecutionRecordType target = (ExecutionRecordType) object;
                    target.setStatus( (org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return null;
            }
        } );
        desc.setHandler( new org.exolab.castor.xml.handlers.EnumFieldHandler(org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase.class, handler));
        desc.setImmutable(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _status
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- initialize element descriptors
        
        //-- _messageList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.applications.beans.v1.cea.castor.MessageType.class, "_messageList", "message", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ExecutionRecordType target = (ExecutionRecordType) object;
                return target.getMessage();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ExecutionRecordType target = (ExecutionRecordType) object;
                    target.addMessage( (org.astrogrid.applications.beans.v1.cea.castor.MessageType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.applications.beans.v1.cea.castor.MessageType();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.astrogrid.org/schema/CEATypes/v1");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _messageList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    } //-- org.astrogrid.workflow.beans.v1.execution.ExecutionRecordTypeDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAccessMode
     */
    public org.exolab.castor.mapping.AccessMode getAccessMode()
    {
        return null;
    } //-- org.exolab.castor.mapping.AccessMode getAccessMode() 

    /**
     * Method getExtends
     */
    public org.exolab.castor.mapping.ClassDescriptor getExtends()
    {
        return null;
    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends() 

    /**
     * Method getIdentity
     */
    public org.exolab.castor.mapping.FieldDescriptor getIdentity()
    {
        return identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
     * Method getJavaClass
     */
    public java.lang.Class getJavaClass()
    {
        return org.astrogrid.workflow.beans.v1.execution.ExecutionRecordType.class;
    } //-- java.lang.Class getJavaClass() 

    /**
     * Method getNameSpacePrefix
     */
    public java.lang.String getNameSpacePrefix()
    {
        return nsPrefix;
    } //-- java.lang.String getNameSpacePrefix() 

    /**
     * Method getNameSpaceURI
     */
    public java.lang.String getNameSpaceURI()
    {
        return nsURI;
    } //-- java.lang.String getNameSpaceURI() 

    /**
     * Method getValidator
     */
    public org.exolab.castor.xml.TypeValidator getValidator()
    {
        return this;
    } //-- org.exolab.castor.xml.TypeValidator getValidator() 

    /**
     * Method getXMLName
     */
    public java.lang.String getXMLName()
    {
        return xmlName;
    } //-- java.lang.String getXMLName() 

}
