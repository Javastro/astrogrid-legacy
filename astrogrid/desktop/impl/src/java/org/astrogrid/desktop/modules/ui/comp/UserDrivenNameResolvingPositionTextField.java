/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.geom.Point2D;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JButton;

import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.TimerDrivenNameResolvingPositionTextField.ResolutionEvent;
import org.astrogrid.desktop.modules.ui.comp.TimerDrivenNameResolvingPositionTextField.ResolutionListener;

/** Position input field that also accepts object names. these are
 * resolved into positions using simbad.
 * <p/>
 * Alternative implementation to {@link TimerDrivenNameResolvingPositionTextField} (which users don't like).
 * This one provides a button for the user to click to resolve the object name.
 * <p/>
 * If the user enters an input into the field that the superclass parser fails to parse (i.e. not in the format ra,dec)
 * then this class presents a button, which when clicked calls sesame to resolve the object.
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
public class UserDrivenNameResolvingPositionTextField extends PositionTextField  {


    /** Construct a new resolving component */
    public UserDrivenNameResolvingPositionTextField(final UIComponent parent, final Sesame ses) {
        super();
        this.ses = ses;
        this.parent = parent;
        // stick an 'adapter' in front of existing formatters.
        super.decimal = new SesameResolver(decimal);
        super.sexa = new SesameResolver(sexa);    
        setToolTipText("Enter an object name (e.g. 3c273) and click on glasses to resolve, or \n" + super.getToolTipText());

        progressButton = new JButton();
        progressButton.setIcon( IconHelper.loadIcon("lookup16.png"));
        indicator = new IndeterminateProgressIndicator();
        indicator.setDisplayedWhenStopped(false);
        progressButton.add(indicator);
        progressButton.setToolTipText("Resolve the object name to a position using the CDS Sesame service");
        // interestingly, the button doesn't have an action associated with it.
        // it's enough for the position text field to lose focus to trigger object resolution.
        // the button is just there to encourage people to do that.
    }

    /** access a button that can be used to trigger (and indicate progress of) name resolution */
    public JButton getProgressButton() {
        return progressButton;
    }

    //name resolver.
    private  final Sesame ses;
    // need a reference to this, as all calls to sesame need to run in background threads.
    private final UIComponent parent;

    // event machinery
    private final ArrayList<TimerDrivenNameResolvingPositionTextField.ResolutionListener> listeners= new ArrayList<ResolutionListener>();

    protected String objectName;
    private SesamePositionBean pos = new SesamePositionBean();

    private BackgroundWorker latest;

    private final JButton progressButton;
    private final IndeterminateProgressIndicator indicator;    

    public void addResolutionListener(final ResolutionListener l) {
        listeners.add(l);
    }


    /** while resolving is happeningm, the name being resolved can be accessed
     * via this method. after resolution, and all other times, this method returns null.
     * 
     * Can be used in client code to resolve by hand if value is not resolved already and client
     * code is unable to wait.
     */
    public String getObjectName() {
        return objectName;
    }
    public SesamePositionBean getPositionAsSesameBean() {
        return pos;
    }

    public void removeResolutionListener(final ResolutionListener l) {
        listeners.remove(l);
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
        indicator.startAnimation();
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
                    indicator.stopAnimation();
                }
            }
            @Override
            protected void doError(final Throwable ex) {
                if (this == latest) {
                    setText(inputPos); // put things back as they were.			 
                    parent.showTransientError("Unable to resolve '" + inputPos + "'",ExceptionFormatter.formatException(ex));
                    fireResolveFailed();
                }
            }
            @Override
            protected void doFinished(final SesamePositionBean newPos) {	
                if (this == latest) { // i.e. hasn't been superceded by a more recent task			     
                    setPosition(new Point2D.Double(newPos.getRa(),newPos.getDec()));
                    // record this position bean
                    pos = newPos;
                    fireResolved();
                }
            }			
        };
        latest.start();

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
    protected void fireResolving() {
        final ResolutionEvent re = new ResolutionEvent(this);
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).resolving(re);
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
}
