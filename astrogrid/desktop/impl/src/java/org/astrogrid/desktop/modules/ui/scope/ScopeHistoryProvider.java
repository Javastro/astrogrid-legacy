/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.io.File;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.comp.DoubleDimension;
import org.astrogrid.desktop.modules.ui.folders.AbstractListProvider;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.RangeList;
import ca.odell.glazedlists.UniqueList;

/** Provides a persistent model (for multiple views) of the astroscope
 * search history.
 * History only remembers position - not protocols, or the results.
 * @testme
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 2, 20072:07:44 PM
 */
public class ScopeHistoryProvider extends AbstractListProvider{


	/** number of history items to remeber */
	public static final int HISTORY_SIZE = 20;

	
	public ScopeHistoryProvider(final UIContext parent,Preference workDir, XmlPersist xml) {
		
		super(parent,new File(new File(workDir.getValue()),"scopeHistory.xml"),xml);
	}
	protected EventList createList() {
		// we use a range list to limit the number of history items stored.
		// and a unique list to remove duplicates
		//using a range list does mean that the in-memory history list
		// continuouysly grows - even though it's not seen or required.
		// probably not an issue though, given the number of searches performed in 
		// an average session..
	    RangeList historyList = new RangeList(new UniqueList(	new BasicEventList()));			
	    historyList.setHeadRange(0,HISTORY_SIZE);
	    return historyList;
	}
	

    protected void initializeFolderList() {
        // no init needed
    }


	public static class SearchHistoryItem implements Comparable{
		private DoubleDimension radius;
		private SesamePositionBean position;


		public DoubleDimension getRadius() {
			return this.radius;
		}

		public void setRadius(DoubleDimension radius) {
			this.radius = radius;
		}

		public SesamePositionBean getPosition() {
			return this.position;
		}

		public void setPosition(SesamePositionBean position) {
			this.position = position;
		}
		public int hashCode() {
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + ((this.position == null) ? 0 : this.position.hashCode());
			result = PRIME * result + ((this.radius == null) ? 0 : this.radius.hashCode());
			return result;
		}
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final SearchHistoryItem other = (SearchHistoryItem) obj;
			if (this.position == null) {
				if (other.position != null)
					return false;
			} else if (!this.position.equals(other.position))
				return false;

			if (this.radius == null) {
				if (other.radius != null)
					return false;
			} else if (!this.radius.equals(other.radius))
				return false;
			return true;
		}
		public int compareTo(Object arg0) {
			SearchHistoryItem other = (SearchHistoryItem)arg0;
			return new CompareToBuilder()
				.append(position.getRa(),other.position.getRa())
				.append(position.getDec(),other.position.getDec())
				.append(radius.getWidth(),other.radius.getWidth())
				.append(radius.getHeight(),other.radius.getHeight())
				.toComparison();
		}
		
	}


}
