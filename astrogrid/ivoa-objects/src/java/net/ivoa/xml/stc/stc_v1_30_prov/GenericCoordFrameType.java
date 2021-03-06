/*
 * $Id: GenericCoordFrameType.java,v 1.2 2011/09/13 13:43:22 pah Exp $
 * Copyright 2011 Paul Harrison (paul.harrison@manchester.ac.uk)
 *
 * File originally generated by JAXB, but since hand updated
 */


package net.ivoa.xml.stc.stc_v1_30_prov;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for genericCoordFrameType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="genericCoordFrameType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}coordFrameType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}CoordRefFrame" minOccurs="0"/>
 *         &lt;element ref="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}CoordRefPos" minOccurs="0"/>
 *         &lt;element ref="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}CoordFlavor"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "genericCoordFrameType", propOrder = {
    "coordRefFrame",
    "coordRefPos",
    "coordFlavor"
})
public class GenericCoordFrameType
    extends CoordFrameType
{

    @XmlElementRef(name = "CoordRefFrame", namespace = "http://www.ivoa.net/xml/STC/stc-v1.30.xsd", type = JAXBElement.class)
    protected JAXBElement<? extends CoordRefFrameType> coordRefFrame;
    @XmlElement(name = "CoordRefPos")
    protected CustomRefPosType coordRefPos;
    @XmlElementRef(name = "CoordFlavor", namespace = "http://www.ivoa.net/xml/STC/stc-v1.30.xsd", type = JAXBElement.class)
    protected JAXBElement<? extends CoordFlavorType> coordFlavor;

    /**
     * Gets the value of the coordRefFrame property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Cart1DRefFrameType }{@code >}
     *     {@link JAXBElement }{@code <}{@link Cart2DRefFrameType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CoordRefFrameType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SphericalRefFrameType }{@code >}
     *     {@link JAXBElement }{@code <}{@link Cart3DRefFrameType }{@code >}
     *     
     */
    public JAXBElement<? extends CoordRefFrameType> getCoordRefFrame() {
        return coordRefFrame;
    }

    /**
     * Sets the value of the coordRefFrame property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Cart1DRefFrameType }{@code >}
     *     {@link JAXBElement }{@code <}{@link Cart2DRefFrameType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CoordRefFrameType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SphericalRefFrameType }{@code >}
     *     {@link JAXBElement }{@code <}{@link Cart3DRefFrameType }{@code >}
     *     
     */
    public void setCoordRefFrame(JAXBElement<? extends CoordRefFrameType> value) {
        this.coordRefFrame = ((JAXBElement<? extends CoordRefFrameType> ) value);
    }

    /**
     * Gets the value of the coordRefPos property.
     * 
     * @return
     *     possible object is
     *     {@link CustomRefPosType }
     *     
     */
    public CustomRefPosType getCoordRefPos() {
        return coordRefPos;
    }

    /**
     * Sets the value of the coordRefPos property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomRefPosType }
     *     
     */
    public void setCoordRefPos(CustomRefPosType value) {
        this.coordRefPos = value;
    }

    /**
     * Provides the coordinate definitions: number of axes, SPHERICAL, CARTESIAN, UNITSPHERE, POLAR, or HEALPIX, presence of velocities.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CoordFlavorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CoordFlavorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CoordFlavorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CoordFlavorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CoordFlavorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link HealpixType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CoordFlavorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CoordFlavorType }{@code >}
     *     
     */
    public JAXBElement<? extends CoordFlavorType> getCoordFlavor() {
        return coordFlavor;
    }

    /**
     * Provides the coordinate definitions: number of axes, SPHERICAL, CARTESIAN, UNITSPHERE, POLAR, or HEALPIX, presence of velocities.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CoordFlavorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CoordFlavorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CoordFlavorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CoordFlavorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CoordFlavorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link HealpixType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CoordFlavorType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CoordFlavorType }{@code >}
     *     
     */
    public void setCoordFlavor(JAXBElement<? extends CoordFlavorType> value) {
        this.coordFlavor = ((JAXBElement<? extends CoordFlavorType> ) value);
    }

}
