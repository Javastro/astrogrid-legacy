/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SpatialTypeDescriptor.java,v 1.3 2004/03/05 09:51:59 KevinBenson Exp $
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
 * Class SpatialTypeDescriptor.
 * 
 * @version $Revision: 1.3 $ $Date: 2004/03/05 09:51:59 $
 */
public class SpatialTypeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


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

    public SpatialTypeDescriptor() {
        super();
        nsURI = "http://www.ivoa.net/xml/VODataService/v0.4";
        xmlName = "SpatialType";
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.xml.XMLFieldHandler              handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _regionList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.registry.beans.resource.dataservice.RegionType.class, "_regionList", "Region", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                SpatialType target = (SpatialType) object;
                return target.getRegion();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    SpatialType target = (SpatialType) object;
                    target.addRegion( (org.astrogrid.registry.beans.resource.dataservice.RegionType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.registry.beans.resource.dataservice.RegionType();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.ivoa.net/xml/VODataService/v0.4");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _regionList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _spatialResolution
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(float.class, "_spatialResolution", "SpatialResolution", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                SpatialType target = (SpatialType) object;
                if(!target.hasSpatialResolution())
                    return null;
                return new Float(target.getSpatialResolution());
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    SpatialType target = (SpatialType) object;
                    // if null, use delete method for optional primitives 
                    if (value == null) {
                        target.deleteSpatialResolution();
                        return;
                    }
                    target.setSpatialResolution( ((Float)value).floatValue());
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return null;
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.ivoa.net/xml/VODataService/v0.4");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _spatialResolution
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            FloatValidator typeValidator = new FloatValidator();
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _regionOfRegard
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(float.class, "_regionOfRegard", "RegionOfRegard", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                SpatialType target = (SpatialType) object;
                if(!target.hasRegionOfRegard())
                    return null;
                return new Float(target.getRegionOfRegard());
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    SpatialType target = (SpatialType) object;
                    // if null, use delete method for optional primitives 
                    if (value == null) {
                        target.deleteRegionOfRegard();
                        return;
                    }
                    target.setRegionOfRegard( ((Float)value).floatValue());
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return null;
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.ivoa.net/xml/VODataService/v0.4");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _regionOfRegard
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            FloatValidator typeValidator = new FloatValidator();
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
    } //-- org.astrogrid.registry.beans.resource.dataservice.SpatialTypeDescriptor()


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
        return org.astrogrid.registry.beans.resource.dataservice.SpatialType.class;
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
