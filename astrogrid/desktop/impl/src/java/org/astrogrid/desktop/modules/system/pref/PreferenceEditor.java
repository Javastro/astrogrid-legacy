/**
 * 
 */
package org.astrogrid.desktop.modules.system.pref;

import javax.swing.SwingUtilities;

import org.astrogrid.desktop.modules.system.ui.UIContext;

/**  @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 31, 20072:33:22 PM
 */
public class PreferenceEditor implements Runnable {


   private PreferenceEditorDialogue dialog;
private final PreferencesArranger arr;
private final Preference adv;
private final UIContext cxt;

   public PreferenceEditor(PreferencesArranger arr, Preference adv, UIContext cxt) {
    super();
    this.arr = arr;
    this.adv = adv;
    this.cxt = cxt;
}
public void run() {
       // always on the EDT at this point - so no need to acquire a lock.
       // see if it's been initialized..
       if (dialog == null) {
           dialog = new PreferenceEditorDialogue(arr,adv,cxt);
       }
       dialog.run();
   }
}