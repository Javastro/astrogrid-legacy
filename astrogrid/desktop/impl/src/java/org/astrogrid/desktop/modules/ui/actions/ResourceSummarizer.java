package org.astrogrid.desktop.modules.ui.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.HashBag;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ivoa.resource.PrettierResourceFormatter;
import org.astrogrid.desktop.modules.system.Tuple;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;
import org.astrogrid.desktop.modules.ui.fileexplorer.StorageTableFormat;

/** reusable class that produces a summary for a set of resources */
public class ResourceSummarizer implements Iterable<Tuple<Integer,String>>{
    private final Bag types = new HashBag();
    /** single port of call- convenience for clear(), add() on erach, then result () */
    public Iterator<Tuple<Integer,String>> summarize(final List<Resource> rs) {
        clear();
        for(final Resource r: rs) {
            add(r);
        }
        return iterator();
    }

    /** access the result of the summarization
     * @return an iterator of count,name pairs - the number of resources, and the display name for this resource.
     */
    public Iterator<Tuple<Integer,String>> iterator() {
        // sort the map items by most frequent.
        final List<Tuple<Integer,String>> l = new ArrayList<Tuple<Integer,String>>();
        for (final Object n: types.uniqueSet()) {
            l.add(new Tuple<Integer, String>(types.getCount(n),(String)n));
        }
        // now want to sort the list by count.
        Collections.sort(l,new Comparator<Tuple<Integer,?>>() {

            public int compare(final Tuple<Integer, ?> o1, final Tuple<Integer, ?> o2) {
                return o2.fst() - o1.fst();
            } 
        });
        return l.iterator();
    }

    /** add a resource to the sumarizer
     * @param r
     */
    public void add(final Resource r) {
        types.add(PrettierResourceFormatter.formatResourceType(r));
    }
    
    public void add(final FileObjectView v) {
        types.add(StorageTableFormat.findBestContentType(v));
    }

    /**
     * clear any previous data
     */
    public void clear() {
        types.clear();
    }
}