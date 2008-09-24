#!/usr/bin/env python
import string, sys, os, time, thread, math
import xmlrpclib
from astrogrid import acr
from astrogrid import utils
from astrogrid.threadpool import easy_pool
from urllib import *
from urlparse import *

#=================================================================
#		Swift combined image and catalogue search.
#
# Notes:
# (1) I have the spherical distance calculated column name as _r.
#     The problem is I don't know whether this is standard or not.
# (2) What if the service has more than one table?
#     The standard python ConeSearch seems to imply that this can
#     be the case for dsa; it has the dsatab keyword on the 
#     execute function.
#=================================================================

#
# Global arguments passed in...
ra = None
dec = None
radius = None
ifile = None
odir = None
#
# Global arguments raised from the control file,
# giving the minimum images considered satisfactory from a given source
# and the maximum images desired
minimages = None
maximages = None
#
# Global variables to control logging:
DEBUG = True
TRACE = True

class Service:
	
	ar = None
	tables = None
	try:	
		#
		# Connect to acr
		fname = os.path.expanduser( "~/.astrogrid-desktop" )
		assert os.path.exists( fname ),  'No AR running: Please start your AR and rerun this script'
		prefix = file( fname ).next().rstrip()
		ar = xmlrpclib.Server( prefix + "xmlrpc" )
		tables = ar.util.tables
	except Exception, e:
		print 'Could not use AR in establishing service: ', e

	def __init__( self, name, ivorn ):
		self.name = name
		self.ivorn = ivorn
		return
	#
	#
	# Generic execute() meant to be overridden
	def execute( self, ra, dec, radius ):	
		return
		

##################################################################
# ConeSearch class to substitute for the standard one,
# which seems not to allow all columns to be returned
#
class ConeSearch( Service ):

	def __init__( self, name, ivorn, table, columns, searchFunctionName ):
		Service.__init__( self, name, ivorn )
		self.table = table             # table can be None where service expects only one table!
		self.columns = columns         # self.columns is a list!
		self.searchFunctionName = searchFunctionName
	#
	# Not sure what to make of the dsatab keyword.
	# What happens normally where a service has more than one table?
	# Or is this a feature only of dsa? Ask Noel.
	def execute( self, ra, dec, radius ):	
		# use AR to build the query URL       
		query = Service.ar.ivoa.cone.constructQuery( self.ivorn, ra, dec, radius )
		fullURL = Service.ar.ivoa.cone.addOption( query, "VERB", "3" )
		doc = Service.tables.convertFromFile( fullURL, "votable", "votable" )
		return doc


def cmpOnSphericalDistance( x, y ):
	if x[1] > y[1] :
		return 1
	elif x[1] < y[1] :
		return -1
	else:
		return 0

###################################################################
# A class to hold thread safe collections of stars / galaxies
# 
class Hits:
	
	def __init__( self ):
		self.lock = thread.allocate_lock()
		self.list = []
		self.finished = False

	def push( self, serviceName, builtRows ):
		self.lock.acquire()
		if self.finished == True:
			print 'Warning. Results ignored for: ' + serviceName 
		else:
			for row in builtRows:
				self.list.append( row )
		self.lock.release()
		return

	#
	# Note. Should only be called when all threads have finished.
	#
	def getHits( self ):
		self.lock.acquire()
		self.finished = True
		self.lock.release()
		#
		# Good place to sort would be here!
		self.list.sort( cmpOnSphericalDistance )
		return self.list

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
#
#
def formNullValuesMap( vot ):
	global TRACE, DEBUG
#	if TRACE: print 'formNullValuesMap() enter'
	nullVals = {}
	fields = vot.getFields()
	for field in fields:
		vals = field.getNodesByName( 'VALUES' )
		name = field.getAttributes()['name']
		#print 'name type: ', type(name)
		for val in vals:
			attrs = val.getAttributes()
			#print '=====attrs====='
			#print attrs
			nullValue = attrs[ 'null' ]
			nullVals[ vot.getColumnIdx( name ) ] = nullValue
	#print '=====nullVals====='
#	print nullVals
#	if TRACE: print 'formNullValuesMap() exit'
	return nullVals

def testAndSetNull( x, index, nullsMap ):
	global TRACE, DEBUG
#	if TRACE: print 'testAndSetNull() enter'
#	print 'x: ', x, 'index: ', index
#	print type(x)
#	print str(x)
	retVal = None
	if x != None:
		if str(x) != 'NaN':
			if nullsMap.has_key( index ) == True:
				if x != nullsMap[ index ]:
					retVal = x
			else:
				retVal = x
