/*
 * $Id: CommandLineApplicationEnvironment.java,v 1.2 2008/09/03 14:18:53 pah Exp $
 *
 * Created on 24 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.commandline;

import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.environment.CannotCreateWorkingDirectoryException;
import org.astrogrid.applications.environment.WorkingDirectoryAlreadyExists;
import org.astrogrid.applications.manager.idgen.IdGen;
/**
 * Encapsulates all that is needed to run a command line application in its own workspace.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 * @TODO there seems to be nothing special just for commandline any more
 */
public class CommandLineApplicationEnvironment  extends ApplicationEnvironment {
 
   public CommandLineApplicationEnvironment(String jobstepId, org.astrogrid.community.User user, IdGen idg, Configuration config)
      throws CannotCreateWorkingDirectoryException, WorkingDirectoryAlreadyExists {

      super(jobstepId, user, idg, config);
   }

}
