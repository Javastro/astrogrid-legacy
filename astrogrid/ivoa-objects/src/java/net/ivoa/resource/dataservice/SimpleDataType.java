//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.3-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.06.16 at 04:47:20 PM BST 
//


package net.ivoa.resource.dataservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 *             a simple data type that does not imply a size nor precise format.
 *          
 * 
 * <p>Java class for SimpleDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SimpleDataType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.ivoa.net/xml/VODataService/v1.1>SimpleScalarDataType">
 *       &lt;attribute name="arraysize" type="{http://www.ivoa.net/xml/VODataService/v1.1}ArrayShape" default="1" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SimpleDataType", propOrder = {
    "value"
})
public class SimpleDataType {

    @XmlValue
    protected SimpleScalarDataType value;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String arraysize;

    /**
     * 
     *             This set is intended for describing simple input parameters to 
     *             a service or function
     *          
     * 
     * @return
     *     possible object is
     *     {@link SimpleScalarDataType }
     *     
     */
    public SimpleScalarDataType getValue() {
        return value;
    }

    /**
     * 
     *             This set is intended for describing simple input parameters to 
     *             a service or function
     *          
     * 
     * @param value
     *     allowed object is
     *     {@link SimpleScalarDataType }
     *     
     */
    public void setValue(SimpleScalarDataType value) {
        this.value = value;
    }

    /**
     * Gets the value of the arraysize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArraysize() {
        if (arraysize == null) {
            return "1";
        } else {
            return arraysize;
        }
    }

    /**
     * Sets the value of the arraysize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArraysize(String value) {
        this.arraysize = value;
    }

}