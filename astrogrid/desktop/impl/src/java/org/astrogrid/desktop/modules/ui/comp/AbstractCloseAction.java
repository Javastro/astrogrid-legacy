package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.astrogrid.desktop.icons.IconHelper;

/** abstract definition of a close action */
 public abstract  class AbstractCloseAction extends AbstractAction {
     public AbstractCloseAction() {
         super("Close Window");
         //super("Close Window",IconHelper.loadIcon("close16.png"));
         this.putValue(SHORT_DESCRIPTION,"Close window");
         this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
         this.putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_W,Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
     }       
 }