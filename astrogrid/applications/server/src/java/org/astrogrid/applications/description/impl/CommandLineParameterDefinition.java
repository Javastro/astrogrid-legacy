//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.3-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.06.18 at 08:28:07 PM BST 
//


package org.astrogrid.applications.description.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.astrogrid.applications.description.base.BaseParameterDefinition;


/**
 * 
 *             implmentation note - it would probably be better to
 *             implement most of the properties that are attributes as
 *             elements, as there are relationships between the attributes
 *             that are not properly expressed in this type.
 *          
 * 
 * <p>Java class for CommandLineParameterDefinition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CommandLineParameterDefinition">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ivoa.net/xml/CEA/base/v1.1}BaseParameterDefinition">
 *       &lt;attribute name="commandPosition" type="{http://www.w3.org/2001/XMLSchema}int" default="-1" />
 *       &lt;attribute name="commandSwitch" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="fileRef" type="{http://www.astrogrid.org/schema/CEAImplementation/v2.0}FileRefTypes" default="file" />
 *       &lt;attribute name="isStreamable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="localFileName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="switchType" type="{http://www.astrogrid.org/schema/CEAImplementation/v2.0}SwitchTypes" default="normal" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommandLineParameterDefinition")
public class CommandLineParameterDefinition
    extends BaseParameterDefinition
{

    //IMPL certain fields below have been set to default values - this will probably lead to them being written out again in the XML which would mean that roundtrippin would not produce exactly the same results, but does lead to functionally unambiguous instance docs - this is not all expressed in the schema 
    @XmlAttribute
    protected Integer commandPosition = -1;
    @XmlAttribute
    protected String commandSwitch;
    @XmlAttribute
    protected FileRefTypes fileRef = FileRefTypes.NO;
    @XmlAttribute
    protected Boolean isStreamable = false;
    @XmlAttribute
    protected String localFileName;
    @XmlAttribute
    protected SwitchTypes switchType = SwitchTypes.NORMAL;

    /**
     * Gets the value of the commandPosition property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getCommandPosition() {
        if (commandPosition == null) {
            return -1;
        } else {
            return commandPosition;
        }
    }

    /**
     * Sets the value of the commandPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCommandPosition(Integer value) {
        this.commandPosition = value;
    }

    /**
     * Gets the value of the commandSwitch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommandSwitch() {
        return commandSwitch;
    }

    /**
     * Sets the value of the commandSwitch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommandSwitch(String value) {
        this.commandSwitch = value;
    }

    /**
     * Gets the value of the fileRef property.
     * 
     * @return
     *     possible object is
     *     {@link FileRefTypes }
     *     
     */
    public FileRefTypes getFileRef() {
        if (fileRef == null) {
            return FileRefTypes.FILE;
        } else {
            return fileRef;
        }
    }

    /**
     * Sets the value of the fileRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link FileRefTypes }
     *     
     */
    public void setFileRef(FileRefTypes value) {
        this.fileRef = value;
    }

    /**
     * Gets the value of the isStreamable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIsStreamable() {
        if (isStreamable == null) {
            return false;
        } else {
            return isStreamable;
        }
    }

    /**
     * Sets the value of the isStreamable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsStreamable(Boolean value) {
        this.isStreamable = value;
    }

    /**
     * Gets the value of the localFileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalFileName() {
        return localFileName;
    }

    /**
     * Sets the value of the localFileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalFileName(String value) {
        this.localFileName = value;
    }

    /**
     * Gets the value of the switchType property.
     * 
     * @return
     *     possible object is
     *     {@link SwitchTypes }
     *     
     */
    public SwitchTypes getSwitchType() {
        if (switchType == null) {
            return SwitchTypes.NORMAL;
        } else {
            return switchType;
        }
    }

    /**
     * Sets the value of the switchType property.
     * 
     * @param value
     *     allowed object is
     *     {@link SwitchTypes }
     *     
     */
    public void setSwitchType(SwitchTypes value) {
        this.switchType = value;
    }

    /**
     * Adds any necessary switches to the commandline parameter. This is controlled by the @link #commandPosition, @link #commandSwitch and @link #switchType fields. 
     * If the commandPosition is anything other than -1 then no adornment is added. If a switch string is to be added then the style is controlled by switchType and the
     * string for the switch is given by commandSwitch, or if that is null the parameter name is used. 
     * @param val
     * @return stringbuffer containing original value, plus any required adornments.
     */
    public List<String> addCmdlineAdornment(String val)
    {
       List<String> cmdarg = new ArrayList<String>();
    
       if (commandPosition == -1) {
          // if not a command position type parameter then we need to add a switch
          String sw = id;
          if(commandSwitch != null)
          {
             sw = commandSwitch;
          }
          
          if (!sw.equals("---")) //TODO perhaps do this in a nicer way in the schema
          {
            if (switchType.equals(SwitchTypes.NORMAL)) {
               cmdarg.add("-" + sw);
               cmdarg.add(val);
    
            }
            else if (switchType.equals(SwitchTypes.KEYWORD)) {
               cmdarg.add(sw + "=" + val);
            }
         }
         else
         {
          // do nothing - do not put the parameter on the commandline if the switch name is ---
         }
       }
       else
       {
          cmdarg.add(val);
       }
    
       return cmdarg;
    }

    /**
     * is this parameter a file reference?
     * @deprecated this should probably not be used any more, as now the file reference can be to a directory as well.
     * @return
     */
    public boolean isFileRef() {
        return ! fileRef.equals(FileRefTypes.NO);
    }
    
    

}