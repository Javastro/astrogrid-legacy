package org.astrogrid.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.JTextField;

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
    * A text document which will reject any characters that are not
    * numerical digits or a '.'
    */
   public class DoubleNumberDocument extends PlainDocument
   {
      public void insertString(int offs, String str, AttributeSet atts)
                                                   throws BadLocationException
      {
         if ( 
              !Character.isDigit(str.charAt(0)) &&
              !(str.charAt(0) == '-') &&
              !(str.charAt(0) == '.') &&
              !(str.charAt(0) == 'e') &&
              !(str.charAt(0) == 'E')
            ) {
            return;
         }
         super.insertString(offs, str, atts);
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
