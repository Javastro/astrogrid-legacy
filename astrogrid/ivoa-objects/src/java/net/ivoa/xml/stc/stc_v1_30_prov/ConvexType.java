/*
 * $Id: ConvexType.java,v 1.2 2011/09/13 13:43:25 pah Exp $
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
 * A convex polygon defined by one or more Constraints.
 * 
 * <p>Java class for convexType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="convexType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}shapeType">
 *       &lt;sequence>
 *         &lt;element name="Halfspace" type="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}halfspaceType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "convexType", propOrder = {
    "halfspace"
})
public class ConvexType
    extends ShapeType
{

    @XmlElement(name = "Halfspace", required = true, type = HalfspaceType.class)
    protected List<HalfspaceType> halfspace;

    /**
     * Gets the value of the halfspace property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the halfspace property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHalfspace().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HalfspaceType }
     * 
     * 
     */
    public List<HalfspaceType> getHalfspace() {
        if (halfspace == null) {
            halfspace = new ArrayList<HalfspaceType>();
        }
        return this.halfspace;
    }

}
