package org.astrogrid.security.rfc3820;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.globus.gsi.X509ExtensionSet;
import org.globus.gsi.bc.BouncyCastleUtil;
import org.globus.gsi.proxy.ext.GlobusProxyCertInfoExtension;
import org.globus.gsi.proxy.ext.ProxyCertInfo;
import org.globus.gsi.proxy.ext.ProxyCertInfoExtension;
import org.globus.gsi.proxy.ext.ProxyPolicy;

/**
 *
 * @author Guy Rixon
 */
public class ProxyCertificateFactory {
  
  /**
   * Constructs a ProxyCertificateFactory.
   */
  public ProxyCertificateFactory() {
  }
  
  /**
   * Creates a proxy certificate.
   * <p>
   * The proxy is a full-delegation proxy, a.k.a. "impersonation proxy". Its
   * encoding can follow either Globus GSI-3 rules or RFC 3830 rules (a.k.a.
   * GSI-4 rules) depending on the rfcCompliant parameter. A ProxyCertInfo
   * extension is always added, and a KeyUsage extension will be added if the
   * issuer certificate has one. The KeyUsage extension is copied from the
   * issuer certificate and the keyCertSign and nonRepudiation bits are
   * turned off. No other extension are added.
   * <p>
   * The subject of the proxy certificate is the issuer's subject with one
   * added common-name element (RDN), as required by RFC3820. The value of this 
   * added RDN is a random integer. The serial number of the proxy certificate
   * is another random integer. Thus, the serial number and subject are
   * not guaranteed to be unique among all proxies by this issuer, but
   * uniqueness is highly likely; RFC 3820 allows this.
   * <p>
   * This method is adapted from the class 
   * org.globus.gsi.bc.BouncyCastleCertProcessingFactory in the Java
   * CoG kit 1.4.4. That class is Copyright 1999-2006 University of Chicago,
   * licenced under Apache License, Version 2.0. In this adaptation,
   * the code is made independent of the Bouncy Castle JCE provider (but
   * remains dependent on other classes in the Bouncy Castle product); the 
   * internationalisation has been removed since AstroGrid code does not
   * support it; only impersonation proxies of GSI-3 and GSI-4 are produced;
   * the caller may not set the RDN for the proxy; the caller may not
   * specify exstensions to the proxy certificate.
   * 
   * @param issuerCert the issuing certificate
   * @param issuerKey private key matching the public key of issuer
   *        certificate. The new proxy certificate will be
   *        signed by that key.
   * @param publicKey the public key of the new certificate
   * @param lifetime lifetime of the new certificate in seconds.
   *        If 0 (or less then) the new certificate will have the
   *        same lifetime as the issuing certificate.
   * @param rfcCompliant If true, proxy compies with RFC 3820; otherwise with Globus.
   * @return The new proxy certificate.
   * @throws GeneralSecurityException For any failure.
   */
  public X509Certificate createProxyCertificate(X509Certificate issuerCert, 
                                                PrivateKey issuerKey,
                                                PublicKey publicKey,
                                                int lifetime,
                                                boolean rfcCompliant) 
      throws GeneralSecurityException {
	
    X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
	
    // Build up the structure describing the proxy nature.
    // The nested objects match the nesting of ASN.1 definitions.
    // The outermost structure is an X.509 certificate-extension.
    ProxyPolicy policy = new ProxyPolicy(ProxyPolicy.IMPERSONATION);
    ProxyCertInfo proxyCertInfo = new ProxyCertInfo(policy);
    org.globus.gsi.X509Extension x509Ext = null;
    if (rfcCompliant) {
      // RFC-3820-compliant OID
      x509Ext = new ProxyCertInfoExtension(proxyCertInfo);
    }
    else {
      // Globus OID
      x509Ext = new GlobusProxyCertInfoExtension(proxyCertInfo);
    }
  
    // add ProxyCertInfo extension to the new cert
    certGen.addExtension(x509Ext.getOid(),
		         x509Ext.isCritical(),
                         x509Ext.getValue());
                
    addKeyUsageExtension(issuerCert, certGen);

    Random rand = new Random();

    // Derive the distinguished name (DN) of the proxy certificate.
    // This DN is the issuer's DN with an extra CN= part, the value of
    // which is a random integer. In the derivation, use the RFC 2253
    // string-form of the DN in which the CN part comes at the beginning.
    String issuerDN = 
        issuerCert.getSubjectX500Principal().getName(X500Principal.RFC2253);
    String delegDN = String.valueOf(Math.abs(rand.nextInt()));
    String proxyDn = "CN=" + delegDN + "," + issuerDN;
    certGen.setSubjectDN(new X500Principal(proxyDn));
    certGen.setIssuerDN(issuerCert.getSubjectX500Principal());

    BigInteger serialNum = new BigInteger(20, rand);
    certGen.setSerialNumber(serialNum);
    
    certGen.setPublicKey(publicKey);
    certGen.setSignatureAlgorithm(issuerCert.getSigAlgName());

    GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
    date.add(Calendar.MINUTE, -5); // Allow for a five minute clock skew here.
    certGen.setNotBefore(date.getTime());
    // If hours = 0, then cert lifetime is set to that of user cert
    if (lifetime <= 0) {
      certGen.setNotAfter(issuerCert.getNotAfter());
    }
    else {
      date.add(Calendar.MINUTE, 5);
      date.add(Calendar.SECOND, lifetime);
      certGen.setNotAfter(date.getTime());
    }

    return certGen.generate(issuerKey);
  }
  
