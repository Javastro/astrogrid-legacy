/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: NameReferenceType.java,v 1.1 2004/03/03 03:40:59 pcontrer Exp $
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
 * Class NameReferenceType.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:59 $
 */
public class NameReferenceType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Unambiguous reference to the resource conforming to the IVOA
     *  standard for identifiers
     *  
     */
    private org.astrogrid.registry.generated.package.Identifier _identifier;

    /**
     * the name of someone or something
     */
    private java.lang.String _name;


      //----------------/
     //- Constructors -/
    //----------------/

    public NameReferenceType() {
        super();
    } //-- org.astrogrid.registry.generated.package.NameReferenceType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'identifier'. The field
     * 'identifier' has the following description: Unambiguous
     * reference to the resource conforming to the IVOA
     *  standard for identifiers
     *  
     * 
     * @return the value of field 'identifier'.
     */
    public org.astrogrid.registry.generated.package.Identifier getIdentifier()
    {
        return this._identifier;
    } //-- org.astrogrid.registry.generated.package.Identifier getIdentifier() 

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: the name of someone or something
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

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
     * Sets the value of field 'identifier'. The field 'identifier'
     * has the following description: Unambiguous reference to the
     * resource conforming to the IVOA
     *  standard for identifiers
     *  
     * 
     * @param identifier the value of field 'identifier'.
     */
    public void setIdentifier(org.astrogrid.registry.generated.package.Identifier identifier)
    {
        this._identifier = identifier;
    } //-- void setIdentifier(org.astrogrid.registry.generated.package.Identifier) 

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: the name of someone or something
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.NameReferenceType) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.NameReferenceType.class, reader);
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
