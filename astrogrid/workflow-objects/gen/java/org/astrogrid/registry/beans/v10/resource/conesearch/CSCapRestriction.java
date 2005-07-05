/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CSCapRestriction.java,v 1.2 2005/07/05 08:27:01 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.conesearch;

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
 * an abstract capability that sets the standardURL to the
 * ConeSearch
 *  standard definition
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:27:01 $
 */
public class CSCapRestriction extends org.astrogrid.registry.beans.v10.resource.Capability 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _standardID
     */
    private java.lang.String _standardID;

    /**
     * Field _standardURL
     */
    private java.lang.String _standardURL = "http://www.us-vo.org/metadata/conesearch.html";


      //----------------/
     //- Constructors -/
    //----------------/

    public CSCapRestriction() {
        super();
        setStandardURL("http://www.us-vo.org/metadata/conesearch.html");
    } //-- org.astrogrid.registry.beans.v10.resource.conesearch.CSCapRestriction()


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
        
        if (obj instanceof CSCapRestriction) {
        
            CSCapRestriction temp = (CSCapRestriction)obj;
            if (this._standardID != null) {
                if (temp._standardID == null) return false;
                else if (!(this._standardID.equals(temp._standardID))) 
                    return false;
            }
            else if (temp._standardID != null)
                return false;
            if (this._standardURL != null) {
                if (temp._standardURL == null) return false;
                else if (!(this._standardURL.equals(temp._standardURL))) 
                    return false;
            }
            else if (temp._standardURL != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'standardID'.
     * 
     * @return the value of field 'standardID'.
     */
    public java.lang.String getStandardID()
    {
        return this._standardID;
    } //-- java.lang.String getStandardID() 

    /**
     * Returns the value of field 'standardURL'.
     * 
     * @return the value of field 'standardURL'.
     */
    public java.lang.String getStandardURL()
    {
        return this._standardURL;
    } //-- java.lang.String getStandardURL() 

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
     * Sets the value of field 'standardID'.
     * 
     * @param standardID the value of field 'standardID'.
     */
    public void setStandardID(java.lang.String standardID)
    {
        this._standardID = standardID;
    } //-- void setStandardID(java.lang.String) 

    /**
     * Sets the value of field 'standardURL'.
     * 
     * @param standardURL the value of field 'standardURL'.
     */
    public void setStandardURL(java.lang.String standardURL)
    {
        this._standardURL = standardURL;
    } //-- void setStandardURL(java.lang.String) 

    /**
     * Method unmarshalCSCapRestriction
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.conesearch.CSCapRestriction unmarshalCSCapRestriction(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.conesearch.CSCapRestriction) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.conesearch.CSCapRestriction.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.conesearch.CSCapRestriction unmarshalCSCapRestriction(java.io.Reader) 

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
