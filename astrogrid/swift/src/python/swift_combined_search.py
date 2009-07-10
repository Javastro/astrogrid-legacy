#!/usr/bin/env python
"""
Swift script for combined image and catalogue search.

Inputs: 
  (1) ra, dec, radius in degrees
  (2) control file of info regarding VO services
  (3) path to a directory to contain results

Outputs:
  (1) image files received from services
  (2) starHits.csv file of data regarding candidate stars
  (3) galaxyHits.csv file of data regarding candidate galaxies

Notes.
  (1) The csv files are sorted rows of comma separated data merged from the different sources.
      They are sorted on spherical outset distance from the input arguments.
      Each row is tagged with the source name taken from the control file.
      As each source can contribute different columns, each value is preceded
      by a keyword giving its variable name (which maps to a column name).
  (2) Here is a complete example of a control file, with appropriate comments:

SWIFT SEARCH CONTROL FILE
######################################################################################
# Continuation lines are supported by the backslash convention...
#      line one \
#      line two
# Be careful! Continuation lines are horrid if you make a mistake.
#
IMAGE SECTION:
######################################################################################
# The Image Section contains service ivorns for SIAP searches.
# The first line contains the minimum and maximum number of images to retrieve 
# from a given source of images, or its mirrors. If the minimum is achieved, 
# any siblings will be terminated.
[ 1 2 ]
#
# The following lines obey this format...
# integer
# [ short name for service | ivorn of service | bandpasscolumn:bandpassvalue]
#
# where integer is the number of services to use as alternatives for a given source.
# What is an 'alternative' is down to your judgement. It could be a complete mirror,
# or something much looser.
# The bandpasscolumn and value define which VOTable column should be used to choose which
# image to receive, and what value that column needs to have.
######################################################################################
# DSS Image:
2
#[STSCI | ivo://archive.stsci.edu/dss/dss2 | ]
[ESO_DSS | ivo://archive.eso.org/dss | survey:DSS2-blue]
[HEASARC | ivo://nasa.heasarc/skyview/dss2 | ]
#[DSSBLUE | ivo://archive.stsci.edu/dss/dss2blue | ]


#
# 2Mass Image:
1
[IRSA2 | ivo://irsa.ipac/2MASS-QL | BandPass_ID:K]
#[NED | ivo://ned.ipac/Image]

CATALOGUE SECTION:
######################################################################################
# The Catalogue Section contains service ivorns and associated data for cone searches.
#
# The format is...
# integer
# [ short name for service | service ivorn | table name | column names | search function name | URL:ID | filtercolumn ]
#
# where:
#  . integer is the number of services to use as alternatives for a given source.
#    What is an 'alternative' is down to your judgement. It could be a complete mirror,
#    or something much looser.
#  . table name (if required, but leave blank for the moment!)
#  . column names are a set of paired mappings of column names to variables names 
#    eg:  Bmag:B where Bmag is column name and B is variable name  
#  . The search function name is a real function name in the code, so beware!
#    There are three sorts: three generic functions (searchStars, searchGalaxies and searchXrays)
#    and one where code has to be hand crafted to decide between stars and galaxies.
#    Look at the comments below and the associated function names.   
#  . If Dist appears as a partner in a column:variable name combination, it is 
#    assumed to be the spherical offset distance calculated and returned by the 
#    service. If it does not appear, it is calculated by the script and inserted
#    into the final results (eg: see entries for 6df below).
#    The penultimate field allows an HTML lookup of the details from the catalogue. If the source
#    is a Vizier catalogue the first part can be blank, otherwise it should contain
#    the full URL with %VAR% in place of the variable to be filled
#    After the colon the second part gives the name of the column whose contents will
#    be substituted into %VAR%
#    The final field can be omitted, if provided it should contain a column:value pair
#    and a stipulation that column=value will be added to the query
#####################################################################################
# SDSS for stars and galaxies:
1
[SDSS | ivo://CDS.VizieR/II/282 |  | _r:Dist RAJ2000:RA DEJ2000:Dec umag:u gmag:g rmag:R imag:i zmag:z | searchSDSS | !SDSS ]
#
# APM for galaxies:
1
[APM-N | ivo://CDS.VizieR/I/267 |  | _r:Dist RAJ2000:RA DEJ2000:Dec rmag:R rDiam:RDiam bmag:B \ 
  bDiam:BDiam | searchGalaxies | !APM-ID]
#
# PSCz for galaxies:
1
[PSCz | ivo://CDS.VizieR/VII/221 |  | _r:Dist _RAJ2000:RA _DEJ2000:Dec Bmag:B MajAxiO:Maj \
  MinAxiO:Min  | searchGalaxies | !recno ]
#
# USNO for stars and galaxies:
1
[USNO-B1 | ivo://CDS.VizieR/I/284 |  | _r:Dist RAJ2000:RA DEJ2000:Dec B1mag:B1 R1mag:R1 B2mag:B2 \
  R2mag:R2 Imag:I  | searchUSNO | !USNO-B1.0 ]
#
# 2MASS for stars:
1
[2MASS | ivo://CDS.VizieR/II/246 |  | _r:Dist RAJ2000:RA DEJ2000:Dec Jmag:j Hmag:h Kmag:k | searchStars | !2MASS ]
#
# NED
#[NED | ivo://ned.ipac/Basic_Data_Near_Position | | 

#SSS
#1
#[SSS | ivo://wfau.roe.ac.uk/ssa-dsa/ssa | Source | ra:RA dec:Dec classMagB:B classMagR1:R1 classMagR2:R2 classMagI:I meanClass:class  | searchGalaxies | surveys.roe.ac.uk:8080/ssa/SSASQL?sqlstmt=select%20*%20from%20Source%20where%20objID=%VAR%&action=freeform&server=amachine&format=HTML!objID | meanClass:1 ]

# Xray master
1
[Xray | ivo://uk.ac.le.star.tmpledas/ledas/ledas/xray | | ra:RA decl:Dec count_rate:RATE flux:FLUX observatory:MISSION name:NAME | searchXrays | ledas-www.star.le.ac.uk/arnie5/arnie5.php?catname=xray&querymode=advanced&cat_equinox=2000&xcoordinates=&coordinates=&csystem=Equatorial&equinox=2000&search=cone&radius=0.333333333333333&squared=1&ra_dim=1&dec_dim=1&outcoord=sexigesimal&outsys=D&outequ=J2000&outputformat=html&nlines=10000&process.x=7&process.y=15&process=Submit&display%5B%5D=name&filter1_name=%3D&min_name=%VAR%&filter2_name=%3C&max_name=&display%5B%5D=database_table&filter1_database_table=%3E&min_database_table=&filter2_database_table=%3C&max_database_table=&display%5B%5D=ra&filter1_ra=%3E&min_ra=&filter2_ra=%3C&max_ra=&display%5B%5D=decl&filter1_decl=%3E&min_decl=&filter2_decl=%3C&max_decl=&display%5B%5D=lii&filter1_lii=%3E&min_lii=&filter2_lii=%3C&max_lii=&display%5B%5D=bii&filter1_bii=%3E&min_bii=&filter2_bii=%3C&max_bii=&display%5B%5D=count_rate&filter1_count_rate=%3E&min_count_rate=&filter2_count_rate=%3C&max_count_rate=&display%5B%5D=count_rate_error&filter1_count_rate_error=%3E&min_count_rate_error=&filter2_count_rate_error=%3C&max_count_rate_error=&display%5B%5D=error_radius&filter1_error_radius=%3E&min_error_radius=&filter2_error_radius=%3C&max_error_radius=&display%5B%5D=flux&filter1_flux=%3E&min_flux=&filter2_flux=%3C&max_flux=&display%5B%5D=exposure&filter1_exposure=%3E&min_exposure=&filter2_exposure=%3C&max_exposure=&display%5B%5D=class&filter1_class=%3E&min_class=&filter2_class=%3C&max_class=&display%5B%5D=observatory&filter1_observatory=%3E&min_observatory=&filter2_observatory=%3C&max_observatory=&sortby=_r!name]

"""
import string, sys, os, time, thread, math, getopt
import xmlrpclib
from astrogrid import acr
from astrogrid import utils
from astrogrid.threadpool import easy_pool
from urllib import *
from urlparse import *
from math import *

