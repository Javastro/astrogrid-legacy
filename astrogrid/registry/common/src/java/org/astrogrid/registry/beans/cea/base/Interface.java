/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Interface.java,v 1.2 2004/03/19 08:16:47 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.cea.base;

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
 * @version $Revision: 1.2 $ $Date: 2004/03/19 08:16:47 $
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
    private org.astrogrid.registry.beans.cea.base.Input _input;

    /**
     * Field _output
     */
    private org.astrogrid.registry.beans.cea.base.Output _output;


      //----------------/
     //- Constructors -/
    //----------------/

    public Interface() {
        super();
    } //-- org.astrogrid.registry.beans.cea.base.Interface()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'input'.
     * 
     * @return the value of field 'input'.
     */
    public org.astrogrid.registry.beans.cea.base.Input getInput()
    {
        return this._input;
    } //-- org.astrogrid.registry.beans.cea.base.Input getInput() 

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
    public org.astrogrid.registry.beans.cea.base.Output getOutput()
    {
        return this._output;
    } //-- org.astrogrid.registry.beans.cea.base.Output getOutput() 

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
    public void setInput(org.astrogrid.registry.beans.cea.base.Input input)
    {
        this._input = input;
    } //-- void setInput(org.astrogrid.registry.beans.cea.base.Input) 

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
    public void setOutput(org.astrogrid.registry.beans.cea.base.Output output)
    {
        this._output = output;
    } //-- void setOutput(org.astrogrid.registry.beans.cea.base.Output) 

    /**
     * Method unmarshalInterface
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.cea.base.Interface unmarshalInterface(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.cea.base.Interface) Unmarshaller.unmarshal(org.astrogrid.registry.beans.cea.base.Interface.class, reader);
    } //-- org.astrogrid.registry.beans.cea.base.Interface unmarshalInterface(java.io.Reader) 

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
