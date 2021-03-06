/*
 * $Id: ObservatoryLocationType.java,v 1.2 2011/09/13 13:43:24 pah Exp $
 * Copyright 2011 Paul Harrison (paul.harrison@manchester.ac.uk)
 *
 * File originally generated by JAXB, but since hand updated
 */


package net.ivoa.xml.stc.stc_v1_30_prov;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Type for an observatory location.
 * 
 * <p>Java class for observatoryLocationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="observatoryLocationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}stcDescriptionType">
 *       &lt;sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}AstroCoordSystem"/>
 *         &lt;/sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}AstroCoords"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "observatoryLocationType")
public class ObservatoryLocationType
    extends StcDescriptionType
{


}
