// JCommaDoubleField v1.1
// Alan Maxwell
//
// Version History
//
// 1.2:  09 Dec 2002
//       Removed the overridden setText() method because it was generating
//       exceptions when called with empty string to clear box. Its 
//       functionality is not needed anyway as the new NumberDocument prevents
//       the entering of any characters in the field that would make the
//       current contents a non-number...
// 1.1:  14 Nov 2002
//       Rewrote the NumberDocument class so that it now, rather than testing 
//       char-by-char on insert, builds a temporary version of what the text
//       field WOULD contain if it were allowed to have the new content
//       inserted and then tests this with NumberChecker.isPotentialDouble(). 
//       If this test
//       fails the insert does not happen. The net effect is similar to
//       checking char-by-char but is more robust and prevents false strings 
//       like '-5..5-' which would be accepted by the char tester but 
//       rejected by the NumberChecker.isPotentialDouble() function.
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
 * JDoubleField.java
 *
 * A convenient subclass of JTextField that just allows double
 * numbers to be entered and provides get/setValue methods.
 *
 * Derived from the similar JIntegerField code
 *
 * @author Alan Maxwell
 */

public class JCommaDoubleField extends JTextField
{

   
   /**
    * Constructs field
    */
   public JCommaDoubleField()
   {
      super();
      setDocument(new DoubleNumberDocument());
   }

   /**
    * Returns the part of the text representing the individual value
    * numbered 'paramNum' (in a comma separated list, values start at
    * 0). If 'paramNum' is greater than the number of doubles in the
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
   public double getValue(int paramNum)
   {
      try
      {
         String value = getText(paramNum);

         if (value.length() == 0)
           return 0.0;
         else
           return Double.parseDouble(value);
      }
      catch (NumberFormatException nfe)
      {
         //should never happen, so make it a fatal/application exception
         throw new RuntimeException("NumberField contains non-number '"+getText()+"'");
      }
   }

   /**
    * Double property setter
    */
   public void setValue(double value)
   {
      // Calls superclass directly as there is no need to for the double
      // check in setText(String);
      super.setText("" + value);
   }
}

/**
 * A text document which will reject any characters that are not
 * numerical digits or a '.'
 */
class DoubleNumberDocument extends PlainDocument
{
   public void insertString(int offs, String str, AttributeSet atts)
												throws BadLocationException
   {
	 // Get the existing text, and do a 'test' insert of new string...
	 StringBuffer testText = new StringBuffer(getText(0, getLength()));
	 testText.insert(offs, str);

	 StringTokenizer testTokens = new StringTokenizer(testText.toString());

	 boolean areValidDoubles = true;
	 while ( (areValidDoubles == true) && (testTokens.hasMoreTokens()) )
	 {
	   areValidDoubles = 
		 (areValidDoubles) &&
		 (NumberChecker.isPotentialDouble(testTokens.nextToken(",").trim()));
	 };
        
	 if ( areValidDoubles == true )
	 {
	   super.insertString(offs, str, atts);
	 };
   }
}
