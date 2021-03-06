/*
 * $Id: GenericRefPosType.java,v 1.2 2011/09/13 13:43:23 pah Exp $
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
 * Type for custom positions: specifies reference origin.
 * 
 * <p>Java class for genericRefPosType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="genericRefPosType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}referencePositionType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}GenCoordinate"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "genericRefPosType", propOrder = {
    "genCoordinate"
})
public class GenericRefPosType
    extends ReferencePositionType
{

    @XmlElementRef(name = "GenCoordinate", namespace = "http://www.ivoa.net/xml/STC/stc-v1.30.xsd", type = JAXBElement.class)
    protected JAXBElement<? extends CoordinateType> genCoordinate;

    /**
     * Gets the value of the genCoordinate property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ScalarCoordinateType }{@code >}
     *     {@link JAXBElement }{@code <}{@link StringCoordinateType }{@code >}
     *     {@link JAXBElement }{@code <}{@link GenVector3CoordinateType }{@code >}
     *     {@link JAXBElement }{@code <}{@link GenVector2CoordinateType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CoordinateType }{@code >}
     *     
     */
    public JAXBElement<? extends CoordinateType> getGenCoordinate() {
        return genCoordinate;
    }

    /**
     * Sets the value of the genCoordinate property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ScalarCoordinateType }{@code >}
     *     {@link JAXBElement }{@code <}{@link StringCoordinateType }{@code >}
     *     {@link JAXBElement }{@code <}{@link GenVector3CoordinateType }{@code >}
     *     {@link JAXBElement }{@code <}{@link GenVector2CoordinateType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CoordinateType }{@code >}
     *     
     */
    public void setGenCoordinate(JAXBElement<? extends CoordinateType> value) {
        this.genCoordinate = ((JAXBElement<? extends CoordinateType> ) value);
    }

}
