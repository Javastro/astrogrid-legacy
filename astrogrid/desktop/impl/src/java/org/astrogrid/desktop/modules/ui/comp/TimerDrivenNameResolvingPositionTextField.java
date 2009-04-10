/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;

import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;

/** 
 * UNUSED - replaced by user-driven alternative.
 * 
 * Position input field that also accepts object names. these are
 * resolved into positions using simbad.
 * <p/>
 * If the user enters an input into the field that the superclass parser fails to parse (i.e. not in the format ra,dec)
 * then this class contacts sesame in a background thread to attempt to resolve the name.
 * <p/>
 * While the connection to sesame is occurring, the internal model is set to a temporary 'duff' value
 * - at this point, calling {@link #getPosition()} will return a point containing {@link java.lang.Double#NaN} for both
 * ra and dec. 
 * <p/>
 * Once the resolving process completes, the internal model is updated to the correct value,
 * or an error is displayed and the input field reset to what the user entered (for ease of editing) 
 * @author Noel Winstanley
 * @since May 16, 20068:12:34 AM
 */
public class TimerDrivenNameResolvingPositionTextField extends PositionTextField implements DocumentListener, ActionListener {
    /**listener interface for components that wish to be notified of resolution behaviour. */
    public static interface ResolutionListener extends EventListener{
        /** messaged when this position text field starts resolving an object name via sesame
         * (and so the contents of the field are currently invalid)
         * @param ev
         */
        public void resolving(ResolutionEvent ev);
        /** messaged when the position text field had successfully resolved an object name
         * (and so the contents of this field is now valid)
         * @param ev
         */
        public void resolved(ResolutionEvent ev);
        
        /** messaged when the position text field has failed to resolve an object name
         * (and so contents of the field have been left as-is.
         */
        public void resolveFailed(ResolutionEvent ev);
    }
    
    /** event object fired to a resolution listener */
    public static class ResolutionEvent extends EventObject {

        /**
         * @param source
         */
        public ResolutionEvent(final Object source) {
            super(source);
        }
    }
    
	/** Construct a new resolving component */
    public TimerDrivenNameResolvingPositionTextField(final UIComponent parent, final Sesame ses) {
        super();
        this.ses = ses;
        this.parent = parent;
        // stick an 'adapter' in front of existing formatters.
        super.decimal = new SesameResolver(decimal);
        super.sexa = new SesameResolver(sexa);    
        getDocument().addDocumentListener(this);
        setToolTipText("Object name (3c273) or " + super.getToolTipText());
    }
    
    /* unused
    public NameResolvingPositionTextField(UIComponent parent, Sesame ses,Point2D p) {
        super(p);
        this.ses = ses;
        this.parent = parent;
        // stick an 'adapter' in front of existing formatters.
        super.decimal = new SesameResolver(decimal);
        super.sexa = new SesameResolver(sexa);        
        setToolTipText("Object name (3c273) or Position (187.27,+2.05 or 12:29:06.00,+02:03:08.60)");
        
    }*/
    
    //name resolver.
    private  final Sesame ses;
    // need a reference to this, as all calls to sesame need to run in background threads.
    private final UIComponent parent;

    // event machinery
    private final ArrayList<ResolutionListener> listeners= new ArrayList<ResolutionListener>();
    
    public void addResolutionListener(final ResolutionListener l) {
        listeners.add(l);
    }
    public void removeResolutionListener(final ResolutionListener l) {
        listeners.remove(l);
    }
    
