// JCommaIntegerField v1.1
// Alan Maxwell
//
// Version History
//
// 1.1:  14 Nov 2002
//       Rewrote the NumberDocument class so that it now, rather than testing 
//       char-by-char on insert, builds a temporary version of what the text
//       field WOULD contain if it were allowed to have the new content
//       inserted and then tests this with NumberChecker.isPotentialInt(). 
//       If this test
//       fails the insert does not happen. The net effect is similar to
//       checking char-by-char but is more robust and prevents false strings 
//       like '-5..5-' which would be accepted by the char tester but 
//       rejected by the NumberChecker.isPotentialInt() function.
// 1.0:  05 Nov 2002
//       Initial version.
//

package org.astrogrid.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.JTextField;

import java.util.StringTokenizer;

import org.astrogrid.ui.NumberChecker;

/**
 * JCommaIntegerField.java
 *
 * A convenient subclass of JTextField that just allows integer
 * numbers to be entered and provides get/setValue methods
 * Any number of integers can be entered, separated by a comma ","
 * the method getValue(int) allows the retrieval of each comma value
 * with the first value numbered '0', etc...
 *
 * @author Alan Maxwell
 */

public class JCommaIntegerField extends JTextField
{

   /**
    * A text document which will reject any characters that are not
    * digits.
    */
   public class NumberDocument extends PlainDocument
   {
      public void insertString(int offs, String str, AttributeSet atts)
                                                   throws BadLocationException
      {
        // Get the existing text, and do a 'test' insert of new string...
        StringBuffer testText = new StringBuffer(getText(0, getLength()));
        testText.insert(offs, str);

        StringTokenizer testTokens = new StringTokenizer(testText.toString());

        boolean areValidInts = true;
        while ( (areValidInts == true) && (testTokens.hasMoreTokens()) )
        {
          areValidInts = 
            (areValidInts) &&
            (NumberChecker.isPotentialInt(testTokens.nextToken(",").trim()));
        };
        
        if ( areValidInts == true )
        {
          super.insertString(offs, str, atts);
        };
      }
   }
   
   /**
    * Constructs field
    */
   public JCommaIntegerField()
   {
      super();
      setDocument(new NumberDocument());
   }

   /**
    * Returns the part of the text representing the individual value
    * numbered 'paramNum' (in a comma separated list, values start at
    * 0). If 'paramNum' is greater than the number of integers in the
    * list, the returned string will be empty...
    */
   public String getText(int paramNum)
   {
     if (getText() == null)
     {
       return "";
     };

     StringTokenizer tokenizer = new StringTokenizer(getText().trim());

     int atToken = 0;

     while ((tokenizer.hasMoreTokens()) && (atToken < paramNum))
     {
       tokenizer.nextToken(","); // Skip over tokens to get one we want
       atToken++;
     };

     if ((tokenizer.hasMoreTokens()) && (atToken == paramNum))
     {
       return tokenizer.nextToken(",").trim();
     }
     else
     {
       return "";
     }
   }

   /**
    * Returns the value entered in the field.  It <i>should</i> be impossible
    * to have a NumberFormatException, as there should be no way of setting
    * the text to be anything else, but the exception must still be caught
    */
   public int getValue(int paramNum)
   {
      try
      {
         String value = getText(paramNum);

         if (value.length() == 0)
           return 0;
         else
           return Integer.parseInt(value);
      }
      catch (NumberFormatException nfe)
      {
         //should never happen, so make it a fatal/application exception
         throw new RuntimeException("NumberField contains non-number '"+getText()+"'");
      }
   }

   /**
    * Integer property setter
    */
   public void setValue(int value)
   {
      // Calls superclass directly as there is no need to for the integer
      // check in setText(String);
      super.setText("" + value);
   }
   
   /**
    * Overrides the superclass method to check that the value the field is
    * being set to is a valid integer/integer list.
    */
   public void setText(String value)
   {
      try
      {
        StringTokenizer tokenizer = new StringTokenizer(value);

        // Check any/all passed values are integers
        while (tokenizer.hasMoreTokens())
        {
          Integer.parseInt(tokenizer.nextToken(","));
        };

        super.setText(value);
      }
      catch (NumberFormatException nfe)
      {
         throw new IllegalArgumentException("'"+value+"' is not an integer/integer list");
      }
   }
}

