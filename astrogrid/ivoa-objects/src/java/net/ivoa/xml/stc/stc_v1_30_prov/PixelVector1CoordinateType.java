/*
 * $Id: PixelVector1CoordinateType.java,v 1.2 2011/09/13 13:43:22 pah Exp $
 * Copyright 2011 Paul Harrison (paul.harrison@manchester.ac.uk)
 *
 * File originally generated by JAXB, but since hand updated
 */


package net.ivoa.xml.stc.stc_v1_30_prov;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Scalar pixel coordinate type.
 * 
 * <p>Java class for pixelVector1CoordinateType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pixelVector1CoordinateType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}coordinateType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}Value" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pixelVector1CoordinateType", propOrder = {
    "value"
})
public class PixelVector1CoordinateType
    extends CoordinateType
{

    @XmlElement(name = "Value", nillable = true)
    protected Double1Type value;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link Double1Type }
     *     
     */
    public Double1Type getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double1Type }
     *     
     */
    public void setValue(Double1Type value) {
        this.value = value;
    }

}