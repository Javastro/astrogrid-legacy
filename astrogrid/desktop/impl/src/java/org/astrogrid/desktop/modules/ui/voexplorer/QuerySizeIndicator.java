package org.astrogrid.desktop.modules.ui.voexplorer;

import java.awt.Color;

import javax.swing.JProgressBar;
import javax.swing.UIManager;
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

	public QuerySizeIndicator(UIComponent parent,QuerySizer qs) {
		this(parent,qs,500,1000); //@todo make this configurable.
	}
	
	public QuerySizeIndicator(UIComponent parent,QuerySizer qs,int goodThresh, int acceptableThresh) {
		super(HORIZONTAL);
		this.parent = parent;
		this.sizer = qs;
		//System.out.println(UIManager.getColor("ProgressBar.selectionForeground")); //204,204,204, osX 0,0,0
        //System.out.println(UIManager.getColor("ProgressBar.selectionBackground")); // 102, 102, 153 osX 255,255,255
        //System.out.println(UIManager.getColor("ProgressBar.background"));        // for metal, same as foreground, osx unefined
		setUI(new MetalProgressBarUI());// as OSX l&f doesn't allow bar to change color.
		Color bgColor = UIManager.getColor("ProgressBar.background");
		if (bgColor == null) { // not provided by osX laf
		    bgColor = Color.DARK_GRAY;
		}
        setBackground(bgColor);
		
		setStringPainted(true);
		setMinimum(0);
		setValue(-1);
		setToolTipText("Indicates how many resources this query is likely to return");
		this.goodThresh = goodThresh;
		this.acceptableThresh = acceptableThresh;
		(new BackgroundWorker(parent,"Finding registry size") {
		    {
		        setTransient(true);
		    }
			protected Object construct() throws Exception {
				return sizer.regSize();
			}
			protected void doFinished(Object result) {
				int size = ((Integer)result).intValue();
				setMaximum(size);		
			}
		}).start();			
	}

	private final QuerySizer sizer;		
	private final UIComponent parent;		
	private final int goodThresh;
	private final int acceptableThresh;
	
	public void setValue(int n) {
		super.setValue(n);
		if (n < 0) {
			setString("Incomplete query");
		} else {
			setString("Matches " + n + " of " + getMaximum() + " resources");
		}
		if (n <= goodThresh) {
			setForeground(Color.GREEN);
		} else if (n <= acceptableThresh) {
			setForeground(Color.YELLOW);
		} else {
			setForeground(Color.RED);
		}                           
	}
	private BackgroundWorker latest;
	public void setValue(final SRQL query) {
		setIndeterminate(true);
		setString("Computing query size");
		if (latest != null) { // previous sizing task running.
			latest.interrupt();
		}
		latest = new BackgroundWorker(parent,"Computing query size",5000,Thread.MAX_PRIORITY) {

			protected Object construct() throws Exception {
				return sizer.size(query);
			}
			protected void doFinished(Object result) {
				if (this == latest) { // i.e. it hasn't been superceded in the meantime. 
					int size = ((Integer)result).intValue();
					setValue(size);
				}
			}
			protected void doError(Throwable ex) {
				// no point reporting -just fail gracefully
				if (this == latest) {
					setValue(-1);
				}
			}
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
		latest = new BackgroundWorker(parent,"Computing query size",5000,Thread.MAX_PRIORITY) {

			protected Object construct() throws Exception {
				return sizer.size(query);
			}
			protected void doFinished(Object result) {
				if (this == latest) { // i.e. it hasn't been superceded in the meantime. 
					int size = ((Integer)result).intValue();
					setValue(size);
				}
			}
			protected void doError(Throwable ex) {
				// no point reporting -just fail gracefully
				if (this == latest) {
					setValue(-1);
				}
			}
			protected void doAlways() {
				if (this == latest) {
					setIndeterminate(false);
					latest = null;
				}
			}
		};
		latest.start();
	}
	
}