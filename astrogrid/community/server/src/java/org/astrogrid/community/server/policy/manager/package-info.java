/**
 * Classes for managing user accounts. They form a layer on top of
 * Castor JDO for manipulating the user database.
 * <p>
 * {@link AccountManagerImpl} is for direct manipulation of account records.
 * It is used in many servlets of the community web-application.
 * {@link Account} is a support class for AccountManagerImpl.
 * <p>
 * {@link PolicyManagerImpl} implements the SOAP service PolicyManager.
 * This service is obsolete and due to be removed. When that happens,
 * PolicyManagerImpl will be removed from the package.
 * <p>
 * {@link MockNodeManagerDelegate} mocks the file-manager interface of
 * AstroGrid MySpace; it is used for unit testing.
 * <p>
 * {@link VOSpace} is a manager for the remote storage associated with a
 * user account, either in VOSpace or AstroGrid MySpace. It is used when
 * allocating a home-space for an account. {@link CredentialResolver} is
 * a support class for VOSpace.
 */
package org.astrogrid.community.server.policy.manager;