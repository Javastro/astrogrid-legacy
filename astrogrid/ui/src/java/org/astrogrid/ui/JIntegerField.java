package org.astrogrid.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.JTextField;

/**
 * JIntegerField.java
 *
 * A convenient subclass of JTextField that just allows integer
 * numbers to be entered and provides get/setValue methods
 *
 * @author M Hill
 */

public class JIntegerField extends JTextField
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
         if (
              !Character.isDigit(str.charAt(0)) &&
              !(str.charAt(0) == '-') &&
              !(str.charAt(0) == 'e') &&
              !(str.charAt(0) == 'E')
            )  {
            return;
         }
         super.insertString(offs, str, atts);
      }
   }
   
   /**
    * Constructs field
    */
   public JIntegerField()
   {
      super();
      setDocument(new NumberDocument());
   }

   /**
    * Returns the value entered in the field.  It <i>should</i> be impossible
    * to have a NumberFormatException, as there should be no way of setting
    * the text to be anything else, but the exception must still be caught
    */
   public int getValue()
   {
      try
      {
         if ((getText() == null) || (getText().trim().length() == 0))
            return 0;
         
         return Integer.parseInt(getText().trim());
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
      //check in setText(String);
      super.setText(""+value);
   }
   
   /**
    * Overrides the superclass method to check that the value the field is
    * being set to is a valid integer.
    */
   public void setText(String value)
   {
      try
      {
         super.setText(""+Integer.parseInt(value.trim()));
      }
      catch (NumberFormatException nfe)
      {
         throw new IllegalArgumentException("'"+value+"' is not an integer");
      }
   }
}

