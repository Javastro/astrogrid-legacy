/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: RegionType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
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
 * Class RegionType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class RegionType extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _fill_factor
     */
    private double _fill_factor = 1;

    /**
     * keeps track of state for field: _fill_factor
     */
    private boolean _has_fill_factor;

    /**
     * Field _note
     */
    private java.lang.String _note;


      //----------------/
     //- Constructors -/
    //----------------/

    public RegionType() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.RegionType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteFill_factor
     */
    public void deleteFill_factor()
    {
        this._has_fill_factor= false;
    } //-- void deleteFill_factor() 

    /**
     * Returns the value of field 'fill_factor'.
     * 
     * @return the value of field 'fill_factor'.
     */
    public double getFill_factor()
    {
        return this._fill_factor;
    } //-- double getFill_factor() 

    /**
     * Returns the value of field 'note'.
     * 
     * @return the value of field 'note'.
     */
    public java.lang.String getNote()
    {
        return this._note;
    } //-- java.lang.String getNote() 

    /**
     * Method hasFill_factor
     */
    public boolean hasFill_factor()
    {
        return this._has_fill_factor;
    } //-- boolean hasFill_factor() 

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
     * Sets the value of field 'fill_factor'.
     * 
     * @param fill_factor the value of field 'fill_factor'.
     */
    public void setFill_factor(double fill_factor)
    {
        this._fill_factor = fill_factor;
        this._has_fill_factor = true;
    } //-- void setFill_factor(double) 

    /**
     * Sets the value of field 'note'.
     * 
     * @param note the value of field 'note'.
     */
    public void setNote(java.lang.String note)
    {
        this._note = note;
    } //-- void setNote(java.lang.String) 

    /**
     * Method unmarshalRegionType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.RegionType unmarshalRegionType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.RegionType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.RegionType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.RegionType unmarshalRegionType(java.io.Reader) 

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
