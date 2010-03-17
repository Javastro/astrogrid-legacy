/**
 * RFC3820-enabled implementation of SSL for Apache Tomcat 6.0.
 * <p>
 * RFC3820 defines proxy certificates. This SSL implementation allows
 * Tomcat to authenticate users from certificate chains that contain such
 * certificates; c.f. normal PKIX rules which forbid proxies.
 * <p>
 * To support RFC3820 in SSL, the SSL trust-manager must be patched to
 * handle proxy certificates.
 * {@link org.astrogrid.security.rfc3820.tomcat6.RFC3820Trustmanager} is a
 * suitable manager. The trust manager uses the
 * {@link org.astrogrid.security.rfc3820.tomcat6.CertificateChainValidator}
 * which in turn uses
 * {@link org.astrogrid.security.rfc3820.tomcat6.ProxyCertPathReviewer}.
 * <p>
 * The {@link org.astrogrid.security.rfc3820.tomcat6.JSSESocketFactory} is a
 * patch to Tomcat's equivalent class that imposes the trust manager of choice.
 * <p>
 * The other classes are copied directly from the implementation of SSL in
 * Tomcat 6.0.26.
 * <p>
 * All the classes that do logging use JULI APIs. This makes them
 * compatible with Tomcat 6.0 but incompatible with Tomcat 5.5.
 */
package org.astrogrid.security.rfc3820.tomcat6;