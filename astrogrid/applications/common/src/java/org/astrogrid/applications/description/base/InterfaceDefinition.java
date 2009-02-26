/*
 * $Id: InterfaceDefinition.java,v 1.1 2009/02/26 12:25:47 pah Exp $
 * 
 * Created on 10 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 


package org.astrogrid.applications.description.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.astrogrid.applications.description.ApplicationDefinition;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.Cardinality;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.ParameterDirection;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;


/**
 * 
 *             description of an interface
 *          
 * 
 * <p>Java class for InterfaceDefinition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InterfaceDefinition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="constants" type="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}InterfaceConstants" minOccurs="0"/>
 *         &lt;element name="input" type="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}InputParameterReferenceSpecification"/>
 *         &lt;element name="output" type="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}OutputParameterReferenceSpecification"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Mar 2008
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InterfaceDefinition", propOrder = {
    "constants",
    "input",
    "output",
    "description"
})
public class InterfaceDefinition implements ApplicationInterface {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
	    .getLog(InterfaceDefinition.class);

    protected InterfaceConstants constants;
    @XmlElement(required = true)
    protected InputParameterReferenceSpecification input;
    @XmlElement(required = true)
    protected OutputParameterReferenceSpecification output;
    protected String description;
    @XmlAttribute(required = true)
    protected String id;
    @XmlAttribute
    protected String name;

    @XmlTransient
  /** back link to the application description */ //TODO would be nice to have this final - look at getting context from jaxb...
    private  ApplicationDefinition applicationDefinition; 

   public InterfaceDefinition(String id, ApplicationDefinition description){
       this();
       this.id = id;
       this.applicationDefinition = description;
       
   }
   
   /**
    * constructor that is used by JAXB. Protected to stop use in java programmed descriptions.
 * 
 */
