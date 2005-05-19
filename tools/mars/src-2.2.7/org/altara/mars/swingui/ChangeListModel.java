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

import org.altara.mars.*;
import org.altara.mars.engine.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

/** ChangeListModel contains a list of service status change events.
	It provides the user with a way to view the history of service
	status changes. 
*/

public class ChangeListModel extends AbstractListModel
		implements StatusChangeListener {

	private static final int DEFAULT_MAXLEN = 200;

	private ArrayList changeList;
	private int maxlen;

	public ChangeListModel() {
		this.changeList = new ArrayList();
		this.maxlen = DEFAULT_MAXLEN;
	}
	
	public int getSize() {
		return changeList.size();
	}

	public Object getElementAt(int index) {
		return changeList.get(index);
	}

	public void statusChanged(StatusChangeEvent sce) {
		// insert the change event at the head of the list
		changeList.add(0,sce);
		// fire notification
		fireIntervalAdded(this,0,0);
		// remove the last element if the list is now too large
		if (changeList.size() > maxlen) {
			changeList.remove(changeList.size()-1);
			fireIntervalRemoved(this,changeList.size(),changeList.size());
		}
	}

	void clear() {
		int oldsz = changeList.size();
		changeList.clear();
		if (oldsz > 0) fireIntervalRemoved(this,0,oldsz-1);
	}
}
