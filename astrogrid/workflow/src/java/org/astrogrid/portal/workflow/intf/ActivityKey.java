/*$Id: ActivityKey.java,v 1.3 2004/03/11 13:53:36 nw Exp $
 * Created on 01-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;

import org.astrogrid.common.bean.BaseBean;
import org.astrogrid.workflow.beans.v1.Workflow;

/** Implementation of activity keys for the new workflow object model
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Mar-2004
 */
public class ActivityKey {
    /** no public constructor
     * 
     */
    private ActivityKey(String xpath) {
        this.xpath = xpath;
    }
    private final String xpath;
    
    /** create a key for the current position in a workflow 
     * 
     * @param root document root object
     * @param current object to create activity key for
     * @return activity key that points to the current object.
     * @throws IllegalArgumentException - if <tt>root</tt> or <tt>current</tt> is null, or <tt>current</tt> is not within the workflow object tree
     * @todo - can we strengthen the type of 'current' - ie. is it always going to be a Step, etc?
     */
    public static ActivityKey createKey(Workflow root, BaseBean current) throws IllegalArgumentException {
        if (root == null) {
            throw new IllegalArgumentException("workflow root is null");
        }
        if (current == null) {
            throw new IllegalArgumentException("current node is null");
        }
        String xpath = root.getXPathFor(current);
        if (xpath == null) {
            throw new IllegalArgumentException("current node not found in workflow");
        }
        return new ActivityKey(xpath);
    }  
    
    /** apply the activity key to a document, to access the object it refers to 
     * 
     * @param root workflow document
     * @return the object in the tree that the activity key pointed to, or null if no object was found
     */
    public BaseBean extractFrom(Workflow root) {
        return (BaseBean)root.findXPathValue(this.xpath);
    }
    
    // machine-generated guff
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ActivityKey:");
        buffer.append(" xpath: ");
        buffer.append(xpath);
        buffer.append("]");
        return buffer.toString();
    }
        /**
    * Returns <code>true</code> if this <code>ActivityKey</code> is the same as the o argument.
    *
    * @return <code>true</code> if this <code>ActivityKey</code> is the same as the o argument.
    */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        ActivityKey castedObj = (ActivityKey) o;
        return ((this.xpath == null ? castedObj.xpath == null : this.xpath.equals(castedObj.xpath)));
    }
        /**
    * Override hashCode.
    *
    * @return the Objects hashcode.
    */
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (xpath == null ? 0 : xpath.hashCode());
        return hashCode;
    }

}


/* 
$Log: ActivityKey.java,v $
Revision 1.3  2004/03/11 13:53:36  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.2.4.1  2004/03/11 13:36:46  nw
tidied up interfaces, documented

Revision 1.2  2004/03/03 01:36:38  nw
merged interfaces in from branch nww-int05-bz#146

Revision 1.1.2.1  2004/03/01 19:02:57  nw
refined interfaces. almost ready to publish
 
*/