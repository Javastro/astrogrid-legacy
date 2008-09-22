#!/usr/bin/env python
import string, sys, os, time, thread
from math import *
from astrogrid import acr
from astrogrid import ConeSearch
from astrogrid import utils
from astrogrid.threadpool import easy_pool
from urllib import *
from urlparse import *

#=================================================================
#		Swift catalogue cone search.
#=================================================================

#
# Global arguments passed in...
ra = 0
dec = 0
radius = 0
ifile = ''
odir = ''
#
# Global variable to control logging:
log = True

##################################################################
# A simple class to hold service specific data.
#
class Service:

	def __init__( self, name, ivorn, table, columns, searchFunctionName ):
		self.name = name
		self.ivorn = ivorn
		self.table = table             # table can be None where service expects only one table!
		self.columns = columns         # self.columns is a list!
		self.searchFunctionName = searchFunctionName
		self.coneSearch = None         # The conesearch itself is created later
### end of class Service ##########################################

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
		global log
		if log:
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
					service = Service( service_name, ivorn, table_name, col_names, search_function_name ) 
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
	global log
	if log: print 'searchAndRetrieve for ' + service.name + ' enter'
	if terminateFlag.isSet():
		print service.name + ' has been terminated prematurely!'
		return 
	try:
		if log: print 'About to coneSearch: ' + service.name, ra, dec, radius	
		res = service.coneSearch.execute( ra, dec, radius, dsatab=service.table )
		if res: 
			try:
				vot = utils.read_votable( res, ofmt='votable' )
				if len( vot.getDataRows() ) == 0 : 
					print service.name + ': no hits!'
				elif terminateFlag.isSet():
					print service.name + ' has been terminated prematurely!'
					return
				else:
					if log: print 'About to eval'
					eval( service.searchFunctionName + '( service, ra, dec, radius, vot )' )
			except Exception, e1:
				print service.name + ': bad vot: ', e1
		else:
			print service.name + ': no result vot!'	
	except Exception, e2:
		print service.name + ' cone Search failed: ', e2	
	terminateFlag.set()
	if log: print 'searchAndRetrieve for ' + service.name + ' exit'
	return	
# end of searchAndRetrieve( service, ra, dec, radius, terminateFlag )

#
#
# Returned values: RAJ2000 DEJ2000 umag gmag rmag imag zmag
# Columns needed for criteria: cl == 6 for stars, 3 for galaxies
def searchSDSS( service, ra, dec, radius, vot ):
	global log, stars, gals
	if log: print 'searchSDSS enter'
	if log: print service.name + ' returned this number of rows: ' + str( len( vot.getDataRows() ) )
	#
	# We decide between stars and galaxies by using the class column
	clColIndex = vot.getColumnIdx( 'cl' )
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
		rc = cells[ clColIndex ]
		#
		# Only proceed if a star or galaxy...
		if rc != '3' and rc != '6':
			continue
		# Begin each constructed row with the service name 
		# and then append all the data requested to each row...
		subRow = [ service.name ]
		subRow.append( sphericalDistance( ra, dec, cells[0], cells[1] ) )
		for index in colIndices:
			subRow.append( cells[ index ] )

		if rc == '3':
			galList.append( subRow )
		else:
			starList.append( subRow )
	#
	# Now append found data to the overall collections....
	if len( starList ) > 0:
		stars.push( service.name, starList )
	if len( galList ) > 0:
		gals.push( service.name, galList )
	if log: print 'searchSDSS exit'
	return

def searchUSNO( service, ra, dec, radius, vot ):
	global log, stars, gals
	if log: print 'searchUSNO enter'
	if log: print service.name + ' returned this number of rows: ' + str( len( vot.getDataRows() ) )
	#
	# We need to decide between stars and galaxies.
	# These are just the indices for the criteria...
	b1cIndex = vot.getColumnIdx( 'B1s/g' ) 
	b2cIndex = vot.getColumnIdx( 'B2s/g' ) 
	r1cIndex = vot.getColumnIdx( 'R1s/g' ) 
	r2cIndex = vot.getColumnIdx( 'R2s/g' ) 
	icIndex = vot.getColumnIdx( 'Is/g' ) 
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
		#
		# Get data to decide between stars and galaxies
		# and test for both stars and galaxies!
		b1c = cells[ b1cIndex ] 
		b2c = cells[ b2cIndex ] 
		r1c = cells[ r1cIndex ] 
		r2c = cells[ r2cIndex ] 
		ic = cells[ icIndex ] 
		bStars = testForStarsSDSS( b1c, b2c, r1c, r2c, ic )
		bGalaxies = testForGalsSDSS( b1c, b2c, r1c, r2c, ic )
		#
		# Only proceed if a star or galaxy...
		if bStars == False and bGalaxies == False :
			continue
		# Begin each constructed row with the service name 
		# and then append all the data requested to each row...
		subRow = [ service.name ]
		subRow.append( sphericalDistance( ra, dec, cells[0], cells[1] ) )
		for index in colIndices:
			subRow.append( cells[ index ] )
		#
		# Are stars and galaxies mutually exclusive,
		# or can there be an element of doubt?
		if bGalaxies:
			galList.append( subRow )
		elif bStars:
			starList.append( subRow )
		else:
			if log: print'Warning. An SDSS hit does not fall into the star or galaxy class.'
	#
	# Now append found data to the overall collections....
	if len( starList ) > 0:
		stars.push( service.name, starList )
	if len( galList ) > 0:
		gals.push( service.name, galList )
	if log: print 'searchUSNO exit'
	return

