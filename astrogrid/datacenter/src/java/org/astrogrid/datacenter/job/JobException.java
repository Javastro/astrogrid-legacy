/*
 * @(#)JobException.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.datacenter.job;

import org.astrogrid.AstroGridException;
import org.astrogrid.i18n.AstroGridMessage;
/** exception type for jobs */
public class JobException extends AstroGridException {

    public JobException( AstroGridMessage message ) {
      super( message ) ;
    }

    public JobException( AstroGridMessage message, Exception exception ) {
      super( message, exception ) ;
    }

}
