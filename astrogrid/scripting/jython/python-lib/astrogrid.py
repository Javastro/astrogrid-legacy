#Python script to set up classpath, etc.
import sys
import os
astro_dir='../lib'
for j in os.listdir(astro_dir):
	sys.path.append(astro_dir + os.sep + j)

#work around - forces classes to be loaded from new path.
import VOTableUtil

import org.astrogrid.scripting.Astrogrid
#fails at moment, can't initialize log4j.
# part of a wider problem with classloading in jython. ongoing.

Astrogrid = org.astrogrid.scripting.Astrogrid.getInstance()