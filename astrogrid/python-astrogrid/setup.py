__id__='$Id: setup.py,v 1.1 2009/02/12 12:29:14 egs Exp $'


# First check for appropriate Python version
import sys
python_version = sys.version.split()[0].split(".")
if map(int, python_version[:2]) < [2, 4]:
	print "You need at least Python version 2.4"
	sys.exit()

print 'Python version %s detected. Running setup.\n' % ('.'.join(python_version))

# Some checks here to warn people of faulty Python installations
try:
	import urllib2
	import md5
except:
	raise

# We use setuptools
from ez_setup import use_setuptools
use_setuptools()

import os
import sys
import string

execfile(os.path.join("astrogrid", "release.py"))

from setuptools import setup, find_packages

setup(name = "astrogrid",
      version = version,
      description = "Python Interface to ACR",
      author = author,
      author_email = email,
      maintainer_email = email,
      license = "http://www.astrogrid.org/LICENSE",
      long_description=long_description,
	  zip_safe=True,
      url = "http://www.astrogrid.org",
      platforms = ["Linux","Solaris","Mac OS X","Win"],
      packages = ['astrogrid','astrogrid.examples'],
      package_dir = {'astrogrid': 'astrogrid', 'astrogrid.examples': 'astrogrid/examples'},
      package_data = {'astrogrid': ['*.chm']}
      )

sys.exit()

