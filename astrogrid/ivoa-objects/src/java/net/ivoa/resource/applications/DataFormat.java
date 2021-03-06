/*
 * $Id: DataFormat.java,v 1.2 2011/09/13 13:43:32 pah Exp $
 * Copyright 2011 Paul Harrison (paul.harrison@manchester.ac.uk)
 *
 * File originally generated by JAXB, but since hand updated
 */


package net.ivoa.resource.applications;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *             Data File Format. This should be a uri reference to a value
 *             from the ivo://net.ivoa.application/formats resource. Editor
 *             note - it would really be nice to have a connection with
 *             mime-types here rather/as well as the standard reference
 *             mechanism
 *          
 * 
 * <p>Java class for DataFormat complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DataFormat">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="direction" use="required" type="{http://www.ivoa.net/xml/VOApplication/v1.0rc1}DataFormatDirection" />
 *       &lt;attribute name="standardID" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataFormat")
public class DataFormat {

    @XmlAttribute(required = true)
    protected DataFormatDirection direction;
    @XmlAttribute(required = true)
    protected String standardID;

    /**
     * Gets the value of the direction property.
     * 
     * @return
     *     possible object is
     *     {@link DataFormatDirection }
     *     
     */
    public DataFormatDirection getDirection() {
        return direction;
    }

    /**
     * Sets the value of the direction property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataFormatDirection }
     *     
     */
    public void setDirection(DataFormatDirection value) {
        this.direction = value;
    }

    /**
     * Gets the value of the standardID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStandardID() {
        return standardID;
    }

    /**
     * Sets the value of the standardID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStandardID(String value) {
        this.standardID = value;
    }

}
