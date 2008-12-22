#!/usr/bin/env python
#exercise the apihelp componennt, and visually inspect formatting of
# generated documentation. not a lot of asserting going on here.

import unittest

class ApiHelpExercise(unittest.TestCase):
    def setUp(self):
        self.api = ar.system.apihelp
    def notestModuleHelp(self):
        for m in self.api.listModules():
            print self.api.moduleHelp(m)
    def notestComponentHelp(self):
        for m in self.api.listModules():
            for c in self.api.listComponentsOfModule(m):
                print self.api.componentHelp(c)
                print
    def testMethodHelp(self):
        m = 'ivoa'
        for c in self.api.listComponentsOfModule(m):
            for meth in self.api.listMethodsOfComponent(c):
                print self.api.methodHelp(meth)
                print
                
    def notestAllMethodAndComponentHelp(self):
        for m in self.api.listModules():
            for c in self.api.listComponentsOfModule(m):
                print self.api.componentHelp(c)                
                for meth in self.api.listMethodsOfComponent(c):                
                    print self.api.methodHelp(meth)
                    print
def suite():
    return unittest.TestLoader().loadTestsFromTestCase(ApiHelpExercise)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    