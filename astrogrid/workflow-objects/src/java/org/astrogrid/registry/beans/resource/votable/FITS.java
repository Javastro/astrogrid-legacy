/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: FITS.java,v 1.14 2007/01/04 16:26:14 clq2 Exp $
 */

package org.astrogrid.registry.beans.resource.votable;

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
 * Class FITS.
 * 
 * @version $Revision: 1.14 $ $Date: 2007/01/04 16:26:14 $
 */
public class FITS extends org.astrogrid.registry.beans.resource.votable.StreamTABLEFORMATType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _extnum
     */
    private int _extnum;

    /**
     * keeps track of state for field: _extnum
     */
    private boolean _has_extnum;


      //----------------/
     //- Constructors -/
    //----------------/

    public FITS() {
        super();
    } //-- org.astrogrid.registry.beans.resource.votable.FITS()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteExtnum
     */
    public void deleteExtnum()
    {
        this._has_extnum= false;
    } //-- void deleteExtnum() 

    /**
     * Note: hashCode() has not been overriden
     * 
     * @param obj
     */
    public boolean equals(java.lang.Object obj)
    {
        if ( this == obj )
            return true;
        
        if (super.equals(obj)==false)
            return false;
        
        if (obj instanceof FITS) {
        
            FITS temp = (FITS)obj;
            if (this._extnum != temp._extnum)
                return false;
            if (this._has_extnum != temp._has_extnum)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'extnum'.
     * 
     * @return the value of field 'extnum'.
     */
    public int getExtnum()
    {
        return this._extnum;
    } //-- int getExtnum() 

    /**
     * Method hasExtnum
     */
    public boolean hasExtnum()
    {
        return this._has_extnum;
    } //-- boolean hasExtnum() 

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
     * Sets the value of field 'extnum'.
     * 
     * @param extnum the value of field 'extnum'.
     */
    public void setExtnum(int extnum)
    {
        this._extnum = extnum;
        this._has_extnum = true;
    } //-- void setExtnum(int) 

    /**
     * Method unmarshalFITS
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.votable.FITS unmarshalFITS(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.votable.FITS) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.votable.FITS.class, reader);
    } //-- org.astrogrid.registry.beans.resource.votable.FITS unmarshalFITS(java.io.Reader) 

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
