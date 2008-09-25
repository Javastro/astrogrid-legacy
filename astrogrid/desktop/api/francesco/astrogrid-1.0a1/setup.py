#!/usr/bin/env python
# Author: Francesco Pierfederici <fpierfed@eso.org>.
# Licensed under the Academic Free License version 2.0 (see LICENSE.txt). 
from distutils.core import setup, Extension
from distutils import sysconfig
import sys

##############################################################################
#                                   Setup                                    #
##############################################################################
setup(name='astrogrid',
    version='1.0a1',
    description='Python package to interface with services offered by the Astrogrid Client Runtime (ACR).',
    author='Francesco Pierfederici',
    author_email='fpierfed@eso.org',
    url='http://www.eurovotech.org/',
    package_dir = {'astrogrid': '.'}, 
    packages = ['astrogrid'],
    ext_modules = [],
)
