//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.3-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.06.10 at 03:44:35 PM BST 
//


package net.ivoa.resource.sia;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import net.ivoa.resource.Capability;


/**
 * 
 *             See vr:Capability for documentation on inherited children.
 *          
 * 
 * <p>Java class for SIACapRestriction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SIACapRestriction">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.ivoa.net/xml/VOResource/v1.0}Capability">
 *       &lt;sequence>
 *         &lt;element name="validationLevel" type="{http://www.ivoa.net/xml/VOResource/v1.0}Validation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *         &lt;element name="interface" type="{http://www.ivoa.net/xml/VOResource/v1.0}Interface" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="standardID" use="required" type="{http://www.ivoa.net/xml/VOResource/v1.0}IdentifierURI" fixed="ivo://ivoa.net/std/SIA" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SIACapRestriction")
public abstract class SIACapRestriction
    extends Capability
{


}