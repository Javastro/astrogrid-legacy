/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.io.File;

import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.folders.AbstractListProvider;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.RangeList;
import ca.odell.glazedlists.UniqueList;

/** Provides a persistent model of the astroscope
 * search history. Can be shared between multiple views.
 * History only remembers position - not protocols, or the results.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 2, 20072:07:44 PM
 */
public class ScopeHistoryProvider extends AbstractListProvider<PositionHistoryItem>{


	/** number of history items to remeber */
	public static final int HISTORY_SIZE = 30;

	
	public ScopeHistoryProvider(final UIContext parent,final Preference workDir, final XmlPersist xml) {
		
		super(parent,new File(new File(workDir.getValue()),"scopeHistory.xml"),xml);
	}
	@Override
    protected EventList<PositionHistoryItem> createList() {
		// we use a range list to limit the number of history items stored.
		// and a unique list to remove duplicates
		//using a range list does mean that the in-memory history list
		// continuouysly grows - even though it's not seen or required.
		// probably not an issue though, given the number of searches performed in 
		// an average session..
	    final RangeList<PositionHistoryItem> historyList = new RangeList<PositionHistoryItem>(
	                new UniqueList<PositionHistoryItem>(	
	                        new BasicEventList<PositionHistoryItem>()));			
	    historyList.setHeadRange(0,HISTORY_SIZE);
	    return historyList;
	}
	

    @Override
    protected void initializeFolderList() {
        // no init needed
    }


}
