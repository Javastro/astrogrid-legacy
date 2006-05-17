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

/** Enhances standard posion text field by being able to resolve object names
 * into positions using simbad.
 * @author Noel Winstanley
 * @since May 16, 20068:12:34 AM
 */
public class NameResolvingPositionTextField extends PositionTextField {

    
    /**
     * Method: PositionTextField constructor
     * @param ses cds type resolover service for resolving given objects into positions in the sky.
     */
    public NameResolvingPositionTextField(UIComponent parent) {
        super();
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

    
    private class SesameResolver extends AbstractFormatter {
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
     * via this method. after resolution, and all other times, this method returns null
     * @return
     */
    public String getObjectName() {
    	return objectName;
    }

    private void resolveNameToPosition(final String inputPos) throws ParseException {
    	objectName = inputPos;
    	(new BackgroundWorker(parent,"Resolving " + inputPos + " using Sesame",5000) {
			protected Object construct() throws Exception {
				return ses.sesame(inputPos.trim(),"x");   
			}
			protected void doAlways() {
				objectName=null;
			}
			protected void doFinished(Object result) {				
	            String temp = (String) result;
	            try {
	                double ra = Double.parseDouble( temp.substring(temp.indexOf("<jradeg>")+ 8, temp.indexOf("</jradeg>")));
	                double dec = Double.parseDouble( temp.substring(temp.indexOf("<jdedeg>")+ 8, temp.indexOf("</jdedeg>")));
	                setPosition(new Point2D.Double(ra,dec));
	            } catch (Throwable t) {
	            	// oh well. report, and put it back as-was.
	            	parent.showError("Failed to resolve '" + inputPos +"' to a position using Sesame",new Exception());
	            	setText(inputPos);
	            }
			}
    	}).start();

    }
}
