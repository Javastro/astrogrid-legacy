/*$Id: TranslationFrame.java,v 1.1 2003/09/02 14:45:38 nw Exp $
 * Created on 01-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers;
import java.util.*;
import org.apache.commons.lang.StringUtils;

/** class to maintain a set of translations 
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Sep-2003
 *
 */
public class TranslationFrame {
    protected Map m;    
    public TranslationFrame() {
        m = new HashMap();
    }
    
    /** add a binding between key and value
     * 
     * @param key key to store under
     * @param val value to store
     */
    public void add(String key,String val) {
        if (m.containsKey(key)) {
            Object currentVal = m.get(key);
            if (currentVal instanceof String) {            
                List list = new ArrayList();
                list.add(currentVal);
                list.add(val);
                m.put(key,list);
            } else { // must be a list then
                List list = (List)currentVal;
                list.add(val);
            }            
        } else {
            m.put(key,val);
        }
    }
    /** add a binding between key and value
     * 
     * @param key
     * @param val
     */
    public void add(String key,StringBuffer val) {
        add(key,val.toString()); // may produce a more efficient impl later.
    }
    
    /** retrieve the bindings for a key
     * 
     * @param key 
     * @return empty string if no binding<br>
     * single value if one binding<br>
     * comma separated concatenated string if multiple bindings
     */
    public String get(String key) {
        return get(key,", ");
    }
    /** retrieve the bindings for a key
     * 
     * @param key key to retreive
     * @param separator string to use as separator when concatentaing multiple bindings
     * @return empty string if no binding<br>
     * single value if one binding<br>
     * concatenated string if multiple bindings, separated by <tt>separator</tt>
     */
    public String get(String key,String separator) {
        Object result = m.get(key);
        if (result == null) {
            return "";
        } else if (result instanceof String) {
            return unTrim((String) result);
        } else {
            List list = (List)result;
            return unTrim(StringUtils.join(list.iterator(),separator));
        }
    }
    /** pad a stirng with space after if it lacks it already */
    private String unTrim(String s) {
        String result = s;
      //  if (!result.startsWith(" ")) {
       //     result = " " + result;
       // }
        if (!result.endsWith(" ")) {
            result += " ";
        }
        return result; 
    }

    /** convert to a string
     *  @return for multiple bindings, a list of bindings<br>
     * for a single binding, the value of that binding
     */
    public String toString() {
        if (m.size() == 1) {
            return m.values().iterator().next().toString(); // bit clunky - improve later.
        } else {
            return m.toString();
        }
    }
    /** handy container to keep translation frames in 
     * 
     * @author Noel Winstanley nw@jb.man.ac.uk 01-Sep-2003
     *
     */
    public static class Stack {
        List l = new ArrayList();

        /** push a new strnaslation frame on the stack */
        public void pushNew() {
            push(new TranslationFrame());
        }
        /** push this translation frame onto the stack
         * 
         * @param f the frame to add to the stack 
         */
        public void push(TranslationFrame f) {
            l.add(f);
        }
        /**remove the frame from the top of the stack 
         * 
         * @return the frame just popped off the stack
         */
        public TranslationFrame pop() {
            TranslationFrame trans = top();
            l.remove(l.size() - 1);
           
            return trans;
        }
        /** access the frame on the top of the stack (leaving it there
         * 
         * @return the frame on the top of the stack
         */
        public TranslationFrame top() {
            return (TranslationFrame)l.get(l.size() -1);
        }
    }

}


/* 
$Log: TranslationFrame.java,v $
Revision 1.1  2003/09/02 14:45:38  nw
datastructure used during translation
 
*/