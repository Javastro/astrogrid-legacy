/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: CapabilityType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package;

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
 * This is a base type for defining specific kinds of service 
 *  capability descriptions, each with its own special mark-up
 * schema.
 *  
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class CapabilityType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * A URL that points to a human-readable document that
     * describes the 
     *  standard upon which a resource is based.
     *  
     */
    private java.lang.String _standardURL;

    /**
     * An identifier for a registered standard. 
     *  
     */
    private org.astrogrid.registry.generated.package.StandardID _standardID;


      //----------------/
     //- Constructors -/
    //----------------/

    public CapabilityType() {
        super();
    } //-- org.astrogrid.registry.generated.package.CapabilityType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'standardID'. The field
     * 'standardID' has the following description: An identifier
     * for a registered standard. 
     *  
     * 
     * @return the value of field 'standardID'.
     */
    public org.astrogrid.registry.generated.package.StandardID getStandardID()
    {
        return this._standardID;
    } //-- org.astrogrid.registry.generated.package.StandardID getStandardID() 

    /**
     * Returns the value of field 'standardURL'. The field
     * 'standardURL' has the following description: A URL that
     * points to a human-readable document that describes the 
     *  standard upon which a resource is based.
     *  
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
     * Sets the value of field 'standardID'. The field 'standardID'
     * has the following description: An identifier for a
     * registered standard. 
     *  
     * 
     * @param standardID the value of field 'standardID'.
     */
    public void setStandardID(org.astrogrid.registry.generated.package.StandardID standardID)
    {
        this._standardID = standardID;
    } //-- void setStandardID(org.astrogrid.registry.generated.package.StandardID) 

    /**
     * Sets the value of field 'standardURL'. The field
     * 'standardURL' has the following description: A URL that
     * points to a human-readable document that describes the 
     *  standard upon which a resource is based.
     *  
     * 
     * @param standardURL the value of field 'standardURL'.
     */
    public void setStandardURL(java.lang.String standardURL)
    {
        this._standardURL = standardURL;
    } //-- void setStandardURL(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.CapabilityType) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.CapabilityType.class, reader);
    } //-- java.lang.Object unmarshal(java.io.Reader) 

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
