package org.astrogrid.community.server.sso;

import java.io.IOException;
import java.math.BigInteger;
import java.security.AccessControlException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import javax.security.auth.x500.X500Principal;
import org.astrogrid.community.server.security.service.SecurityServiceImpl;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.globus.gsi.CertUtil;
import org.globus.gsi.GSIConstants;
import org.globus.gsi.X509ExtensionSet;
import org.globus.gsi.bc.BouncyCastleUtil;
import org.globus.gsi.proxy.ext.GlobusProxyCertInfoExtension;
import org.globus.gsi.proxy.ext.ProxyCertInfo;
import org.globus.gsi.proxy.ext.ProxyCertInfoExtension;
import org.globus.gsi.proxy.ext.ProxyPolicy;

/**
 * A factory for making proxy certificates from long-term credentials.
 *
 * @author Guy Rixon
 */
public class ProxyFactory {
  
  /**
   * The community's collection of credentials for all users.
   */
  private CredentialStore store;
  
  /**
   * The community's password store.
   */
  private SecurityServiceImpl securityService;
  
  /**
   * Constructs a ProxyFactory.
   */
  public ProxyFactory() throws GeneralSecurityException, IOException {
    this.store = new CredentialStore();
    this.securityService = new SecurityServiceImpl();
  }
  
  /**
   * Validates the user name and password.
   */
  public void authenticate(String userName, String password)
      throws AccessControlException {
    this.securityService.authenticate(userName, password);
  }
  
  /**
   * Creates a proxy certificate from the user's stored credentials and
   * the given public key.
   *
   * @return A certificate chain starting with the new proxy.
   */
  public CertPath createProxy(String    userName,
                              String    password,
                              PublicKey proxyKey,
                              int       lifetime) throws GeneralSecurityException {
    
    // Get the user's stored certificate-chain. This is typically just one
    // EEC but could also be an EEC and a proxy.
    List certificates = this.store.getCertificateChain(userName, password);
    if (certificates.size() == 0) {
      throw new GeneralSecurityException(userName +
                                         " has an empty certificate-chain.");
    }
    
    // Generate a proxy certificate signed by the certificate on the
    // front of the stored chain. Add this to the front of the chain.
    System.out.println("Got " + certificates.size() + " certificates.");
    X509Certificate proxy = createProxyCertificate(
        (X509Certificate) (certificates.get(0)),
        this.store.getPrivateKey(userName, password),
        proxyKey,
        lifetime,
        GSIConstants.GSI_3_IMPERSONATION_PROXY,
        (X509ExtensionSet) null,
        null  // CN value: null means derive it from signing certificate.
    );
    certificates.add(0, proxy);
    
    // Return the chain as a CertPath.
    return CertificateFactory.getInstance("X509").generateCertPath(certificates);
  }

