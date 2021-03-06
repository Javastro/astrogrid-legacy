/*
 * $Id: SectorType.java,v 1.2 2011/09/13 13:43:24 pah Exp $
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
 * A sector is the counter-clockwise area between two half-lines.
 * 
 * <p>Java class for sectorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sectorType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}shapeType">
 *       &lt;sequence>
 *         &lt;element name="Position" type="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}double2Type"/>
 *         &lt;element name="PosAngle1" type="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}posAngleType"/>
 *         &lt;element name="PosAngle2" type="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}posAngleType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sectorType", propOrder = {
    "position",
    "posAngle1",
    "posAngle2"
})
public class SectorType
    extends ShapeType
{

    @XmlElement(name = "Position", required = true)
    protected Double2Type position;
    @XmlElement(name = "PosAngle1", required = true)
    protected PosAngleType posAngle1;
    @XmlElement(name = "PosAngle2", required = true)
    protected PosAngleType posAngle2;

    /**
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link Double2Type }
     *     
     */
    public Double2Type getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double2Type }
     *     
     */
    public void setPosition(Double2Type value) {
        this.position = value;
    }

    /**
     * Gets the value of the posAngle1 property.
     * 
     * @return
     *     possible object is
     *     {@link PosAngleType }
     *     
     */
    public PosAngleType getPosAngle1() {
        return posAngle1;
    }

    /**
     * Sets the value of the posAngle1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link PosAngleType }
     *     
     */
    public void setPosAngle1(PosAngleType value) {
        this.posAngle1 = value;
    }

    /**
     * Gets the value of the posAngle2 property.
     * 
     * @return
     *     possible object is
     *     {@link PosAngleType }
     *     
     */
    public PosAngleType getPosAngle2() {
        return posAngle2;
    }

    /**
     * Sets the value of the posAngle2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link PosAngleType }
     *     
     */
    public void setPosAngle2(PosAngleType value) {
        this.posAngle2 = value;
    }

}
