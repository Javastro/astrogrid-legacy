/*
 * XML Type:  polygonType
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.PolygonType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * An XML polygonType(@http://www.ivoa.net/xml/STC/STCregion/v1.10).
 *
 * This is a complex type.
 */
public class PolygonTypeImpl extends org.astrogrid.stc.region.v1_10.beans.impl.ShapeTypeImpl implements org.astrogrid.stc.region.v1_10.beans.PolygonType
{
    
    public PolygonTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VERTEX$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Vertex");
    
    
    /**
     * Gets array of all "Vertex" elements
     */
    public org.astrogrid.stc.region.v1_10.beans.VertexType[] getVertexArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(VERTEX$0, targetList);
            org.astrogrid.stc.region.v1_10.beans.VertexType[] result = new org.astrogrid.stc.region.v1_10.beans.VertexType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Vertex" element
     */
    public org.astrogrid.stc.region.v1_10.beans.VertexType getVertexArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.VertexType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.VertexType)get_store().find_element_user(VERTEX$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Vertex" element
     */
    public int sizeOfVertexArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(VERTEX$0);
        }
    }
    
    /**
     * Sets array of all "Vertex" element
     */
    public void setVertexArray(org.astrogrid.stc.region.v1_10.beans.VertexType[] vertexArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(vertexArray, VERTEX$0);
        }
    }
    
    /**
     * Sets ith "Vertex" element
     */
    public void setVertexArray(int i, org.astrogrid.stc.region.v1_10.beans.VertexType vertex)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.VertexType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.VertexType)get_store().find_element_user(VERTEX$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(vertex);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Vertex" element
     */
    public org.astrogrid.stc.region.v1_10.beans.VertexType insertNewVertex(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.VertexType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.VertexType)get_store().insert_element_user(VERTEX$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Vertex" element
     */
    public org.astrogrid.stc.region.v1_10.beans.VertexType addNewVertex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.VertexType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.VertexType)get_store().add_element_user(VERTEX$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "Vertex" element
     */
    public void removeVertex(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(VERTEX$0, i);
        }
    }
}
