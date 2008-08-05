package org.astrogrid.desktop.modules.ui.voexplorer;

import java.awt.Color;
import java.util.EventListener;
import java.util.HashSet;

import javax.swing.JProgressBar;
import javax.swing.plaf.metal.MetalProgressBarUI;

import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQL;

/**
 * class that submits queries against the registry to compute their size,
 * then displays this info using a barchart.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 27, 200712:21:31 PM
 */
public class QuerySizeIndicator extends JProgressBar {

	public QuerySizeIndicator(final UIComponent parent,final QuerySizer qs) {
		super(HORIZONTAL);
		this.parent = parent;
		this.sizer = qs;

        // OSX l&f doesn't allow bar to change color, so change it.
        // Selection background/foreground are the colour of the text - 
        // defaults can be illegible on OSX.
		setUI(new MetalProgressBarUI() {
            @Override
            public Color getSelectionForeground() {
                return Color.DARK_GRAY;
            }
            @Override
            public Color getSelectionBackground() {
                return Color.DARK_GRAY;
            }
        });
		
		setStringPainted(true);
		setMinimum(0);
		setValue(-1);
		setToolTipText("Indicates how many resources this query is likely to return");

		(new BackgroundWorker(parent,"Finding registry size",BackgroundWorker.LONG_TIMEOUT,Thread.MIN_PRIORITY + 3) {
		    {
		        setTransient(true);
		    }
			@Override
            protected Object construct() throws Exception {
				return sizer.regSize();
			}
			@Override
            protected void doFinished(final Object result) {
				final int size = ((Integer)result).intValue();
				setMaximum(size);		
			}
		}).start();			
		
	}

	private final QuerySizer sizer;		
	private final UIComponent parent;		
	
	/** overidden to fire 'invalid query' */
	@Override
	public void setIndeterminate(final boolean newValue) {
	    if (newValue) {
	        fireInvalid();
	    }
	    super.setIndeterminate(newValue);
	}
	/** hacky little flag used to indicate when value has been set to -1
	 * need to do this, as internal model of the JProgressBar rejects setValues that are out-of-bounds
	 */
	private boolean minus1; 
	@Override
    public void setValue(final int n) {
		super.setValue(n);
		if (n < 0) {
			setString("Incomplete query");
			minus1 = true;
		} else {
		    minus1 = false;
			setString("Matches " + n + " of " + getMaximum() + " resources");
		}
		if (n >= 0 && n <= sizer.getGoodThreshold()) {
			setForeground(Color.GREEN);
		} else if (n >= 0 && n <= sizer.getOversizeThreshold()) {
			setForeground(Color.YELLOW);
		} else {
			setForeground(Color.RED);
		}
		notifyListeners();
	}
	

	private BackgroundWorker latest;
	public void setValue(final SRQL query) {
		setIndeterminate(true);
		setString("Computing query size");
		if (latest != null) { // previous sizing task running.
			latest.interrupt();
		}
		latest = new BackgroundWorker(parent,"Computing query size",BackgroundWorker.VERY_SHORT_TIMEOUT,Thread.MAX_PRIORITY) {

			@Override
            protected Object construct() throws Exception {
				return sizer.size(query);
			}
			@Override
            protected void doFinished(final Object result) {
				if (this == latest) { // i.e. it hasn't been superceded in the meantime. 
					final int size = ((Integer)result).intValue();
					setValue(size);
				}
			}
			@Override
            protected void doError(final Throwable ex) {
				// no point reporting -just fail gracefully
				if (this == latest) {
					setValue(-1);
				}
			}
			@Override
            protected void doAlways() {
				if (this == latest) {
					setIndeterminate(false);
					latest = null;
				}
			}
		};
		latest.start();
	}
	
	public void setValue(final String query) {
		setIndeterminate(true);
		setString("Computing query size");
		if (latest != null) { // previous sizing task running.
			latest.interrupt();
		}
		latest = new BackgroundWorker(parent,"Computing query size",BackgroundWorker.VERY_SHORT_TIMEOUT,Thread.MAX_PRIORITY) {

			@Override
            protected Object construct() throws Exception {
				return sizer.size(query);
			}
			@Override
            protected void doFinished(final Object result) {
				if (this == latest) { // i.e. it hasn't been superceded in the meantime. 
					final int size = ((Integer)result).intValue();
					setValue(size);
				}
			}
			@Override
            protected void doError(final Throwable ex) {
				// no point reporting -just fail gracefully
				if (this == latest) {
					setValue(-1);
				}
			}
			@Override
            protected void doAlways() {
				if (this == latest) {
					setIndeterminate(false);
					latest = null;
				}
			}
		};
		latest.start();
	}
	
	/** returns true if current query is 
	 * not syntactically incorrect, and doesn't return an oversize set of results (if the prevent oversize preference is true)
	 * @return
	 */
	public boolean isValidQuery() {

	    return  !minus1
	     && (! sizer.isPreventOversizeQueries() || getValue() <= sizer.getOversizeThreshold());
	}

	private final HashSet<QuerySizeListener> listeners = new HashSet<QuerySizeListener>();
	
	public void addQuerySizeListener(final QuerySizeListener l) {
	    listeners.add(l);
	}
	
	public void removeQuerySizeListener(final QuerySizeListener l) {
	    listeners.remove(l);
	}
	
	private void notifyListeners() {
	    final boolean valid = isValidQuery();
	    for (final QuerySizeListener l : listeners) {
            if (valid) {
                l.validQuery();
            } else {
                l.invalidQuery();
            }
        }
	}
	
	   private void fireInvalid() {
	        for (final QuerySizeListener l : listeners) {
	                l.invalidQuery();	            
	        }
	    }
	
	
	/** listener interface for components interested in the validity of queries */
	public interface QuerySizeListener extends EventListener {
	    /** messaged when a query is determined to be valid*/
	    public void validQuery();
	    /** messaged when a query is determined to be invalid */
	    public void invalidQuery();
	}
	
	
	
}
