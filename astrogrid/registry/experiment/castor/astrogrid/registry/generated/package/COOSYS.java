/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: COOSYS.java,v 1.1 2004/03/03 03:40:59 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.registry.generated.package.types.COOSYSSystemType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class COOSYS.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:59 $
 */
public class COOSYS extends org.astrogrid.registry.generated.package.AnyTEXT 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _ID
     */
    private java.lang.String _ID;

    /**
     * Field _equinox
     */
    private java.lang.String _equinox;

    /**
     * Field _epoch
     */
    private java.lang.String _epoch;

    /**
     * Field _system
     */
    private org.astrogrid.registry.generated.package.types.COOSYSSystemType _system = org.astrogrid.registry.generated.package.types.COOSYSSystemType.valueOf("eq_FK5");

    /**
     * internal content storage
     */
    private java.lang.String _content = "";


      //----------------/
     //- Constructors -/
    //----------------/

    public COOSYS() {
        super();
        setSystem(org.astrogrid.registry.generated.package.types.COOSYSSystemType.valueOf("eq_FK5"));
        setContent("");
    } //-- org.astrogrid.registry.generated.package.COOSYS()


      //-----------/
     //- Methods -/
    //-----------/

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
     * Returns the value of field 'epoch'.
     * 
     * @return the value of field 'epoch'.
     */
    public java.lang.String getEpoch()
    {
        return this._epoch;
    } //-- java.lang.String getEpoch() 

    /**
     * Returns the value of field 'equinox'.
     * 
     * @return the value of field 'equinox'.
     */
    public java.lang.String getEquinox()
    {
        return this._equinox;
    } //-- java.lang.String getEquinox() 

    /**
     * Returns the value of field 'ID'.
     * 
     * @return the value of field 'ID'.
     */
    public java.lang.String getID()
    {
        return this._ID;
    } //-- java.lang.String getID() 

    /**
     * Returns the value of field 'system'.
     * 
     * @return the value of field 'system'.
     */
    public org.astrogrid.registry.generated.package.types.COOSYSSystemType getSystem()
    {
        return this._system;
    } //-- org.astrogrid.registry.generated.package.types.COOSYSSystemType getSystem() 

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
     * Sets the value of field 'epoch'.
     * 
     * @param epoch the value of field 'epoch'.
     */
    public void setEpoch(java.lang.String epoch)
    {
        this._epoch = epoch;
    } //-- void setEpoch(java.lang.String) 

    /**
     * Sets the value of field 'equinox'.
     * 
     * @param equinox the value of field 'equinox'.
     */
    public void setEquinox(java.lang.String equinox)
    {
        this._equinox = equinox;
    } //-- void setEquinox(java.lang.String) 

    /**
     * Sets the value of field 'ID'.
     * 
     * @param ID the value of field 'ID'.
     */
    public void setID(java.lang.String ID)
    {
        this._ID = ID;
    } //-- void setID(java.lang.String) 

    /**
     * Sets the value of field 'system'.
     * 
     * @param system the value of field 'system'.
     */
    public void setSystem(org.astrogrid.registry.generated.package.types.COOSYSSystemType system)
    {
        this._system = system;
    } //-- void setSystem(org.astrogrid.registry.generated.package.types.COOSYSSystemType) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.COOSYS) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.COOSYS.class, reader);
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