#
#
def testForStarsSDSS( b1c, b2c, r1c, r2c, ic ):
#	if log: print 'testForStarsSDSS() enter'
	#
	# If either no s/g are set, OR any of them are >= 6, call it a star
	# NOTE. There is ambiguity in the original script regarding star selection!!!
	clist = [ b1c, b2c, r1c, r2c, ic ]
	set = False
	for c in clist:
		if c != None:
			if c >= 6:
				set = True
				break
#	if log: print 'testForStarsSDSS() exit'
	return set

def testForGalsSDSS( b1c, b2c, r1c, r2c, ic ):
#	if log: print 'testForGalsSDSS() enter'
	#
  # If at least 1 s/g is set, and all those which are set are <6, call it gal
	clist = [ b1c, b2c, r1c, r2c, ic ]
	set = False
	for c in clist:
		if c != None:
			if c < 6:
				set = True
			else:
				set = False
				break
#	if log: print 'testForGalsSDSS() exit'
	return set


#
# Searches for unfiltered stars.
def searchStars( service, ra, dec, radius, vot ):
	global log, stars
	if log: print 'searchStars enter'
	stars.push( service.name, genericRetrieval( service, ra, dec, radius, vot ) )
	if log: print 'searchStars exit'
	return
#
# Searches for unfiltered galaxies.
def searchGalaxies( service, ra, dec, radius, vot ):
	global log, gals
	if log: print 'searchGalaxies enter'
	gals.push( service.name, genericRetrieval( service, ra, dec, radius, vot ) )
	if log: print 'searchGalaxies exit'
	return

def genericRetrieval( service, ra, dec, radius, vot ):
	global log
	if log: print 'genericRetrieval for ' + service.name + ' enter'
	if log: print service.name + ' returned this number of rows: ' + str( len( vot.getDataRows() ) )
	#
	# Here are the column indices, found
	# using the votable and the column names 
	# passed to us from the control file...	
	colIndices = []
	for column in service.columns:
		colIndices.append( vot.getColumnIdx( column ) )
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
		subRow.append( sphericalDistance( ra, dec, cells[0], cells[1] ) )
		for index in colIndices:
			subRow.append( cells[ index ] )
		hitList.append( subRow )
	if log: print 'genericRetrieval for ' + service.name + ' exit'
	return hitList

# Convert the latitudes and longitudes to radians, observing that latitude is
#positive in the northern hemisphere, and negative in the southern
#hemisphere, and by convention among astronomers east longitude is positive
#and west longitude is negative (why? No one knows).
#
#Let's call one point the 'start point' and the other one the 'end point.
#
#Let L be the start point latitude in radians, and D be the end point
#latitude in radians.
#
#Let LHA be the difference between the two longitudes, in radians.
#
#Then the sine of the distance angle between the two points, measured from
#the center of the earth, is:
#
#sin(distance angle) = sin(L) * sin(D) + cos(L) * cos(D) * cos(LHA)
#
#and the angle in radians of the distance angle is:
#
#da = asin(distance angle).
#
#Convert this angle to degrees.
def sphericalDistance( ra, dec, hitRa, hitDec ):
	fRA = float( ra )
	fDEC = float( dec )
	fHitRA = float( hitRa )
	fHitDEC = float( hitDec )
	sineDistanceAngle = \
	( sin( radians(fDEC) ) * sin( radians(fHitDEC) ) ) + \
	( cos( radians(fDEC) ) * cos( radians(fHitDEC) ) * cos( radians(fRA - fHitRA) ) )
	sd = degrees( asin( sineDistanceAngle ) ) 
	return sd/10

def sphericalDistance2( ra, dec, hitRa, hitDec ):
	fRA = float( ra )
	fDEC = float( dec )
	fHitRA = float( hitRa )
	fHitDEC = float( hitDec )
	cosineDistanceAngle = \
	( cos( radians(90 - fDEC) ) * cos( radians(90 - fHitDEC) ) ) + \
	( sin( radians(90 - fDEC) ) * cos( radians(90 - fHitDEC) ) * cos( radians(fRA - fHitRA) ) )
	sd = degrees( acos( cosineDistanceAngle ) ) 
	return sd

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
#sys.exit(0)

#
# Log in to Astrogrid runtime...
#print 'Logging in to Astrogrid runtime at ' + time.strftime('%T')
#acr.login() 				
#print 'Logged in at ' + time.strftime('%T') 
#
# Create the overall search output directory...
if log: print( 'Output folder is called ' + odir )
os.mkdir( odir )

#
# We have a list of sources and a list of services for each source.
# Generate a cone search for each service...
serviceCount = 0
for source in sources:
	# We have a list of services for each source...
	for service in source:
		# Generate a cone search for each service...
		try:
			if log: print 'Generating service for: ' + service.name + ' ivorn: --->' + service.ivorn + '<---'
			service.coneSearch = ConeSearch( service.ivorn )
			serviceCount += 1 # The count gets incremented only if the service is kosher
#			print service.coneSearch.info['content']['description']
		except:
			# A failure means we have to deal with a None value in the service!
			print 'ConeSearch() failed for ' + service.name

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
		# This is where we deal with a None value within the service object
		if isinstance( service.coneSearch, ConeSearch ):
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
stars.getHits()
stars.debug( 'stars' )
gals.getHits()
gals.debug( 'galaxies' )
print( 'Finished at '  + time.strftime('%T') ) 
