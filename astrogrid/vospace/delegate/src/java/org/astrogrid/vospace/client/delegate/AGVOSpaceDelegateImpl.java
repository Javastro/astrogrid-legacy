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

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.security.SecurityGuard;

import org.astrogrid.vospace.v11.client.system.SystemDelegate;
import org.astrogrid.vospace.v11.client.system.SystemDelegateImpl;

import org.astrogrid.vospace.v11.client.service.ServiceDelegateResolver;
import org.astrogrid.vospace.v11.client.service.ServiceDelegateResolverImpl;

/**
 * AstroGrid VOSpace delegate implementation.
 *
 */
public class AGVOSpaceDelegateImpl
extends SystemDelegateImpl
implements AGVOSpaceDelegate
    {

    /**
     * Our debug logger.
     *
     */
    protected static Log log = LogFactory.getLog(AGVOSpaceDelegateImpl.class);

    /**
     * Public constructor.
     *
     */
    public AGVOSpaceDelegateImpl(ServiceDelegateResolver resolver)
        {
        super(resolver);
        }

    /**
     * Public constructor.
     *
     */
    public AGVOSpaceDelegateImpl(ServiceDelegateResolver resolver, SecurityGuard guard)
        {
        super(resolver, guard);
        }


    }
