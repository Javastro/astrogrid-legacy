/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.awt.Color;

/** A single annotation made by a user.
 * 
 * extends the external annotation with additional fields only available to the 
 * user.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 18, 20077:16:17 PM
 */
public class UserAnnotation extends Annotation {

	/** a boolean flag that can be set / unset for the resource */
	private boolean flagged;
	
	/** a color to highlight the resource in */
	private Color highlight;

	/**
	 * @return the flagged
	 */
	public final boolean isFlagged() {
		return this.flagged;
	}

	/**
	 * @param flagged the flagged to set
	 */
	public final void setFlagged(final boolean flagged) {
		this.flagged = flagged;
	}

	/**
	 * @return the highlight
	 */
	public final Color getHighlight() {
		return this.highlight;
	}

	/**
	 * @param highlight the highlight to set
	 */
	public final void setHighlight(final Color highlightColour) {
		this.highlight = highlightColour;
	}
	
}
