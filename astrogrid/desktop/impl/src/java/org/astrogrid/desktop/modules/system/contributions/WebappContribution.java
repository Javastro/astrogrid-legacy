/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

/** Contributes a webapp to deploy into the servlet container.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 27, 20089:56:35 PM
 */
public class WebappContribution {
    private String context;
    private String location;
    public final String getContext() {
        return this.context;
    }
    public final void setContext(final String context) {
        this.context = context;
    }
    public final String getLocation() {
        return this.location;
    }
    public final void setLocation(final String location) {
        this.location = location;
    }
}
