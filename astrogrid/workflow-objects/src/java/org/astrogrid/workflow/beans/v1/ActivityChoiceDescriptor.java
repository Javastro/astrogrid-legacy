/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ActivityChoiceDescriptor.java,v 1.2 2004/03/02 14:09:49 pah Exp $
 */

package org.astrogrid.workflow.beans.v1;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;

/**
 * Class ActivityChoiceDescriptor.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/02 14:09:49 $
 */
public class ActivityChoiceDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


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

    public ActivityChoiceDescriptor() {
        super();
        nsURI = "http://www.astrogrid.org/schema/AGWorkflow/v1";
        xmlName = "activityChoice";
        
        //-- set grouping compositor
        setCompositorAsChoice();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.xml.XMLFieldHandler              handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _sequence
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.workflow.beans.v1.Sequence.class, "_sequence", "sequence", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ActivityChoice target = (ActivityChoice) object;
                return target.getSequence();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ActivityChoice target = (ActivityChoice) object;
                    target.setSequence( (org.astrogrid.workflow.beans.v1.Sequence) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.workflow.beans.v1.Sequence();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.astrogrid.org/schema/AGWorkflow/v1");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _sequence
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _flow
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.workflow.beans.v1.Flow.class, "_flow", "flow", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ActivityChoice target = (ActivityChoice) object;
                return target.getFlow();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ActivityChoice target = (ActivityChoice) object;
                    target.setFlow( (org.astrogrid.workflow.beans.v1.Flow) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.workflow.beans.v1.Flow();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.astrogrid.org/schema/AGWorkflow/v1");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _flow
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _step
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.workflow.beans.v1.Step.class, "_step", "step", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ActivityChoice target = (ActivityChoice) object;
                return target.getStep();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ActivityChoice target = (ActivityChoice) object;
                    target.setStep( (org.astrogrid.workflow.beans.v1.Step) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.workflow.beans.v1.Step();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.astrogrid.org/schema/AGWorkflow/v1");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _step
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    } //-- org.astrogrid.workflow.beans.v1.ActivityChoiceDescriptor()


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
        return org.astrogrid.workflow.beans.v1.ActivityChoice.class;
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
