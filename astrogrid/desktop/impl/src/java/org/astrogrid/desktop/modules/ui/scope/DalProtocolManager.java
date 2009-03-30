/*$Id: DalProtocolManager.java,v 1.14 2009/03/30 16:55:29 nw Exp $
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

/** Aggregates a set of {@link DalProtocol} so they can be operated as a whole.
 * also manages a tablemodel that summarizes the resultls of querying each service - this is also accessible through
 * the separate {@link QueryResultCollector} interface - as later, we may want to refactor this elewhere.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Jan-2006
 *
 */
public class DalProtocolManager implements Iterable<DalProtocol>{

    public DalProtocolManager() {
    	l = new ObservableElementList<DalProtocol>(new BasicEventList<DalProtocol>(),new ProtocolListener());
    }
    private final EventList<DalProtocol> l;

    /** add a protocol to the manager */
    public void add(final DalProtocol r) {
        l.add(r);
    }
    /** access a list where events are triggered when checkboxes are clicked */
    public EventList<DalProtocol> getList() {
    	return l;
    }
    
    /** return an iterator over all the protocols in the manager */
    public Iterator<DalProtocol> iterator() {
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
    	if (size() % 2 != 0) {
    		num++;
    	}
    	final StringBuffer sb = new StringBuffer("d");
    	for (int i = 1; i < num; i++) {
    		sb.append(",d");
    	}
    	return sb.toString();
    }
    /**
     * An observable list connector for {@code DalProtocol} - listens to each's checkbox. 
     * @author Noel.Winstanley@manchester.ac.uk
     */
    private static class ProtocolListener 
    	implements ObservableElementList.Connector<DalProtocol>, ItemListener {

		public EventListener installListener(final DalProtocol p) {
			p.getCheckBox().addItemListener(this);
			return this;
		}

		private ObservableElementList<DalProtocol> l;
		public void uninstallListener(final DalProtocol p, final EventListener arg1) {
			if ( arg1 != this) {
				return;
			}
			p.getCheckBox().removeItemListener(this);
		}

		public void itemStateChanged(final ItemEvent e) {
			final JCheckBox cb = (JCheckBox)e.getSource();
			final DalProtocol o = (DalProtocol)cb.getClientProperty(DalProtocol.OWNER);
			l.elementChanged(o);
		}

        public void setObservableElementList(
                final ObservableElementList<? extends DalProtocol> arg0) {
            this.l = (ObservableElementList<DalProtocol>)arg0;
        }
    }

}


/* 
$Log: DalProtocolManager.java,v $
Revision 1.14  2009/03/30 16:55:29  nw
upgraded glazed lists.

Revision 1.13  2008/11/04 14:35:48  nw
javadoc polishing

Revision 1.12  2008/05/28 12:27:49  nw
Complete - task 408: Adjust count reporting in astroscope and voexplorer.

Revision 1.11  2008/03/10 17:13:28  nw
fixed test for oddness.

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