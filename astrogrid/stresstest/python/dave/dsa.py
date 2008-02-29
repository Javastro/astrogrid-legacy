"""
A wrapper for running ADQL queries on DSA applications
"""
from cea import Application 

class Dataset(Application):
    """
    Represents a DSA-style application.
    
    Usage:
    dsa = Dataset(ar, "ivo://wfau.roe.ac.uk/twomass-dsa/ceaApplication")
    print dsa.execute(ar, query, output)
    """

    def __init__(self, ar, ivorn):

        Application.__init__(self, ar, ivorn, "adql")

    def execute(self, ar, query, output):

        return Application.execute(self, ar, {'Query':query, 'Format' : 'VOTABLE' },{ 'Result':output})

    def executeasynch(self, ar, query, output, callback):

        return Application.executeasynch(self, ar, {'Query':query, 'Format' : 'VOTABLE' },{ 'Result':output}, callback)

