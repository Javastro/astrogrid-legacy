/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Tool.java,v 1.1 2004/08/10 21:09:29 pah Exp $
 */

package jes;

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
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;

/**
 * the CEA definition of the tool to be run 
 * 
 * @version $Revision: 1.1 $ $Date: 2004/08/10 21:09:29 $
 */
public class Tool extends org.astrogrid.common.bean.BaseBean 
implements Serializable
{
      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

//-- org.astrogrid.workflow.beans.v1.Tool()


      //-----------/
     //- Methods -/
    //-----------/

//-- boolean equals(java.lang.Object) 


    /**
     * Returns the value of field 'input'. The field 'input' has
     * the following description: the list of input parameters
     * 
     * @return the value of field 'input'.
     */
    public Input getInput()
    {
        return this._input;
    } //-- org.astrogrid.workflow.beans.v1.Input getInput() 

    /**
     * Returns the value of field 'interface'.
     * 
     * @return the value of field 'interface'.
     */
    public String getInterface()
    {
        return this._interface;
    } //-- java.lang.String getInterface() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'output'. The field 'output' has
     * the following description: the list of output paramters
     * 
     * @return the value of field 'output'.
     */
    public Output getOutput()
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
    }

 //-- boolean isValid() 


//-- void marshal(java.io.Writer) 

//-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'input'. The field 'input' has the
     * following description: the list of input parameters
     * 
     * @param input the value of field 'input'.
     */
    public void setInput(Input input)
    {
        this._input = input;
    } //-- void setInput(org.astrogrid.workflow.beans.v1.Input) 

    /**
     * Sets the value of field 'interface'.
     * 
     * @param _interface
     * @param interface the value of field 'interface'.
     */
    public void setInterface(String _interface)
    {
        this._interface = _interface;
    } //-- void setInterface(java.lang.String) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'output'. The field 'output' has the
     * following description: the list of output paramters
     * 
     * @param output the value of field 'output'.
     */
    public void setOutput(Output output)
    {
        this._output = output;
    }

    /**
     * @link aggregationByValue
     * @supplierCardinality 0..1 
     */
    private jes.Input lnkInput;

    /**
     * @link aggregationByValue
     * @supplierCardinality 0..1 
     */
    private jes.Output lnkOutput;
 //-- void setOutput(org.astrogrid.workflow.beans.v1.Output) 


//-- org.astrogrid.workflow.beans.v1.Tool unmarshalTool(java.io.Reader) 

//-- void validate() 

}