#=================================================================
#   Look for comment 'Mainline' for the main process logic.
#   It is at the bottom of this script.
#
# Notes:
# (1) What if the service has more than one table?
#     The standard python ConeSearch seems to imply that this can
#     be the case for dsa; it has the dsatab keyword on the 
#     execute function.
# (2) We have a windows problem with VOTables.
#        I think it is within the utils module...
#=================================================================
#
# Global variables to control logging:
FEEDBACK = True
ERROR = True
WARN = True
DEBUG = False
TRACE = False
EXTREME_DEBUG = False
EXTREME_TRACE = False
#
# Global variable for controlling the image format returned.
# Can be a coma-delimited string:
# FORMAT = 'image/fits,image/png,image/jpeg,image/gif'
#
# Can be set to 'ALL', but you must be prepared to filter.
# Some services provide lots of bumf, eg: html with requests.
FORMAT = 'image/fits'
#
# Some obscure python thing for enabling class methods.
class Callable:
    def __init__(self, anycallable):
        self.__call__ = anycallable

##################################################################
# Base class for all services.
# 
##################################################################
class Service:
    #
    # I assume this code is run at class loading time.
    # I certainly hope so. It has global implications as it
    # loads the Astrogrid runtime.
    ar = None
    tables = None
    try:    
        #
        # Attempt to connect to acr
        fname = os.path.expanduser( "~/.astrogrid-desktop" )
        if os.path.exists( fname ) == False:
            if ERROR: print 'No AR running. Please start your AR and rerun this script.'
            sys.exit(1)
        prefix = file( fname ).next().rstrip()
        ar = xmlrpclib.Server( prefix + "xmlrpc" )
        tables = ar.util.tables
    except Exception, e:
        if ERROR: print 'Could not use AR in establishing service environment: ', e
        sys.exit(1)
    #
    # All services have a name and ivorn.
    # Each service is also passed the search arguments (ra, dec, radius)
    # rather than having them as global variables. This will facilitate 
    # extending the script to searching a number of areas.
    # The votable will hold the results of the search.
    # The dispatched flag helps in selecting a service for dispatching
    # on a separate thread. See ServiceGroup.
    # The terminateFlag is used to signal termination across threads;
    # it is provided by ServiceGroup.
    # 
    def __init__( self, name, ivorn, ra, dec, radius ):
        self.dispatchedFlag = False
        self.name = name
        self.ivorn = ivorn
        self.ra = ra
        self.dec = dec
        self.radius = radius
        self.votable = None
        self.terminateFlag = None
        return
    #
    # Generic searchAndRetrieve() meant to be overridden.
    def searchAndRetrieve( self ):    
        return
    #
    # Stringify the instance. Used by print.
    # Good for debugging purposes.
    def __str__( self ):
        return 'Service Name: ' + self.name + '\n' \
               'Ivorn: ' +  self.ivorn + '\n' \
             'ra: ' + str(self.ra)  + '\n' \
             'dec: ' + str(self.dec)  + '\n' \
             'radius: ' + str(self.radius)  + '\n' \
             'dispatched: ' + str(self.dispatchedFlag) + '\n'


