/*
 * $Id: CoordValueType.java,v 1.2 2011/09/13 13:43:24 pah Exp $
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
 * A type that just holds a 1-3D coordinate value; see comment in the CoordValue head element.
 * 
 * <p>Java class for coordValueType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="coordValueType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}CoordValue"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "coordValueType", propOrder = {
    "coordValue"
})
public class CoordValueType {

    @XmlElementRef(name = "CoordValue", namespace = "http://www.ivoa.net/xml/STC/stc-v1.30.xsd", type = JAXBElement.class)
    protected JAXBElement<?> coordValue;

    /**
     * Gets the value of the coordValue property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Double2Type }{@code >}
     *     {@link JAXBElement }{@code <}{@link Curve3Type }{@code >}
     *     {@link JAXBElement }{@code <}{@link Double3Type }{@code >}
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     {@link JAXBElement }{@code <}{@link Curve2Type }{@code >}
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     {@link JAXBElement }{@code <}{@link Double1Type }{@code >}
     *     
     */
    public JAXBElement<?> getCoordValue() {
        return coordValue;
    }

    /**
     * Sets the value of the coordValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Double2Type }{@code >}
     *     {@link JAXBElement }{@code <}{@link Curve3Type }{@code >}
     *     {@link JAXBElement }{@code <}{@link Double3Type }{@code >}
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     {@link JAXBElement }{@code <}{@link Curve2Type }{@code >}
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     {@link JAXBElement }{@code <}{@link Double1Type }{@code >}
     *     
     */
    public void setCoordValue(JAXBElement<?> value) {
        this.coordValue = ((JAXBElement<?> ) value);
    }

}
