//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.3-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.03.11 at 03:35:57 PM GMT 
//


package org.astrogrid.applications.description.cea;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.astrogrid.applications.description.base.ApplicationBase;


/**
 * 
 *               A derived application definition that has the following
 *               semantics; the parameter and interface definition lists
 *               are assumed extend the existing lists that are defined in
 *               the CeaApplication that is pointed to by the extends
 *               attribute. Redefinition or deletion of existing parameter
 *               and interface definions is not allowed.
 *               
 *               
 *            
 * 
 * <p>Java class for DerivedApplicationDefinition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DerivedApplicationDefinition">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}ApplicationBase">
 *       &lt;attribute name="extends" type="{http://www.ivoa.net/xml/VOResource/v1.0}IdentifierURI" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DerivedApplicationDefinition")
public class DerivedApplicationDefinition
    extends ApplicationBase
{

    @XmlAttribute(name = "extends")
    protected String _extends;

    /**
     * Gets the value of the extends property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtends() {
        return _extends;
    }

    /**
     * Sets the value of the extends property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtends(String value) {
        this._extends = value;
    }

}