##################################################################
# ConeSearch class to substitute for the standard one,
# which seems not to allow all columns to be returned.
##################################################################
class ConeSearch( Service ):
    #
    # table is the table to search. Currently left null and not used.
    # At some point this will require change: the DSA allows you to
    # choose which table to run a cone search upon.
    # Columns is a list of column / variable names lifted from the control file.
    # searchFunctionName is the function name within this script that must be
    # invoked. Needs more thought to generalize the capability.
    
    # 2009-02-19 PAE: Added url and urlvar, and on 23, filtCol, filtval
    def __init__( self, name, ivorn, ra, dec, radius, table, columns, searchFunctionName, url, urlvar, filtCol, filtVal ):
        Service.__init__( self, name, ivorn, ra, dec, radius )
        self.table = table             # table can be None where service expects only one table!
        self.searchFunctionName = searchFunctionName
        # The columns list is in fact a list of "pairs", with each pair consisting of 
        # database column name and program variable name, separated by a colon. For example:
        # B1mag:B1
        # where B1mag is the database column name and B1 the program variable name.
        # This code forms column entities as a list of lists. For example:
        # [ [_r,Dist],[RAJ2000,RA],[DEJ2000,Dec],[rmag,R],[rDiam,RDiam],[bmag,B],[bDiam,BDiam],[APM-ID,id] ]
        self.columnList = []
        for column in columns:
            cpair = column.split(':')
            self.columnList.append( cpair )
            
        #2009-02-19 PAE:
        self.url=url
        self.urlvar=urlvar
        self.filtCol=filtCol
        self.filtVal=filtVal
        if DEBUG: print "Adding service",self.name,"with urlvar",self.urlvar, "and filtCol", self.filtCol
            
        return
    #
    # Not sure what to make of the dsatab keyword.
    # What happens normally where a service has more than one table?
    # Or is this a feature only of dsa? Ask Noel.
    def execute( self ):    
        # use AR to build the query URL       
        query = Service.ar.ivoa.cone.constructQuery( self.ivorn, self.ra, self.dec, self.radius )
        fullURL = Service.ar.ivoa.cone.addOption( query, "VERB", "3" )
        #2009-02-23 PAE: filter on a col if need be
        if (self.filtCol):
            if DEBUG: print "Adding filter for",self.name,"with",self.filtCol,"=",self.filtVal
            fullURL = Service.ar.ivoa.cone.addOption( fullURL, self.filtCol, self.filtVal )
        #2009-02-20 PAE: Use tables
        if (self.table):
            fullURL = Service.ar.ivoa.cone.addOption( fullURL, "DSATAB", self.table )
        doc = Service.tables.convertFromFile( fullURL, "votable", "votable" )
        return doc
    #
    # Stringify the service instance.
    # Good for debugging.
    def __str__( self ):
        return Service.__str__( self ) + 'Search function name: ' + self.searchFunctionName + '\n'

    #
    #    Uses the results votable to form a more friendly map of those
    # columns that can have null values returned.
    # The map is keyed on column index within a row...
    # column index : null value
    def formNullValuesMap( self ):
        global EXTREME_TRACE, EXTREME_DEBUG
        if EXTREME_TRACE: print 'formNullValuesMap() enter'
        nullVals = {}
        fields = self.votable.getFields()
        for field in fields:
            vals = field.getNodesByName( 'VALUES' )
            name = field.getAttributes()['name']
            if EXTREME_DEBUG: print 'name type: ', type(name)
            for val in vals:
                attrs = val.getAttributes()
                if EXTREME_DEBUG: 
                    print '=====attrs=====\n', attrs
                nullValue = attrs[ 'null' ]
                nullVals[ self.votable.getColumnIdx( name ) ] = nullValue
        if EXTREME_DEBUG: 
            print '=====nullVals=====\n', nullVals
        if EXTREME_TRACE: print 'formNullValuesMap() exit'
        return nullVals
    #
    # Uses the nullsMap, the value and the index
    # to return a usable value (ie: not null) or the python
    # equivalent of null (the None object).
    def testAndSetNull( self, x, index, nullsMap ):
        global EXTREME_TRACE, EXTREME_DEBUG
        if EXTREME_TRACE: print 'testAndSetNull() enter'
        if EXTREME_DEBUG: 
            print 'x: ', x, 'index: ', index
            print type(x)
            print str(x)
        retVal = None
        if x != None:
            if str(x) != 'NaN':
                if nullsMap.has_key( index ) == True:
                    if x != nullsMap[ index ]:
                        retVal = x
                else:
                    retVal = x
        if EXTREME_DEBUG: print retVal
        if EXTREME_TRACE: print 'testAndSetNull() exit'
        return retVal
    #
    # If the None object is undesirable as a value, 
    # this returns an empty string instead.    
    def testAndSetEmpty( self, x ):
        if x == None:
            return ''
        return x            
    #
    #
    # Returned values: RAJ2000 DEJ2000 umag gmag rmag imag zmag
    # Columns needed for criteria: cl == 6 for stars, 3 for galaxies
    def searchSDSS( self ):
        global TRACE, DEBUG, FEEDBACK, stars, gals
        if TRACE: print 'enter: searchSDSS()'
        if FEEDBACK: print self.name + ' returned this number of rows: ' + str( len( self.votable.getDataRows() ) )
        #
        # We decide between stars and galaxies by using the class column
        clColIndex = self.votable.getColumnIdx( 'cl' )
        #
        # Form a map for those columns that can return a null value...
        nullsMap = self.formNullValuesMap()
        if DEBUG: print nullsMap
        #
        # Here are the rest of the indices, found
        # using the votable and the column names 
        # passed to us in the control file...    
        colIndices = []
        for column in self.columnList:
            colIndices.append( self.votable.getColumnIdx( column[0] ) )
        #
        # Form local lists of stars and galaxies
        starList = []
        galList = []
        #
        # Locate the data rows
        dataRows = self.votable.getDataRows()
        for row in dataRows:
            cells = self.votable.getData(row)
            cl = self.testAndSetNull( cells[ clColIndex ], clColIndex, nullsMap )
            if cl == None:
                continue
            #
            # Only proceed if a star or galaxy...
            cl = int(cl)
            if cl != 3 and cl != 6:
                continue
            # Begin each constructed row with the service name 
            # and then append all the data requested to each row...
            subRow = [ self.name ]
            for index in colIndices:
                subRow.append( self.testAndSetEmpty( self.testAndSetNull( cells[ index ], index, nullsMap ) ) )
    
            if cl == 3:
                galList.append( subRow )
            else:
                starList.append( subRow )
        #
        # Now append found data to the overall collections....
        if len( starList ) > 0:
            stars.push( self, starList )
        else:
            if DEBUG: print 'SDSS found no stars'
        if len( galList ) > 0:
            gals.push( self, galList )
        else:
            if DEBUG: print 'SDSS found no galaxies'
        if TRACE: print 'enter: searchSDSS()'
        return
    
    def searchUSNO( self ):
        global TRACE, DEBUG, FEEDBACK, stars, gals
        if TRACE: print 'enter: searchUSNO()'
        if FEEDBACK: print self.name + ' returned this number of rows: ' + str( len( self.votable.getDataRows() ) )
        #
        # We need to decide between stars and galaxies.
        # These are just the indices for the criteria...
        b1cIndex = self.votable.getColumnIdx( 'B1s/g' ) 
        b2cIndex = self.votable.getColumnIdx( 'B2s/g' ) 
        r1cIndex = self.votable.getColumnIdx( 'R1s/g' ) 
        r2cIndex = self.votable.getColumnIdx( 'R2s/g' ) 
        icIndex = self.votable.getColumnIdx( 'Is/g' ) 
        #
        # Form a map for those columns that can return a null value...
        nullsMap = self.formNullValuesMap()
        #
        # Here are the rest of the indices, found
        # using the votable and the column names 
        # passed to us in the control file...    
        colIndices = []
        for column in self.columnList:
            colIndices.append( self.votable.getColumnIdx( column[0] ) )
        #
        # Form local lists of stars and galaxies
        starList = []
        galList = []
        #
        # Locate the data rows
        dataRows = self.votable.getDataRows()
        for row in dataRows:
            cells = self.votable.getData(row)
            #
            # Get data to decide between stars and galaxies
            # and test for both stars and galaxies!
            b1c = self.testAndSetNull( cells[ b1cIndex ], b1cIndex, nullsMap ) 
            b2c = self.testAndSetNull( cells[ b2cIndex ], b2cIndex, nullsMap ) 
            r1c = self.testAndSetNull( cells[ r1cIndex ], r1cIndex, nullsMap ) 
            r2c = self.testAndSetNull( cells[ r2cIndex ], r2cIndex, nullsMap )  
            ic = self.testAndSetNull( cells[ icIndex ], icIndex, nullsMap ) 
            bStars = self.testForStarsUSNO( b1c, b2c, r1c, r2c, ic )
            bGalaxies = self.testForGalsUSNO( b1c, b2c, r1c, r2c, ic )
            #
            # Only proceed if a star or galaxy...
            if bStars == False and bGalaxies == False :
                continue
            # Begin each constructed row with the service name 
            # and then append all the data requested to each row...
            subRow = [ self.name ]
            for index in colIndices:
                subRow.append( self.testAndSetEmpty( self.testAndSetNull( cells[ index ], index, nullsMap ) ) )
            #
            # Are stars and galaxies mutually exclusive,
            # or can there be an element of doubt?
            if bGalaxies:
                galList.append( subRow )
            elif bStars:
                starList.append( subRow )
            else:
                if DEBUG: print'An SDSS hit does not fall into the star or galaxy class.'
        #
        # Now append found data to the overall collections....
        if len( starList ) > 0:
            stars.push( self, starList )
        if len( galList ) > 0:
            gals.push( self, galList )
        if TRACE: print 'exit: searchUSNO()'
        return
    
    #
    #
    def testForStarsUSNO( self, b1c, b2c, r1c, r2c, ic ):
        global EXTREME_TRACE, EXTREME_DEBUG
        if EXTREME_TRACE: print 'enter: testForStarsUSNO()'
        #
        # If either no s/g are set, OR any of them are >= 6, call it a star
        # NOTE. There is ambiguity in the original script regarding star selection!!! PAE: so there is. Doh!
        clist = [ b1c, b2c, r1c, r2c, ic ]
        if EXTREME_DEBUG: print 'clist: ', clist
        set = False
        for c in clist:
            if c != None:
                if int(c) >= 6:
                    set = True
                    break
        if EXTREME_TRACE: print 'testForStarsUSNO() exit'
        return set
    
    def testForGalsUSNO( self, b1c, b2c, r1c, r2c, ic ):
        global EXTREME_TRACE, EXTREME_DEBUG
        if EXTREME_TRACE: print 'testForGalsUSNO() enter'
        #
      # If at least 1 s/g is set, and all those which are set are <6, call it gal
        clist = [ b1c, b2c, r1c, r2c, ic ]
        set = False
        for c in clist:
            if c != None:
                if int(c) < 6:
                    set = True
                else:
                    set = False
                    break
        if EXTREME_TRACE: print 'testForGalsUSNO() exit'
        return set
    
    #
    # Searches for unfiltered stars.
    def searchStars( self ):
        global TRACE, DEBUG, stars
        if TRACE: print 'enter: searchStars() for: ' + self.name
        stars.push( self, self.genericRetrieval() )
        if TRACE: print 'exit: searchStars() for: ' + self.name
        return
    #
    # Searches for unfiltered galaxies.
    def searchGalaxies( self ):
        global TRACE, DEBUG, gals
        if TRACE: print 'enter: searchGalaxies() for: ' + self.name
        gals.push( self, self.genericRetrieval() )
        if TRACE: print 'exit: searchGalaxies() for: ' + self.name
        return
        
    ## 2009-02-18 PAE: Added a searchXrays function
    # Searches for X-ray sources
    def searchXrays( self ):
        global TRACE, DEBUG, xrays
        if TRACE: print 'enter: searchXrays() for: ' + self.name
        xrays.push( self, self.genericRetrieval() )
        if TRACE: print 'exit: searchXrays() for: ' + self.name
        return
    
    
    def genericRetrieval( self ):
        global TRACE, DEBUG, FEEDBACK
        if TRACE: print 'enter: genericRetrieval() for ' + self.name
        if FEEDBACK: print self.name + ' returned this number of rows: ' + str( len( self.votable.getDataRows() ) )
        #
        # Here are the column indices, found
        # using the votable and the column names 
        # passed to us from the control file...    
        colIndices = []
        for column in self.columnList:
            colIndices.append( self.votable.getColumnIdx( column[0] ) )
            if DEBUG: print "Column name: ",column[0], "has index", colIndices[-1]
            
       
        if DEBUG: print 'colIndices: ', colIndices
        if DEBUG: print 'columns: ', self.columnList
        #
        # Form a map for those columns that can return a null value...
        nullsMap = self.formNullValuesMap()
        #
        # Form local list of hits
        hitList = []
        #
        # Locate the data rows
        dataRows = self.votable.getDataRows()
        for row in dataRows:
            cells = self.votable.getData(row)
            # Begin each constructed row with the service name 
            # and then append all the data requested to each row...
            subRow = [ self.name ]
            for index in colIndices:
                subRow.append( self.testAndSetEmpty( self.testAndSetNull( cells[ index ], index, nullsMap ) ) )
                
            # 2009-02-19 PAE: Need to add the urlvalue if it was set
            if (self.urlvar):
                urlindex=self.votable.getColumnIdx(self.urlvar)
                subRow.append( self.testAndSetEmpty( self.testAndSetNull( cells[ urlindex ], index, nullsMap ) ) )


            hitList.append( subRow )
            if DEBUG: print 'subRow: ', subRow
        if TRACE: print 'exit: genericRetrieval() for ' + self.name 
        return hitList

    def searchAndRetrieve( self ):
        global TRACE, DEBUG, FEEDBACK, ERROR, WARN
        if TRACE: print 'enter: searchAndRetrieve() for ' + self.name
        if self.terminateFlag.isSet():
            if FEEDBACK: print self.name + ' has been terminated prematurely!'
            return 
        try:
            if DEBUG: print 'About to coneSearch: ' + self.name, self.ra, self.dec, self.radius    
            res = self.execute()
            if res: 
                try:
                    self.votable = utils.read_votable( res, ofmt='votable' )
                    if len( self.votable.getDataRows() ) == 0 : 
                        if WARN: print self.name + ': no hits!'
                    elif self.terminateFlag.isSet():
                        if FEEDBACK: print self.service.name + ' has been terminated prematurely!'
                        return
                    else:
                        if DEBUG: 
                            print 'About to eval...'
                            print 'Service: ', self
                        eval( 'self.' + self.searchFunctionName + '()' )
                except Exception, e1:
                    if ERROR: print self.name + ': bad vot: ', e1
            else:
                if WARN: print self.name + ': no result vot!'    
        except Exception, e2:
            if ERROR: print self.name + ' cone Search failed. Incorrect URL?', e2
        self.terminateFlag.set()
        if TRACE: print 'exit: coneSearchAndRetrieve() for ' + self.name
        return    
    # end of coneSearchAndRetrieve()

