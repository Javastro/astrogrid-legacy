/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ConstraintType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class ConstraintType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class ConstraintType extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _vector
     */
    private CoordsType _vector;

    /**
     * Field _offset
     */
    private double _offset;

    /**
     * keeps track of state for field: _offset
     */
    private boolean _has_offset;


      //----------------/
     //- Constructors -/
    //----------------/

    public ConstraintType() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'offset'.
     * 
     * @return the value of field 'offset'.
     */
    public double getOffset()
    {
        return this._offset;
    } //-- double getOffset() 

    /**
     * Returns the value of field 'vector'.
     * 
     * @return the value of field 'vector'.
     */
    public CoordsType getVector()
    {
        return this._vector;
    } //-- CoordsType getVector() 

    /**
     * Method hasOffset
     */
    public boolean hasOffset()
    {
        return this._has_offset;
    } //-- boolean hasOffset() 

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
     * Sets the value of field 'offset'.
     * 
     * @param offset the value of field 'offset'.
     */
    public void setOffset(double offset)
    {
        this._offset = offset;
        this._has_offset = true;
    } //-- void setOffset(double) 

    /**
     * Sets the value of field 'vector'.
     * 
     * @param vector the value of field 'vector'.
     */
    public void setVector(CoordsType vector)
    {
        this._vector = vector;
    } //-- void setVector(CoordsType) 

    /**
     * Method unmarshalConstraintType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType unmarshalConstraintType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ConstraintType unmarshalConstraintType(java.io.Reader) 

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
