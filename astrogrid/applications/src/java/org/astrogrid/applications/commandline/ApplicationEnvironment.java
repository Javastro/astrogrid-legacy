/*
 * $Id: ApplicationEnvironment.java,v 1.2 2003/11/26 22:07:24 pah Exp $
 *
 * Created on 24 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.commandline;

import java.io.File;
public class ApplicationEnvironment {
   private File errorLog;
   private int executionId;
   private File outputLog;
   private File executionDirectory;
   private CmdLineApplication cmdLineApplication;
}