##################################################################
# SiapSearch class to substitute for the standard one,
# With this one I can switch options on/off.
##################################################################
class SiapSearch( Service ):
    # 2009-02-19 PAE: Enable filtering of images by bandpass
    def __init__( self, name, ivorn, ra, dec, radius, minimages, maximages, format, bandCol, bandCrit ):
    
        Service.__init__( self, name, ivorn, ra, dec, radius )
        self.minimages = minimages
        self.maximages = maximages
        self.format = format
        # 2009-02-19 PAE: Enable filtering of images by bandpass
        self.bandCol=bandCol
        self.bandCrit=bandCrit
    
        return
                
    #
    #
    def execute( self ):    
        # use AR to build the query URL       
        query = Service.ar.ivoa.siap.constructQuery( self.ivorn, self.ra, self.dec, self.radius )
        fullURL = Service.ar.ivoa.cone.addOption( query, "FORMAT", self.format )

        # 2009-02-19 PAE: Filter to select the correct bandpass
        if(self.bandCol and self.bandCrit):
            if DEBUG: print "Filtering for ",self.bandCol,"=",self.bandCrit
            fullURL = Service.ar.ivoa.cone.addOption( fullURL, self.bandCol, self.bandCrit)

        doc = Service.tables.convertFromFile( fullURL, "votable", "votable" )
        return doc


    #
    # 
    #
    def searchAndRetrieve( self ) :
        global FORMAT, FEEDBACK, DEBUG, TRACE, ERROR
        if TRACE: print 'enter: searchAndRetrieve()'
        if self.terminateFlag.isSet():
            if DEBUG: print self.name + ' has been terminated prematurely!'
            return 
        try:
            if DEBUG: print 'About to image search: ' + self.name, self.ra, self.dec, self.radius    
            res = self.execute()
            if res: 
                try:
    #+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    # We have a windows problem with VOTables.
    #    I think it is here within the utils module...
    #+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                    self.votable = utils.read_votable( res, ofmt='votable' )
                    countImages = self.retrieveImages()
                    if countImages >= self.minimages :
                        self.terminateFlag.set()
                except:
                    if WARN: print self.name + ': bad vot!'
            else:
                if WARN: print self.name + ': no result!'
        except Exception, e2:
            if ERROR: print self.name + ' image Search failed. Incorrect URL? ', e2    
        if TRACE: print 'exit: searchAndRetrieve()'
        return
    # end of searchAndRetrieve()



    #
    # Download function (retrieves images).
    def retrieveImages( self ) :
        global DEBUG, TRACE, FEEDBACK
        if TRACE: print 'enter: retrieveImages()'
        # Get column index for access url
        urlColIdx = self.votable.getColumnIdx ('VOX:Image_AccessReference')
        formatIdx = self.votable.getColumnIdx ('VOX:Image_Format')
        # 2009-02-19 PAE: For future - may want to select on bandpass (or at least record it)