    /**
     * Creates a proxy certificate. A set of X.509 extensions
     * can be optionally included in the new proxy certificate. <BR>
     * If a GSI-2 proxy is created, the serial number of the proxy 
     * certificate will be the same as of the issuing certificate.
     * Also, none of the extensions in the issuing certificate will
     * be copied into the proxy certificate.<BR>
     * If a GSI-3 proxy is created, the serial number of the proxy
     * certificate will be picked randomly. If the issuing certificate
     * contains a <i>KeyUsage</i> extension, the extension
     * will be copied into the proxy certificate with <i>keyCertSign</i>
     * and <i>nonRepudiation</i> bits turned off. No other extensions
     * are currently copied.
     * <p>
     * This method is adapted from the class 
     * org.globus.gsi.bc.BouncyCastleCertProcessingFactory in the Java
     * CoG kit 1.4.4. That class is Copyright 1999-2006 University of Chicago,
     * licenced under Apache License, Version 2.0. In this adaptation,
     * the code is made independent of the Bouncy Castle JCE provider (but
     * remains dependent on other classes in the Bouncy Castle product). The 
     * internationalisation has been removed since AstroGrid code does not
     * support it. Currently, all the options supported by the CoG are still
     * allowed. However, only one kind of proxy is actually generated, so the
     * code could be simplified. 
     * 
     * @param issuerCert the issuing certificate
     * @param issuerKey private key matching the public key of issuer
     *        certificate. The new proxy certificate will be
     *        signed by that key.
     * @param publicKey the public key of the new certificate
     * @param lifetime lifetime of the new certificate in seconds.
     *        If 0 (or less then) the new certificate will have the
     *        same lifetime as the issuing certificate. 
     * @param proxyType can be one of 
     *        {@link GSIConstants#DELEGATION_LIMITED
     *        GSIConstants.DELEGATION_LIMITED}, 
     *        {@link GSIConstants#DELEGATION_FULL
     *        GSIConstants.DELEGATION_FULL}, 
     *        {@link GSIConstants#GSI_2_LIMITED_PROXY
     *        GSIConstants.GSI_2_LIMITED_PROXY}, 
     *        {@link GSIConstants#GSI_2_PROXY
     *        GSIConstants.GSI_2_PROXY}, 
     *        {@link GSIConstants#GSI_3_IMPERSONATION_PROXY
     *        GSIConstants.GSI_3_IMPERSONATION_PROXY}, 
     *        {@link GSIConstants#GSI_3_LIMITED_PROXY
     *        GSIConstants.GSI_3_LIMITED_PROXY},
     *        {@link GSIConstants#GSI_3_INDEPENDENT_PROXY
     *        GSIConstants.GSI_3_INDEPENDENT_PROXY},
     *        {@link GSIConstants#GSI_3_RESTRICTED_PROXY
     *        GSIConstants.GSI_3_RESTRICTED_PROXY}.
     *        If {@link GSIConstants#DELEGATION_LIMITED
     *        GSIConstants.DELEGATION_LIMITED} and if
     *        {@link CertUtil#isGsi3Enabled() CertUtil.isGsi3Enabled}
     *        returns true then a GSI-3 limited proxy will be created. If not,
     *        a GSI-2 limited proxy will be created. 
     *        If {@link GSIConstants#DELEGATION_FULL
     *        GSIConstants.DELEGATION_FULL} and if
     *        {@link CertUtil#isGsi3Enabled() CertUtil.isGsi3Enabled}
     *        returns true then a GSI-3 impersonation proxy will be created.
     *        If not, a GSI-2 full proxy will be created. 
     * @param extSet a set of X.509 extensions to be included in the new
     *        proxy certificate. Can be null. If delegation mode is 
     *        {@link GSIConstants#GSI_3_RESTRICTED_PROXY
     *        GSIConstants.GSI_3_RESTRICTED_PROXY} then 
     *        {@link org.globus.gsi.proxy.ext.ProxyCertInfoExtension 
     *        ProxyCertInfoExtension} must be present in the extension
     *        set. 
     * @param cnValue the value of the CN component of the subject of
     *        the new certificate. If null, the defaults will be used
     *        depending on the proxy certificate type created.
     * @return <code>X509Certificate</code> the new proxy certificate.
     * @exception GeneralSecurityException if a security error
     *            occurs.
     */
    protected X509Certificate createProxyCertificate(X509Certificate issuerCert, 
						     PrivateKey issuerKey,
						     PublicKey publicKey,
						     int lifetime,
						     int proxyType,
						     X509ExtensionSet extSet,
						     String cnValue) 
	throws GeneralSecurityException {
	
	if (proxyType == GSIConstants.DELEGATION_LIMITED) {
	    int type = BouncyCastleUtil.getCertificateType(issuerCert);
            if (CertUtil.isGsi4Proxy(type)) {
		proxyType = GSIConstants.GSI_4_LIMITED_PROXY;
	    } else if (CertUtil.isGsi3Proxy(type)) {
		proxyType = GSIConstants.GSI_3_LIMITED_PROXY;
	    } else if (CertUtil.isGsi2Proxy(type)) {
		proxyType = GSIConstants.GSI_2_LIMITED_PROXY;
	    } else {
                // default to Globus OID
		proxyType = (CertUtil.isGsi3Enabled()) ? 
		    GSIConstants.GSI_3_LIMITED_PROXY :
		    GSIConstants.GSI_2_LIMITED_PROXY;
	    }
	} else if (proxyType == GSIConstants.DELEGATION_FULL) {
	    int type = BouncyCastleUtil.getCertificateType(issuerCert);
            if (CertUtil.isGsi4Proxy(type)) {
		proxyType = GSIConstants.GSI_4_IMPERSONATION_PROXY;
            } else if (CertUtil.isGsi3Proxy(type)) {
		proxyType = GSIConstants.GSI_3_IMPERSONATION_PROXY;
	    } else if (CertUtil.isGsi2Proxy(type)) {
		proxyType = GSIConstants.GSI_2_PROXY;
	    } else {
                // Default to Globus OID
		proxyType = (CertUtil.isGsi3Enabled()) ? 
		    GSIConstants.GSI_3_IMPERSONATION_PROXY :
		    GSIConstants.GSI_2_PROXY;
	    }
	}

	X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

	org.globus.gsi.X509Extension x509Ext = null;
	BigInteger serialNum = null;
	String delegDN = null;
	
	if (CertUtil.isGsi3Proxy(proxyType) || 
            CertUtil.isGsi4Proxy(proxyType) ) {
	    Random rand = new Random();
	    delegDN = String.valueOf(Math.abs(rand.nextInt()));
	    serialNum = new BigInteger(20, rand);
	
	    if (extSet != null) {
		x509Ext = extSet.get(ProxyCertInfo.OID.getId());
                if (x509Ext == null) {
                    x509Ext = extSet.get(ProxyCertInfo.OLD_OID.getId());
                }
	    }
	    
	    if (x509Ext == null) {
		// create ProxyCertInfo extension
		ProxyPolicy policy = null;
                if (CertUtil.isLimitedProxy(proxyType)) {
                    policy = new ProxyPolicy(ProxyPolicy.LIMITED);
                } else if (CertUtil.isIndependentProxy(proxyType)) {
                    policy = new ProxyPolicy(ProxyPolicy.INDEPENDENT);
                } else if (CertUtil.isImpersonationProxy(proxyType)) {
                    //since limited has already been checked, this should work.
                    policy = new ProxyPolicy(ProxyPolicy.IMPERSONATION);
		} else if 
                    ((proxyType == GSIConstants.GSI_3_RESTRICTED_PROXY) 
                     || (proxyType == GSIConstants.GSI_4_RESTRICTED_PROXY)) {
		    throw new IllegalArgumentException(
                        "You can't make a restricted proxy unless you "+
                        "pass a ProxyCertInfo extension"
                    );
		} else {
		    throw new IllegalArgumentException(
                        "An invalid type of proxy was requested."
                    );
		}
		
		ProxyCertInfo proxyCertInfo = new ProxyCertInfo(policy);
		x509Ext = new ProxyCertInfoExtension(proxyCertInfo);
                if (CertUtil.isGsi4Proxy(proxyType)) {
                    // RFC compliant OID
                    x509Ext = new ProxyCertInfoExtension(proxyCertInfo);
                } else {
                    // old OID
                    x509Ext = new GlobusProxyCertInfoExtension(proxyCertInfo);
                }
	    }

	    try {
		// add ProxyCertInfo extension to the new cert
		certGen.addExtension(x509Ext.getOid(),
				     x509Ext.isCritical(),
				     x509Ext.getValue());
		
		// handle KeyUsage in issuer cert
		TBSCertificateStructure crt = 
		    BouncyCastleUtil.getTBSCertificateStructure(issuerCert);

		X509Extensions extensions = crt.getExtensions();
		if (extensions != null) {
		    X509Extension ext;
		    
		    // handle key usage ext
		    ext = extensions.getExtension(X509Extensions.KeyUsage);
		    if (ext != null) {

			// TBD: handle this better
			if (extSet != null && 
			    (extSet.get(X509Extensions.KeyUsage.getId()) 
                             != null)) {
			    throw new GeneralSecurityException(
                                "KeyUsage extension present in issuer certificate, " +
                                "but also provided in X509ExtensionSet. " +
                                "This is unsupported."
                            );
			}
			    
			DERBitString bits = (DERBitString)BouncyCastleUtil
                            .getExtensionObject(ext);
		    
			byte [] bytes = bits.getBytes();

			// make sure they are disabled
			if ((bytes[0] & KeyUsage.nonRepudiation) != 0) {
			    bytes[0] ^= KeyUsage.nonRepudiation;
			}

			if ((bytes[0] & KeyUsage.keyCertSign) != 0) {
			    bytes[0] ^= KeyUsage.keyCertSign;
			}

			bits = new DERBitString(bytes, bits.getPadBits());

			certGen.addExtension(X509Extensions.KeyUsage,
					     ext.isCritical(),
					     bits);
		    }
		}

	    } catch (IOException e) {
		// but this should not happen
		throw new GeneralSecurityException(e.getMessage());
	    }
	    
	} else if (proxyType == GSIConstants.GSI_2_LIMITED_PROXY) {
	    delegDN = "limited proxy";
	    serialNum = issuerCert.getSerialNumber();
	} else if (proxyType == GSIConstants.GSI_2_PROXY) {
	    delegDN = "proxy";
	    serialNum = issuerCert.getSerialNumber();
	} else {
	    throw new IllegalArgumentException(
                "Unsupported proxy type: " + proxyType
            );
	}

	// add specified extensions
	if (extSet != null) {
	    Iterator iter = extSet.oidSet().iterator();
	    while(iter.hasNext()) {
		String oid = (String)iter.next();
		// skip ProxyCertInfo extension
		if (oid.equals(ProxyCertInfo.OID.getId()) ||
                    oid.equals(ProxyCertInfo.OLD_OID.getId())) {
		    continue;
		}
		x509Ext = (org.globus.gsi.X509Extension)extSet.get(oid);
		certGen.addExtension(x509Ext.getOid(),
				     x509Ext.isCritical(),
				     x509Ext.getValue());
	    }
	}

        // Derive the distinguished name (DN) of the proxy certificate.
        // This DN is the issuer's DN with an extra CN= part (the value of
        // which is derived above). In the derivation, use the RFC 2253
        // string-form of the DN in which the CN part comes at the beginning.
        String issuerDN = 
            issuerCert.getSubjectX500Principal().getName(X500Principal.RFC2253);
        String proxyDn = "CN=" +
                         ((cnValue == null) ? delegDN : cnValue) +
                         "," +
                         issuerDN;
        
        certGen.setSubjectDN(new X500Principal(proxyDn));
        certGen.setIssuerDN(issuerCert.getSubjectX500Principal());

        certGen.setSerialNumber(serialNum);
        certGen.setPublicKey(publicKey);
        certGen.setSignatureAlgorithm(issuerCert.getSigAlgName());

	GregorianCalendar date =
	    new GregorianCalendar(TimeZone.getTimeZone("GMT"));
	/* Allow for a five minute clock skew here. */
	date.add(Calendar.MINUTE, -5);
	certGen.setNotBefore(date.getTime());

        /* If hours = 0, then cert lifetime is set to user cert */
        if (lifetime <= 0) {
            certGen.setNotAfter(issuerCert.getNotAfter());
        } else {
	    date.add(Calendar.MINUTE, 5);
	    date.add(Calendar.SECOND, lifetime);
            certGen.setNotAfter(date.getTime());
        }
	
	/**
	 * FIXME: Copy appropriate cert extensions - this should NOT be done
	 * the last time we talked to Doug E. This should investigated more.
	 */

	return certGen.generate(issuerKey);
    }
    
    private X509ExtensionSet createExtensionSet(ProxyCertInfo proxyCertInfo) {
	X509ExtensionSet set = null;
	if (proxyCertInfo != null) {
	    set = new X509ExtensionSet();
	    set.add(new ProxyCertInfoExtension(proxyCertInfo));
	}	
	return set;
    }
  
}
