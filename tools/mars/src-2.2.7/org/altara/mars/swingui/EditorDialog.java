/* MARS Network Monitor Swing User Interface
   Copyright (C) 1999 Brian H. Trammell
   Copyright (C) 2002 Leapfrog Research & Development, LLC

	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, it is available at 
	http:///www.gnu.org/copyleft/gpl.html, or by writing to the
	Free Software Foundation, Inc., 59 Temple Place - Suite 330,
	Boston, MA  02111-1307, USA.
*/

package org.altara.mars.swingui;

import org.altara.util.*;
import org.altara.mars.*;
import org.altara.mars.engine.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

public class EditorDialog extends JDialog {

    MarsView owner;

	public EditorDialog (MarsView owner, Editor editor) {
		super(owner,editor.getEditorTitle());

        this.owner = owner;
		getContentPane().setLayout(new BorderLayout(8,8));
		getContentPane().add((Component)editor,BorderLayout.CENTER);
		getContentPane().add(createButtonBar(editor),BorderLayout.SOUTH);
		this.pack();
		this.show();
	}

	public Box createButtonBar(final Editor editor) {
		Action okAction = new AbstractAction("OK") {
			public void actionPerformed(ActionEvent ae) {
				try {
					editor.commit();
                    owner.postCommitUpdate();
					dispose();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(EditorDialog.this,
						"Invalid input: \n"+ex+
						"\nPlease correct the input error and try again.",
						"Input Error",JOptionPane.ERROR_MESSAGE);
					//ex.printStackTrace();
				}
			}
		};

		Action cancelAction = new AbstractAction("Cancel") {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		};

		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(new JButton(cancelAction));
		buttonBox.add(new JButton(okAction));
		return buttonBox;
	}
}
