package net.mchill.log;

import java.awt.Color;
import java.util.*;
import java.io.*;

/**
 * Event severity class. Defines severity 'enumerators', used
 * to log different types of errors, suitable for filtering/displaying.
 *
 * This is not subclassed from TypeSafeEnumerator, as I want this package
 * to be 'standalone' as much as possible, so it can be conveniently passed
 * around as a package.
 *
 * @version          :
 * @Created          : June 2000
 * @Last Update      : Aug 2001
 *
 * @author           : Martin Hill
 *
 */

public class Severity implements Serializable
{
   private static final Hashtable allSeverities = new Hashtable();   //collection of all severities.

   /** Operator/user-related message */
   public static final Severity ALARM   = new Severity( "ALARM  ", Color.red, Color.white, true, 10 );
   /** Operator/user-related message */
   public static final Severity WARNING = new Severity( "WARNING", Color.yellow, Color.black, false, 20 );
   /** Operator/user-related message */
   public static final Severity INFO    = new Severity( "INFO   ", Color.white, Color.black, false, 30 );
   
   //internal program problems message severities
   public static final Severity ERROR   = new Severity( "ERROR  ", Color.red, Color.white, false,9 );
   public static final Severity DEBUG   = new Severity( "DEBUG  ", Color.white, Color.gray, false,35 );
   public static final Severity TRACE   = new Severity( "TRACE  ", Color.white, Color.gray, false,40 );
   
   private Color fBackColor;     // Background colour for displaying messages based on this severity
   private Color fForeColor;     // Foreground/text colour for displaying messages based on this severity
   private boolean fSoundAlarm;  // True if an alarm should be sounded
   private String fText;         // Severity description text
   private int fLevel;           // an indicator of how important this message is
   
   
   /*-------------------------------------------------------------------*/
   /**
    * Constructor.  Protected so that only the static finals above
    * can construct instances (subclasses also).  The arguments
    * fully describe the instance.
    **/
   protected Severity( String aSeverity, Color aBackground, Color aForeground, boolean soundAlarm, int aLevel )
   {
      fBackColor = aBackground;
      fForeColor = aForeground;
      fSoundAlarm = soundAlarm;
      fText = aSeverity;
      fLevel = aLevel;
      
      allSeverities.put(new Integer(aLevel), this);
   }
   
   /** Background colour used to display messages of this severity */
   public Color getBackground()
   {
      return fBackColor;
   }
   
   /** Foreground colour used to display messages of this severity */
   public Color getForeground()
   {
      return fForeColor;
   }
   
   /**  Returns the descriptive text */
   public String toString()
   {
      return fText;
   }
   
   /**
    * Finds Severity corresponding to given string
    */
   public static Severity getSeverity(String s)
   {
      Enumeration i = allSeverities.elements();
      Severity sev = null;
      
      while (i.hasMoreElements())
      {
         sev = (Severity) i.nextElement();
         if (s.equalsIgnoreCase(sev.toString()))
            return sev;
      }
      return null;
   }

   /*-------------------------------------------------------------------*/
   /**
    * Returns an iterator of all severities.  Can be used, for example,
    * to build combo box selectors out of the texts of all severities,
    * suitable for giving the user the option to filter severities.
    **/
   public static Enumeration getEnumeration()
   {
      return allSeverities.elements();
   }
   
   /**
    * Returns whether this severity is more important than the given
    * severity.  Note that we don't return 'level' as we may want to
    * change the numbers involved if eg we want to add 'very important alarms'
    * as well as 'just bad alarms'
    */
   public boolean isWorseThan(Severity aSeverity)
   {
      if (aSeverity == null)
         return true;
      
      return (fLevel<aSeverity.fLevel);
   }

   /** For Serializing - we don't want to use normal one, because
    * we'll end up with a new instance.  So instead we read & write
    * using the identifying string
    */
 
   private void writeObject(java.io.ObjectOutputStream out)
     throws IOException
   {
      out.writeObject(fText);
   }
   
   private void readObject(java.io.ObjectInputStream in)
     throws IOException, ClassNotFoundException
   {
      fText = (String) in.readObject();
   }

    /** For Serializing - we don't want to use normal one, because
    * we'll end up with a new instance.  So we look up an
    * existing severity using the streamed text
    */
   public Object readResolve() throws ObjectStreamException
   {
      return getSeverity(fText);
   }
    /**/
   
}
