/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.geom.Point2D;
import java.text.ParseException;

import javax.swing.SwingUtilities;

import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;

/** Enhances standard posion text field by also accepting  object names - these are
 * resolved into positions using simbad.
 * <p/>
 * Usage. Construct. Then call {@link #setSesame(Sesame)}. Component is now configured.
 * <p/>
 * If the user enters an input into the field that the superclass parser fails to parse (i.e. not in the format ra,dec)
 * then this class contacts sesame in a background thread to attempt to resolve the name.
 * <p/>
 * While the connection to sesame is occurring, the internal model is set to a temporary 'duff' value
 * - at this point, calling {@link #getPosition()} will return a point containing {@link Double.NaN} for both
 * ra and dec. 
 * <p/>
 * Once the resolving process completes, the internal model is updated to the correct value,
 * or an error is displayed and the input field reset to what the user entered (for ease of editing) 
 * @author Noel Winstanley
 * @since May 16, 20068:12:34 AM
 */
public class NameResolvingPositionTextField extends PositionTextField {

	/** Construct a new resolving component */
    public NameResolvingPositionTextField(UIComponent parent) {
        super();
        this.parent = parent;
        // stick an 'adapter' in front of existing formatters.
        super.decimal = new SesameResolver(decimal);
        super.sexa = new SesameResolver(sexa);        
    }
    
    public NameResolvingPositionTextField(UIComponent parent, Point2D p) {
        super(p);
        this.parent = parent;
        // stick an 'adapter' in front of existing formatters.
        super.decimal = new SesameResolver(decimal);
        super.sexa = new SesameResolver(sexa);        
    }
    
    // setter - necessary to work-around an architectural glitch.
    public void setSesame(Sesame ses) {
    	this.ses = ses;
    }
    
    //name resolver.
    private  Sesame ses;
    // need a reference to this, as all calls to sesame need to run in background threads.
    private final UIComponent parent;

    
    protected class SesameResolver extends AbstractFormatter {
    	public SesameResolver(AbstractFormatter orig) {
    		this.orig = orig;
    	}
    	private final AbstractFormatter orig;
		public Object stringToValue(String arg0) throws ParseException {
			// try the coordinate parser first
			try {
				return orig.stringToValue(arg0);
			} catch (ParseException e) {
			}
			// if not returned yet, try name resolving..
			resolveNameToPosition(arg0);
			return new Point2D.Double(Double.NaN,Double.NaN); // temporary value, while we're finding the correct one.
		}
		public String valueToString(Object arg0) throws ParseException {
			return orig.valueToString(arg0);
		}
    }
    
    
    protected String objectName;
    /** while resolving is happeningm, the name being resolved can be accessed
     * via this method. after resolution, and all other times, this method returns null.
     * 
     * Can be used in client code to resolve by hand if value is not resolved already and client
     * code is unable to wait.
     * @return
     */
    public String getObjectName() {
    	return objectName;
    }

    /** business end -
     * creates a background worker that calls sesame, parses result, zaps result into model
     * if sesame fails to resolve, the input is reverted. there's very little we can report to the user - as 
     * sesame doesn't give anything useful back. 
     * @todo refactor so that this code is reusable - as have almost same routine in astroscope too.
     * */
    private void resolveNameToPosition(final String inputPos) throws ParseException {
    	objectName = inputPos;
    	(new BackgroundWorker(parent,"Resolving " + inputPos + " using Sesame",5000) {
			protected Object construct() throws Exception {
				return ses.sesame(inputPos.trim(),"x");   
			}
			protected void doAlways() {
				objectName=null;
			}
			protected void doError(Throwable ex) {
				setText(inputPos); // put things back as they were.
			}
			protected void doFinished(Object result) {				
	            String temp = (String) result;
	            try {
	                double ra = Double.parseDouble( temp.substring(temp.indexOf("<jradeg>")+ 8, temp.indexOf("</jradeg>")));
	                double dec = Double.parseDouble( temp.substring(temp.indexOf("<jdedeg>")+ 8, temp.indexOf("</jdedeg>")));
	                setPosition(new Point2D.Double(ra,dec));
	            } catch (Throwable t) {
	            	doError(t);
	            }
			}
    	}).start();

    }
}