#	print retVal
#	if TRACE: print 'testAndSetNull() exit'
	return retVal

def testAndSetEmpty( x ):
	if x == None:
		return ''
	return x

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
	global ra, dec, radius, ifile, odir
	# If no arguments were given, print a helpful message
	if len(sys.argv)!=6:
		print 'Usage: ra dec radius file_of_ivorns results_directory'
		sys.exit(0)
	else:
		ra = float( sys.argv[1] )
		dec = float( sys.argv[2] )
		radius = float( sys.argv[3] )
		ifile = sys.argv[4]
		odir = sys.argv[5]
	return
# end of validateArgs()

#
# Process the input file of service ivorns.
#
# For each image source we wish to search, there is a list of alternative ivorns.
# For example, if we wished to search three image sources and there were
# two alternative services providing images for each source, that would
# make 6 ivorns altogether.
#
# Returns a list of service objects
#
#
def processIvornFile( filePath ):
	global TRACE, DEBUG
	sources = []
	services = []
	for line in open( filePath ).readlines():
		l = line.strip()
    # Comments lines and blank lines are ignored...
		if len(l) > 0 and l.startswith( '#' ) == 0:
			# Process the line containing the number of services...
			if l.isdigit():
				if len( services ) > 0:
					sources.append( services )
					services = []
			# Process one information line...
			else:
				# For each service we save the info from the control file.
				lbrace = l.rfind( '[' )
				rbrace = l.rfind( ']' )
				if lbrace == -1 or rbrace == -1:
					print 'Malformed line in ivorn file'
				else:
					l = l[lbrace+1:rbrace]
#					print l
					bits = l.split( '|' )
					# save:
					# 0: short name for this service 
          # 1: its ivorn
					# 2: table name, or nothing if only one table in the collection
					# 3: space separated list of column names
					# 4: name of function to execute search for this service
					service_name = bits[0].strip()
					ivorn = bits[1].strip()
					table_name = bits[2].strip()
					#
					# Setting the table to None makes the cone object easier to invoke...
					if len(table_name) == 0:
						table_name = None 
					col_names = bits[3].strip().split() # col_names is a list!!!
					search_function_name = bits[4].strip()
					service = ConeSearch( service_name, ivorn, table_name, col_names, search_function_name ) 
					services.append( service )
	#
	# Add the last one on if not zero...
	if len( services ) > 0:
		sources.append( services )
	print sources
	return sources
# end of processIvornFile( filePath )

#
# Search function to pass to thread pool.
#
def searchAndRetrieve( service, ra, dec, radius, terminateFlag ) :
	global TRACE, DEBUG
	if TRACE: print 'searchAndRetrieve for ' + service.name + ' enter'
	if terminateFlag.isSet():
		print service.name + ' has been terminated prematurely!'
		return 
	try:
		if DEBUG: print 'About to coneSearch: ' + service.name, ra, dec, radius	
		res = service.execute( ra, dec, radius )
		if res: 
			try:
				vot = utils.read_votable( res, ofmt='votable' )
				if len( vot.getDataRows() ) == 0 : 
					print service.name + ': no hits!'
				elif terminateFlag.isSet():
					print service.name + ' has been terminated prematurely!'
					return
				else:
					if DEBUG: print 'About to eval'
					eval( service.searchFunctionName + '( service, ra, dec, radius, vot )' )
			except Exception, e1:
				print service.name + ': bad vot: ', e1
		else:
			print service.name + ': no result vot!'	
	except Exception, e2:
		print service.name + ' cone Search failed: ', e2	
	terminateFlag.set()
	if TRACE: print 'searchAndRetrieve for ' + service.name + ' exit'
	return	
# end of searchAndRetrieve( service, ra, dec, radius, terminateFlag )

