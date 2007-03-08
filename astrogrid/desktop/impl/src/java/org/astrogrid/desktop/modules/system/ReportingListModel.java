/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.util.EventListener;

import javax.swing.DefaultListModel;
import javax.swing.event.ListDataListener;

/** list model that provides more information.
 * @author Noel Winstanley
 * @todo unused  - delete.
 * @since Jun 16, 20065:53:23 PM
 */
public class ReportingListModel extends DefaultListModel {

	public Object remove(int index) {
		Object o =  super.remove(index);
		fireObjectsRemoved(new Object[]{o});
		return o;
	}
	
	public void removeElementAt(int index) {
		super.removeElementAt(index);
	}
	
	public boolean removeElement(Object obj) {
		boolean b =  super.removeElement(obj);
		if (b) {
			fireObjectsRemoved(new Object[]{obj});
		}
		return b;
	}
	/** extension of the standard list data listener 
	 * if instance of this interface is added as a listener, it will be notified as to 
	 * what obejcts are removed.
	 * @author Noel Winstanley
	 * @since Jun 16, 20065:55:06 PM
	 */
	public interface ReportingListDataListener extends ListDataListener {
		public void objectsRemoved(Object[] obj);
	}
	
	public void fireObjectsRemoved(Object[] obj) {
		EventListener[] ev = super.listenerList.getListeners(ListDataListener.class);
		for (int i = 0; i < ev.length; i++) {
			if (ev[i] instanceof ReportingListDataListener) {
			ReportingListDataListener rl = (ReportingListDataListener)ev[i];
			rl.objectsRemoved(obj);
			}
		}
	}
}
