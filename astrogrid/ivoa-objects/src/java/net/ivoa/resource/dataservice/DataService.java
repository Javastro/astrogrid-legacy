//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.3-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.06.16 at 04:47:20 PM BST 
//


package net.ivoa.resource.dataservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import net.ivoa.resource.ResourceName;
import net.ivoa.resource.Service;


/**
 * 
 *            A service for accessing astronomical data
 *          
 * 
 * <p>Java class for DataService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DataService">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ivoa.net/xml/VOResource/v1.0}Service">
 *       &lt;sequence>
 *         &lt;element name="facility" type="{http://www.ivoa.net/xml/VOResource/v1.0}ResourceName" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="instrument" type="{http://www.ivoa.net/xml/VOResource/v1.0}ResourceName" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="coverage" type="{http://www.ivoa.net/xml/VODataService/v1.1}Coverage" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataService", propOrder = {
    "facility",
    "instrument",
    "coverage"
})
public class DataService
    extends Service
{

    @XmlElement(type = ResourceName.class)
    protected List<ResourceName> facility;
    @XmlElement(type = ResourceName.class)
    protected List<ResourceName> instrument;
    protected Coverage coverage;

    /**
     * Gets the value of the facility property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the facility property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFacility().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceName }
     * 
     * 
     */
    public List<ResourceName> getFacility() {
        if (facility == null) {
            facility = new ArrayList<ResourceName>();
        }
        return this.facility;
    }

    /**
     * Gets the value of the instrument property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the instrument property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInstrument().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceName }
     * 
     * 
     */
    public List<ResourceName> getInstrument() {
        if (instrument == null) {
            instrument = new ArrayList<ResourceName>();
        }
        return this.instrument;
    }

    /**
     * Gets the value of the coverage property.
     * 
     * @return
     *     possible object is
     *     {@link Coverage }
     *     
     */
    public Coverage getCoverage() {
        return coverage;
    }

    /**
     * Sets the value of the coverage property.
     * 
     * @param value
     *     allowed object is
     *     {@link Coverage }
     *     
     */
    public void setCoverage(Coverage value) {
        this.coverage = value;
    }

}