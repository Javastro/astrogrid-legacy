/* Altara Utility Classes
   Copyright (C) 2001,2002 Brian H. Trammell

	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License as published by the Free Software Foundation; either
	version 2.1 of the License, or (at your option) any later version.
	
	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	Lesser General Public License for more details.
	
	You should have received a copy of the GNU Lesser General Public
	License along with this library; if not, it is available at
	http://www.gnu.org/copyleft/lesser.html, or by writing to the
	Free Software Foundation, Inc., 59 Temple Place, Suite 330,
	Boston, MA  02111-1307  USA
*/

package org.altara.util;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

public class TreeContextMenuSupport extends ContextMenuSupport {

	private JTree tree;

	public TreeContextMenuSupport(JTree tree) {
		super(tree);
		this.tree = tree;
	}

	protected Object findInvoked(MouseEvent me) {
		int x = me.getX();
		int y = me.getY();
		TreePath p = tree.getPathForLocation(x,y);
		if (p == null) return tree.getModel().getRoot();
		tree.setSelectionPath(p);
		return p.getLastPathComponent();
	}
}
