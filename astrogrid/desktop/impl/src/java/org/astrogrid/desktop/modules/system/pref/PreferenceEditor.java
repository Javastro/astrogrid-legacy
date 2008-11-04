/**
 * 
 */
package org.astrogrid.desktop.modules.system.pref;

import org.astrogrid.desktop.modules.system.ui.UIContext;

/** 
 * Component for editing preferences.
 *  @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 31, 20072:33:22 PM
 */
public class PreferenceEditor implements Runnable {


   private PreferenceEditorDialogue dialog;
private final PreferencesArranger arr;
private final Preference adv;
private final UIContext cxt;

   public PreferenceEditor(final PreferencesArranger arr, final Preference adv, final UIContext cxt) {
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