// JDoubleField v1.1
// Alan Maxwell
//
// Version History
//
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

public class JDoubleField extends JTextField
{

   /**
    * A text document which will refuse to insert any characters that would
    * make the text document not-an-double.
    */
   public class DoubleNumberDocument extends PlainDocument
   {
      public void insertString(int offs, String str, AttributeSet atts)
                                                   throws BadLocationException
      {
        // Get the existing text, and do a 'test' insert of new string...
        StringBuffer testText = new StringBuffer(getText(0, getLength()));
        testText.insert(offs, str);

        if ( NumberChecker.isPotentialDouble(testText.toString()) == true )
        {
          super.insertString(offs, str, atts);
        };
      }
   }
   
   /**
    * Constructs field
    */
   public JDoubleField()
   {
      super();
      setDocument(new DoubleNumberDocument());
   }

   /**
    * Returns the value entered in the field.  It <i>should</i> be impossible
    * to have a NumberFormatException, as there should be no way of setting
    * the text to be anything else, but the exception must still be caught
    */
   public double getValue()
   {
      try
      {
         if ((getText() == null) || (getText().trim().length() == 0))
            return 0.0;
         
         return Double.parseDouble(getText().trim());
      }
      catch (NumberFormatException nfe)
      {
         // Should never happen, so make it a fatal/application exception
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
   
   /**
    * Overrides the superclass method to check that the value the field is
    * being set to is a valid double.
    */
   public void setText(String value)
   {
      try
      {
         super.setText("" + Double.parseDouble(value.trim()));
      }
      catch (NumberFormatException nfe)
      {
         throw new IllegalArgumentException("'" + value + "' is not a double");
      }
   }
}

