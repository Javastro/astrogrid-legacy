/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ExprSelectionItemDescriptor.java,v 1.2 2004/03/26 16:03:34 eca Exp $
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
 * Class ExprSelectionItemDescriptor.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:34 $
 */
public class ExprSelectionItemDescriptor extends org.astrogrid.datacenter.adql.generated.ogsadai.SelectionItemDescriptor {


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

    public ExprSelectionItemDescriptor() {
        super();
        setExtendsWithoutFlatten(new org.astrogrid.datacenter.adql.generated.ogsadai.SelectionItemDescriptor());
        nsURI = "http://tempuri.org/adql";
        xmlName = "ExprSelectionItem";
        
        //-- set grouping compositor
        setCompositorAsChoice();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.xml.XMLFieldHandler              handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _unaryExpr
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.datacenter.adql.generated.ogsadai.UnaryExpr.class, "_unaryExpr", "UnaryExpr", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ExprSelectionItem target = (ExprSelectionItem) object;
                return target.getUnaryExpr();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ExprSelectionItem target = (ExprSelectionItem) object;
                    target.setUnaryExpr( (org.astrogrid.datacenter.adql.generated.ogsadai.UnaryExpr) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.datacenter.adql.generated.ogsadai.UnaryExpr();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://tempuri.org/adql");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _unaryExpr
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _binaryExpr
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.datacenter.adql.generated.ogsadai.BinaryExpr.class, "_binaryExpr", "BinaryExpr", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ExprSelectionItem target = (ExprSelectionItem) object;
                return target.getBinaryExpr();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ExprSelectionItem target = (ExprSelectionItem) object;
                    target.setBinaryExpr( (org.astrogrid.datacenter.adql.generated.ogsadai.BinaryExpr) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.datacenter.adql.generated.ogsadai.BinaryExpr();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://tempuri.org/adql");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _binaryExpr
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _closedExpr
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.datacenter.adql.generated.ogsadai.ClosedExpr.class, "_closedExpr", "ClosedExpr", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ExprSelectionItem target = (ExprSelectionItem) object;
                return target.getClosedExpr();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ExprSelectionItem target = (ExprSelectionItem) object;
                    target.setClosedExpr( (org.astrogrid.datacenter.adql.generated.ogsadai.ClosedExpr) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.datacenter.adql.generated.ogsadai.ClosedExpr();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://tempuri.org/adql");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _closedExpr
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _columnExpr
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.datacenter.adql.generated.ogsadai.ColumnExpr.class, "_columnExpr", "ColumnExpr", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ExprSelectionItem target = (ExprSelectionItem) object;
                return target.getColumnExpr();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ExprSelectionItem target = (ExprSelectionItem) object;
                    target.setColumnExpr( (org.astrogrid.datacenter.adql.generated.ogsadai.ColumnExpr) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.datacenter.adql.generated.ogsadai.ColumnExpr();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://tempuri.org/adql");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _columnExpr
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _functionExpr
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.datacenter.adql.generated.ogsadai.FunctionExpr.class, "_functionExpr", "FunctionExpr", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ExprSelectionItem target = (ExprSelectionItem) object;
                return target.getFunctionExpr();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ExprSelectionItem target = (ExprSelectionItem) object;
                    target.setFunctionExpr( (org.astrogrid.datacenter.adql.generated.ogsadai.FunctionExpr) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.datacenter.adql.generated.ogsadai.FunctionExpr();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://tempuri.org/adql");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _functionExpr
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _atomExpr
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.datacenter.adql.generated.ogsadai.AtomExpr.class, "_atomExpr", "AtomExpr", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ExprSelectionItem target = (ExprSelectionItem) object;
                return target.getAtomExpr();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ExprSelectionItem target = (ExprSelectionItem) object;
                    target.setAtomExpr( (org.astrogrid.datacenter.adql.generated.ogsadai.AtomExpr) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.datacenter.adql.generated.ogsadai.AtomExpr();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://tempuri.org/adql");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _atomExpr
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ExprSelectionItemDescriptor()


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
        return org.astrogrid.datacenter.adql.generated.ogsadai.ExprSelectionItem.class;
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
