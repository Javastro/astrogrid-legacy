/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CommandLineParameterDefinition.java,v 1.1 2004/02/20 18:36:39 nw Exp $
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
 * Defines what it is to be a command line parameter - needs more
 * thought with experience
 * 
 * @version $Revision: 1.1 $ $Date: 2004/02/20 18:36:39 $
 */
public class CommandLineParameterDefinition extends org.astrogrid.applications.beans.v1.BaseParameterDefinition 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _commandSwitch
     */
    private java.lang.String _commandSwitch;

    /**
     * Field _commandPosition
     */
    private int _commandPosition;

    /**
     * keeps track of state for field: _commandPosition
     */
    private boolean _has_commandPosition;

    /**
     * Field _stdio
     */
    private boolean _stdio = false;

    /**
     * keeps track of state for field: _stdio
     */
    private boolean _has_stdio;


      //----------------/
     //- Constructors -/
    //----------------/

    public CommandLineParameterDefinition() {
        super();
    } //-- org.astrogrid.applications.beans.v1.CommandLineParameterDefinition()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteCommandPosition
     */
    public void deleteCommandPosition()
    {
        this._has_commandPosition= false;
    } //-- void deleteCommandPosition() 

    /**
     * Method deleteStdio
     */
    public void deleteStdio()
    {
        this._has_stdio= false;
    } //-- void deleteStdio() 

    /**
     * Returns the value of field 'commandPosition'.
     * 
     * @return the value of field 'commandPosition'.
     */
    public int getCommandPosition()
    {
        return this._commandPosition;
    } //-- int getCommandPosition() 

    /**
     * Returns the value of field 'commandSwitch'.
     * 
     * @return the value of field 'commandSwitch'.
     */
    public java.lang.String getCommandSwitch()
    {
        return this._commandSwitch;
    } //-- java.lang.String getCommandSwitch() 

    /**
     * Returns the value of field 'stdio'.
     * 
     * @return the value of field 'stdio'.
     */
    public boolean getStdio()
    {
        return this._stdio;
    } //-- boolean getStdio() 

    /**
     * Method hasCommandPosition
     */
    public boolean hasCommandPosition()
    {
        return this._has_commandPosition;
    } //-- boolean hasCommandPosition() 

    /**
     * Method hasStdio
     */
    public boolean hasStdio()
    {
        return this._has_stdio;
    } //-- boolean hasStdio() 

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
     * Sets the value of field 'commandPosition'.
     * 
     * @param commandPosition the value of field 'commandPosition'.
     */
    public void setCommandPosition(int commandPosition)
    {
        this._commandPosition = commandPosition;
        this._has_commandPosition = true;
    } //-- void setCommandPosition(int) 

    /**
     * Sets the value of field 'commandSwitch'.
     * 
     * @param commandSwitch the value of field 'commandSwitch'.
     */
    public void setCommandSwitch(java.lang.String commandSwitch)
    {
        this._commandSwitch = commandSwitch;
    } //-- void setCommandSwitch(java.lang.String) 

    /**
     * Sets the value of field 'stdio'.
     * 
     * @param stdio the value of field 'stdio'.
     */
    public void setStdio(boolean stdio)
    {
        this._stdio = stdio;
        this._has_stdio = true;
    } //-- void setStdio(boolean) 

    /**
     * Method unmarshalCommandLineParameterDefinition
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.CommandLineParameterDefinition unmarshalCommandLineParameterDefinition(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.CommandLineParameterDefinition) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.CommandLineParameterDefinition.class, reader);
    } //-- org.astrogrid.applications.beans.v1.CommandLineParameterDefinition unmarshalCommandLineParameterDefinition(java.io.Reader) 

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
