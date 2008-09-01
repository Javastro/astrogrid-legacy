/*
 * <meta:header>
 *     <meta:licence>
 *         Copyright (C) AstroGrid. All rights reserved.
 *         This software is published under the terms of the AstroGrid Software License version 1.2.
 *         See [http://software.astrogrid.org/license.html] for details. 
 *     </meta:licence>
 *     <svn:header>
 *         $LastChangedRevision: 1099 $
 *         $LastChangedDate: 2008-07-29 18:56:24 +0100 (Tue, 29 Jul 2008) $
 *         $LastChangedBy: dmorris $
 *     </svn:header>
 * </meta:header>
 *
 */
package org.astrogrid.vospace.client.delegate ;

import java.net.URI;
import java.net.URISyntaxException;

import org.astrogrid.security.SecurityGuard;

import org.astrogrid.vospace.v11.client.system.SystemDelegate;
import org.astrogrid.vospace.v11.client.system.SystemDelegateResolver;

/**
 * Interface for an AstroGrid AGVOSpaceDelegate resolver.
 *
 */
public interface AGVOSpaceDelegateResolver
extends SystemDelegateResolver
    {

    /**
     * Create a AGVOSpaceDelegate.
     *
     */
    public AGVOSpaceDelegate resolve();

    /**
     * Create a AGVOSpaceDelegate with a SecurityGuard.
     * @param guard A SecurityGuard containing the client credentials.
     *
     */
    public AGVOSpaceDelegate resolve(SecurityGuard guard);

    }
