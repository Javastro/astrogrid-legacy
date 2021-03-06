/*
 * $Id: CoordinateType.java,v 1.2 2011/09/13 13:43:21 pah Exp $
 * Copyright 2011 Paul Harrison (paul.harrison@manchester.ac.uk)
 *
 * File originally generated by JAXB, but since hand updated
 */


package net.ivoa.xml.stc.stc_v1_30_prov;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlType;


/**
 * Abstract coordinate type; a concrete Coordinate consists of a Value, Error, Resolution, Size, and PixSize.
 * 
 * <p>Java class for coordinateType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="coordinateType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}stcBaseType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="frame_id" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "coordinateType", propOrder = {
    "name"
})
public class CoordinateType
    extends StcBaseType
{

    @XmlElement(name = "Name")
    protected String name;
    @XmlAttribute(name = "frame_id")
    @XmlIDREF
    protected Object frameId;

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

    /**
     * Gets the value of the frameId property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getFrameId() {
        return frameId;
    }

    /**
     * Sets the value of the frameId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setFrameId(Object value) {
        this.frameId = value;
    }

}
