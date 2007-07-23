package org.astrogrid.desktop.modules.ui.comp;

import java.util.EventListener;
import java.util.Observable;
import java.util.Observer;

import ca.odell.glazedlists.ObservableElementList;

/** a connector that listens to changes to observable objects and bridges these 
     * events into the ObservableElementList. Used to display a list of running background tasks.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 200710:00:01 PM
 */
public class ObservableConnector implements ObservableElementList.Connector, Observer, EventListener {
	ObservableElementList list;
	public EventListener installListener(Object arg0) {
		((Observable)arg0).addObserver(this);
		return this;
	}
	public void setObservableElementList(ObservableElementList arg0) {
		this.list = arg0;
	}

	public void uninstallListener(Object arg0, EventListener arg1) {
		if (arg1 == this) {
			((Observable)arg0).deleteObserver(this);
		}
	}
	// update from the thing we're observing.
	public void update(Observable o, Object arg) {
		list.elementChanged(o);
	}
}