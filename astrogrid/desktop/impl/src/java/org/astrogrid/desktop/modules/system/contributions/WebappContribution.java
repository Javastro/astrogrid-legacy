/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

/** contribution bean that describes a webapp to deploy.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 27, 20089:56:35 PM
 */
public class WebappContribution {
    private String context;
    private String location;
    public final String getContext() {
        return this.context;
    }
    public final void setContext(String context) {
        this.context = context;
    }
    public final String getLocation() {
        return this.location;
    }
    public final void setLocation(String location) {
        this.location = location;
    }
}
