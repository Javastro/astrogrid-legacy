/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoundedRangeModel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

/** A progress bar whose setText() messages fade away after a certain interval.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 11, 20073:03:04 PM
 */
public class MessageTimerProgressBar extends JProgressBar implements ActionListener {
	public static final int PERIOD = 5 * 1000; // 5 seconds
	private final Timer timer= new Timer(PERIOD,this) {{
		setRepeats(false); 
	}};
	
	{
		setStringPainted(true);
		super.setString("");
	}
	public MessageTimerProgressBar() {
		super();
	}

	public MessageTimerProgressBar(BoundedRangeModel newModel) {
		super(newModel);
	}

	public MessageTimerProgressBar(int orient, int min, int max) {
		super(orient, min, max);
	}

	public MessageTimerProgressBar(int min, int max) {
		super(min, max);
	}

	public MessageTimerProgressBar(int orient) {
		super(orient);
	}

	@Override
    public void setString(String s) {
		super.setString(s);
		if (timer != null) { // setString() gets called in parent constructor - at which point
				// timer is not yet initialized.
			timer.restart(); // restart the timer. - so it'llhappen again
		}
	}
	public void actionPerformed(ActionEvent e) { // called by the timer.
		super.setString("");
	}

}
