/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: StreamTABLEFORMATType.java,v 1.3 2004/03/05 09:52:02 KevinBenson Exp $
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
 * Class StreamTABLEFORMATType.
 * 
 * @version $Revision: 1.3 $ $Date: 2004/03/05 09:52:02 $
 */
public class StreamTABLEFORMATType extends org.astrogrid.registry.beans.resource.votable.TABLEFORMATType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _STREAM
     */
    private org.astrogrid.registry.beans.resource.votable.STREAM _STREAM;


      //----------------/
     //- Constructors -/
    //----------------/

    public StreamTABLEFORMATType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.votable.StreamTABLEFORMATType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'STREAM'.
     * 
     * @return the value of field 'STREAM'.
     */
    public org.astrogrid.registry.beans.resource.votable.STREAM getSTREAM()
    {
        return this._STREAM;
    } //-- org.astrogrid.registry.beans.resource.votable.STREAM getSTREAM() 

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
     * Sets the value of field 'STREAM'.
     * 
     * @param STREAM the value of field 'STREAM'.
     */
    public void setSTREAM(org.astrogrid.registry.beans.resource.votable.STREAM STREAM)
    {
        this._STREAM = STREAM;
    } //-- void setSTREAM(org.astrogrid.registry.beans.resource.votable.STREAM) 

    /**
     * Method unmarshalStreamTABLEFORMATType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.votable.StreamTABLEFORMATType unmarshalStreamTABLEFORMATType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.votable.StreamTABLEFORMATType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.votable.StreamTABLEFORMATType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.votable.StreamTABLEFORMATType unmarshalStreamTABLEFORMATType(java.io.Reader) 

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
