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
import org.astrogrid.applications.description.base.ApplicationBase;


/**
 * 
 *            A Database Application better to derive from CEAApplication?
 *         
 * 
 * <p>Java class for CeaDBApplicationDefinition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CeaDBApplicationDefinition">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ivoa.net/xml/CEA/base/v1.1}ApplicationBase">
 *       &lt;sequence>
 *         &lt;element name="dbref" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="parMap" type="{http://www.astrogrid.org/schema/CEAImplementation/v2.0}DBParMap"/>
 *         &lt;element name="outMap" type="{http://www.astrogrid.org/schema/CEAImplementation/v2.0}DBOutMap"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CeaDBApplicationDefinition", propOrder = {
    "dbref",
    "parMap",
    "outMap"
})
public class CeaDBApplicationDefinition
    extends ApplicationBase
{

    @XmlElement(required = true)
    protected String dbref;
    @XmlElement(required = true)
    protected DBParMap parMap;
    @XmlElement(required = true)
    protected DBOutMap outMap;

    /**
     * Gets the value of the dbref property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDbref() {
        return dbref;
    }

    /**
     * Sets the value of the dbref property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDbref(String value) {
        this.dbref = value;
    }

    /**
     * Gets the value of the parMap property.
     * 
     * @return
     *     possible object is
     *     {@link DBParMap }
     *     
     */
    public DBParMap getParMap() {
        return parMap;
    }

    /**
     * Sets the value of the parMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link DBParMap }
     *     
     */
    public void setParMap(DBParMap value) {
        this.parMap = value;
    }

    /**
     * Gets the value of the outMap property.
     * 
     * @return
     *     possible object is
     *     {@link DBOutMap }
     *     
     */
    public DBOutMap getOutMap() {
        return outMap;
    }

    /**
     * Sets the value of the outMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link DBOutMap }
     *     
     */
    public void setOutMap(DBOutMap value) {
        this.outMap = value;
    }

}