/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Vel2VectorTypeDescriptor.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;

/**
 * Class Vel2VectorTypeDescriptor.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class Vel2VectorTypeDescriptor extends org.astrogrid.datacenter.adql.generated.ogsadai.VelCoordTypeDescriptor {


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

    public Vel2VectorTypeDescriptor() {
        super();
        setExtendsWithoutFlatten(new org.astrogrid.datacenter.adql.generated.ogsadai.VelCoordTypeDescriptor());
        nsURI = "urn:nvo-coords";
        xmlName = "vel2VectorType";
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.xml.XMLFieldHandler              handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- _pos_ang_unit
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.datacenter.adql.generated.ogsadai.types.AngleUnitType.class, "_pos_ang_unit", "pos_ang_unit", org.exolab.castor.xml.NodeType.Attribute);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                Vel2VectorType target = (Vel2VectorType) object;
                return target.getPos_ang_unit();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Vel2VectorType target = (Vel2VectorType) object;
                    target.setPos_ang_unit( (org.astrogrid.datacenter.adql.generated.ogsadai.types.AngleUnitType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return null;
            }
        } );
        desc.setHandler( new org.exolab.castor.xml.handlers.EnumFieldHandler(org.astrogrid.datacenter.adql.generated.ogsadai.types.AngleUnitType.class, handler));
        desc.setImmutable(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _pos_ang_unit
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- initialize element descriptors
        
        //-- _coordValue
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.datacenter.adql.generated.ogsadai.Coord2ValueType.class, "_coordValue", "CoordValue", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                Vel2VectorType target = (Vel2VectorType) object;
                return target.getCoordValue();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Vel2VectorType target = (Vel2VectorType) object;
                    target.setCoordValue( (org.astrogrid.datacenter.adql.generated.ogsadai.Coord2ValueType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.datacenter.adql.generated.ogsadai.Coord2ValueType();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("urn:nvo-coords");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _coordValue
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _coordError
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.datacenter.adql.generated.ogsadai.Coord2SizeType.class, "_coordError", "CoordError", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                Vel2VectorType target = (Vel2VectorType) object;
                return target.getCoordError();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Vel2VectorType target = (Vel2VectorType) object;
                    target.setCoordError( (org.astrogrid.datacenter.adql.generated.ogsadai.Coord2SizeType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.datacenter.adql.generated.ogsadai.Coord2SizeType();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("urn:nvo-coords");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _coordError
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _coordResolution
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.datacenter.adql.generated.ogsadai.Coord2SizeType.class, "_coordResolution", "CoordResolution", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                Vel2VectorType target = (Vel2VectorType) object;
                return target.getCoordResolution();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Vel2VectorType target = (Vel2VectorType) object;
                    target.setCoordResolution( (org.astrogrid.datacenter.adql.generated.ogsadai.Coord2SizeType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.datacenter.adql.generated.ogsadai.Coord2SizeType();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("urn:nvo-coords");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _coordResolution
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _coordSize
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.datacenter.adql.generated.ogsadai.Coord2SizeType.class, "_coordSize", "CoordSize", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                Vel2VectorType target = (Vel2VectorType) object;
                return target.getCoordSize();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Vel2VectorType target = (Vel2VectorType) object;
                    target.setCoordSize( (org.astrogrid.datacenter.adql.generated.ogsadai.Coord2SizeType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.datacenter.adql.generated.ogsadai.Coord2SizeType();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("urn:nvo-coords");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _coordSize
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _coordPixsize
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.datacenter.adql.generated.ogsadai.Coord2SizeType.class, "_coordPixsize", "CoordPixsize", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                Vel2VectorType target = (Vel2VectorType) object;
                return target.getCoordPixsize();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Vel2VectorType target = (Vel2VectorType) object;
                    target.setCoordPixsize( (org.astrogrid.datacenter.adql.generated.ogsadai.Coord2SizeType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.datacenter.adql.generated.ogsadai.Coord2SizeType();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("urn:nvo-coords");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _coordPixsize
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Vel2VectorTypeDescriptor()


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
        return org.astrogrid.datacenter.adql.generated.ogsadai.Vel2VectorType.class;
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
