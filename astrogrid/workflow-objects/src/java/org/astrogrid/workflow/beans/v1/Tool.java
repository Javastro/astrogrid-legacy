/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Tool.java,v 1.16 2004/03/15 16:53:03 pah Exp $
 */

package org.astrogrid.workflow.beans.v1;

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
 * the CEA definition of the tool to be run 
 * 
 * @version $Revision: 1.16 $ $Date: 2004/03/15 16:53:03 $
 */
public class Tool extends org.astrogrid.common.bean.BaseBean 
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
     * Field _interface
     */
    private java.lang.String _interface;

    /**
     * the list of input parameters
     */
    private org.astrogrid.workflow.beans.v1.Input _input;

    /**
     * the list of output paramters
     */
    private org.astrogrid.workflow.beans.v1.Output _output;


      //----------------/
     //- Constructors -/
    //----------------/

    public Tool() {
        super();
    } //-- org.astrogrid.workflow.beans.v1.Tool()


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
        
        if (obj instanceof Tool) {
        
            Tool temp = (Tool)obj;
            if (this._name != null) {
                if (temp._name == null) return false;
                else if (!(this._name.equals(temp._name))) 
                    return false;
            }
            else if (temp._name != null)
                return false;
            if (this._interface != null) {
                if (temp._interface == null) return false;
                else if (!(this._interface.equals(temp._interface))) 
                    return false;
            }
            else if (temp._interface != null)
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
     * Returns the value of field 'input'. The field 'input' has
     * the following description: the list of input parameters
     * 
     * @return the value of field 'input'.
     */
    public org.astrogrid.workflow.beans.v1.Input getInput()
    {
        return this._input;
    } //-- org.astrogrid.workflow.beans.v1.Input getInput() 

    /**
     * Returns the value of field 'interface'.
     * 
     * @return the value of field 'interface'.
     */
    public java.lang.String getInterface()
    {
        return this._interface;
    } //-- java.lang.String getInterface() 

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
     * Returns the value of field 'output'. The field 'output' has
     * the following description: the list of output paramters
     * 
     * @return the value of field 'output'.
     */
    public org.astrogrid.workflow.beans.v1.Output getOutput()
    {
        return this._output;
    } //-- org.astrogrid.workflow.beans.v1.Output getOutput() 

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
     * Sets the value of field 'input'. The field 'input' has the
     * following description: the list of input parameters
     * 
     * @param input the value of field 'input'.
     */
    public void setInput(org.astrogrid.workflow.beans.v1.Input input)
    {
        this._input = input;
    } //-- void setInput(org.astrogrid.workflow.beans.v1.Input) 

    /**
     * Sets the value of field 'interface'.
     * 
     * @param _interface
     * @param interface the value of field 'interface'.
     */
    public void setInterface(java.lang.String _interface)
    {
        this._interface = _interface;
    } //-- void setInterface(java.lang.String) 

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
     * Sets the value of field 'output'. The field 'output' has the
     * following description: the list of output paramters
     * 
     * @param output the value of field 'output'.
     */
    public void setOutput(org.astrogrid.workflow.beans.v1.Output output)
    {
        this._output = output;
    } //-- void setOutput(org.astrogrid.workflow.beans.v1.Output) 

    /**
     * Method unmarshalTool
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.Tool unmarshalTool(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.Tool) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.Tool.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.Tool unmarshalTool(java.io.Reader) 

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
