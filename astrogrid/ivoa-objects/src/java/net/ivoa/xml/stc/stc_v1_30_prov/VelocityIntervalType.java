/*
 * $Id: VelocityIntervalType.java,v 1.2 2011/09/13 13:43:23 pah Exp $
 * Copyright 2011 Paul Harrison (paul.harrison@manchester.ac.uk)
 *
 * File originally generated by JAXB, but since hand updated
 */


package net.ivoa.xml.stc.stc_v1_30_prov;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * Contains a spatial velocity CoordInterval.
 * 
 * <p>Java class for velocityIntervalType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="velocityIntervalType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}spatialIntervalType">
 *       &lt;attribute name="vel_time_unit" use="required" type="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}velTimeUnitType" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "velocityIntervalType")
public abstract class VelocityIntervalType
    extends SpatialIntervalType
{

    @XmlAttribute(name = "vel_time_unit", required = true)
    protected VelTimeUnitType velTimeUnit;

    /**
     * Gets the value of the velTimeUnit property.
     * 
     * @return
     *     possible object is
     *     {@link VelTimeUnitType }
     *     
     */
    public VelTimeUnitType getVelTimeUnit() {
        return velTimeUnit;
    }

    /**
     * Sets the value of the velTimeUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link VelTimeUnitType }
     *     
     */
    public void setVelTimeUnit(VelTimeUnitType value) {
        this.velTimeUnit = value;
    }

}