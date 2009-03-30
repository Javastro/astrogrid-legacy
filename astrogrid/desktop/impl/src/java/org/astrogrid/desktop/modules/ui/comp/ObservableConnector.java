package org.astrogrid.desktop.modules.ui.comp;

import java.util.EventListener;
import java.util.Observable;
import java.util.Observer;

import ca.odell.glazedlists.ObservableElementList;

/** a bridge between an {@code Observable} object and a {@code ObservableElementList}. Used to display a list of running background tasks.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 200710:00:01 PM
 */
public class ObservableConnector<E extends Observable> implements ObservableElementList.Connector<E>, Observer, EventListener {
	public ObservableConnector() {
	}
    
    ObservableElementList<E> list;
    
	public EventListener installListener(final E arg0) {
		arg0.addObserver(this);
		return this;
	}


	public void uninstallListener(final E arg0, final EventListener arg1) {
		if (arg1 == this) {
			arg0.deleteObserver(this);
		}
	}
	// update from the thing we're observing.
	public void update(final Observable o, final Object arg) {
		list.elementChanged((E)o);
	}
    public void setObservableElementList(final ObservableElementList<? extends E> arg0) {
        this.list = (ObservableElementList<E>)arg0;
    }
}