#        bandIdx=self.votable.getColumnIdx('VOX:BandPass_ID')
#        if bandIdx < 0:
#            if DEBUG: print "Bandpass information missing"       
       
        
        # 2009-02-19 PAE: Crappy way of getting metadata if necessary.
        #fieldlist=self.votable.getFields()
        #z=0
        #for i in fieldlist:
        #    print "FIELD ",z, "=",i.printAllNodes()
        #    z=z+1
        
       
        if urlColIdx < 0:
            if DEBUG: print "No access reference found in VOTable."
        else:
            # NB: Examine this. There's something lax here...
            dataRows = self.votable.getDataRows()
            getData = self.votable.getData
            countImages = len( dataRows )
            if FEEDBACK: print self.name + ' returned this number of images: ' + str( countImages )
            cnt = 0
            for row in dataRows:
                if self.terminateFlag.isSet():
                    if FEEDBACK: print self.name + ' has been terminated prematurely!'
                    break 
                cells = getData (row)
                # 2009-02-18 PAE: Useful debugging info
                if DEBUG: print "The Image row is:",cells
                url = cells[urlColIdx]
                fmt = cells[formatIdx]
                ext = '.'
                if 'jpeg' in fmt:
                    ext += 'jpg'
                elif 'fits' in fmt:
                    ext += 'fits'
                elif 'gif' in fmt:
                    ext += 'gif'
                else:
                    ext = ''
            
                iname = "%s_image%d%s" % ( self.name, cnt, ext )
                if DEBUG: print "Saving image %d as %s" % (cnt, iname) 
                fileOK = self.retrieveOneImage( url, iname )    
                if fileOK == True:
                    cnt += 1    
                    if cnt >= self.maximages:
                        cnt -= 1
                        break
        if TRACE: print 'exit: retrieveImages()'
        return cnt
    # end of retrieveImages()
    
    #
    # Retrieve one image 
    def retrieveOneImage( self, url, fname ) :
        global TRACE, DEBUG, odir
        if TRACE: print 'enter: retrieveOneImage() for:', url, fname 
        fileOK = False
        infile = None
        outfile = None
        filePath = None
        try:
            filePath = odir + os.sep + fname
            infile = urlopen( url )
            outfile = file( filePath, "wb" )
#
# Unfortunately, this strategy retrieves the whole file as one string
# before writing...
#            outfile.write( infile.read () )
#            fileOK = True    

#
# This strategy allows the writing of large files without huge memory
# consumption, and also allows the writing to be interrupted...
            while True:
                # Read a 32K chunk...
                chunk = infile.read( 32768 ) 
                #
                # Test for EOF...
                if not chunk: 
                    fileOK = True    
                    break
                #
                # Check to see whether we should end prematurely, by request.
                # If so, close the file and delete it, otherwise we will have
                # a corrupted/truncated file...
                if self.terminateFlag.isSet():    
                    infile.close()                
                    os.remove( filePath )
                    if DEBUG: print "Removed file before completion: ", filePath
                    break 
                outfile.write( chunk )
        
        except Exception, e:
            if DEBUG: print "Failed to retrieve image from ", url, e
    
        if infile != None:
            try: infile.close() 
            except: 1
        if outfile != None:
            try: outfile.close() 
            except: 1
    
        if fileOK == True:
            fileSize = os.stat( filePath )[6]
            if DEBUG: print 'file size:', fileSize
            if fileSize <= 1024:
                if fileSize < 32:
                    os.remove( filePath )
                    if DEBUG: print 'Removed image file due to incorrect format. Size less than 32: ' + fname
                    fileOK = False
                else:
                    potentialFileToDelete = None
                    try:
                        potentialFileToDelete = file( filePath )
                        line = potentialFileToDelete.readline()
                        sample = line[:32].strip()
                        if DEBUG: print sample
                        if sample.startswith( '<HTML' ):
                            potentialFileToDelete.close()
                            potentialFileToDelete = None
                            os.remove( filePath )    
                            if DEBUG: print 'Removed image file due to incorrect format. HTML file: ' + fname    
                            fileOK = False            
                    except Exception, e:    
                        if DEBUG: print 'Failed to delete outfile: ' + fname, e
                        if potentialFileToDelete != None:
                            try: potentialFileToDelete.close() 
                            except: 1
        if TRACE: print 'exit: retrieveOneImage() for:', url, fname
        return fileOK
    # end of retrieveOneImage( url, fname )



