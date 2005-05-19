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

public abstract class ContextMenuSupport {

	protected HashMap actionListMap;
	protected HashMap defaultActionMap;
	protected HashMap menuMap;
	private Component source;
	private Object invoked;

	protected ContextMenuSupport(Component source) {
		actionListMap = new HashMap();
		defaultActionMap = new HashMap();
		menuMap = new HashMap();

		// bind the mouse listener to the source
		this.source = source;
		source.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if ((InputEvent.BUTTON3_MASK & me.getModifiers()) != 0) {
					// invoke action menu on right click
					invokeActionMenu(me);
				} else if (((InputEvent.BUTTON1_MASK & me.getModifiers()) != 0)
						&& ((InputEvent.CTRL_MASK & me.getModifiers()) != 0)) {
					// invoke action menu on control-click
					invokeActionMenu(me);
				} else if (((InputEvent.BUTTON1_MASK & me.getModifiers()) != 0)
						&& me.getClickCount() == 2) {
					// invoke default action on double-click
					invokeDefaultAction(me);
				}
			}
		});
	}

	public synchronized void addClassAction(Class clazz, Action action) {
		LinkedList actionList = (LinkedList)actionListMap.get(clazz);
		if (actionList == null) actionList = new LinkedList();
		actionList.add(action);
		actionListMap.put(clazz,actionList);
		// signal rebuild of menus
		menuMap = null;
	}

	public synchronized void addDefaultClassAction(Class clazz, Action action) {
		addClassAction(clazz, action);
		defaultActionMap.put(clazz, action);
	}

	private synchronized void rebuildMenuMap() {
		menuMap = new HashMap();
		Iterator classIter = actionListMap.keySet().iterator();
		while (classIter.hasNext()) {
			Class clazz = (Class)classIter.next();
			JPopupMenu thisMenu = new JPopupMenu();
			thisMenu.setInvoker(source);
			Iterator actionIter =
				((Collection)actionListMap.get(clazz)).iterator();
			while (actionIter.hasNext()) {
				thisMenu.add((Action)actionIter.next());
			}
			menuMap.put(clazz,thisMenu);
		}
	}

	protected synchronized void invokeActionMenu(MouseEvent me) {
		// rebuild the menu map if it's been invalidated
		if (menuMap == null) rebuildMenuMap();
		// find the invoked class
		invoked = findInvoked(me);
		// check for null invoke
		if (invoked == null) return;
		// retrieve the appropriate menu
		JPopupMenu menu = (JPopupMenu)menuMap.get(invoked.getClass());
		// display it
		if (menu != null) {
			menu.show(me.getComponent(),me.getX(),me.getY());
		}
	}

	protected synchronized void invokeDefaultAction(MouseEvent me) {
		// find the invoked class
		invoked = findInvoked(me);
		// check for null invoke
		if (invoked == null) return;
		// retrieve the approrpriate action
		Action action = (Action)defaultActionMap.get(invoked.getClass());
		// perform it
		if (action != null && action.isEnabled())
			action.actionPerformed(new ActionEvent(source,0,"DefaultInvoke"));
	}

	protected abstract Object findInvoked(MouseEvent me);

	protected synchronized Object getLastInvoked() {
		return invoked;
	}
}
