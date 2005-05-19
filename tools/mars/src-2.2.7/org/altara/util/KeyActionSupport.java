/* Altara Utility Classes
   Copyright (C) 2001-2004 Brian H. Trammell

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
import javax.swing.event.*;

public class KeyActionSupport extends KeyAdapter {

    private HashMap keyActions;

    public KeyActionSupport() {
        this.keyActions = new HashMap();
    }

    public void addKeyAction(int keyCode, int modifiers, Action action) {
        ModKey mk = new ModKey(keyCode,modifiers);
        keyActions.put(mk, action);
    }

    public void addNormalKey(int keyCode, Action action) {
        addKeyAction(keyCode,0,action);
    }

    public void addCommandKey(int keyCode, Action action) {
        addKeyAction(keyCode,KeyEvent.CTRL_MASK,action);
        addKeyAction(keyCode,KeyEvent.META_MASK,action);
    }

    public void keyReleased(KeyEvent ke) {
        ModKey mk = new ModKey(ke);
        Action action = (Action)keyActions.get(mk);

        if (action != null && action.isEnabled()) {
            action.actionPerformed(
                new ActionEvent(ke.getSource(),0,"KeyTyped"));
            ke.consume();
        }
    }

    protected Object getLastInvoked() {
        return null;
    }

    private static class ModKey {
        int code;
        int mod;

        private ModKey(int code, int mod) {
            this.code = code;
            this.mod = mod;
        }

        private ModKey(KeyEvent ke) {
            this.code = ke.getKeyCode();
            this.mod = ke.getModifiers();
        }

        public String toString() {
            return "["+code+","+mod+"]";
        }

        public boolean equals(Object o) {
            if (o instanceof ModKey) {
                ModKey mko = (ModKey)o;
                return mko.code == code && mko.mod == mod;
            } else {
                return false;
            }
        }

        public int hashCode() {
            return (mod << 8) + code;
        }
    }   
}
