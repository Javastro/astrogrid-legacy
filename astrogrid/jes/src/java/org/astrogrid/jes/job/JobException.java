package org.astrogrid.jes.job;

import org.astrogrid.jes.JesException;

public class JobException extends JesException {
 
    public JobException(String message,Exception exception) {
        super(message,exception);
    }
    
    public JobException(String message) {
        super(message);
    }
}
