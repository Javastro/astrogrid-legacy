/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SummaryType.java,v 1.10 2004/12/03 14:47:40 jdt Exp $
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
 * Class SummaryType.
 * 
 * @version $Revision: 1.10 $ $Date: 2004/12/03 14:47:40 $
 */
public class SummaryType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * An account of the nature of the resource
     *  
     */
    private java.lang.String _description;

    /**
     * URL pointing to a human-readable document describing this 
     *  resource. 
     *  
     */
    private java.lang.String _referenceURL;

    /**
     * a bibliographic reference from which the present resource is
     * 
     *  derived or extracted. 
     *  
     */
    private org.astrogrid.registry.beans.resource.SourceType _source;


      //----------------/
     //- Constructors -/
    //----------------/

    public SummaryType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.SummaryType()


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
        
        if (obj instanceof SummaryType) {
        
            SummaryType temp = (SummaryType)obj;
            if (this._description != null) {
                if (temp._description == null) return false;
                else if (!(this._description.equals(temp._description))) 
                    return false;
            }
            else if (temp._description != null)
                return false;
            if (this._referenceURL != null) {
                if (temp._referenceURL == null) return false;
                else if (!(this._referenceURL.equals(temp._referenceURL))) 
                    return false;
            }
            else if (temp._referenceURL != null)
                return false;
            if (this._source != null) {
                if (temp._source == null) return false;
                else if (!(this._source.equals(temp._source))) 
                    return false;
            }
            else if (temp._source != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'description'. The field
     * 'description' has the following description: An account of
     * the nature of the resource
     *  
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'referenceURL'. The field
     * 'referenceURL' has the following description: URL pointing
     * to a human-readable document describing this 
     *  resource. 
     *  
     * 
     * @return the value of field 'referenceURL'.
     */
    public java.lang.String getReferenceURL()
    {
        return this._referenceURL;
    } //-- java.lang.String getReferenceURL() 

    /**
     * Returns the value of field 'source'. The field 'source' has
     * the following description: a bibliographic reference from
     * which the present resource is 
     *  derived or extracted. 
     *  
     * 
     * @return the value of field 'source'.
     */
    public org.astrogrid.registry.beans.resource.SourceType getSource()
    {
        return this._source;
    } //-- org.astrogrid.registry.beans.resource.SourceType getSource() 

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
     * Sets the value of field 'description'. The field
     * 'description' has the following description: An account of
     * the nature of the resource
     *  
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'referenceURL'. The field
     * 'referenceURL' has the following description: URL pointing
     * to a human-readable document describing this 
     *  resource. 
     *  
     * 
     * @param referenceURL the value of field 'referenceURL'.
     */
    public void setReferenceURL(java.lang.String referenceURL)
    {
        this._referenceURL = referenceURL;
    } //-- void setReferenceURL(java.lang.String) 

    /**
     * Sets the value of field 'source'. The field 'source' has the
     * following description: a bibliographic reference from which
     * the present resource is 
     *  derived or extracted. 
     *  
     * 
     * @param source the value of field 'source'.
     */
    public void setSource(org.astrogrid.registry.beans.resource.SourceType source)
    {
        this._source = source;
    } //-- void setSource(org.astrogrid.registry.beans.resource.SourceType) 

    /**
     * Method unmarshalSummaryType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.SummaryType unmarshalSummaryType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.SummaryType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.SummaryType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.SummaryType unmarshalSummaryType(java.io.Reader) 

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
