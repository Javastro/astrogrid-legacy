//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.3-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.06.18 at 08:28:07 PM BST 
//


package org.astrogrid.applications.description.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *             Description of an HTTP Application
 *          
 * 
 * <p>Java class for WebHttpApplicationSetup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WebHttpApplicationSetup">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.astrogrid.org/schema/CEAImplementation/v2.0}ProxyApplicationSetup">
 *       &lt;sequence>
 *         &lt;element name="PreProcessScript" type="{http://www.astrogrid.org/schema/CEAImplementation/v2.0}PreProcessScript" minOccurs="0"/>
 *         &lt;element name="PostProcessScript" type="{http://www.astrogrid.org/schema/CEAImplementation/v2.0}PostProcessScript" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WebHttpApplicationSetup", propOrder = {
    "preProcessScript",
    "postProcessScript"
})
public class WebHttpApplicationSetup
    extends ProxyApplicationSetup
{

    @XmlElement(name = "PreProcessScript")
    protected PreProcessScript preProcessScript;
    @XmlElement(name = "PostProcessScript")
    protected PostProcessScript postProcessScript;

    /**
     * Gets the value of the preProcessScript property.
     * 
     * @return
     *     possible object is
     *     {@link PreProcessScript }
     *     
     */
    public PreProcessScript getPreProcessScript() {
        return preProcessScript;
    }

    /**
     * Sets the value of the preProcessScript property.
     * 
     * @param value
     *     allowed object is
     *     {@link PreProcessScript }
     *     
     */
    public void setPreProcessScript(PreProcessScript value) {
        this.preProcessScript = value;
    }

    /**
     * Gets the value of the postProcessScript property.
     * 
     * @return
     *     possible object is
     *     {@link PostProcessScript }
     *     
     */
    public PostProcessScript getPostProcessScript() {
        return postProcessScript;
    }

    /**
     * Sets the value of the postProcessScript property.
     * 
     * @param value
     *     allowed object is
     *     {@link PostProcessScript }
     *     
     */
    public void setPostProcessScript(PostProcessScript value) {
        this.postProcessScript = value;
    }

}