  /**
   * Adds a new proxy-certificate to a given chain. The proxy is pushed onto
   * the front of the chain, at position zero. The proxy is signed with
   * the private key matching the certificate at the front of the given chain.
   *
   * @param chain The certificate chain to be extended.
   * @param issuerKey The private key matching the certificate at the front of the given chain.
   * @param proxyKey The public key to be signed into the proxy certificate.
   * @param lifetime The duration of validity of the proxy, in seconds.
   * @param rfcCompliant True for an RFC-3820 proxy, false for a GSI-3 proxy.
   * @throws GeneralSecurityException If the proxy cannot be created.
   */
  public void extendCertificateChain(
      List<X509Certificate> chain,
      PrivateKey            issuerKey,
      PublicKey             proxyKey,
      int                   lifetime,
      boolean               rfcCompliant) throws GeneralSecurityException {
    assert chain != null;
    assert chain.size() > 0;
    
    X509Certificate proxy = createProxyCertificate(chain.get(0),
                                                   issuerKey,
                                                   proxyKey,
                                                   lifetime,
                                                   rfcCompliant);
    chain.add(0, proxy);
  }
  
  
    
    private X509ExtensionSet createExtensionSet(ProxyCertInfo proxyCertInfo) {
	X509ExtensionSet set = null;
	if (proxyCertInfo != null) {
	    set = new X509ExtensionSet();
	    set.add(new ProxyCertInfoExtension(proxyCertInfo));
	}	
	return set;
    }
  
  /**
   * Causes the proxy certificate to have a key-usage extension if needed.
   * The need for such an esxtension depends on its presence in the issuer's
   * certificate.
   *
   * @param issuerCert The issuers certificate.
   * @param certGen    The certificate generator, to receive the extension.
   * @throws GeneralSecurityException If the issuer certificate is incorrect.
   * @throws GeneralSecurityException If the extension cannot be written.
   */ 
  private void addKeyUsageExtension(X509Certificate            issuerCert,
                                    X509V3CertificateGenerator certGen)
      throws GeneralSecurityException {
    try {
      TBSCertificateStructure crt =
          BouncyCastleUtil.getTBSCertificateStructure(issuerCert);
      X509Extensions extensions = crt.getExtensions();
      if (extensions != null) {
        X509Extension ext = extensions.getExtension(X509Extensions.KeyUsage);
        if (ext != null) {
	  DERBitString bits = (DERBitString)BouncyCastleUtil.getExtensionObject(ext);
          byte[] bytes = bits.getBytes();

          // make sure they are disabled (WTF?)
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
  }
  
}
