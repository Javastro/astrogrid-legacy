/*
 * $Id: ConvexHullType.java,v 1.2 2011/09/13 13:43:26 pah Exp $
 * Copyright 2011 Paul Harrison (paul.harrison@manchester.ac.uk)
 *
 * File originally generated by JAXB, but since hand updated
 */


package net.ivoa.xml.stc.stc_v1_30_prov;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * A convex hull: the smallest convex polygon that contains all its points; in spherical coordinates all points have to be contained within a hemisphere.
 * 
 * <p>Java class for convexHullType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="convexHullType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}shapeType">
 *       &lt;sequence>
 *         &lt;element name="Point" type="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}double3Type" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "convexHullType", propOrder = {
    "point"
})
public class ConvexHullType
    extends ShapeType
{

    @XmlElement(name = "Point", required = true, type = Double3Type.class, nillable = true)
    protected List<Double3Type> point;

    /**
     * Gets the value of the point property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the point property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double3Type }
     * 
     * 
     */
    public List<Double3Type> getPoint() {
        if (point == null) {
            point = new ArrayList<Double3Type>();
        }
        return this.point;
    }

}
