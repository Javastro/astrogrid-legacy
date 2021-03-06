/*
 * $Id: DataCollection.java,v 1.2 2011/09/13 13:43:26 pah Exp $
 * Copyright 2011 Paul Harrison (paul.harrison@manchester.ac.uk)
 *
 * File originally generated by JAXB, but since hand updated
 */


package net.ivoa.resource.dataservice;

import static javax.persistence.CascadeType.ALL;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.persistence.annotations.Convert;

import net.ivoa.resource.AccessURL;
import net.ivoa.resource.FacilityName;
import net.ivoa.resource.InstrumentName;
import net.ivoa.resource.Resource;
import net.ivoa.resource.ResourceName;
import net.ivoa.resource.Rights;


/**
 * 
 *            (A dataset is a collection of digitally-encoded data that 
 *            is normally accessible as a single unit, e.g. a file.)
 *          
 * 
 * <p>Java class for DataCollection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DataCollection">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ivoa.net/xml/VOResource/v1.0}Resource">
 *       &lt;sequence>
 *         &lt;element name="facility" type="{http://www.ivoa.net/xml/VOResource/v1.0}ResourceName" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="instrument" type="{http://www.ivoa.net/xml/VOResource/v1.0}ResourceName" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="rights" type="{http://www.ivoa.net/xml/VOResource/v1.0}Rights" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="format" type="{http://www.ivoa.net/xml/VODataService/v1.1}Format" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="coverage" type="{http://www.ivoa.net/xml/VODataService/v1.1}Coverage" minOccurs="0"/>
 *         &lt;element name="tableset" type="{http://www.ivoa.net/xml/VODataService/v1.1}TableSet" minOccurs="0"/>
 *         &lt;element name="accessURL" type="{http://www.ivoa.net/xml/VOResource/v1.0}AccessURL" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataCollection", propOrder = {
    "facility",
    "instrument",
    "rights",
    "format",
    "coverage",
    "tableset",
    "accessURL"
})
public class DataCollection
    extends Resource
{

    @OneToMany(targetEntity = net.ivoa.resource.FacilityName.class, cascade = ALL, orphanRemoval = false)
    @JoinColumn(name="RESOURCE_IDENTIFIER",referencedColumnName = "IDENTIFIER")
    @XmlElement(type = FacilityName.class)
    protected List<FacilityName> facility;
    
    @OneToMany(targetEntity = net.ivoa.resource.InstrumentName.class, cascade = ALL, orphanRemoval = false)
    @JoinColumn(name="RESOURCE_IDENTIFIER",referencedColumnName = "IDENTIFIER")
    @XmlElement(type = InstrumentName.class)
    protected List<InstrumentName> instrument;
    @XmlElement(type = Rights.class, name="rights")
    @Convert("EnumList")
    protected List<Rights> rights;
    
    @OneToMany(cascade = ALL, orphanRemoval = false)
    @JoinColumn(name="RESOURCE_IDENTIFIER",referencedColumnName = "IDENTIFIER")
    @XmlElement(type = Format.class)
    protected List<Format> format;
    
    protected Coverage coverage;
    
    protected TableSet tableset;
    
    protected AccessURL accessURL;

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
    public List<? extends ResourceName> getFacility() {
        if (facility == null) {
            facility = new ArrayList<FacilityName>();
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
    public List<? extends ResourceName> getInstrument() {
        if (instrument == null) {
            instrument = new ArrayList<InstrumentName>();
        }
        return this.instrument;
    }

    /**
     * Gets the value of the rights property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rights property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRights().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Rights }
     * 
     * 
     */
    public List<Rights> getRights() {
        if (rights == null) {
            rights = new ArrayList<Rights>();
        }
        return this.rights;
    }

    /**
     * Gets the value of the format property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the format property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFormat().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Format }
     * 
     * 
     */
    public List<Format> getFormat() {
        if (format == null) {
            format = new ArrayList<Format>();
        }
        return this.format;
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

    /**
     * Gets the value of the tableset property.
     * 
     * @return
     *     possible object is
     *     {@link TableSet }
     *     
     */
    public TableSet getTableset() {
        return tableset;
    }

    /**
     * Sets the value of the tableset property.
     * 
     * @param value
     *     allowed object is
     *     {@link TableSet }
     *     
     */
    public void setTableset(TableSet value) {
        this.tableset = value;
    }

    /**
     * Gets the value of the accessURL property.
     * 
     * @return
     *     possible object is
     *     {@link AccessURL }
     *     
     */
    public AccessURL getAccessURL() {
        return accessURL;
    }

    /**
     * Sets the value of the accessURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessURL }
     *     
     */
    public void setAccessURL(AccessURL value) {
        this.accessURL = value;
    }

}