#
#
# Returned values: RAJ2000 DEJ2000 umag gmag rmag imag zmag
# Columns needed for criteria: cl == 6 for stars, 3 for galaxies
def searchSDSS( service, ra, dec, radius, vot ):
	global TRACE, DEBUG, stars, gals
	if TRACE: print 'searchSDSS enter'
	if DEBUG: print service.name + ' returned this number of rows: ' + str( len( vot.getDataRows() ) )
	#
	# We decide between stars and galaxies by using the class column
	clColIndex = vot.getColumnIdx( 'cl' )
	#
	# Form a map for those columns that can return a null value...
	nullsMap = formNullValuesMap( vot )
	print nullsMap
	#
	# Here are the rest of the indices, found
	# using the votable and the column names 
	# passed to us in the control file...	
	colIndices = []
	for column in service.columns:
		colIndices.append( vot.getColumnIdx( column ) )
	#
	# Form local lists of stars and galaxies
	starList = []
	galList = []
	#
	# Locate the data rows
	dataRows = vot.getDataRows()
	for row in dataRows:
		cells = vot.getData(row)
		cl = testAndSetNull( cells[ clColIndex ], clColIndex, nullsMap )
		if cl == None:
			continue
		#
		# Only proceed if a star or galaxy...
		cl = int(cl)
		if cl != 3 and cl != 6:
			continue
		# Begin each constructed row with the service name 
		# and then append all the data requested to each row...
		subRow = [ service.name ]
		for index in colIndices:
			subRow.append( testAndSetEmpty( testAndSetNull( cells[ index ], index, nullsMap ) ) )

		if cl == 3:
			galList.append( subRow )
		else:
			starList.append( subRow )
	#
	# Now append found data to the overall collections....
	if len( starList ) > 0:
		stars.push( service.name, starList )
	else:
		print 'SDSS found no stars'
	if len( galList ) > 0:
		gals.push( service.name, galList )
	else:
		print 'SDSS found no galaxies'
	if TRACE: print 'searchSDSS exit'
	return

def searchUSNO( service, ra, dec, radius, vot ):
	global TRACE, DEBUG, stars, gals
	if TRACE: print 'searchUSNO enter'
	if DEBUG: print service.name + ' returned this number of rows: ' + str( len( vot.getDataRows() ) )
	#
	# We need to decide between stars and galaxies.
	# These are just the indices for the criteria...
	b1cIndex = vot.getColumnIdx( 'B1s/g' ) 
	b2cIndex = vot.getColumnIdx( 'B2s/g' ) 
	r1cIndex = vot.getColumnIdx( 'R1s/g' ) 
	r2cIndex = vot.getColumnIdx( 'R2s/g' ) 
	icIndex = vot.getColumnIdx( 'Is/g' ) 
	#
	# Form a map for those columns that can return a null value...
	nullsMap = formNullValuesMap( vot )
	#
	# Here are the rest of the indices, found
	# using the votable and the column names 
	# passed to us in the control file...	
	colIndices = []
	for column in service.columns:
		i = vot.getColumnIdx( column )
		if DEBUG:
			print 'column name: ' + column, 'column index: ' + str( i )
		colIndices.append( i )
	#
	# Form local lists of stars and galaxies
	starList = []
	galList = []
	#
	# Locate the data rows
	dataRows = vot.getDataRows()
	for row in dataRows:
		cells = vot.getData(row)
		#
		# Get data to decide between stars and galaxies
		# and test for both stars and galaxies!
		b1c = testAndSetNull( cells[ b1cIndex ], b1cIndex, nullsMap ) 
		b2c = testAndSetNull( cells[ b2cIndex ], b2cIndex, nullsMap ) 
		r1c = testAndSetNull( cells[ r1cIndex ], r1cIndex, nullsMap ) 
		r2c = testAndSetNull( cells[ r2cIndex ], r2cIndex, nullsMap )  
		ic = testAndSetNull( cells[ icIndex ], icIndex, nullsMap ) 
		bStars = testForStarsUSNO( b1c, b2c, r1c, r2c, ic )
		bGalaxies = testForGalsUSNO( b1c, b2c, r1c, r2c, ic )
		#
		# Only proceed if a star or galaxy...
		if bStars == False and bGalaxies == False :
			continue
		# Begin each constructed row with the service name 
		# and then append all the data requested to each row...
		subRow = [ service.name ]
		for index in colIndices:
			subRow.append( testAndSetEmpty( testAndSetNull( cells[ index ], index, nullsMap ) ) )
		#
		# Are stars and galaxies mutually exclusive,
		# or can there be an element of doubt?
		if bGalaxies:
			galList.append( subRow )
		elif bStars:
			starList.append( subRow )
		else:
			if DEBUG: print'Warning. An SDSS hit does not fall into the star or galaxy class.'
	#
	# Now append found data to the overall collections....
	if len( starList ) > 0:
		stars.push( service.name, starList )
	if len( galList ) > 0:
		gals.push( service.name, galList )
	if TRACE: print 'searchUSNO exit'
	return

#
#
def testForStarsUSNO( b1c, b2c, r1c, r2c, ic ):
	global TRACE, DEBUG
#	if TRACE: print 'testForStarsUSNO() enter'
	#
	# If either no s/g are set, OR any of them are >= 6, call it a star
	# NOTE. There is ambiguity in the original script regarding star selection!!!
	clist = [ b1c, b2c, r1c, r2c, ic ]
