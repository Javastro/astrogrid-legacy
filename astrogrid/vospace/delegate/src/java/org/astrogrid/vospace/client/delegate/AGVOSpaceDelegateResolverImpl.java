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

import java.net.URL;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.security.SecurityGuard;
import org.astrogrid.registry.client.query.v1_0.RegistryService;

import org.astrogrid.vospace.v11.client.transfer.inport.InportHandler ;
import org.astrogrid.vospace.v11.client.transfer.export.ExportHandler ;

import org.astrogrid.vospace.v11.client.system.SystemDelegateResolver;
import org.astrogrid.vospace.v11.client.system.SystemDelegateResolverImpl;

/**
 * AstroGrid VOSpaceDelegateResolver implementation.
 * @deprecated Please use the SystemDelegateResolverImpl from the vospace client package.
 *
 */
public class AGVOSpaceDelegateResolverImpl
extends SystemDelegateResolverImpl
implements AGVOSpaceDelegateResolver
    {

    /**
     * Our debug logger.
     *
     */
    protected static Log log = LogFactory.getLog(AGVOSpaceDelegateResolverImpl.class);

    /**
     * Public constructor, using the default RegistryService (from local AstroGrid config).
     *
     */
    public AGVOSpaceDelegateResolverImpl()
        {
        super();
        }

    /**
     * Public constructor, using the default RegistryService (from local AstroGrid config).
     * @param inporters An iterable list of InportHandlers.
     * @param exporters An iterable list of ExportHandlers.
     *
     */
    public AGVOSpaceDelegateResolverImpl(Iterable<InportHandler> inporters, Iterable<ExportHandler> exporters)
        {
        super(
            inporters,
            exporters
            );
        }

    /**
     * Public constructor.
     * @param registry The registry endpoint URL.
     *
     */
    public AGVOSpaceDelegateResolverImpl(URL registry)
        {
        super(
            registry
            );
        }

    /**
     * Public constructor.
     * @param registry A RegistryService delegate.
     *
     */
    public AGVOSpaceDelegateResolverImpl(RegistryService registry)
        {
        super(
            registry
            );
        }

    /**
     * Public constructor.
     * @param registry  The registry endpoint URL.
     * @param inporters An iterable list of InportHandlers.
     * @param exporters An iterable list of ExportHandlers.
     *
     */
    public AGVOSpaceDelegateResolverImpl(URL registry, Iterable<InportHandler> inporters, Iterable<ExportHandler> exporters)
        {
        super(
            registry,
            inporters,
            exporters
            );
        }

    /**
     * Public constructor.
     * @param registry A RegistryService delegate.
     * @param inporters An iterable list of InportHandlers.
     * @param exporters An iterable list of ExportHandlers.
     *
     */
    public AGVOSpaceDelegateResolverImpl(RegistryService registry, Iterable<InportHandler> inporters, Iterable<ExportHandler> exporters)
        {
        super(
            registry,
            inporters,
            exporters
            );
        }

    /**
     * Create a SystemDelegate.
     *
     */
    public AGVOSpaceDelegate resolve()
        {
        log.debug("AGVOSpaceDelegateResolverImpl.resolve()");
        return new AGVOSpaceDelegateImpl(
            this.resolver()
            );
        }

    /**
     * Create a SystemDelegate with a SecurityGuard.
     * @param guard A SecurityGuard containing the client credentials.
     *
     */
    public AGVOSpaceDelegate resolve(SecurityGuard guard)
        {
        log.debug("AGVOSpaceDelegateResolverImpl.resolve(SecurityGuard)");
        log.debug("  Guard [" + ((null != guard) ? guard.getX500Principal() : null) + "]");
        return new AGVOSpaceDelegateImpl(
            this.resolver(),
            guard
            );
        }

    }
