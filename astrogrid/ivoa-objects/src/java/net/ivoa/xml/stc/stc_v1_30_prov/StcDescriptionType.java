/*
 * $Id: StcDescriptionType.java,v 1.2 2011/09/13 13:43:25 pah Exp $
 * Copyright 2011 Paul Harrison (paul.harrison@manchester.ac.uk)
 *
 * File originally generated by JAXB, but since hand updated
 */


package net.ivoa.xml.stc.stc_v1_30_prov;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * Generalized single stcMetadata type.
 * 
 * <p>Java class for stcDescriptionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="stcDescriptionType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}stcMetadataType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}CoordSys" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}Coords" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}CoordArea" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stcDescriptionType", propOrder = {
    "coordSys",
    "coords",
    "coordArea"
})
public class StcDescriptionType
    extends StcMetadataType
{

    @XmlElementRef(name = "CoordSys", namespace = "http://www.ivoa.net/xml/STC/stc-v1.30.xsd", type = JAXBElement.class)
    protected List<JAXBElement<? extends CoordSysType>> coordSys;
    @XmlElementRef(name = "Coords", namespace = "http://www.ivoa.net/xml/STC/stc-v1.30.xsd", type = JAXBElement.class)
    protected List<JAXBElement<? extends CoordsType>> coords;
    @XmlElementRef(name = "CoordArea", namespace = "http://www.ivoa.net/xml/STC/stc-v1.30.xsd", type = JAXBElement.class)
    protected List<JAXBElement<? extends CoordAreaType>> coordArea;

    /**
     * Gets the value of the coordSys property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the coordSys property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCoordSys().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link CoordSysType }{@code >}
     * {@link JAXBElement }{@code <}{@link AstroCoordSystemType }{@code >}
     * {@link JAXBElement }{@code <}{@link PixelCoordSystemType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends CoordSysType>> getCoordSys() {
        if (coordSys == null) {
            coordSys = new ArrayList<JAXBElement<? extends CoordSysType>>();
        }
        return this.coordSys;
    }

    /**
     * Gets the value of the coords property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the coords property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCoords().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link AstroCoordsType }{@code >}
     * {@link JAXBElement }{@code <}{@link CoordsType }{@code >}
     * {@link JAXBElement }{@code <}{@link PixelCoordsType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends CoordsType>> getCoords() {
        if (coords == null) {
            coords = new ArrayList<JAXBElement<? extends CoordsType>>();
        }
        return this.coords;
    }

    /**
     * Gets the value of the coordArea property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the coordArea property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCoordArea().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link AstroCoordAreaType }{@code >}
     * {@link JAXBElement }{@code <}{@link CoordAreaType }{@code >}
     * {@link JAXBElement }{@code <}{@link PixelCoordAreaType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends CoordAreaType>> getCoordArea() {
        if (coordArea == null) {
            coordArea = new ArrayList<JAXBElement<? extends CoordAreaType>>();
        }
        return this.coordArea;
    }

}