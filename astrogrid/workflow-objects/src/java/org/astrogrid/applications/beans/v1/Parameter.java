/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Parameter.java,v 1.1 2004/02/20 18:36:39 nw Exp $
 */

package org.astrogrid.applications.beans.v1;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.applications.beans.v1.types.SwitchTypes;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Parameter.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/02/20 18:36:39 $
 */
public class Parameter extends org.astrogrid.applications.beans.v1.CommandLineParameterDefinition 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _switchType
     */
    private org.astrogrid.applications.beans.v1.types.SwitchTypes _switchType = org.astrogrid.applications.beans.v1.types.SwitchTypes.valueOf("normal");


      //----------------/
     //- Constructors -/
    //----------------/

    public Parameter() {
        super();
        setSwitchType(org.astrogrid.applications.beans.v1.types.SwitchTypes.valueOf("normal"));
    } //-- org.astrogrid.applications.beans.v1.Parameter()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'switchType'.
     * 
     * @return the value of field 'switchType'.
     */
    public org.astrogrid.applications.beans.v1.types.SwitchTypes getSwitchType()
    {
        return this._switchType;
    } //-- org.astrogrid.applications.beans.v1.types.SwitchTypes getSwitchType() 

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
     * Sets the value of field 'switchType'.
     * 
     * @param switchType the value of field 'switchType'.
     */
    public void setSwitchType(org.astrogrid.applications.beans.v1.types.SwitchTypes switchType)
    {
        this._switchType = switchType;
    } //-- void setSwitchType(org.astrogrid.applications.beans.v1.types.SwitchTypes) 

    /**
     * Method unmarshalParameter
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.Parameter unmarshalParameter(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.Parameter) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.Parameter.class, reader);
    } //-- org.astrogrid.applications.beans.v1.Parameter unmarshalParameter(java.io.Reader) 

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
