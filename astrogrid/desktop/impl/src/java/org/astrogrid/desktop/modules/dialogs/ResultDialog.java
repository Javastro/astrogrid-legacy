/*$Id: ResultDialog.java,v 1.9 2008/11/04 14:35:52 nw Exp $
 * Created on 10-May-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.astrogrid.desktop.modules.ui.comp.UIConstants;

import com.l2fprod.common.swing.BaseDialog;
/**
 * Simple dialog that displays a result in a text box.
 * <p/>
 * Allows for cutting / copying
 * the result into something else. 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 10-May-2005
 *
 */
public class ResultDialog extends BaseDialog {

    
    private JEditorPane resultDisplay;

    public static ResultDialog newResultDialog(final Component parent, final Object message) {
        if (parent == null) {
            return new ResultDialog(message);
        }
        final Window window = parent instanceof Window
                      ? (Window) parent
                      : SwingUtilities.getWindowAncestor(parent);
        if (window instanceof Frame) {
          return new ResultDialog((Frame)window,message);
        } else if (window instanceof Dialog) {
          return new ResultDialog((Dialog)window,message);      
        } else {
          return new ResultDialog(message);
        }
    }
    
    public ResultDialog(final Dialog owner, final Object message) {
        super(owner);
        init(message);
        setLocationRelativeTo(owner);
    }
    
    public ResultDialog(final Frame owner,final Object message) {
        super(owner);
        init(message);        
        setLocationRelativeTo(owner);
    }

    
    public ResultDialog(final Object message) {
        init(message);
         centerOnScreen();
    }
    
    private final void init(final Object message) {
        this.setTitle("Result");
        this.setModal(false);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setDialogMode(BaseDialog.CLOSE_DIALOG);
        resultDisplay = new JEditorPane();
        resultDisplay.setEditable(false);
        resultDisplay.setContentType("text/html");
        resultDisplay.setText(message.toString());
        resultDisplay.setBorder(null);
        resultDisplay.putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);      // this key is only defined on 1.5 - no effect on 1.4
        resultDisplay.setFont(UIConstants.SANS_FONT);           
        resultDisplay.setCaretPosition(0);
        getContentPane().add(new JScrollPane(resultDisplay,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        getBanner().setVisible(false);
        this.setSize(565,400);
        this.pack();
        
    }
    
   

    public final JEditorPane getResultDisplay() {
        return this.resultDisplay;
    }

}


/* 
$Log: ResultDialog.java,v $
Revision 1.9  2008/11/04 14:35:52  nw
javadoc polishing

Revision 1.8  2008/04/23 10:54:38  nw
added code for headless mode.

Revision 1.7  2008/02/15 13:09:02  mbt
Replace use of SwingUtilities.getAncestorOfClass(Window.class,comp) with
an idiom which does what that method ought to, viz. returns comp itself
if comp is a Window, rather than only one of its ancestors.
This prevents Dialogs from being unparented and hence risking being
obscured by the windows which ought to be their parent.

Revision 1.6  2007/11/21 07:55:39  nw
Complete - task 65: Replace modal dialogues

Revision 1.5  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.4  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.3.2.1  2005/11/23 04:48:48  nw
doc fix only

Revision 1.3  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.5  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.4  2005/06/08 14:51:59  clq2
1111

Revision 1.1.2.3  2005/06/02 14:34:32  nw
first release of application launcher

Revision 1.1.2.2  2005/05/11 14:25:25  nw
javadoc, improved result transformers for xml

Revision 1.1.2.1  2005/05/11 10:59:05  nw
made results selectable, so can be copied and pasted.
 
*/