##################################################################
# Class for a group of services
# Formed when a group of services is thought of as a set of 
#    equivalent services, ie: mirrors or near mirrors. Really 
# a judgement over whether good results from one member of the
# group is sufficient on its own.
#    
##################################################################
class ServiceGroup:
    
    serviceGroups = []
    totalServiceCount = 0 
    
    #
    # This is the function triggered on each thread.
    # (The syntax is correct. It is passed a tuple of one argument)
    def dispatchSearch( service, ):
        if TRACE: print 'enter: dispatchSearch() for service ' + service.name
        service.searchAndRetrieve()
        if TRACE: print 'exit: dispatchSearch() for service ' + service.name
        return
    dispatchSearch = Callable( dispatchSearch )

    def __init__( self ):
        self.services = []
        self.terminateFlag = TerminateFlag()
        ServiceGroup.serviceGroups.append( self )
        self.mySequenceNumber = len( ServiceGroup.serviceGroups )-1
        return

    def put( self, service ):
        service.terminateFlag = self.terminateFlag
        self.services.append( service )
        ServiceGroup.totalServiceCount += 1
        return
    #
    #
    # 
    def getDispatchableService( self ):    
        for service in self.services:
            if service.dispatchedFlag == False:
                service.dispatchedFlag = True    # We assume it gets dispatched.
                if DEBUG: print 'found: ' + service.name
                return service
        return None
    #
    #
    #
    def getLargestGroupCount():
        max = 0
        for serviceGroup in ServiceGroup.serviceGroups:
            count = len( serviceGroup.services )
            if count > max:
                max = count
        return max
    getLargestGroupCount = Callable( getLargestGroupCount )
    #
    #
    #
    def __str__( self ):
        b = 'Sequence Number: ' + str(self.mySequenceNumber) + '\n'
        for service in self.services:
            b += str( service )
        return b
        
###################################################################
# A class to hold thread safe collections of stars / galaxies
# 
###################################################################
class Hits:
    
    def __init__( self ):
        self.lock = thread.allocate_lock()
        #
        # Each service will have its own separate collection stored
        # as a member of this list...
        self.list = []
        self.finished = False

    def push( self, service, builtRows ):
        global WARN
        self.lock.acquire()
        if self.finished == True:
            if WARN: print 'Warning. Results ignored for: ' + service.name 
        else:
            self.list.append( [service, builtRows ] )
        self.lock.release()
        return        
    #
    #
    #
    def cmpOnSphericalDistance( x, y ):
        xDist = float( x[1].split('=')[1] )
        yDist = float( y[1].split('=')[1] )
        if xDist > yDist :
            return 1
        elif xDist < yDist :
            return -1
        else:
            return 0
    cmpOnSphericalDistance = Callable( cmpOnSphericalDistance )
        

    #
    # Note. Should only be called when all threads have finished.
    #
    def getHits( self ):
        self.lock.acquire()
        self.finished = True 
        self.lock.release()
        #
        # The sorted list will be used to return all hits...
        sortedList = []
        #
        # Format every data line held in each of the services data collection.
        # Basically, turning each data item from something like 
        # 20.42 into B1=20.42 
        for dataCollection in self.list:
            service = dataCollection[0]
            #2009-02-18 PAE: Add some debugging while I work out how everything works, in 3 places bellow
            if DEBUG: print 'dataCollection: ',dataCollection
            colvarNames = service.columnList
            for row in dataCollection[1]:
                if DEBUG: print 'row: ',row
                formattedRow = []
                i=0
                for value in row:
                    if DEBUG: print 'I:',i,'value:',value
                    if i==0:
                        # The first entry in the formatted row is the source designation:
                        formattedRow.append( 'Source=' + value )
                    #2009-02-20: PAE should be <=, not <
                    elif (i<=len(colvarNames)):
                        varName = colvarNames[i-1][1]
                        if i==1 and varName != 'Dist':
                            formattedRow.append( 'Dist=' + str( Hits.formDist( service, row ) ) )
                        # The rest are driven by the columns list variable name:           
                        formattedRow.append( varName + '=' + str(value) )                
                    i += 1
                    #2009-02-19 PAE:
                    lastvalue=value
                
                # If self.urlvar is set, then lastvalue contains the urlvar value
                if DEBUG: print "Service.urlvar:",service.urlvar
                if (service.urlvar):
                    formattedRow.append( 'LOOKUP:'+service.url+'!'+service.urlvar+'='+str(lastvalue))
                
                if DEBUG: print 'formattedRow: ', formattedRow
                sortedList.append( formattedRow )
        #
        # We sort the combined lists on spherical offset...
        sortedList.sort( Hits.cmpOnSphericalDistance )
        return sortedList

    #
    #
    #
    def sphericalDistance( ra1, dec1, ra2, dec2 ):
        if TRACE: print 'entry: sphericalDistance()' 
        ra1 = float( ra1 )
        ra2 = float( ra2 )
        dec1 = float( dec1 )
        dec2 = float( dec2 ) 
        if DEBUG: print 'ra1, dec1, ra2, dec2: ', ra1, dec1, ra2, dec2
        if ra1 >= 180.0: ra1 = 360.0 - ra1
        ra1 = ra1 * math.pi/180.0

        if ra2 >= 180.0: ra2 = 360.0 - ra2
        ra2 = ra2 * math.pi/180.0

        if dec1 >= 180.0: dec1 = 360.0 - dec1
        dec1 = dec1 * math.pi/180.0

        if dec2 >= 180.0: dec2 = 360.0 - dec2
        dec2 = dec2 * math.pi/180.0
 
        ang = acos( cos(dec1)*cos(dec2)*cos(ra2-ra1) + sin(dec1)*sin(dec2) )
        ang = abs(ang)