    protected void fireResolving() {
        final ResolutionEvent re = new ResolutionEvent(this);
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).resolving(re);
        }
    }
    
    protected void fireResolved() {
        final ResolutionEvent re = new ResolutionEvent(this);
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).resolved(re);
        }        
    }
    protected void fireResolveFailed() {
        final ResolutionEvent re = new ResolutionEvent(this);
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).resolveFailed(re);
        }        
    }    
    
    protected class SesameResolver extends AbstractFormatter {
    	public SesameResolver(final AbstractFormatter orig) {
    		this.orig = orig;
    	}
    	private final AbstractFormatter orig;
		@Override
        public Object stringToValue(final String arg0) throws ParseException {
			// try the coordinate parser first
			objectName=null;
			try {
				final Point2D.Double p = (Point2D.Double)orig.stringToValue(arg0);
				// construct a fake sesame bean from this..
				pos = new SesamePositionBean();
				pos.setRa(p.getX());
				pos.setDec(p.getY());
				return p;
			} catch (final ParseException e) {
			}
			// if not returned yet, try name resolving..
			resolveNameToPosition(arg0);
			return new Point2D.Double(Double.NaN,Double.NaN); // temporary value, while we're finding the correct one.
		}
		@Override
        public String valueToString(final Object arg0) throws ParseException {
			return orig.valueToString(arg0);
		}
    }
    
    
    protected String objectName;
    /** while resolving is happeningm, the name being resolved can be accessed
     * via this method. after resolution, and all other times, this method returns null.
     * 
     * Can be used in client code to resolve by hand if value is not resolved already and client
     * code is unable to wait.
     */
    public String getObjectName() {
    	return objectName;
    }

    /** business end -
     * creates a background worker that calls sesame, parses result, zaps result into model
     * if sesame fails to resolve, the input is reverted. there's very little we can report to the user - as 
     * sesame doesn't give anything useful back. 
     * */
    private void resolveNameToPosition(final String inputPos) throws ParseException {
        if (latest != null) {// halt previous sizing task.
            latest.interrupt();        
        }
        setEnabled(false);
        fireResolving();                
    	objectName = inputPos;
    	pos = new SesamePositionBean(); // temporary placehoder
    	latest = new BackgroundWorker<SesamePositionBean>(parent,"Resolving " + inputPos + " using Sesame",BackgroundWorker.SHORT_TIMEOUT,Thread.MAX_PRIORITY) {
			@Override
            protected SesamePositionBean construct() throws Exception {
				return ses.resolve(inputPos.trim());   
			}
			@Override
            protected void doAlways() {
				if (this == latest) {
				    latest = null;
				    setEnabled(true);				    
				}
			}
			@Override
            protected void doError(final Throwable ex) {
			    if (this == latest) {
			        setText(inputPos); // put things back as they were.
			        if (ex instanceof NotFoundException ) {
			            parent.showTransientError("Unable to resolve " + inputPos,ExceptionFormatter.formatException(ex));
			        } else if (ex instanceof ServiceException) {
			            parent.showError("Sesame service is unavailable",ex);
			        } else{
			            super.doError(ex);
			        }
			        fireResolveFailed();
			    }
			}
			@Override
            protected void doFinished(final SesamePositionBean pos) {	
			    if (this == latest) { // i.e. hasn't been superceded by a more recent task			     
			        setPosition(new Point2D.Double(pos.getRa(),pos.getDec()));
			        fireResolved();
			    }
			}			
    	};
    	latest.start();

    }
    
    private SesamePositionBean pos = new SesamePositionBean();

	public SesamePositionBean getPositionAsSesameBean() {
		return pos;
	}
	// document listener interface.
    public void changedUpdate(final DocumentEvent e) {
        if (latest == null) { // otherwise this is a change driven by the timer anyhow.
            timer.restart();
        }
    }
    public void insertUpdate(final DocumentEvent e) {
        if (latest == null) {
            timer.restart();
        }
    }
    public void removeUpdate(final DocumentEvent e) {
        if (latest == null) {
            timer.restart();
        }
    }
    
    private final Timer timer = new Timer(2000,this) {{
        setRepeats(false);
    }};

    // called by the timer.
    public void actionPerformed(final ActionEvent e) {
        if (latest == null && StringUtils.isNotEmpty(getText())) { // if we're resolving at the moment, take no more action
            try {
                commitEdit();
            } catch (final ParseException x) {
                // don't care.
            }
        }
    }
    
    private BackgroundWorker latest;
}
