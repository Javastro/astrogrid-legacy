/*
 * $Id: Coord3VecIntervalType.java,v 1.2 2011/09/13 13:43:22 pah Exp $
 * Copyright 2011 Paul Harrison (paul.harrison@manchester.ac.uk)
 *
 * File originally generated by JAXB, but since hand updated
 */


package net.ivoa.xml.stc.stc_v1_30_prov;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 *  3-D coordinate interval type.
 * 
 * <p>Java class for coord3VecIntervalType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="coord3VecIntervalType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}coordIntervalType">
 *       &lt;sequence>
 *         &lt;element name="LoLimit3Vec" type="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}double3Type" minOccurs="0"/>
 *         &lt;element name="HiLimit3Vec" type="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}double3Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "coord3VecIntervalType", propOrder = {
    "loLimit3Vec",
    "hiLimit3Vec"
})
public class Coord3VecIntervalType
    extends CoordIntervalType
{

    @XmlElementRef(name = "LoLimit3Vec", namespace = "http://www.ivoa.net/xml/STC/stc-v1.30.xsd", type = JAXBElement.class)
    protected JAXBElement<Double3Type> loLimit3Vec;
    @XmlElementRef(name = "HiLimit3Vec", namespace = "http://www.ivoa.net/xml/STC/stc-v1.30.xsd", type = JAXBElement.class)
    protected JAXBElement<Double3Type> hiLimit3Vec;

    /**
     * Gets the value of the loLimit3Vec property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Double3Type }{@code >}
     *     
     */
    public JAXBElement<Double3Type> getLoLimit3Vec() {
        return loLimit3Vec;
    }

    /**
     * Sets the value of the loLimit3Vec property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Double3Type }{@code >}
     *     
     */
    public void setLoLimit3Vec(JAXBElement<Double3Type> value) {
        this.loLimit3Vec = ((JAXBElement<Double3Type> ) value);
    }

    /**
     * Gets the value of the hiLimit3Vec property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Double3Type }{@code >}
     *     
     */
    public JAXBElement<Double3Type> getHiLimit3Vec() {
        return hiLimit3Vec;
    }

    /**
     * Sets the value of the hiLimit3Vec property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Double3Type }{@code >}
     *     
     */
    public void setHiLimit3Vec(JAXBElement<Double3Type> value) {
        this.hiLimit3Vec = ((JAXBElement<Double3Type> ) value);
    }

}
