/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ApplicationDefinitionDescriptor.java,v 1.15 2004/08/30 17:36:48 jdt Exp $
 */

package org.astrogrid.registry.beans.cea;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;

/**
 * Class ApplicationDefinitionDescriptor.
 * 
 * @version $Revision: 1.15 $ $Date: 2004/08/30 17:36:48 $
 */
public class ApplicationDefinitionDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


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

    public ApplicationDefinitionDescriptor() {
        super();
        nsURI = "http://www.ivoa.net/xml/CEAService/v0.1";
        xmlName = "ApplicationDefinition";
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.xml.XMLFieldHandler              handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _applicationKind
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.applications.beans.v1.types.ApplicationKindType.class, "_applicationKind", "ApplicationKind", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ApplicationDefinition target = (ApplicationDefinition) object;
                return target.getApplicationKind();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ApplicationDefinition target = (ApplicationDefinition) object;
                    target.setApplicationKind( (org.astrogrid.applications.beans.v1.types.ApplicationKindType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return null;
            }
        } );
        desc.setHandler( new org.exolab.castor.xml.handlers.EnumFieldHandler(org.astrogrid.applications.beans.v1.types.ApplicationKindType.class, handler));
        desc.setImmutable(true);
        desc.setNameSpaceURI("http://www.ivoa.net/xml/CEAService/v0.1");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _applicationKind
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _parameters
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.registry.beans.cea.Parameters.class, "_parameters", "Parameters", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ApplicationDefinition target = (ApplicationDefinition) object;
                return target.getParameters();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ApplicationDefinition target = (ApplicationDefinition) object;
                    target.setParameters( (org.astrogrid.registry.beans.cea.Parameters) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.registry.beans.cea.Parameters();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.ivoa.net/xml/CEAService/v0.1");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _parameters
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _interfaces
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.applications.beans.v1.InterfacesType.class, "_interfaces", "Interfaces", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ApplicationDefinition target = (ApplicationDefinition) object;
                return target.getInterfaces();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ApplicationDefinition target = (ApplicationDefinition) object;
                    target.setInterfaces( (org.astrogrid.applications.beans.v1.InterfacesType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.applications.beans.v1.InterfacesType();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.ivoa.net/xml/CEAService/v0.1");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _interfaces
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    } //-- org.astrogrid.registry.beans.cea.ApplicationDefinitionDescriptor()


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
        return org.astrogrid.registry.beans.cea.ApplicationDefinition.class;
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
