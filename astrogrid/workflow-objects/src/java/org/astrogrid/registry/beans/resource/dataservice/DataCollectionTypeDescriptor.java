/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: DataCollectionTypeDescriptor.java,v 1.14 2007/01/04 16:26:07 clq2 Exp $
 */

package org.astrogrid.registry.beans.resource.dataservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;

/**
 * Class DataCollectionTypeDescriptor.
 * 
 * @version $Revision: 1.14 $ $Date: 2007/01/04 16:26:07 $
 */
public class DataCollectionTypeDescriptor extends org.astrogrid.registry.beans.resource.ResourceTypeDescriptor {


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

    public DataCollectionTypeDescriptor() {
        super();
        setExtendsWithoutFlatten(new org.astrogrid.registry.beans.resource.ResourceTypeDescriptor());
        nsURI = "http://www.ivoa.net/xml/VODataService/v0.4";
        xmlName = "DataCollectionType";
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.xml.XMLFieldHandler              handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _facilityList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.registry.beans.resource.ResourceReferenceType.class, "_facilityList", "Facility", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                DataCollectionType target = (DataCollectionType) object;
                return target.getFacility();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    DataCollectionType target = (DataCollectionType) object;
                    target.addFacility( (org.astrogrid.registry.beans.resource.ResourceReferenceType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.registry.beans.resource.ResourceReferenceType();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.ivoa.net/xml/VOResource/v0.9");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _facilityList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _instrumentList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.registry.beans.resource.ResourceReferenceType.class, "_instrumentList", "Instrument", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                DataCollectionType target = (DataCollectionType) object;
                return target.getInstrument();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    DataCollectionType target = (DataCollectionType) object;
                    target.addInstrument( (org.astrogrid.registry.beans.resource.ResourceReferenceType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.registry.beans.resource.ResourceReferenceType();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.ivoa.net/xml/VOResource/v0.9");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _instrumentList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _coverage
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.registry.beans.resource.dataservice.CoverageType.class, "_coverage", "Coverage", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                DataCollectionType target = (DataCollectionType) object;
                return target.getCoverage();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    DataCollectionType target = (DataCollectionType) object;
                    target.setCoverage( (org.astrogrid.registry.beans.resource.dataservice.CoverageType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.registry.beans.resource.dataservice.CoverageType();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.ivoa.net/xml/VODataService/v0.4");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _coverage
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _access
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.registry.beans.resource.dataservice.AccessType.class, "_access", "Access", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                DataCollectionType target = (DataCollectionType) object;
                return target.getAccess();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    DataCollectionType target = (DataCollectionType) object;
                    target.setAccess( (org.astrogrid.registry.beans.resource.dataservice.AccessType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.registry.beans.resource.dataservice.AccessType();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.ivoa.net/xml/VODataService/v0.4");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _access
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    } //-- org.astrogrid.registry.beans.resource.dataservice.DataCollectionTypeDescriptor()


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
        return super.getExtends();
    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends() 

    /**
     * Method getIdentity
     */
    public org.exolab.castor.mapping.FieldDescriptor getIdentity()
    {
        if (identity == null)
            return super.getIdentity();
        return identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
     * Method getJavaClass
     */
    public java.lang.Class getJavaClass()
    {
        return org.astrogrid.registry.beans.resource.dataservice.DataCollectionType.class;
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
