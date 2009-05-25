/**
 * A certificate authority for use inside the community service.
 * Each community service may own and operate one certificate authority (CA), 
 * and these classes are the CA software. CAs in different communities are 
 * distinguished by their "root" credentials, and these are read from the
 * software environment rather than being contained in this package. The
 * cryptographic operations are done using openssl (the native executable,
 * run in a  sub-process).
 */
package org.astrogrid.community.server.ca;