#	if DEBUG: print 'clist', clist
	set = False
	for c in clist:
		if c != None:
			if int(c) >= 6:
				set = True
				break
#	if TRACE: print 'testForStarsUSNO() exit'
	return set

def testForGalsUSNO( b1c, b2c, r1c, r2c, ic ):
	global TRACE, DEBUG
#	if TRACE: print 'testForGalsUSNO() enter'
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
#	if TRACE: print 'testForGalsUSNO() exit'
	return set



#
# Searches for unfiltered stars.
def searchStars( service, ra, dec, radius, vot ):
	global TRACE, DEBUG, stars
	if TRACE: print 'searchStars enter'
	stars.push( service.name, genericRetrieval( service, ra, dec, radius, vot ) )
	if TRACE: print 'searchStars exit'
	return
#
# Searches for unfiltered galaxies.
def searchGalaxies( service, ra, dec, radius, vot ):
	global TRACE, DEBUG, gals
	if TRACE: print 'searchGalaxies enter'
	gals.push( service.name, genericRetrieval( service, ra, dec, radius, vot ) )
	if TRACE: print 'searchGalaxies exit'
	return

def genericRetrieval( service, ra, dec, radius, vot ):
	global TRACE, DEBUG
	if TRACE: print 'genericRetrieval for ' + service.name + ' enter'
	if DEBUG: print service.name + ' returned this number of rows: ' + str( len( vot.getDataRows() ) )
	#
	# Here are the column indices, found
	# using the votable and the column names 
	# passed to us from the control file...	
	colIndices = []
	for column in service.columns:
		colIndices.append( vot.getColumnIdx( column ) )
	#
	# Form a map for those columns that can return a null value...
	nullsMap = formNullValuesMap( vot )
	#
	# Form local list of hits
	hitList = []
	#
	# Locate the data rows
	dataRows = vot.getDataRows()
	for row in dataRows:
		cells = vot.getData(row)
		# Begin each constructed row with the service name 
		# and then append all the data requested to each row...
		subRow = [ service.name ]
		for index in colIndices:
			subRow.append( testAndSetEmpty( testAndSetNull( cells[ index ], index, nullsMap ) ) )
		hitList.append( subRow )
	if TRACE: print 'genericRetrieval for ' + service.name + ' exit'
	return hitList

def outputResults( directoryPathString, fileName, results ) :
	try:
		outfile = file( directoryPathString + '/' + fileName, "w" )
		# Convert all values to strings...
#		for row in results:
#			i = 0
#			for col in row:
#				row[i] = str( col )
#				i += 1
		# Write each row as one line...
		for row in results:
			# Yes, this is code! It produces a comma separated string from the column values
			line = ','.join( row ) + '\n' 
			outfile.write( line )
	except Exception, e:
		print "Failed to write results for file: ", fileName, e
	outfile.close()
	return

#####################################################
#    Mainline                                       #
#####################################################
print 'Started at: ' + time.strftime('%T') 
#
# Validate the input arguments passed to us...
validateArgs()
print ra, dec, radius, ifile, odir

#
# Retrieve ivorns for the sources we wish to search...
sources = processIvornFile( ifile )
serviceCount = 0 
for source in sources:
	for service in source:
		serviceCount += 1

#
# Define the command to execute and the pool size
pool = easy_pool( searchAndRetrieve )
pool.start_threads( serviceCount )

for source in sources:
	# We have a list of services for each source.
	# Fill out the thread information...
	iIndex = 0
	terminateFlag = TerminateFlag()
	for service in source:
		#
		# Add the service and its inputs to the thread pool...
		input = ( service, ra, dec, radius, terminateFlag )
		pool.put( input )
#
# Now observe the real work:
# Print information while executing...
i=0
while 1 :
	p = pool.qinfo()
	print "Time: %3d sec    Queued: %2d    Running: %2d    Finished: %2d" % \
                                        (i, p[1], serviceCount-p[1]-p[3], p[3])
	time.sleep(1)
	i=i+1
	if p[3]==serviceCount: break
    
pool.stop_threads()
starHits = stars.getHits()
galHits = gals.getHits()
#stars.debug( 'STARS' )
#gals.debug( 'GALAXIES' )
#
# Create the overall search output directory...
if len(starHits) > 0 or len(galHits) > 0:
	if DEBUG: print( 'Output folder is called ' + odir )
	os.mkdir( odir )

# Write the files...
if len( starHits ) > 0:
	outputResults( odir, 'starHits.csv', starHits )
if len( galHits ) > 0:
	outputResults( odir, 'galaxyHits.csv', galHits )

print( 'Finished at '  + time.strftime('%T') ) 
