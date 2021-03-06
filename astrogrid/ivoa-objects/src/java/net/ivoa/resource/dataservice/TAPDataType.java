/*
 * $Id: TAPDataType.java,v 1.2 2011/09/13 13:43:27 pah Exp $
 * Copyright 2011 Paul Harrison (paul.harrison@manchester.ac.uk)
 *
 * File originally generated by JAXB, but since hand updated
 */


package net.ivoa.resource.dataservice;

import java.math.BigInteger;

import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *             an abstract parent for the specific data types supported 
 *             by the Table Access Protocol.
 *          
 * 
 * <p>Java class for TAPDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TAPDataType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.ivoa.net/xml/VODataService/v1.1>TableDataType">
 *       &lt;attribute name="size" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TAPDataType")
public abstract class TAPDataType
    extends TableDataType
{

    @XmlAttribute
    protected BigInteger size;

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSize(BigInteger value) {
        this.size = value;
    }

}