#        asec = ang * 648000.0 / math.pi
        if DEBUG: print 'calculated DIST: ', ang
        if TRACE: print 'exit: sphericalDistance()' 
        return ang
    sphericalDistance = Callable( sphericalDistance )

    def formDist( service, row ):
        colvarNames = service.columnList
        ra1 = service.ra
        dec1 = service.dec
        ra2 = None
        dec2 = None
        i=0
        for value in row:
            if i > 0:
                varName = colvarNames[i-1][1]
                if varName == 'RA':
                    ra2 = value
                elif varName == 'Dec':
                    dec2 = value
                if ra2 != None and dec2 != None:
                    break
            i += 1
        sd = Hits.sphericalDistance( ra1, dec1, ra2, dec2 )
        return sd
    formDist = Callable( formDist )
 

    #
    # Simple print function for debug purposes.
    # Best to pipe this to a file.
    #
    def debug( self, myDesignation ):
        global TRACE, DEBUG
        if DEBUG:
            print '========================================= '\
            + myDesignation\
            + ' ==========================================='
            for row in self.list:
                print row
### end of class Hits ############################################

#
# Global, shared collections of stars and galaxies.
# The Hits class is thread safe.
stars = Hits()
gals = Hits()
#2009-02-18 PAE: Create X-ray lsit as well
xrays = Hits()

########################################################
# A simple class to help manage thread termination 
#
class TerminateFlag:

    def __init__( self ):
        self.flag = [ False ]
        self.lock = thread.allocate_lock()

    def isSet( self ):
        self.lock.acquire()
        flagValue = self.flag[0]
        self.lock.release()
        return flagValue

    def set( self ):
        self.lock.acquire()
        self.flag[0] = True 
        self.lock.release()
        return
### end of class TerminateFlag #########################


#
# Validate arguments passed. 
# Nothing fancy here, so I'm not using anything heavyweight.
# 
# arg1: ra 
# arg2: dec
# arg3: radius
# arg4: path to file containing list of service ivorns and other info
# arg5: unique name for the overall search directory to hold results.
#
def validateArgs():
    # If no arguments were given, print a helpful message
    # 2009-02-19 PAE: Allow the image radius to be different from the cone radius
    if ( len(sys.argv)<6 or len(sys.argv)>7):
        if ERROR: print 'Usage: ra dec radius control_file results_directory [image_search radius]'
        sys.exit(1)

    ra = float( sys.argv[1] )
    dec = float( sys.argv[2] )
    radius = float( sys.argv[3] )
    ifile = sys.argv[4]
    odir = sys.argv[5]
    
    # 2009-02-19 PAE: Allow the image radius to be different from the cone radius
    if (len(sys.argv)==7):
        imradius=float(sys.argv[6])
    else:
        imradius=radius
    return ra, dec, radius, ifile, odir, imradius
# end of validateArgs()

#
# Strip the brackets from a control file line,
# or throw a wobbly
#
def stripBrackets( line ):
    global ERROR
    lbrace = line.rfind( '[' )
    rbrace = line.rfind( ']' )
    if lbrace == -1 or rbrace == -1:
        if ERROR: print 'Malformed line in control file'
        sys.exit(1)
    else:
        line = line[lbrace+1:rbrace]
    return line

def processCatalogueLine( line, ra, dec, radius ):
    global ERROR
    l = stripBrackets( line )
    bits = l.split( '|' )
    # save:
    # 0: short name for this service 
  # 1: its ivorn
    # 2: table name, or nothing if only one table in the collection
    # 3: space separated list of column name / display name pairs
    # 4: name of function to execute search for this service
    service_name = bits[0].strip()
    ivorn = bits[1].strip()
    table_name = bits[2].strip()
    #
    # Setting the table to None makes the cone object easier to invoke...
    if len(table_name) == 0:
        table_name = None 
    col_names = bits[3].strip().split() # col_names is a list!!!
    for c in col_names:
        if c.index( ':' ) >= 0:
            continue
        else:
            if ERROR: print 'Malformed column names in control file: ' + c
            sys.exit(1)
    search_function_name = bits[4].strip()
    
    #2009-02-19 PAE: Need to include data for HTML lookup
    lookup=bits[5].strip()
    look=lookup.split('!')
    url=None
    urlvar=None
    
    if (len(look)>1):
        if(len(look[0])==0):
            # If the first part was not specified then we either set it to IVORN, 
            # or (if the whole thing was empty) set things to None
            if(len(look[1])==0):
                url=None
                urlvar=None
            else:
                url=ivorn
                urlvar=look[1]
        else:
            url=look[0]
            urlvar=look[1]

    #2009-02-23 PAE: Add the last field, an optional column to filter on
    filtCol=None
    filtVal=None
    if (len(bits)>6):
        filt=bits[6].strip()
        filtBits=filt.split(':')
        if(len(filtBits)==2):
            filtCol=filtBits[0]
            filtVal=filtBits[1]
    
    #2009-02-19 PAE: And pass these new variables too
    service = ConeSearch( service_name, ivorn, ra, dec, radius, table_name, col_names, search_function_name, url, urlvar, filtCol, filtVal ) 

    return service

# 2009-02-19 PAE: Receive imradius as well
def processImageLine( line, minimages, maximages, ra, dec, imradius ):

    global FORMAT
    l = stripBrackets( line )
    bits = l.split( '|' )
    # save:
    # 0: short name for this service 
    # 1: its ivorn
    # 2009-02-19:PAE Also save the bandpass filter specs.
    # 2: filter column
    # 3: filter value
    service_name = bits[0].strip()
    ivorn = bits[1].strip()
    # 2009-02-19:PAE 
    dump=bits[2].split(':')
    if(len(dump)>1):
        bandCol=dump[0].strip()
        bandCrit=dump[1].strip()
    else:
        bandCol=None
        bandCrit=None
    # 2009-02-19 PAE: Pass imradius as well
    service = SiapSearch( service_name, ivorn, ra, dec, imradius, minimages, maximages, FORMAT, bandCol, bandCrit )
    
    return service

