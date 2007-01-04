/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SourceType.java,v 1.14 2007/01/04 16:26:23 clq2 Exp $
 */

package org.astrogrid.registry.beans.resource;

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
 * Class SourceType.
 * 
 * @version $Revision: 1.14 $ $Date: 2007/01/04 16:26:23 $
 */
public class SourceType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private java.lang.String _content = "";

    /**
     * The reference format. Recognized values include "bibcode", 
     *  refering to a standard astronomical bibcode bibcode
     *  (http://cdsweb.u-strasbg.fr/simbad/refcode.html). 
     *  
     */
    private java.lang.String _format;


      //----------------/
     //- Constructors -/
    //----------------/

    public SourceType() {
        super();
        setContent("");
    } //-- org.astrogrid.registry.beans.resource.SourceType()


      //-----------/
     //- Methods -/
    //-----------/

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
        
        if (obj instanceof SourceType) {
        
            SourceType temp = (SourceType)obj;
            if (this._content != null) {
                if (temp._content == null) return false;
                else if (!(this._content.equals(temp._content))) 
                    return false;
            }
            else if (temp._content != null)
                return false;
            if (this._format != null) {
                if (temp._format == null) return false;
                else if (!(this._format.equals(temp._format))) 
                    return false;
            }
            else if (temp._format != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'content'.
     */
    public java.lang.String getContent()
    {
        return this._content;
    } //-- java.lang.String getContent() 

    /**
     * Returns the value of field 'format'. The field 'format' has
     * the following description: The reference format. Recognized
     * values include "bibcode", 
     *  refering to a standard astronomical bibcode bibcode
     *  (http://cdsweb.u-strasbg.fr/simbad/refcode.html). 
     *  
     * 
     * @return the value of field 'format'.
     */
    public java.lang.String getFormat()
    {
        return this._format;
    } //-- java.lang.String getFormat() 

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
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(java.lang.String content)
    {
        this._content = content;
    } //-- void setContent(java.lang.String) 

    /**
     * Sets the value of field 'format'. The field 'format' has the
     * following description: The reference format. Recognized
     * values include "bibcode", 
     *  refering to a standard astronomical bibcode bibcode
     *  (http://cdsweb.u-strasbg.fr/simbad/refcode.html). 
     *  
     * 
     * @param format the value of field 'format'.
     */
    public void setFormat(java.lang.String format)
    {
        this._format = format;
    } //-- void setFormat(java.lang.String) 

    /**
     * Method unmarshalSourceType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.SourceType unmarshalSourceType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.SourceType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.SourceType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.SourceType unmarshalSourceType(java.io.Reader) 

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
