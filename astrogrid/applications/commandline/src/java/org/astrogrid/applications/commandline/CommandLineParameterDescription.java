/*$Id: CommandLineParameterDescription.java,v 1.2 2004/07/01 11:07:59 nw Exp $
 * Created on 17-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.commandline;

import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.applications.description.base.BaseParameterDescription;

import java.util.ArrayList;
import java.util.List;

/** extended parameter description for commandline - contains further fields loaded from config - flag format, etc.
 *
 */
public class CommandLineParameterDescription extends BaseParameterDescription {
    /** Construct a new CommandLineParameterDescription
     * 
     */
    public CommandLineParameterDescription() {
        super();
    }

   
    /**
     * The string that makes up the command switch. If this is not specified then it is assumed that the switch is the same as the name
     */
    private String commandSwitch=null;
    /**
     * The switchtype can be "normal" i.e. it is a -switch form or "keyword" where is is of the form switch=par
     */
    private String switchType = "normal";
    /**
     * The commandPosition indicates where on the command line the parameter is to be placed - The first parameter position is 1. If this value is specified then it means that no switch will be output, but the parameter value will be placed directly on the command line at that position.
     */
    private int commandPosition = -1;
   

    /** flag that indicates application expects a file-reference containing this parameter in the arguments, rather than the parameter value itself. */ 
    private boolean file = false;

    /**
     * Adds any necessary switches to the commandline parameter. This is controlled by the @link #commandPosition, @link #commandSwitch and @link #switchType fields. 
     * If the commandPosition is anything other than -1 then no adornment is added. If a switch string is to be added then the style is controlled by switchType and the
     * string for the switch is given by commandSwitch, or if that is null the parameter name is used. 
     * @param val
     * @return stringbuffer containing original value, plus any required adornments.
     */
    public List addCmdlineAdornment(String val)
    {
       List cmdarg = new ArrayList();
      
       if (commandPosition == -1) {
          // if not a command position type parameter then we need to add a switch
          String sw = name;
          if(commandSwitch != null)
          {
             sw = commandSwitch;
          }
          if (switchType.equalsIgnoreCase("normal")) {
             cmdarg.add( "-" + sw);
             cmdarg.add(val);
            
          }
          else {             
             cmdarg.add(sw + "=" + val);
          }
       
       }
       else
       {
          cmdarg.add(val);
       }
      
       return cmdarg;
    }
   
    /**
     * @return
     */
    public int getCommandPosition() {
       return commandPosition;
    }

    /**
     * @return
     */
    public String getCommandSwitch() {
       return commandSwitch;
    }

    /**
     * @return
     */
    public String getSwitchType() {
       return switchType;
    }

    /**
     * @param i
     *
     */
    public void setCommandPosition(int i) {
       commandPosition = i;
    }

    /**
     * @param string
     * 
     */
    public void setCommandSwitch(String string) {
       commandSwitch = string;
    }

    /**
     * @param string
     * 
     */
    public void setSwitchType(String string) {
       switchType = string;
    }

   
   /** setter method to set superclasses 'type' field from a string - used within the digester parsing of the configuration file */
    public void setTypeString(String arg0) {
        super.setType(ParameterTypes.valueOf(arg0));
    }

    public boolean isFile() {
        return file;
    } 
    
    public void setFile(boolean isFile) {
        this.file = isFile;
    }

}


/* 
$Log: CommandLineParameterDescription.java,v $
Revision 1.2  2004/07/01 11:07:59  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/07/01 01:43:39  nw
final version, before merge
 
*/