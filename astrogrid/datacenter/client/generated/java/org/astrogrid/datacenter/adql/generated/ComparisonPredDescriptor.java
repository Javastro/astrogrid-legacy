/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ComparisonPredDescriptor.java,v 1.11 2003/12/01 16:30:01 nw Exp $
 */

package org.astrogrid.datacenter.adql.generated;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;

/**
 * Class ComparisonPredDescriptor.
 * 
 * @version $Revision: 1.11 $ $Date: 2003/12/01 16:30:01 $
 */
public class ComparisonPredDescriptor extends org.astrogrid.datacenter.adql.generated.PredicateDescriptor {


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

    public ComparisonPredDescriptor() {
        super();
        setExtendsWithoutFlatten(new org.astrogrid.datacenter.adql.generated.PredicateDescriptor());
        nsURI = "http://tempuri.org/adql";
        xmlName = "ComparisonPred";
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.xml.XMLFieldHandler              handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _firstExpr
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.datacenter.adql.generated.ScalarExpression.class, "_firstExpr", "FirstExpr", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ComparisonPred target = (ComparisonPred) object;
                return target.getFirstExpr();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ComparisonPred target = (ComparisonPred) object;
                    target.setFirstExpr( (org.astrogrid.datacenter.adql.generated.ScalarExpression) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.datacenter.adql.generated.ScalarExpression();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://tempuri.org/adql");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _firstExpr
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _compare
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.datacenter.adql.generated.types.Comparison.class, "_compare", "Compare", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ComparisonPred target = (ComparisonPred) object;
                return target.getCompare();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ComparisonPred target = (ComparisonPred) object;
                    target.setCompare( (org.astrogrid.datacenter.adql.generated.types.Comparison) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return null;
            }
        } );
        desc.setHandler( new org.exolab.castor.xml.handlers.EnumFieldHandler(org.astrogrid.datacenter.adql.generated.types.Comparison.class, handler));
        desc.setImmutable(true);
        desc.setNameSpaceURI("http://tempuri.org/adql");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _compare
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _secondExpr
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.astrogrid.datacenter.adql.generated.ScalarExpression.class, "_secondExpr", "SecondExpr", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                ComparisonPred target = (ComparisonPred) object;
                return target.getSecondExpr();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ComparisonPred target = (ComparisonPred) object;
                    target.setSecondExpr( (org.astrogrid.datacenter.adql.generated.ScalarExpression) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.astrogrid.datacenter.adql.generated.ScalarExpression();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://tempuri.org/adql");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _secondExpr
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    } //-- org.astrogrid.datacenter.adql.generated.ComparisonPredDescriptor()


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
        return org.astrogrid.datacenter.adql.generated.ComparisonPred.class;
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
