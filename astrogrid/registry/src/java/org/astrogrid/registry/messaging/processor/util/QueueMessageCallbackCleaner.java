package org.astrogrid.registry.messaging.processor.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class QueueMessageCallbackCleaner implements Runnable {
  // Logger.
  private Category logger = Category.getInstance(QueueMessageCallbackCleaner.class);
  
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
        proxyId = (String) keyIt.next();
        proxy = (SoapProxy) soapProxyMap.get(proxyId);
        
        if(currentTime - proxy.getLastMessageTime() > proxyLifetime) {
          proxy.shutdown();
          soapProxyMap.remove(proxyId);
        }
      }
    }
  }
  
  public void stop() {
    running = false;
  }
}
