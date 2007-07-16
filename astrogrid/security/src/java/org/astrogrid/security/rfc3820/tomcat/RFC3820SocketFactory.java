package org.astrogrid.security.rfc3820.tomcat;

import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import javax.net.ssl.TrustManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.net.jsse.JSSE14SocketFactory;


/**
 * A factory for SSL server-sockets that supports RFC3820.
 * A custom trust-manager is used to provide the RFC3820 support. Apart from
 * the trust manager, the factory is identical to its superclass which is
 * part of Tomcat's JSSE-based implementation.
 *
 * @author Guy Rixon
 */
public class RFC3820SocketFactory extends JSSE14SocketFactory {
  static Log log = LogFactory.getLog(RFC3820SocketFactory.class);
  
  /**
   * Constructs a RFC3820SocketFactory.
   */
  public RFC3820SocketFactory() {
    super();
  }

  /**
   * Gets the intialized trust managers.
   */
  protected TrustManager[] getTrustManagers(String keystoreType, String algorithm)
        throws Exception {
    
    // Trust-store type may be set in the configuration attributes declared in the
    // superclass. If it is not declared, it default to the key-store type.
    String truststoreType = (String)this.attributes.get("truststoreType");
    if (truststoreType == null) {
      truststoreType = keystoreType;
    }
    
    // Load the trust store. The superclass knows how to find it.
    KeyStore trustStore = this.getTrustStore(truststoreType);

    // Create exactly one trust manager, for X509 credentials, that knows
    // what to do with RFC3820.
    TrustManager[] tms = new TrustManager[1];
    tms[0] = new RFC3820TrustManager(this.getTrustAnchors(trustStore));
    
    return tms;
  }
  
  
  /**
   * Extracts the trust anchors from the trust store.
   * The trust store may also contain the host's credentials, so the
   * contents have to be filtered to extract only "trusted certificate"
   * entries.
   *
   * @param trustStore The trust store.
   */
  private X509Certificate[] getTrustAnchors(KeyStore trustStore) throws Exception {
    int nAnchors = 0;
    Enumeration n = trustStore.aliases();
    while (n.hasMoreElements()) {
      String alias = (String)n.nextElement();
      if (trustStore.isCertificateEntry(alias)) {
        nAnchors++;
      }
    }
    
    X509Certificate[] anchors = new X509Certificate[nAnchors];
    
    int i = 0;
    n = trustStore.aliases();
    while (n.hasMoreElements()) {
      String alias = (String)n.nextElement();
      if (trustStore.isCertificateEntry(alias)) {
        anchors[i] = (X509Certificate)(trustStore.getCertificate(alias));
        log.info("A trust anchor was loaded for " + alias);
        i++;
      }
    }
    
    return anchors;
  }
  
}
