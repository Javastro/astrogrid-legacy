/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ConvexType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class ConvexType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class ConvexType extends org.astrogrid.datacenter.adql.generated.ogsadai.ShapeType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _constraintList
     */
    private java.util.Vector _constraintList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ConvexType() {
        super();
        _constraintList = new Vector();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ConvexType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addConstraint
     * 
     * @param vConstraint
     */
    public void addConstraint(org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType vConstraint)
        throws java.lang.IndexOutOfBoundsException
    {
        _constraintList.addElement(vConstraint);
    } //-- void addConstraint(org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType) 

    /**
     * Method addConstraint
     * 
     * @param index
     * @param vConstraint
     */
    public void addConstraint(int index, org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType vConstraint)
        throws java.lang.IndexOutOfBoundsException
    {
        _constraintList.insertElementAt(vConstraint, index);
    } //-- void addConstraint(int, org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType) 

    /**
     * Method enumerateConstraint
     */
    public java.util.Enumeration enumerateConstraint()
    {
        return _constraintList.elements();
    } //-- java.util.Enumeration enumerateConstraint() 

    /**
     * Method getConstraint
     * 
     * @param index
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType getConstraint(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _constraintList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType) _constraintList.elementAt(index);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType getConstraint(int) 

    /**
     * Method getConstraint
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType[] getConstraint()
    {
        int size = _constraintList.size();
        org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType[] mArray = new org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType) _constraintList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType[] getConstraint() 

    /**
     * Method getConstraintCount
     */
    public int getConstraintCount()
    {
        return _constraintList.size();
    } //-- int getConstraintCount() 

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
     * Method removeAllConstraint
     */
    public void removeAllConstraint()
    {
        _constraintList.removeAllElements();
    } //-- void removeAllConstraint() 

    /**
     * Method removeConstraint
     * 
     * @param index
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType removeConstraint(int index)
    {
        java.lang.Object obj = _constraintList.elementAt(index);
        _constraintList.removeElementAt(index);
        return (org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType) obj;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType removeConstraint(int) 

    /**
     * Method setConstraint
     * 
     * @param index
     * @param vConstraint
     */
    public void setConstraint(int index, org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType vConstraint)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _constraintList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _constraintList.setElementAt(vConstraint, index);
    } //-- void setConstraint(int, org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType) 

    /**
     * Method setConstraint
     * 
     * @param constraintArray
     */
    public void setConstraint(org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType[] constraintArray)
    {
        //-- copy array
        _constraintList.removeAllElements();
        for (int i = 0; i < constraintArray.length; i++) {
            _constraintList.addElement(constraintArray[i]);
        }
    } //-- void setConstraint(org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType) 

    /**
     * Method unmarshalConvexType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.ConvexType unmarshalConvexType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.ConvexType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.ConvexType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ConvexType unmarshalConvexType(java.io.Reader) 

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
