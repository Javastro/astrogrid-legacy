package org.astrogrid.registry.messaging.processor.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Category;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class QueueMessageCallbackCleaner implements Runnable {
  // Logger.
  private Category logger = Category.getInstance(getClass());
  
  // True if we are running.
  private boolean running;
  
  // Timeout values.
  private long sleepTime;
  private long proxyLifetime;

  // Map identifiers to SOAP proxies.
  private Map soapProxyMap;

  public QueueMessageCallbackCleaner(long sleepTime, long proxyLifetime, Map soapProxyMap) {
    super();
    
    this.sleepTime = sleepTime;
    this.proxyLifetime = proxyLifetime;
    this.soapProxyMap = soapProxyMap;
  }

  /**
   * @see java.lang.Runnable#run()
   */
  public void run() {
    running = true;
    
    long currentTime = 0;
    
    Set keySet = null;
    Iterator keyIt = null;
    String proxyId = null;
    SoapProxy proxy = null;
    
    while(running) {
      currentTime = System.currentTimeMillis();
      
      keySet = soapProxyMap.keySet();
      keyIt = keySet.iterator();
      while(keyIt.hasNext()) {
        // Sleep until cleanup time
        try {
          Thread.sleep(sleepTime);
        }
        catch(InterruptedException e) {
          // continue processing
        }

        // Clean up old proxies.
        proxyId = (String) keyIt.next();
        proxy = (SoapProxy) soapProxyMap.get(proxyId);
        
        if(currentTime - proxy.getLastMessageTime() > proxyLifetime) {
          try {
            logger.debug("[run] shutting down proxy: " + proxyId + " ...");
            proxy.shutdown();
            soapProxyMap.remove(proxyId);
            logger.debug("[run] ... shut down proxy");
          }
          catch(ServiceException e) {
            logger.error("[run] failed to shutdown proxy, id: " + proxyId, e);
          }
        }
      }
    }
  }
  
  public void stop() {
    running = false;
  }
}
