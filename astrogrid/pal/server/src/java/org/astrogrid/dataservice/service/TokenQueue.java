package org.astrogrid.dataservice.service;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Queue with a fixed maximum number of active positions, typically
 * used for controlling allocation of a limited number of resources.
 * Positions in the queue are taken by acquiring a {@link #Token} object,
 * with either the blocking method {@link #waitForToken} or the
 * non-blocking method {@link #getToken}.  If there are active positions
 * free, both these methods will return immediately with a token.
 * If all the active positions are used, <code>getToken</code> returns
 * immediately with a null, and <code>waitForToken</code> blocks until
 * one is free.  Tokens should be released when they are no longer in
 * use (though when they are garbage collected they release themselves).
 *
 * @author   Mark Taylor
 * @since    15 Jan 2007
 */
public class TokenQueue {

    private final int maxActive;
    private final Collection activeSet; // collection of References to Tokens
    private final List queueList;       // collection of References to Tokens
    private int iToken;

    private static Log log = LogFactory.getLog(TokenQueue.class);

    /**
     * Constructor.
     *
     * @param   maxActive  number of active positions in the queue
     */
    public TokenQueue(int maxActive) {
        this.maxActive = maxActive;
        this.activeSet = new ArrayList();
        this.queueList = new ArrayList();
    }

    /**
     * Non-blocking token acquisition method.
     * Returns a new token if an active position is free, otherwise returns
     * null.
     *
     * @return  token or null
     */
    public synchronized Token getToken() {
        if (activeSet.size() < maxActive) {
            Token token = createToken();
            addToCollection(token, activeSet);
            logState();
            return token;
        }
        else {
            return null;
        }
    }

    /**
     * Blocking token acquisition method.
     * Waits until an active position is free, and returns a token 
     * representing it.
     *
     * @return  token
     */
    public Token waitForToken() throws InterruptedException {
        Token token;
        synchronized (this) {
            token = createToken();
            addToCollection(token, queueList);
            adjustCollections();
            logState();
        }
        synchronized (token) {
            while (! collectionContains(token, activeSet)) {
                token.wait();
            }
        }
        return token;
    }

    /**
     * Returns the maximum number of active positions in this queue.
     *
     * @return  maximum active tokens
     */
    public int getActiveLimit() {
        return maxActive;
    }

    /**
     * Returns the number of currently active tokens.
     *
     * @return   number of active positions currently occupied
     */
    public int getActiveCount() {
        return activeSet.size();
    }

    /**
     * Returns the number of items waiting for an active position.
     *
     * @return  number of pending calls to {@link #waitForToken}
     */
    public int getWaitingCount() {
        return queueList.size();
    }

    public synchronized String toString() {
        return new StringBuffer()
            .append("TokenQueue: ")
            .append("active: ")
            .append(collectionToString(activeSet))
            .append("; ")
            .append("waiting: ")
            .append(collectionToString(queueList))
            .toString();
    }

    /**
     * Creates a new token for use in this queue.
     *
     * @return  new token
     */
    private synchronized Token createToken() {
        return new Token(++iToken);
    }

    /**
     * Ensures the internal state of this queue is correct following a
     * change to the active and/or queue lists.  Specifically, it will
     * attempt to fill any free active postions from the queue list.
     */
    private synchronized void adjustCollections() {
        while (queueList.size() > 0 && activeSet.size() < maxActive) {
            Reference ref = (Reference) queueList.remove(0);
            Token token = (Token) ref.get();
            if (token != null) {
                activeSet.add(ref);
                assert collectionContains(token, activeSet);
                synchronized (token) {
                    token.notifyAll();
                }
            }
        }
    }

    /**
     * Removes a given token from a collection of references to tokens,
     * and updates state appropriately.
     *
     * @param  token  token which is a referent of one of the References in 
     *                <code>collection</code>
     * @param  collection  collection of References to Tokens
     * @return  true iff a removal was made
     */
    private synchronized boolean removeFromCollection(Token token,
                                                      Collection collection) {
        boolean removed = false;
        for (Iterator it = collection.iterator(); it.hasNext(); ) {
            Reference ref = (Reference) it.next();
            Token tok = (Token) ref.get();

            // Must check for null as well as the requested token here,
            // since the reference may have been cleared.  In this case it's
            // not guaranteed that the correct reference is being removed,
            // but there's no point keeping cleard references in the list
            // in any case.
            if (tok == null || tok.equals(token)) {
                it.remove();
                removed = true;
            }
        }
        if (removed) {
            adjustCollections();
        }
        return removed;
    }

    /**
     * Adds a token to a collection of references.
     *
     * @param  token  token to add
     * @param  collection  collection of references
     */
    private synchronized void addToCollection(Token token,
                                              Collection collection) {
        collection.add(new WeakReference(token));
    }

    /**
     * Indicates whether a collection of references contains a reference
     * to a given token.
     *
     * @param  token  token to find
     * @param  collection  collection of references
     * @return   true iff the token was found
     */
    private boolean collectionContains(Token token, Collection collection) {
        boolean found = false;
        for (Iterator it = collection.iterator(); it.hasNext();) {
            Token t = (Token) ((Reference) it.next()).get();
            found = found || token.equals(t);
        }
        return found;
    }

    /**
     * Stringifies a collection of references.
     *
     * @param  collection  collection of references
     * @return  string reperesentation of collection
     */
    private String collectionToString(Collection collection) {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("(");
        for (Iterator it = collection.iterator(); it.hasNext();) {
            sbuf.append(((Reference) it.next()).get());
            if (it.hasNext()) {
                sbuf.append(", ");
            }
        }
        sbuf.append(")");
        return sbuf.toString();
    }

    /**
     * Writes the current state of this queue through the logging system.
     */
    private void logState() {
        log.info(this);
    }

    /**
     * Token representing one of the active positions in this queue.
     * Don't forget to release when no longer in use.
     */
    public class Token {
        private final int index;

        /**
         * Private constructor.
         *
         * @param  index   token creation counter
         */
        private Token(int index) {
            this.index = index;
        }

        /**
         * Call this method to indicate that this token is no longer required -
         * the active position in the queue is thereby relinquished for use
         * by others.
         */
        public void release() {
            synchronized (TokenQueue.this) {
                if (removeFromCollection(this, queueList) ||
                    removeFromCollection(this, activeSet)) {
                    log.info("Token " + this + " released");
                    logState();
                }
                else {
                    throw new IllegalArgumentException("Token " + this +
                                                       " is not active");
                }
            }
        }

        protected void finalize() throws Throwable {
            try {
                synchronized (TokenQueue.this) {
                    if (removeFromCollection(this, queueList) ||
                        removeFromCollection(this, activeSet)) {
                        log.info("Token " + this + " became unreachable");
                        logState();
                    }
                }
            }
            finally {
                super.finalize();
            }
        }

        public String toString() {
            return "T" + Integer.toString(index);
        }
    }
}
