package org.astrogrid.datacenter.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import org.astrogrid.dataservice.service.TokenQueue;

public class TokenQueueTest extends TestCase {

    public TokenQueueTest(String name) {
        super(name);
    }

    public void testQueue() throws InterruptedException {

        // Note: this test could fail, since it relies on some assumptions
        // about threading and garbage collection.
        int nActive = 3;
        final TokenQueue q = new TokenQueue(nActive);
        List tokenList = new ArrayList();
        for (int i = 0; i < 6; i++) {
            TokenQueue.Token token = q.getToken();
            if (i < nActive) {
                assertTrue(token != null);
                tokenList.add(token);
            }
            else {
                assertTrue(token == null);
            }
            assertEquals(Math.min(nActive, i + 1), q.getActiveCount());
            assertEquals(0, q.getWaitingCount());
        }
        Collections.shuffle(tokenList);
        for (Iterator it = tokenList.iterator(); it.hasNext();) {
            ((TokenQueue.Token) it.next()).release();
        }

        int nWait = 10;
        Thread[] waiters = new Thread[nWait];
        for (int i = 0; i < nWait; i++ ) {
            final int index = i;
            waiters[index] = new Thread() {
                public void run() {
                    {
                        TokenQueue.Token token = null;
                        try {
                            token = q.waitForToken();
                            Thread.sleep(100*index);
                        }
                        catch (InterruptedException e) {
                        }
                        finally {
                            if (token != null) {
                                if ((index % 2) == 0) {
                                    token.release();
                                }
                            }
                        }
                    }
                    System.gc();
                }
            };
            waiters[index].start();
        }
        for (int i = 0; i < nWait; i++) {
            waiters[i].join();
        }
        Thread.sleep(100);
        assertEquals(0, q.getWaitingCount());
        TokenQueue.Token token = q.getToken();
        assertTrue(token != null);
        token.release();
        assertEquals(0, q.getWaitingCount());
    }
}
