/*
 * $Id: TypeSafeEnumerator.java,v 1.2 2003/10/02 13:10:46 mch Exp $
 */

package org.astrogrid.util;


import java.util.Vector;
import java.util.Hashtable;
import java.util.Iterator;


/**
* A superclass and set of helper methods for easily making, using
 * and streaming type-safe enumerations, if you don't have the new generic
 * java stuff.
* <P>
* Type-safe enumerators are a way of creating pre-defined lists of things,
* ensuring that only things of those correct instances are used as arguments.
* A good example is the Severity class of the event logs; it is simply not
* possible to pass anything except a Severity into the relevant MessageLog methods.
* <P>
* By subclassing from util.TypeSafeEnumerator, you get lots of advantages:
* string lookups, ability to iterate through the options, automatic BML
* stream handling and combo-box property editor that lists all the options.
* <P>
* Subclasses might override getList() and getFor() as described in those method
* docs below, to provide properly typed methods.
* <P>
* For use with a PropertyEditor, all you need to do is create the subclass, the appropriate get/set
* property methods and register the class with the TypeSafeEnumEditor at
* the end of the PropertyPane class (plenty of examples there) and bobs your uncle.
* <P>
 * Example use of TypeSafeEnumerator:
 * <pre>
 * public class Severity extends TypeSafeEnumerator {
 *     public final static Severity INFO = new Severity("Information only");
 *     public final static Severity WARNING = new Severity("Warning");
 *     public final static Severity ALARM = new Severity("Alarm");
 *
 *     public Severity(String msg) {
 *         super(msg);
 *     }
 * }
 * </pre>
 * You can now use Severity as a parameter, and you will only ever get the
 * ones defined above (or subclasses if not final!).
 * <p>
 * Extended from a javaworld article
 * <p>
 * @author M Hill
 */



public class TypeSafeEnumerator implements java.io.Serializable
{
   /** a hashtable of vectors...  each class look up corresponds to the
    * subclass of the typesafeenumerator.  the vector consists of the list
    * of that subclass's instance. */
   protected static Hashtable classLookup = new Hashtable();

   //instance stuff
   private String text; //all instances have an associated bit of text

   /**
    * Standard Constructor
    */
   protected TypeSafeEnumerator(String aString)
   {
      text = aString;
      addInstance();
   }

   /**
    * Adds instance to the classLookup table.  Might be called by
    * subclass constructors.
    */
   protected void addInstance()
   {
      Vector instanceList = (Vector) classLookup.get(this.getClass().getName());

      if (instanceList == null)
      {
         //no entries yet for this type
         instanceList = new Vector();
         classLookup.put(this.getClass().getName(), instanceList);

         //register with property editor manager
         //not sure about this as we start to make a simple class dependent on things like property editors,
         //but still...  can always comment it out...
         //       java.beans.PropertyEditorManager.registerEditor(this.getClass(), builder.property.editor.TypeSafeEnumEditor.class);
      }

      instanceList.add(this);
   }

   /**
    * Returns a string representing the enumeration instance
    */
   public String toString()
   {
//    return ""+this.getClass() + ":" + text;   //for testing
      return text;
   }

   /**
    * There can be problems when deserialising objects because a new instance
   * is created (regardless of whether the constructor is private) by the
    * stream instantiator.
    * <p>
    * See java tip 122 on www.javaworld.com
    * <p>
    * Note that we can use .getClass() here because it's an instance method -
    * we can't use it in the static (class) methods because in those cases
    * the class will always be TypeSafeEnumerator rather than the appropriate
    * subclass...
    */
    private Object readResolve () throws java.io.ObjectStreamException
    {
        return getFor(this.getClass(), text);   //get the instance already existing in the table
    }

   /**
    * Returns the text defining the enumeration instance
    */
   public String getText()
   {
      return text;
   }

   /**
    * Gets an iterator over the list of all those instances for the
    * given class.  A nice subclass might implement:
    * <pre>
    * public static Iterator getIterator() { return getIterator(<SubClass>.class);
    * </pre>
    }
    */
   public static Iterator getIterator(Class aClass)
   {
      Vector instanceList = (Vector) classLookup.get(aClass.getName());

      if (instanceList == null)
      {
         //no reference has been made to the subclass yet, so the values have
         //not been created.  Do so by making one instance here
         try {
            aClass.newInstance();
            //try again
            instanceList = (Vector) classLookup.get(aClass.getName());
         } catch (Exception e) {
            throw new NullPointerException("Could not create instances of typesafe enumerator "+aClass);
         }
      }

      return instanceList.iterator();
   }

   /**
    * Returns a list of the various instances for a particular class.  Suitable for combo boxes, etc.
    * I don't really want to return the instance vector as it means people can
    * fiddle with it...
    * <P>
   * A nice subclass might implement:
    * <pre>
    *
    *     public static Object[] getAll() { return getAll(<SubClass>.class); }
    *
    * </pre>
    * or use getList() to return a typed array
    * @see getList()
    */
   public static Object[] getAll(Class aClass)
   {
      return getList(aClass).toArray();
   }

   /**
    * Provides a way for subclasses to do a typesafe getAll(), by implementing
    * the following method:
    * <pre>
    *    public static <SubClass>[] getAll() {
    *        return (<SubClass>[] getList(<SubClass>.class).toArray(new <SubClass>[])
    *    }
   */
   public static Vector getList(Class aClass)
   {
      return (Vector) classLookup.get(aClass.getName());
   }

   /**
    * Return the instance of the subclass given the particular string
    * A nice subclass might implement:
    * <code>
          public static <b>SubClass</b> getFor(String aString)
          {
             return (<b>SubClass</b>) getFor(<b>SubClass</b>.class, aString);
          }
    * </code>
    */
   public static TypeSafeEnumerator getFor(Class aClass, String aString)
   {
      TypeSafeEnumerator instance;

      for (Iterator iterator = getIterator(aClass); iterator.hasNext(); )
      {
         instance = (TypeSafeEnumerator) iterator.next();

         if (instance.getText().equalsIgnoreCase(aString))
            return instance;
      }
      throw new IllegalArgumentException("No enumeration (instance) of "+aClass+" found for '"+aString+"'");
   }


}

/*
 * $Log: TypeSafeEnumerator.java,v $
 * Revision 1.2  2003/10/02 13:10:46  mch
 * Added documentation
 *
 */



