/*
 * $Id: Curve2Type.java,v 1.2 2011/09/13 13:43:22 pah Exp $
 * Copyright 2011 Paul Harrison (paul.harrison@manchester.ac.uk)
 *
 * File originally generated by JAXB, but since hand updated
 */


package net.ivoa.xml.stc.stc_v1_30_prov;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * A curve in 2-D space, defined by its end points and a shape attribute (default: line or great circle).
 * 
 * <p>Java class for curve2Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="curve2Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}stcBaseType">
 *       &lt;sequence>
 *         &lt;element name="P1" type="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}double2Type"/>
 *         &lt;element name="P2" type="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}double2Type"/>
 *       &lt;/sequence>
 *       &lt;attribute name="curve_shape" type="{http://www.w3.org/2001/XMLSchema}string" default="line" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "curve2Type", propOrder = {
    "p1",
    "p2"
})
public class Curve2Type
    extends StcBaseType
{

    @XmlElement(name = "P1", required = true, nillable = true)
    protected Double2Type p1;
    @XmlElement(name = "P2", required = true, nillable = true)
    protected Double2Type p2;
    @XmlAttribute(name = "curve_shape")
    protected String curveShape;

    /**
     * Gets the value of the p1 property.
     * 
     * @return
     *     possible object is
     *     {@link Double2Type }
     *     
     */
    public Double2Type getP1() {
        return p1;
    }

    /**
     * Sets the value of the p1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double2Type }
     *     
     */
    public void setP1(Double2Type value) {
        this.p1 = value;
    }

    /**
     * Gets the value of the p2 property.
     * 
     * @return
     *     possible object is
     *     {@link Double2Type }
     *     
     */
    public Double2Type getP2() {
        return p2;
    }

    /**
     * Sets the value of the p2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double2Type }
     *     
     */
    public void setP2(Double2Type value) {
        this.p2 = value;
    }

    /**
     * Gets the value of the curveShape property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurveShape() {
        if (curveShape == null) {
            return "line";
        } else {
            return curveShape;
        }
    }

    /**
     * Sets the value of the curveShape property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurveShape(String value) {
        this.curveShape = value;
    }

}