public InterfaceDefinition(){
       this.input = new InputParameterReferenceSpecification();
       this.output = new OutputParameterReferenceSpecification();
   }
    
    /**
     * Gets the value of the constants property.
     * 
     * @return
     *     possible object is
     *     {@link InterfaceConstants }
     *     
     */
    public InterfaceConstants getConstants() {
        return constants;
    }

    /**
     * Sets the value of the constants property.
     * 
     * @param value
     *     allowed object is
     *     {@link InterfaceConstants }
     *     
     */
    public void setConstants(InterfaceConstants value) {
        this.constants = value;
    }

    /**
     * Gets the value of the input property.
     * 
     * @return
     *     possible object is
     *     {@link InputParameterReferenceSpecification }
     *     
     */
    public InputParameterReferenceSpecification getInput() {
        return input;
    }

    /**
     * Sets the value of the input property.
     * 
     * @param value
     *     allowed object is
     *     {@link InputParameterReferenceSpecification }
     *     
     */
    public void setInput(InputParameterReferenceSpecification value) {
        this.input = value;
    }

    /**
     * Gets the value of the output property.
     * 
     * @return
     *     possible object is
     *     {@link OutputParameterReferenceSpecification }
     *     
     */
    public OutputParameterReferenceSpecification getOutput() {
        return output;
    }

    /**
     * Sets the value of the output property.
     * 
     * @param value
     *     allowed object is
     *     {@link OutputParameterReferenceSpecification }
     *     
     */
    public void setOutput(OutputParameterReferenceSpecification value) {
        this.output = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    
    /** add input parameter to the interface.
     * 
     * @param parameterName name of the parameter
     * @throws ParameterDescriptionNotFoundException if parameter name is not already defined in the applicationDescription this
     * interface is owned by.
     */
       public void addInputParameter(String parameterName)
          throws ParameterDescriptionNotFoundException {
           this.addInputParameter(parameterName,Cardinality.MANDATORY);
       }
       
       public void addInputParameter(String parameterName,Cardinality card)
       {
          getInput().addPref(new ParameterRef(parameterName,card));

       }
     
       public void addInputParameter(String parameterName,int minoccurs, int maxoccurs)
        {
	   addInputParameter(parameterName, new Cardinality(minoccurs,maxoccurs));

       }
       public void addOutputParameter(String parameterName)
          {
           this.addOutputParameter(parameterName,Cardinality.MANDATORY);
       }
       public void addOutputParameter(String parameterName,Cardinality card)
       {
	   getOutput().getPref().add(new ParameterRef(parameterName, card));
       }
       
       public void addOutputParameter(String parameterName,int minoccurs, int maxoccurs)
        {   
          this.addOutputParameter(parameterName, new Cardinality(minoccurs, maxoccurs));
 
       }
       
       
       

       public ParameterDescription getInputParameter(String parameterName)
          throws ParameterNotInInterfaceException {
	  assert applicationDefinition != null : "programming error cannot get parameter from interface before ApplicationDescription created ";

          ParameterDescription ad = null;
          if (getInput().contains(parameterName)) {
             try {
                ad = applicationDefinition.getParameterDescription(parameterName);
             }
             catch (ParameterDescriptionNotFoundException e) {
                logger.error(
                   "this should not happen - the checks on the original storage of the parameters should prevent it - internal program error",
                   e);
             }
          }
          else {
             throw new ParameterNotInInterfaceException("unknown parameter="+parameterName);
          }
          return ad;
       }
       public ParameterDirection getParameterDirection(String parameterName)
       {
          ParameterDirection retval = ParameterDirection.NOTFOUND;
          if(getInput().contains(parameterName))
          {
             retval = ParameterDirection.INPUT;
          }
          if (getOutput().contains(parameterName)) {
             retval = ParameterDirection.OUTPUT;
          }
          return retval;
       }
       public ParameterDescription getOutputParameter(String parameterName)
           throws ParameterNotInInterfaceException {
           assert applicationDefinition != null : "programming error cannot get parameter from interface before ApplicationDescription created ";

           ParameterDescription ad = null;
           if (getOutput().contains(parameterName)) {
              try {
                 ad = applicationDefinition.getParameterDescription(parameterName);
              }
              catch (ParameterDescriptionNotFoundException e) {
                 logger.error(
                    "this should not happen - the checks on the original storage of the parameters should prevent it - internal program error",
                    e);
              }
           }
           else {
              throw new ParameterNotInInterfaceException("unknown parameter="+parameterName);
           }
           return ad;
        }
        



        /**
         * @see org.astrogrid.applications.description.ApplicationInterface#getParameterCardinality(java.lang.String)
         */
        public Cardinality getParameterCardinality(String name) throws ParameterNotInInterfaceException {
            if (getOutput().contains(name)) {
        	ParameterRef pref = getOutput().get(name);
                return new Cardinality(pref.getMinOccurs(), pref.getMaxOccurs());
            } else if (getInput().contains(name)) {
                ParameterRef pref = getInput().get(name);
                return new Cardinality(pref.getMinOccurs(), pref.getMaxOccurs());
               
            } else {
                throw new ParameterNotInInterfaceException(name);
            }
            
        }

        @Override
	public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append("[ApplicationInterface:");
            buffer.append(" id: ");
            buffer.append(id);
            buffer.append(" name: ");
            buffer.append(name);
            buffer.append("\n\t applicationDescription: ");
            buffer.append(applicationDefinition.getId());

            buffer.append("\n\t inputs: ");
            buffer.append(getInput().toString());
            buffer.append("\n\t outputs: ");
            buffer.append(getOutput().toString());
            buffer.append("\n]");
            return buffer.toString();
        }
    
    
    public void addInput(String string) {
	getInput().getPrefOrPgroupOrCgroupHead().add(new ParameterRef(string));
   }

    public void addOutput(String string) {
	getOutput().getPref().add(new ParameterRef(string));
    }
    public ApplicationDefinition getApplicationDescription() {
        return applicationDefinition;
    }
    public void setApplicationDescription(
	    ApplicationDefinition applicationDefinition) {
        this.applicationDefinition = applicationDefinition;
    }
    public String[] getArrayofInputs() {
	List inputs = new ArrayList();
	
	for (Iterator iterator = input.prefOrPgroupOrCgroupHead.iterator(); iterator.hasNext();) {
	    Object object = (Object) iterator.next();
	    if (object instanceof ParameterRef) {
		ParameterRef pref = (ParameterRef) object;
		inputs.add(pref.getId());
	    }
	}
	return (String[]) inputs.toArray(new String[0]);
    }
    public String[] getArrayofOutputs() {
	List<String> outputs = new ArrayList<String>();
	for (ParameterRef pref : output.pref) {
	    outputs.add(pref.getId());
	}
	return (String[]) outputs.toArray(new String[0]);
   }

}
