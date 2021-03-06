/*
 * $Id: RegCapRestriction.java,v 1.2 2011/09/13 13:43:28 pah Exp $
 * Copyright 2011 Paul Harrison (paul.harrison@manchester.ac.uk)
 *
 * File originally generated by JAXB, but since hand updated
 */


package net.ivoa.resource.registry;

import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import net.ivoa.resource.Capability;


/**
 * 
 *             See vr:Capability for documentation on inherited children.
 *          
 * 
 * <p>Java class for RegCapRestriction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegCapRestriction">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.ivoa.net/xml/VOResource/v1.0}Capability">
 *       &lt;sequence>
 *         &lt;element name="validationLevel" type="{http://www.ivoa.net/xml/VOResource/v1.0}Validation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *         &lt;element name="interface" type="{http://www.ivoa.net/xml/VOResource/v1.0}Interface" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="standardID" use="required" type="{http://www.ivoa.net/xml/VOResource/v1.0}IdentifierURI" fixed="ivo://ivoa.net/std/Registry" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegCapRestriction")
public abstract class RegCapRestriction
    extends Capability
{


}
