/**
 * $Id: TextSetter.java,v 1.1 2005/04/01 10:41:02 mch Exp $
 *
 */

package org.astrogrid.storebrowser.textview;

import java.io.ByteArrayOutputStream;
import javax.swing.JTextArea;


/**
 * Runnable that sets the given textarea to the buffer text */

public class TextSetter extends ContentSetter {
    
   JTextArea textArea = null;
   
   public TextSetter(JTextArea targetComponent) {
      this.textArea = targetComponent;
   }

   public void run() {
      textArea.setText(buffer.toString());
   }
   
   public void setError(Throwable th) {
      textArea.setText(th.toString());
   }
   
}
