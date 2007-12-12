/*$Id: DalProtocolManager.java,v 1.10 2007/12/12 13:54:13 nw Exp $
 * Created on 27-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventListener;
import java.util.Iterator;

import javax.swing.JCheckBox;

import org.apache.commons.collections.iterators.UnmodifiableIterator;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.ObservableElementList;

/**
 * aggregates a set of retreivers together - so they can be operated as a whole.
 * also manages a tablemodel that summarizes the resultls of querying each service - this is also accessible through
 * the separate {@link QueryResultCollector} interface - as later, we may want to refactor this elewhere.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Jan-2006
 *
 */
public class DalProtocolManager {

    public DalProtocolManager() {
    	l = new ObservableElementList(new BasicEventList(),new ProtocolListener());
    }
    private final EventList l;

    /** add a protocol to the manager */
    public void add(DalProtocol r) {
        l.add(r);
    }
    /** access a list where events are triggered when checkboxes are clicked */
    public EventList getList() {
    	return l;
    }
    
    /** return an iterator over all the protocols in the manager */
    public Iterator iterator() {
        return UnmodifiableIterator.decorate(l.iterator());
    }

    public int size() {
        return l.size();
    }

    /** helper method for working with forms layout 
     * returns enough row specs to display the protocols.(2 per column)
     */
    public String getRowspec() {
    	int num= size() / 2;
    	if (size() % 2 == 1) {
    		num++;
    	}
    	StringBuffer sb = new StringBuffer("d");
    	for (int i = 1; i < num; i++) {
    		sb.append(",d");
    	}
    	return sb.toString();
    }
    
    private static class ProtocolListener 
    	implements ObservableElementList.Connector, ItemListener {

		public EventListener installListener(Object arg0) {
			DalProtocol p = (DalProtocol)arg0;
			p.getCheckBox().addItemListener(this);
			return this;
		}

		public void setObservableElementList(ObservableElementList arg0) {
			this.l = arg0;
		}
		private ObservableElementList l;
		public void uninstallListener(Object arg0, EventListener arg1) {
			if ( arg1 != this) {
				return;
			}
			DalProtocol p = (DalProtocol)arg0;
			p.getCheckBox().removeItemListener(this);
		}

		public void itemStateChanged(ItemEvent e) {
			JCheckBox cb = (JCheckBox)e.getSource();
			Object o = cb.getClientProperty(DalProtocol.OWNER);
			l.elementChanged(o);
		}
    }

}


/* 
$Log: DalProtocolManager.java,v $
Revision 1.10  2007/12/12 13:54:13  nw
astroscope upgrade, and minor changes for first beta release

Revision 1.9  2007/09/21 16:35:15  nw
improved error reporting,
various code-review tweaks.

Revision 1.8  2007/05/03 19:20:43  nw
removed helioscope.merged into uberscope.

Revision 1.7  2007/05/02 15:38:32  nw
changes for 2007.3.alpha1

Revision 1.6  2007/01/29 10:43:49  nw
documentation fixes.

Revision 1.5  2006/10/31 12:57:31  nw
removed commented out code - available in previous revision if needed.

Revision 1.3  2006/08/15 10:01:12  nw
migrated from old to new registry models.

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.6.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/