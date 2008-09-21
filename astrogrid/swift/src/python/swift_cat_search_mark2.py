#!/usr/bin/env python
import string, sys, os, time, thread
from astrogrid import acr
from astrogrid import ConeSearch
from astrogrid import utils
from astrogrid.threadpool import easy_pool
from urllib import *
from urlparse import *

#
#		Swift catalogue cone search.
#

#
# Global arguments passed in...
#
ra = 0
dec = 0
radius = 0
ifile = ''
odir = ''

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

#	def __getattr__( self, attrName ):
#		if attrName == 'name':
#			return self.name
#		elif attrName == 'ivorn':
#			return self.ivorn
#		elif attrName == 'table':
#			return self.table
#		elif attrName == 'columns':
#			return self.columns          # Don't forget. This is a list!
#		elif attrName == 'searchFunctionName':
#			return self.searchFunctionName
#		elif attrName == 'coneSearch':
#			return self.coneSearch
#		else:
#			raise AttributeError, attrName
### end of class Service ##########################################

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
#			dataRows = vot.getDataRows()	
#			for row in dataRows:
#				cols = [ serviceName ]
#				vals = vot.getData( row )
#				for val in vals:
#					cols.append( val )
#				self.list.append( cols )
			self.list.append( builtRows )
		self.lock.release()
		return

	#
	# Note. Should only be called when all threads have finished.
	#
	def getHits( self ):
		self.lock.acquire()
		self.finished = True
		self.lock.release()
		return self.list
### end of class Hits ############################################

#
# Shared collections of stars and galaxies.
# 
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
	if terminateFlag.isSet():
		print service.name + ' has been terminated prematurely!'
		return 
	try:
		print 'searchAndRetrieve: ' + service.name, ra, dec, radius	
		res = service.coneSearch.execute( ra, dec, radius, dsatab=service.table )
		if res: 
			try:
#				print service.name + ' enters: utils.read_votable()'
				vot = utils.read_votable( res, ofmt='votable' )
#				print service.name + ' exits: utils.read_votable()'
#				raColIndex = vot.getColumnIdx ( 'POS_EQ_RA_MAIN' )
#				if raColIndex < 0 :
#					print service.name + ': no hits!'
				if len( vot.getDataRows() ) == 0 : 
					print service.name + ': no hits!'
				elif terminateFlag.isSet():
					print service.name + ' has been terminated prematurely!'
					return
				else:
					eval( service.searchFunctionName + '( service, ra, dec, radius, vot, terminateFlag )' )
			except Exception, e1:
				print service.name + ': bad vot: ', e1
		else:
			print service.name + ': no result vot!'	
	except Exception, e2:
		print service.name + ' cone Search failed: ', e2	
	terminateFlag.set()
	return	
# end of searchAndRetrieve( service, ra, dec, radius, terminateFlag )

#
#
# Returned values: RAJ2000 DEJ2000 umag gmag rmag imag zmag
# Columns needed for criteria: cl == 6 for stars, 3 for galaxies
def searchSDSS( service, ra, dec, radius, vot, terminateFlag ):
	print 'searchSDSS enter'
	global stars, gals
	print service.name + ' returned this number of rows: ' + str( len( vot.getDataRows() ) )
	#
	# We decide between stars and galaxies by using the class column
	clColIndex = vot.getColumnIdx( 'cl' )
	#
	# Here are the rest of the indices
	raColIndex = vot.getColumnIdx( 'RAJ2000' )
	decColIndex = vot.getColumnIdx( 'DEJ2000' )
	umagColIndex = vot.getColumnIdx( 'umag' )
	gmagColIndex = vot.getColumnIdx( 'gmag' )
	rmagColIndex = vot.getColumnIdx( 'rmag' )
	imagColIndex = vot.getColumnIdx( 'imag' )
	zmagColIndex = vot.getColumnIdx( 'zmag' )
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
		# Extract the requested data
		# and add the service name to each row...
		subRow = [ service.name, \
               str( cells[ raColIndex ] ), \
               str( cells[ decColIndex ] ), \
               str( cells[ umagColIndex ] ), \
               str( cells[ gmagColIndex ] ), \
               str( cells[ rmagColIndex ] ), \
               str( cells[ imagColIndex ] ), \
               str( cells[ zmagColIndex ] ) ]
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
	print 'searchSDSS exit'
	return

#
# RAJ2000 DEJ2000 rmag rDiam bmag bDiam rClass bClass APM-ID
def searchAPM( service, ra, dec, radius, vot, terminateFlag ):
	global gals
	print 'searchAPM enter'
	print service.name + ' returned this number of rows: ' + str( len( vot.getDataRows() ) )
	#
	# Here are the column indices
	raColIndex = vot.getColumnIdx( 'RAJ2000' )
	decColIndex = vot.getColumnIdx( 'DEJ2000' )
	rmagColIndex = vot.getColumnIdx( 'rmag' )
	rDiamColIndex = vot.getColumnIdx( 'rDiam' )
	bmagColIndex = vot.getColumnIdx( 'bmag' )
	bDiamColIndex = vot.getColumnIdx( 'bDiam' )
	rClassColIndex = vot.getColumnIdx( 'rClass' )
	bClassColIndex = vot.getColumnIdx( 'bClass' )
	APMIDColIndex = vot.getColumnIdx( 'APM-ID' )
	#
	# Form local lists of galaxies
	galList = []
	#
	# Locate the data rows
	dataRows = vot.getDataRows()
	for row in dataRows:
		cells = vot.getData(row)
		# Extract the requested data
		# and add the service name to each row...
		subRow = [ service.name, \
               str( cells[ raColIndex ] ), \
               str( cells[ decColIndex ] ), \
               str( cells[ rmagColIndex ] ), \
               str( cells[ rDiamColIndex ] ), \
               str( cells[ bmagColIndex ] ), \
               str( cells[ rClassColIndex ] ), \
               str( cells[ bClassColIndex ] ) ]
		galList.append( subRow )
	#
	# Now append found data to the overall collections....
	if len( galList ) > 0:
		gals.push( service.name, galList )
	print 'searchAPM exit'
	return


def searchPSCz( service, ra, dec, radius, vot, terminateFlag ):
	global gals
	print 'searchPSCz enter'
	print service.name + ' returned this number of rows: ' + str( len( vot.getDataRows() ) )



	print 'searchPSCz exit'

def searchUSNO( service, ra, dec, radius, vot, terminateFlag ):
	global gals
	print 'searchUSNO enter'
	print service.name + ' returned this number of rows: ' + str( len( vot.getDataRows() ) )
	print 'searchUSNO exit'

def search6DF( service, ra, dec, radius, vot, terminateFlag ):
	global gals
	print 'search6DF enter'
	print service.name + ' returned this number of rows: ' + str( len( vot.getDataRows() ) )
	print 'search6DF exit'

def search2MASS( service, ra, dec, radius, vot, terminateFlag ):
	global stars
	print 'search2MASS enter'
	print service.name + ' returned this number of rows: ' + str( len( vot.getDataRows() ) )
	print 'search2MASS exit'


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
print( 'Output folder is called ' + odir )
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
			print 'Generating service for: ' + service.name + ' ivorn: --->' + service.ivorn + '<---'
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

#print sources

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
#print '========================================= stars ==========================================='
#print stars.getHits()
#print '======================================= galaxies =========================================='
#print gals.getHits()
print( 'Finished at '  + time.strftime('%T') ) 
