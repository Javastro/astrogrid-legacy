/*$Id: TranslationFrame.java,v 1.3 2003/09/17 14:51:30 nw Exp $
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** class to maintain a set of translations 
 * 
 * <h3>Description</h3>
 * <pre>
 * > What's TranslatorFrame for?  How would you use it?
>

Its part of the translator code, which really should be better documented.

Here's how it developed...

I original tried to write a translator by hand, explicitly coding the 
traversal down the tree, accessing results, etc.

However, I quickly found this was becoming a nightmare - castor, based on the 
ADQL schema - has generated a lot of classes that are containers for the real 
data, and just take up space in the object tree. These seem to appear 
whenever there's a 'choice' type in the schema.

I'm not explaining this very well, but the upshot is that traversing the tree 
is cumbersome, and you also need to check for null at each stage (as null 
means the element isn't present). So I was writing code that was very 
specific to the current schema and hints provided to castor.

I'd have like to use my already-implemented visitor to traverse the tree, and 
separate the translation logic (hand-coded, static) from the traversal code 
(reflection, prone to change). However, this doesn't work by itself, as the 
visitor pattern doesn't provide a nice way of passing results of evaluating 
child nodes up to the evaluation of parent nodes. You need to provide 
something within the visitor object itself to keep track of intermediate 
results. 

That's what the translatorframe is - it manages the accumulation of 
intermediate translations , and makes these available to the translations of 
parent nodes.

The base translator class - org.astrogrid.datacenter.queriers.DatabaseQuerier 
- provides the machinery to traverse the object tree (using the dynamic 
visitor pattern) and maintains a stack of these translatorFrames.

Then you get a subclass of DatabaseQuerier for each dialect of SQL you're 
translating - look at mysql.MySqlQuerier. This contains, in essence, a rule 
for each node of interest in the query model. This makes it easy to override 
the few rules that differ between different SQL dialects.

For each rule, its result is stored back in the current translator frame. 
Other rules can retreive these results by key. This makes the translation 
process much more independent of changes to the underlying structure of the 
object model, and also handles the (very common) case of some nodes not being 
present - no need to check for null, the translatorFrame just returns an 
empty string.

Because of the reflection, this design is less efficient than a hand-coded 
approach. But I figure that can be provided once the schema is settled upon - 
until then, these rule-based translators are much easier to keep up-to-date.
 * </pre>
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Sep-2003
 *@todo tody this documentation
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
            return TranslationFrame.unTrim((String) result);
        } else {
            List list = (List)result;
            return TranslationFrame.unTrim(TranslationFrame.join(list.iterator(),separator));
        }
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
// static helper methods.    
    /** append a space to a string it lacks a space already
      * 
      * */
     public static  String unTrim(String s) {
         String result = s;

         if (!result.endsWith(" ")) {
             result += " ";
         }
         return result; 
     }
    /**
         * <p>Joins the elements of the provided array into a single String
         * containing the provided list of elements.</p>
         *
         * <p>No delimiter is added before or after the list.
         * A <code>null</code> separator is the same as an empty String (""). 
         * Null objects or empty strings within the array are represented by 
         * empty strings.</p>
         *
         * <pre>
         * StringUtils.join(null, *)                = null
         * StringUtils.join([], *)                  = ""
         * StringUtils.join([null], *)              = ""
         * StringUtils.join(["a", "b", "c"], "--")  = "a--b--c"
         * StringUtils.join(["a", "b", "c"], null)  = "abc"
         * StringUtils.join(["a", "b", "c"], "")    = "abc"
         * StringUtils.join([null, "", "a"], ',')   = ",,a"
         * </pre>
         *   *<i>Lifted from commons.lang.StringUtil</i>
         * @param array  the array of values to join together, may be null
         * @param separator  the separator character to use, null treated as ""
         * @return the joined String, <code>null</code> if null array input
         */
        public static String join(Object[] array, String separator) {
            if (array == null) {
                return null;
            }
            if (separator == null) {
                separator = "";
            }
            int arraySize = array.length;

            // ArraySize ==  0: Len = 0
            // ArraySize > 0:   Len = NofStrings *(len(firstString) + len(separator))
            //           (Assuming that all Strings are roughly equally long)
            int bufSize 
                = ((arraySize == 0) ? 0 
                    : arraySize * ((array[0] == null ? 16 : array[0].toString().length()) 
                        + ((separator != null) ? separator.length(): 0)));

            StringBuffer buf = new StringBuffer(bufSize);

            for (int i = 0; i < arraySize; i++) {
                if ((separator != null) && (i > 0)) {
                    buf.append(separator);
                }
                if (array[i] != null) {
                    buf.append(array[i]);
                }
            }
            return buf.toString();
        }    
    
    /**
     * <p>Joins the elements of the provided array into a single String
     * containing the provided list of elements.</p>
     *
     * <p>No delimiter is added before or after the list.
     * Null objects or empty strings within the array are represented by 
     * empty strings.</p>
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([null], *)             = ""
     * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
     * StringUtils.join(["a", "b", "c"], null) = "abc"
     * StringUtils.join([null, "", "a"], ';')  = ";;a"
     * </pre>
   *<i>Lifted from commons.lang.StringUtil</i>
     * @param array  the array of values to join together, may be null
     * @param separator  the separator character to use
     * @return the joined String, <code>null</code> if null array input
     */
    public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }
        int arraySize = array.length;
        int bufSize = (arraySize == 0 ? 0 : ((array[0] == null ? 16 : array[0].toString().length()) + 1) * arraySize);
        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
    
    /**
      * <p>Joins the elements of the provided <code>Iterator</code> into
      * a single String containing the provided elements.</p>
      *
      * <p>No delimiter is added before or after the list.
      * A <code>null</code> separator is the same as an empty String ("").</p>
      *
      * <p>See the examples here: {@link #join(Object[],String)}. </p>
      *     *<i>Lifted from commons.lang.StringUtil</i>
      * @param iterator  the <code>Iterator</code> of values to join together, may be null
      * @param separator  the separator character to use, null treated as ""
      * @return the joined String, <code>null</code> if null iterator input
      */
     public static String join(Iterator iterator, String separator) {
         if (iterator == null) {
             return null;
         }
         StringBuffer buf = new StringBuffer(256);  // Java default is 16, probably too small
         while (iterator.hasNext()) {
             Object obj = iterator.next();
             if (obj != null) {
                 buf.append(obj);
             }
             if ((separator != null) && iterator.hasNext()) {
                 buf.append(separator);
             }
          }
         return buf.toString();
     }
// inner class - a container for translatiion frames    
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
Revision 1.3  2003/09/17 14:51:30  nw
tidied imports - will stop maven build whinging

Revision 1.2  2003/09/03 13:47:30  nw
improved documentaiton.
split existing MySQLQueryTranslator into a vanilla-SQL
version, and MySQL specific part.

Revision 1.1  2003/09/02 14:45:38  nw
datastructure used during translation
 
*/