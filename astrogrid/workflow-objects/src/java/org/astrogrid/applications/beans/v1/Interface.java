/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Interface.java,v 1.18 2004/03/30 22:42:55 pah Exp $
 */

package org.astrogrid.applications.beans.v1;

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
 * description of an interface
 * 
 * @version $Revision: 1.18 $ $Date: 2004/03/30 22:42:55 $
 */
public class Interface extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _input
     */
    private org.astrogrid.applications.beans.v1.Input _input;

    /**
     * Field _output
     */
    private org.astrogrid.applications.beans.v1.Output _output;


      //----------------/
     //- Constructors -/
    //----------------/

    public Interface() {
        super();
    } //-- org.astrogrid.applications.beans.v1.Interface()


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
        
        if (obj instanceof Interface) {
        
            Interface temp = (Interface)obj;
            if (this._name != null) {
                if (temp._name == null) return false;
                else if (!(this._name.equals(temp._name))) 
                    return false;
            }
            else if (temp._name != null)
                return false;
            if (this._input != null) {
                if (temp._input == null) return false;
                else if (!(this._input.equals(temp._input))) 
                    return false;
            }
            else if (temp._input != null)
                return false;
            if (this._output != null) {
                if (temp._output == null) return false;
                else if (!(this._output.equals(temp._output))) 
                    return false;
            }
            else if (temp._output != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'input'.
     * 
     * @return the value of field 'input'.
     */
    public org.astrogrid.applications.beans.v1.Input getInput()
    {
        return this._input;
    } //-- org.astrogrid.applications.beans.v1.Input getInput() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'output'.
     * 
     * @return the value of field 'output'.
     */
    public org.astrogrid.applications.beans.v1.Output getOutput()
    {
        return this._output;
    } //-- org.astrogrid.applications.beans.v1.Output getOutput() 

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
     * Sets the value of field 'input'.
     * 
     * @param input the value of field 'input'.
     */
    public void setInput(org.astrogrid.applications.beans.v1.Input input)
    {
        this._input = input;
    } //-- void setInput(org.astrogrid.applications.beans.v1.Input) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'output'.
     * 
     * @param output the value of field 'output'.
     */
    public void setOutput(org.astrogrid.applications.beans.v1.Output output)
    {
        this._output = output;
    } //-- void setOutput(org.astrogrid.applications.beans.v1.Output) 

    /**
     * Method unmarshalInterface
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.Interface unmarshalInterface(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.Interface) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.Interface.class, reader);
    } //-- org.astrogrid.applications.beans.v1.Interface unmarshalInterface(java.io.Reader) 

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