#
# Process the input file of service ivorns.
#
# For each image source we wish to search, there is a list of alternative ivorns.
# For example, if we wished to search three image sources and there were
# two alternative services providing images for each source, that would
# make 6 ivorns altogether. We would return 3 ServiceGroups.
#
# Returns a list of ServiceGroups
#
#
# 2009-02-19 PAE: Receive imradius and getImages as well
def processControlFile( ra, dec, radius, imradius, filePath, getImages ):

    global TRACE, DEBUG, ERROR
    if TRACE: print( 'processControlFile() enter' )
    bControlFile = False
    bImageSection = False
    bCatalogueSection = False
    minimages = None
    maximages = None
    serviceGroup = None
    serviceGroups = []
    bLine = '' # buffer line used for buffering continuation lines

    for line in open( filePath ).readlines():
        l = line.strip()
        if DEBUG: print l
        #
        # First line must be the control file heading line...
        if bControlFile == False:
            if l == 'SWIFT SEARCH CONTROL FILE':
                bControlFile = True                
                if DEBUG: print 'Found control file heading.'
                continue
            else:
                if ERROR: print 'Malformed control file: Heading missing.'
                sys.exit(1)
        #
    # Comments lines and blank lines are ignored...
        if len(l) == 0 or l.startswith( '#' ) == True :
            continue

        #
        # Section triggers...
        if bCatalogueSection == False and l == 'CATALOGUE SECTION:':
            if DEBUG: print 'Found catalogue section'
            bCatalogueSection = True
            bImageSection = False
            continue
        elif bImageSection == False and l == 'IMAGE SECTION:':
            if DEBUG: print 'Found image section'
            bImageSection = True
            bCatalogueSection = False
            continue
        #
        # Buffer any continuation lines...
        if l.endswith( '\\' ):
            bLine += l[0:len(l)-1]
            continue
        #
        # Or process continuation lines...
        elif len( bLine ) > 0:
            bLine += l
            l = bLine
            bLine = ''
        #
        # Completed continuation lines 
        # and singleton lines
        # are processed here...            
        #
        # Process the line containing the number of services...
        if l.isdigit():
            # New service group required, but first save the previous...
            if serviceGroup != None:
                serviceGroups.append( serviceGroup )
            #
            # New service group for each new source triggered by the numeric...
            serviceGroup = ServiceGroup()
            continue
        #
        # Process one information line...
        if bCatalogueSection == True:
            serviceGroup.put( processCatalogueLine( l, ra, dec, radius ) )
        elif bImageSection == True:
            #2009-02-19 PAE: Skip this if the -n flag was sent.
            if not getImages:
                continue
            if minimages == None and maximages == None:
                l = stripBrackets( l )
                bits = l.split()
                minimages = int( bits[0] )
                maximages = int( bits[1] )    
            else:
                # 2009-02-19 PAE: Pass imradius as well
                serviceGroup.put( processImageLine( l, minimages, maximages, ra, dec, imradius ) )
        else:
            if ERROR: print 'Malformed line in control file.', l
    # Save the last service group...
    serviceGroups.append( serviceGroup )            
    if DEBUG: 
        for serviceGroup in serviceGroups:
            print serviceGroup
    if TRACE: print( 'processControlFile() exit' )
    return serviceGroups
# end of processIvornFile( filePath )

#
# Outputs the merged catalogue results as a comma separated file
#
def outputResults( directoryPathString, fileName, results ) :
    global TRACE, DEBUG, FEEDBACK, ERROR
    if TRACE: print 'enter: outputResults() for: ' + fileName
    outfile = None
    try:
        outfile = file( directoryPathString + os.sep + fileName, "w" )
        # Write each row as one line...
        for row in results:
            # Yes, this is code! It produces a comma separated string from the column values
            line = ','.join( row ) + '\n' 
            outfile.write( line )
    except Exception, e:
        if ERROR: print "Failed to write results for file: ", fileName, e
    if outfile != None:
        try: outfile.close()
        except: 1
    if TRACE: print 'exit: outputResults() for: ' + fileName
    return

######################################################
# Mainline                                           #
#                                                    #
# The dispatching algorithm means all service groups #
# have one service dispatched at a time. That is...  #
# service group A: dispatch 1st service              #
# service group B: dispatch 1st service              #
# service group C: dispatch 1st service              #
# service group A: dispatch 2nd service              #
# service group B: dispatch 2nd service              #
# service group C: dispatch 2nd service              #
# and so on.                                         #
######################################################
if FEEDBACK: print 'Started at: ' + time.strftime('%T') 


#2009-02-19 PAE: Allow -n option, which means the images will not be retrieved
getImages=True
optlist,alist=getopt.getopt(sys.argv[1:], 'n')
for opt in optlist:
    if(opt[0]=='-n'):
        getImages=False
        sys.argv=alist
        sys.argv.insert(0,'null')

# Validate the input arguments passed to us...
# 2009-02-19 PAE: Added imradius
( ra, dec, radius, ifile, odir, imradius ) = validateArgs()
if DEBUG: print ra, dec, radius, ifile, odir, imradius
#
# Retrieve ivorns plus controls for the sources we wish to search...
# 2009-02-19 PAE: Pass imradius and getImages as well
serviceGroups = processControlFile( ra, dec, radius, imradius, ifile, getImages )

#
# Create the overall search output directory...
if DEBUG: print( 'Output folder is called ' + odir )
if (os.path.exists(odir)): 
    print "Deleting",odir
    os.system("rm -fr "+odir)
    

os.mkdir( odir )
#
# Define the command to execute and the pool size
# i.e. when we run pool.put(fred), the serviceGroup.dispatchSearch(fred) is executed.
pool = easy_pool( ServiceGroup.dispatchSearch ) 
pool.start_threads( ServiceGroup.totalServiceCount )
#
# We have a list of ServiceGroups.
# (See dispatching algorithm above).
# Set up all the services for dispatching...
maxCount = ServiceGroup.getLargestGroupCount()
if DEBUG: print 'maxCount: ', maxCount
for i in range (1, maxCount+1):
    for serviceGroup in serviceGroups:
        service = serviceGroup.getDispatchableService()
        # pool expects a tuple.
        # Here it is passed a tuple of one argument.
        if service != None:
            input = ( service, )
            pool.put( input )
#
# Now observe the real work happening on different threads:
# Print information while executing...
i=0
while 1 :
    p = pool.qinfo()
    if FEEDBACK:
        print "Time: %3d sec    Queued: %2d    Running: %2d    Finished: %2d" % \
                  (i, p[1], ServiceGroup.totalServiceCount-p[1]-p[3], p[3])
    time.sleep(1)
    i=i+1
    if p[3]==ServiceGroup.totalServiceCount: break    
pool.stop_threads()
#
# We have to wait for threads to end in order to process the catalogue search 
# results, as the combined results have to be sorted on offset distance.
# The getHits() method does the sorting...
starHits = stars.getHits()
galHits = gals.getHits()
#2009-02-18 PAE: Save xrays as well.
xrayHits= xrays.getHits()
#
# Write the files...
if len( starHits ) > 0:
    outputResults( odir, 'starHits.csv', starHits )
if len( galHits ) > 0:
    outputResults( odir, 'galaxyHits.csv', galHits )
if len( xrayHits ) > 0:
    outputResults( odir, 'xrayHits.csv', xrayHits )
#
if FEEDBACK: print( 'Finished at '  + time.strftime('%T') ) 
