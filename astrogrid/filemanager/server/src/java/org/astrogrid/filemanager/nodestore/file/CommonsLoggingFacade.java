/*$Id: CommonsLoggingFacade.java,v 1.2 2005/03/11 13:37:05 clq2 Exp $
 * Created on 24-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.nodestore.file;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.transaction.util.LoggerFacade;


/** irritating - jakarta transactions doesn't integration with commons logging yet. need to write my own facade for it... ho hum */
class CommonsLoggingFacade implements LoggerFacade {

    public CommonsLoggingFacade(Log logger) {
        this.logger = logger;
    }
    protected final Log logger;
    
    /**
     * @see org.apache.commons.transaction.util.LoggerFacade#createLogger(java.lang.String)
     */
    public LoggerFacade createLogger(String arg0) {
        return new CommonsLoggingFacade(LogFactory.getLog(arg0));
    }

    /**
     * @see org.apache.commons.transaction.util.LoggerFacade#logInfo(java.lang.String)
     */
    public void logInfo(String arg0) {
        logger.info(arg0);
    }

    /**
     * @see org.apache.commons.transaction.util.LoggerFacade#logFine(java.lang.String)
     */
    public void logFine(String arg0) {
        logger.debug(arg0);
    }

    /**
     * @see org.apache.commons.transaction.util.LoggerFacade#isFineEnabled()
     */
    public boolean isFineEnabled() {
        return logger.isDebugEnabled();
    }

    /**
     * @see org.apache.commons.transaction.util.LoggerFacade#logFiner(java.lang.String)
     */
    public void logFiner(String arg0) {
        logger.trace(arg0);
    }

    /**
     * @see org.apache.commons.transaction.util.LoggerFacade#isFinerEnabled()
     */
    public boolean isFinerEnabled() {
        return logger.isTraceEnabled();
    }

    /**
     * @see org.apache.commons.transaction.util.LoggerFacade#logFinest(java.lang.String)
     */
    public void logFinest(String arg0) {
        logger.trace(arg0);
    }

    /**
     * @see org.apache.commons.transaction.util.LoggerFacade#isFinestEnabled()
     */
    public boolean isFinestEnabled() {
        return logger.isTraceEnabled();
    }

    /**
     * @see org.apache.commons.transaction.util.LoggerFacade#logWarning(java.lang.String)
     */
    public void logWarning(String arg0) {
        logger.warn(arg0);
    }

    /**
     * @see org.apache.commons.transaction.util.LoggerFacade#logWarning(java.lang.String, java.lang.Throwable)
     */
    public void logWarning(String arg0, Throwable arg1) {
        logger.warn(arg0,arg1);
    }

    /**
     * @see org.apache.commons.transaction.util.LoggerFacade#logSevere(java.lang.String)
     */
    public void logSevere(String arg0) {
        logger.error(arg0);
    }

    /**
     * @see org.apache.commons.transaction.util.LoggerFacade#logSevere(java.lang.String, java.lang.Throwable)
     */
    public void logSevere(String arg0, Throwable arg1) {
        logger.error(arg0,arg1);
    }
}

/* 
$Log: CommonsLoggingFacade.java,v $
Revision 1.2  2005/03/11 13:37:05  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:38  nw
split code inito client and server projoects again.

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.1  2005/02/25 12:33:27  nw
finished transactional store
 
*/