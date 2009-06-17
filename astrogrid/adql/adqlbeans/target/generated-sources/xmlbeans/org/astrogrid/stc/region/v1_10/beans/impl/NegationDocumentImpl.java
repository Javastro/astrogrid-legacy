/*
 * An XML document type.
 * Localname: Negation
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.NegationDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * A document containing one Negation(@http://www.ivoa.net/xml/STC/STCregion/v1.10) element.
 *
 * This is a complex type.
 */
public class NegationDocumentImpl extends org.astrogrid.stc.region.v1_10.beans.impl.RegionDocumentImpl implements org.astrogrid.stc.region.v1_10.beans.NegationDocument
{
    
    public NegationDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NEGATION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Negation");
    
    
    /**
     * Gets the "Negation" element
     */
    public org.astrogrid.stc.region.v1_10.beans.NegationType getNegation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.NegationType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.NegationType)get_store().find_element_user(NEGATION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Negation" element
     */
    public void setNegation(org.astrogrid.stc.region.v1_10.beans.NegationType negation)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.NegationType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.NegationType)get_store().find_element_user(NEGATION$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.region.v1_10.beans.NegationType)get_store().add_element_user(NEGATION$0);
            }
            target.set(negation);
        }
    }
    
    /**
     * Appends and returns a new empty "Negation" element
     */
    public org.astrogrid.stc.region.v1_10.beans.NegationType addNewNegation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.NegationType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.NegationType)get_store().add_element_user(NEGATION$0);
            return target;
        }
    }
}
