#!/usr/bin/env python
#Run the automatable system tests.
#pre-reqs - startup AR by hand

import unittest
import xmlrpclib
import os.path
import __builtin__

# test modules
import simpletests
import bug2460
import bug2790
import bug2157
import CdsCoordinate
import CdsSesame
import CdsVizier
import IvoaSiap
import IvoaCone
import IvoaSsap
import IvoaStap
import IvoaStapVoevent
import Cea
import ConstrainingQuery
import XQueryTrials
import Plastic
import Vosi
import filetests

def setupAR():
    """connect to the ar, initialize a global variable"""
    #connect to AR
    fname = os.path.expanduser("~/.astrogrid-desktop")
    assert os.path.exists(fname),  'No AR running: Please start your AR and rerun this script'
    prefix = file(fname).next().rstrip()
    #store AR in a new global variable - define in builtin module, then visible everywhere    
    __builtin__.ar = xmlrpclib.Server(prefix + "xmlrpc")

if __name__ == '__main__':
    setupAR()
    #assemble test suite
    alltests = unittest.TestSuite([
        simpletests.suite()
        ,bug2460.suite()
        ,bug2790.suite()
        ,bug2157.suite()
        ,CdsCoordinate.suite()
        ,CdsSesame.suite()
        ,CdsVizier.suite()
        ,ConstrainingQuery.suite()
        ,IvoaCone.suite()
        ,IvoaSiap.suite() 
        ,IvoaSsap.suite()
        ,IvoaStap.suite()
        ,IvoaStapVoevent.suite()
        ,Cea.suite()
        ,Plastic.suite()
        ,Vosi.suite()
        ,XQueryTrials.suite()
        ,filetests.suite()
        
                                   ])
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(alltests)
    