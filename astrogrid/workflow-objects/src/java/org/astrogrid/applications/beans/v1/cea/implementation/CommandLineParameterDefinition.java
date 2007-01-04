/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CommandLineParameterDefinition.java,v 1.2 2007/01/04 16:26:20 clq2 Exp $
 */

package org.astrogrid.applications.beans.v1.cea.implementation;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.applications.beans.v1.cea.implementation.types.SwitchTypes;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Defines what it is to be a command line parameter - needs more
 * thought with experience
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:20 $
 */
public class CommandLineParameterDefinition extends org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The characters that make up the commandline switch
     */
    private java.lang.String _commandSwitch;

    /**
     * the command position for a position only parameter. The
     * default value means that the parameter is position
     * independent and is assumed to need a switch. Conversely if a
     * parameter has a position value it is assumed not to need a
     * switch 
     */
    private int _commandPosition = -1;

    /**
     * keeps track of state for field: _commandPosition
     */
    private boolean _has_commandPosition;

    /**
     * is stdio - not used in implementation - should probably be
     * removed.
     */
    private boolean _stdio = false;

    /**
     * keeps track of state for field: _stdio
     */
    private boolean _has_stdio;

    /**
     * specifies the style of the switch - would be better to call
     * switchStyle!
     */
    private org.astrogrid.applications.beans.v1.cea.implementation.types.SwitchTypes _switchType = org.astrogrid.applications.beans.v1.cea.implementation.types.SwitchTypes.valueOf("normal");

    /**
     * does the application treat the parameter as a reference to a
     * file
     */
    private boolean _fileRef = true;

    /**
     * keeps track of state for field: _fileRef
     */
    private boolean _has_fileRef;

    /**
     * the local name of the file that this parameter refers to if
     * the application uses a fixed file name
     */
    private java.lang.String _localFileName;


      //----------------/
     //- Constructors -/
    //----------------/

    public CommandLineParameterDefinition() {
        super();
        setSwitchType(org.astrogrid.applications.beans.v1.cea.implementation.types.SwitchTypes.valueOf("normal"));
    } //-- org.astrogrid.applications.beans.v1.cea.implementation.CommandLineParameterDefinition()


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
     * Method deleteFileRef
     */
    public void deleteFileRef()
    {
        this._has_fileRef= false;
    } //-- void deleteFileRef() 

    /**
     * Method deleteStdio
     */
    public void deleteStdio()
    {
        this._has_stdio= false;
    } //-- void deleteStdio() 

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
        
        if (obj instanceof CommandLineParameterDefinition) {
        
            CommandLineParameterDefinition temp = (CommandLineParameterDefinition)obj;
            if (this._commandSwitch != null) {
                if (temp._commandSwitch == null) return false;
                else if (!(this._commandSwitch.equals(temp._commandSwitch))) 
                    return false;
            }
            else if (temp._commandSwitch != null)
                return false;
            if (this._commandPosition != temp._commandPosition)
                return false;
            if (this._has_commandPosition != temp._has_commandPosition)
                return false;
            if (this._stdio != temp._stdio)
                return false;
            if (this._has_stdio != temp._has_stdio)
                return false;
            if (this._switchType != null) {
                if (temp._switchType == null) return false;
                else if (!(this._switchType.equals(temp._switchType))) 
                    return false;
            }
            else if (temp._switchType != null)
                return false;
            if (this._fileRef != temp._fileRef)
                return false;
            if (this._has_fileRef != temp._has_fileRef)
                return false;
            if (this._localFileName != null) {
                if (temp._localFileName == null) return false;
                else if (!(this._localFileName.equals(temp._localFileName))) 
                    return false;
            }
            else if (temp._localFileName != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'commandPosition'. The field
     * 'commandPosition' has the following description: the command
     * position for a position only parameter. The default value
     * means that the parameter is position independent and is
     * assumed to need a switch. Conversely if a parameter has a
     * position value it is assumed not to need a switch 
     * 
     * @return the value of field 'commandPosition'.
     */
    public int getCommandPosition()
    {
        return this._commandPosition;
    } //-- int getCommandPosition() 

    /**
     * Returns the value of field 'commandSwitch'. The field
     * 'commandSwitch' has the following description: The
     * characters that make up the commandline switch
     * 
     * @return the value of field 'commandSwitch'.
     */
    public java.lang.String getCommandSwitch()
    {
        return this._commandSwitch;
    } //-- java.lang.String getCommandSwitch() 

    /**
     * Returns the value of field 'fileRef'. The field 'fileRef'
     * has the following description: does the application treat
     * the parameter as a reference to a file
     * 
     * @return the value of field 'fileRef'.
     */
    public boolean getFileRef()
    {
        return this._fileRef;
    } //-- boolean getFileRef() 

    /**
     * Returns the value of field 'localFileName'. The field
     * 'localFileName' has the following description: the local
     * name of the file that this parameter refers to if the
     * application uses a fixed file name
     * 
     * @return the value of field 'localFileName'.
     */
    public java.lang.String getLocalFileName()
    {
        return this._localFileName;
    } //-- java.lang.String getLocalFileName() 

    /**
     * Returns the value of field 'stdio'. The field 'stdio' has
     * the following description: is stdio - not used in
     * implementation - should probably be removed.
     * 
     * @return the value of field 'stdio'.
     */
    public boolean getStdio()
    {
        return this._stdio;
    } //-- boolean getStdio() 

    /**
     * Returns the value of field 'switchType'. The field
     * 'switchType' has the following description: specifies the
     * style of the switch - would be better to call switchStyle!
     * 
     * @return the value of field 'switchType'.
     */
    public org.astrogrid.applications.beans.v1.cea.implementation.types.SwitchTypes getSwitchType()
    {
        return this._switchType;
    } //-- org.astrogrid.applications.beans.v1.cea.implementation.types.SwitchTypes getSwitchType() 

    /**
     * Method hasCommandPosition
     */
    public boolean hasCommandPosition()
    {
        return this._has_commandPosition;
    } //-- boolean hasCommandPosition() 

    /**
     * Method hasFileRef
     */
    public boolean hasFileRef()
    {
        return this._has_fileRef;
    } //-- boolean hasFileRef() 

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
     * Sets the value of field 'commandPosition'. The field
     * 'commandPosition' has the following description: the command
     * position for a position only parameter. The default value
     * means that the parameter is position independent and is
     * assumed to need a switch. Conversely if a parameter has a
     * position value it is assumed not to need a switch 
     * 
     * @param commandPosition the value of field 'commandPosition'.
     */
    public void setCommandPosition(int commandPosition)
    {
        this._commandPosition = commandPosition;
        this._has_commandPosition = true;
    } //-- void setCommandPosition(int) 

    /**
     * Sets the value of field 'commandSwitch'. The field
     * 'commandSwitch' has the following description: The
     * characters that make up the commandline switch
     * 
     * @param commandSwitch the value of field 'commandSwitch'.
     */
    public void setCommandSwitch(java.lang.String commandSwitch)
    {
        this._commandSwitch = commandSwitch;
    } //-- void setCommandSwitch(java.lang.String) 

    /**
     * Sets the value of field 'fileRef'. The field 'fileRef' has
     * the following description: does the application treat the
     * parameter as a reference to a file
     * 
     * @param fileRef the value of field 'fileRef'.
     */
    public void setFileRef(boolean fileRef)
    {
        this._fileRef = fileRef;
        this._has_fileRef = true;
    } //-- void setFileRef(boolean) 

    /**
     * Sets the value of field 'localFileName'. The field
     * 'localFileName' has the following description: the local
     * name of the file that this parameter refers to if the
     * application uses a fixed file name
     * 
     * @param localFileName the value of field 'localFileName'.
     */
    public void setLocalFileName(java.lang.String localFileName)
    {
        this._localFileName = localFileName;
    } //-- void setLocalFileName(java.lang.String) 

    /**
     * Sets the value of field 'stdio'. The field 'stdio' has the
     * following description: is stdio - not used in implementation
     * - should probably be removed.
     * 
     * @param stdio the value of field 'stdio'.
     */
    public void setStdio(boolean stdio)
    {
        this._stdio = stdio;
        this._has_stdio = true;
    } //-- void setStdio(boolean) 

    /**
     * Sets the value of field 'switchType'. The field 'switchType'
     * has the following description: specifies the style of the
     * switch - would be better to call switchStyle!
     * 
     * @param switchType the value of field 'switchType'.
     */
    public void setSwitchType(org.astrogrid.applications.beans.v1.cea.implementation.types.SwitchTypes switchType)
    {
        this._switchType = switchType;
    } //-- void setSwitchType(org.astrogrid.applications.beans.v1.cea.implementation.types.SwitchTypes) 

    /**
     * Method unmarshalCommandLineParameterDefinition
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.cea.implementation.CommandLineParameterDefinition unmarshalCommandLineParameterDefinition(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.cea.implementation.CommandLineParameterDefinition) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.cea.implementation.CommandLineParameterDefinition.class, reader);
    } //-- org.astrogrid.applications.beans.v1.cea.implementation.CommandLineParameterDefinition unmarshalCommandLineParameterDefinition(java.io.Reader) 

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
