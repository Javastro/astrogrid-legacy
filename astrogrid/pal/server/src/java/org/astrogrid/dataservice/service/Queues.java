package org.astrogrid.dataservice.service;

import javax.servlet.ServletException;
import org.astrogrid.cfg.ConfigFactory;

/**
 * Provides resource allocation queues.
 *
 * @author   Mark Taylor
 * @since    15 Jan 2008
 */
public class Queues {

    private static TokenQueue syncConnectionQueue;
    private static TokenQueue asyncConnectionQueue;

    /**
     * Private constructor prevents instantiation.
     */
    private Queues() {
    }

    /**
     * Returns a queue for allocation of JDBC {@link java.sql.Connection}s
     * to be used for synchronous services.
     *
     * @return  synchronous Connection queue
     */
    public static TokenQueue getSyncConnectionQueue() {
        if (syncConnectionQueue == null) {
            int limit = 5;
            limit = ConfigFactory.getCommonConfig()
                                 .getInt("datacenter.max.queries", limit);
            limit = ConfigFactory.getCommonConfig()
                                 .getInt("datacenter.max.sync.queries", limit);
            syncConnectionQueue = new TokenQueue(limit);
        }
        return syncConnectionQueue;
    }

    /**
     * Returns a queue for allocation of JDBC {@link java.sql.Connection}s
     * to be used for asynchronous services.
     *
     * @return  asynchronous Connection queue
     */
    public static TokenQueue getAsyncConnectionQueue() {
        if (asyncConnectionQueue == null) {
            int limit = 5;
            limit = ConfigFactory.getCommonConfig()
                                 .getInt("datacenter.max.queries", limit);
            limit = ConfigFactory.getCommonConfig()
                                 .getInt("datacenter.max.sync.queries", limit);
            asyncConnectionQueue = new TokenQueue(limit);
        }
        return asyncConnectionQueue;
    }

    /**
     * Utility method to provide a guaranteed non-null token for use with
     * a synchronous queue.  If a token is available, it is returned.
     * Otherwise, a suitable ServletException is thrown.
     * This method never blocks and never returns null.
     *
     * @param   queue   resource allocation queue
     * @return   non-null token from queue
     * @throws   ServletException   if no token is immediately available
     */
    public static TokenQueue.Token getSyncToken(TokenQueue queue)
            throws ServletException {
        TokenQueue.Token token = queue.getToken();
        if (token != null) {
            return token;
        }
        else {
            throw new ServletException("Too many concurrent users" +
                                       " (" + queue.getActiveLimit() + ")" +
                                       " - try again later");
        }
    }
}
