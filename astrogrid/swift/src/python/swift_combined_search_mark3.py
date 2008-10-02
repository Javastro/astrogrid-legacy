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
# (3) We have a windows problem with VOTables.
#	    I think it is within the utils module...
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
#
FORMAT = 'image/fits'

class Callable:
	def __init__(self, anycallable):
		self.__call__ = anycallable

##################################################################
# Base class for services.
# 
#	
#
class Service:
	
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

	def __init__( self, name, ivorn, ra, dec, radius ):
		self.dispatched = False
		self.name = name
		self.ivorn = ivorn
		self.ra = ra
		self.dec = dec
		self.radius = radius
		self.vot = None
		self.terminateFlag = None
		return
	#
	#
	# Generic searchAndRetrieve() meant to be overridden
	def searchAndRetrieve( self ):	
		return
	#
	#
	#
	def __str__( self ):
		return 'Service Name: ' + self.name + '\n' \
		       'Ivorn: ' +  self.ivorn + '\n' \
	         'ra: ' + str(self.ra)  + '\n' \
	         'dec: ' + str(self.dec)  + '\n' \
	         'radius: ' + str(self.radius)  + '\n' \
	         'dispatched: ' + str(self.dispatched) + '\n'

		

##################################################################
# ConeSearch class to substitute for the standard one,
# which seems not to allow all columns to be returned
#
class ConeSearch( Service ):

	def __init__( self, name, ivorn, ra, dec, radius, table, columns, searchFunctionName ):
		Service.__init__( self, name, ivorn, ra, dec, radius )
		self.table = table             # table can be None where service expects only one table!
		self.columns = columns         # self.columns is a list of column / variable name pairs!
		self.searchFunctionName = searchFunctionName
		return
	#
	# Not sure what to make of the dsatab keyword.
	# What happens normally where a service has more than one table?
	# Or is this a feature only of dsa? Ask Noel.
	def execute( self ):	
		# use AR to build the query URL       
		query = Service.ar.ivoa.cone.constructQuery( self.ivorn, self.ra, self.dec, self.radius )
		fullURL = Service.ar.ivoa.cone.addOption( query, "VERB", "3" )
		doc = Service.tables.convertFromFile( fullURL, "votable", "votable" )
		return doc

	#
	#
	#
	def __str__( self ):
		return Service.__str__( self ) + 'Search function name: ' + self.searchFunctionName + '\n'



	#
	# The columns list is in fact a list of "pairs", with each pair consisting of 
  	# database column name and program variable name, separated by a colon. For example:
	# B1mag:B1
	# where B1mag is the database column name and B1 the program variable name.
	# This routine returns these column entities as a list of lists. For example:
	# [ [_r,Dist],[RAJ2000,RA],[DEJ2000,Dec],[rmag,R],[rDiam,RDiam],[bmag,B],[bDiam,BDiam],[APM-ID,id] ]
	def getColumnList( self ):
		columnList = []
		for column in self.columns:
			columnList.append( column.split( ':' ) )
		return columnList
		
		
	#
	#
	#
	def formNullValuesMap( self ):
		global EXTREME_TRACE, EXTREME_DEBUG
		if EXTREME_TRACE: print 'formNullValuesMap() enter'
		nullVals = {}
		fields = self.vot.getFields()
		for field in fields:
			vals = field.getNodesByName( 'VALUES' )
			name = field.getAttributes()['name']
			if EXTREME_DEBUG: print 'name type: ', type(name)
			for val in vals:
				attrs = val.getAttributes()
				if EXTREME_DEBUG: 
					print '=====attrs=====\n', attrs
				nullValue = attrs[ 'null' ]
				nullVals[ self.vot.getColumnIdx( name ) ] = nullValue
		if EXTREME_DEBUG: 
			print '=====nullVals=====\n', nullVals
		if EXTREME_TRACE: print 'formNullValuesMap() exit'
		return nullVals
	
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
		if FEEDBACK: print self.name + ' returned this number of rows: ' + str( len( self.vot.getDataRows() ) )
		#
		# We decide between stars and galaxies by using the class column
		clColIndex = self.vot.getColumnIdx( 'cl' )
		#
		# Form a map for those columns that can return a null value...
		nullsMap = self.formNullValuesMap()
		if DEBUG: print nullsMap
		#
		# Here are the rest of the indices, found
		# using the votable and the column names 
		# passed to us in the control file...	
		colIndices = []
		for column in self.getColumnList():
			colIndices.append( self.vot.getColumnIdx( column[0] ) )
		#
		# Form local lists of stars and galaxies
		starList = []
		galList = []
		#
		# Locate the data rows
		dataRows = self.vot.getDataRows()
		for row in dataRows:
			cells = self.vot.getData(row)
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
		if FEEDBACK: print self.name + ' returned this number of rows: ' + str( len( self.vot.getDataRows() ) )
		#
		# We need to decide between stars and galaxies.
		# These are just the indices for the criteria...
		b1cIndex = self.vot.getColumnIdx( 'B1s/g' ) 
		b2cIndex = self.vot.getColumnIdx( 'B2s/g' ) 
		r1cIndex = self.vot.getColumnIdx( 'R1s/g' ) 
		r2cIndex = self.vot.getColumnIdx( 'R2s/g' ) 
		icIndex = self.vot.getColumnIdx( 'Is/g' ) 
		#
		# Form a map for those columns that can return a null value...
		nullsMap = self.formNullValuesMap()
		#
		# Here are the rest of the indices, found
		# using the votable and the column names 
		# passed to us in the control file...	
		colIndices = []
		for column in self.getColumnList():
			colIndices.append( self.vot.getColumnIdx( column[0] ) )
		#
		# Form local lists of stars and galaxies
		starList = []
		galList = []
		#
		# Locate the data rows
		dataRows = self.vot.getDataRows()
		for row in dataRows:
			cells = self.vot.getData(row)
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
		# NOTE. There is ambiguity in the original script regarding star selection!!!
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
	
	def genericRetrieval( self ):
		global TRACE, DEBUG, FEEDBACK
		if TRACE: print 'enter: genericRetrieval() for ' + self.name
		if FEEDBACK: print self.name + ' returned this number of rows: ' + str( len( self.vot.getDataRows() ) )
		#
		# Here are the column indices, found
		# using the votable and the column names 
		# passed to us from the control file...	
		colIndices = []
		for column in self.getColumnList():
			colIndices.append( self.vot.getColumnIdx( column[0] ) )
		#
		# Form a map for those columns that can return a null value...
		nullsMap = self.formNullValuesMap()
		#
		# Form local list of hits
		hitList = []
		#
		# Locate the data rows
		dataRows = self.vot.getDataRows()
		for row in dataRows:
			cells = self.vot.getData(row)
			# Begin each constructed row with the self name 
			# and then append all the data requested to each row...
			subRow = [ self.name ]
			for index in colIndices:
				subRow.append( self.testAndSetEmpty( self.testAndSetNull( cells[ index ], index, nullsMap ) ) )
			hitList.append( subRow )
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
					self.vot = utils.read_votable( res, ofmt='votable' )
					if len( self.vot.getDataRows() ) == 0 : 
						if DEBUG: print self.name + ': no hits!'
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
				if DEBUG: print self.name + ': no result vot!'	
		except Exception, e2:
			if DEBUG: print self.name + ' cone Search failed: ', e2
			if FEEDBACK: print self.name + ' returned this number of rows: 0'	
		self.terminateFlag.set()
		if TRACE: print 'exit: coneSearchAndRetrieve() for ' + self.name
		return	
	# end of coneSearchAndRetrieve()

##################################################################
# SiapSearch class to substitute for the standard one,
# With this one I can switch options on/off.
#
class SiapSearch( Service ):

	def __init__( self, name, ivorn, ra, dec, radius, minimages, maximages, format ):
		Service.__init__( self, name, ivorn, ra, dec, radius )
		self.minimages = minimages
		self.maximages = maximages
		self.format = format
		return
				
	#
	#
	def execute( self ):	
		# use AR to build the query URL       
		query = Service.ar.ivoa.siap.constructQuery( self.ivorn, self.ra, self.dec, self.radius )
		fullURL = Service.ar.ivoa.cone.addOption( query, "FORMAT", self.format )
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
	#	I think it is here within the utils module...
	#+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
					self.vot = utils.read_votable( res, ofmt='votable' )
					countImages = self.retrieveImages()
					if countImages >= self.minimages :
						self.terminateFlag.set()
				except:
					if FEEDBACKL: print self.name + ': bad vot!'
			else:
				if FEEDBACK: print self.name + ': no result!'
		except Exception, e2:
			if ERROR: print self.name + ' image Search failed: ', e2	
		if TRACE: print 'exit: searchAndRetrieve()'
		return
	# end of searchAndRetrieve()



	#
	# Download function (retrieves images).
	# 
	def retrieveImages( self ) :
		global DEBUG, TRACE, FEEDBACK
		if TRACE: print 'enter: retrieveImages()'
		# Get column index for access url
		urlColIdx = self.vot.getColumnIdx ('VOX:Image_AccessReference')
		formatIdx = self.vot.getColumnIdx ('VOX:Image_Format')
		if urlColIdx < 0:
			if DEBUG: print "No access reference found in VOTable."
		else:
			# NB: Examine this. There's something lax here...
			dataRows = self.vot.getDataRows()
			getData = self.vot.getData
			countImages = len( dataRows )
			if FEEDBACK: print self.name + ' returned this number of images: ' + str( countImages )
			cnt = 0
			for row in dataRows:
				if self.terminateFlag.isSet():
					if FEEDBACK: print self.name + ' has been terminated prematurely!'
					break 
				cells = getData (row)
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
	#
	#
	#
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
			outfile.write( infile.read () )
			fileOK = True	
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
# Base class for a group of services
# 
#	
#
class ServiceGroup:
	
	serviceGroups = []
	totalServiceCount = 0 
	
	#
	# This is the function triggered on each thread.
#	def dispatchSearch( service ):
#		if TRACE: print 'enter: dispatchSearch() for service ' + service.name
#		service.searchAndRetrieve()
#		if TRACE: print 'exit: dispatchSearch() for service ' + service.name
#		return
#	dispatchSearch = Callable( dispatchSearch )

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
			if service.dispatched == False:
				service.dispatched = True	# We assume it gets dispatched.
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
			colvarNames = service.getColumnList()
			for row in dataCollection[1]:
				formattedRow = []
				i=0
				for value in row:
					if i==0:
						# The first entry in the formatted row is the source designation:
						formattedRow.append( 'Source=' + value )
					else:
						# The rest are driven by the columns list variable name:
						varName = colvarNames[i-1][1]
						formattedRow.append( varName + '=' + str(value) )
					i += 1
				sortedList.append( formattedRow )
		#
		# We sort the combined lists on spherical offset...
		sortedList.sort( Hits.cmpOnSphericalDistance )
		return sortedList

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
	if len(sys.argv)!=6:
		if ERROR: print 'Usage: ra dec radius file_of_ivorns results_directory'
		sys.exit(1)

	ra = float( sys.argv[1] )
	dec = float( sys.argv[2] )
	radius = float( sys.argv[3] )
	ifile = sys.argv[4]
	odir = sys.argv[5]
	return ra, dec, radius, ifile, odir
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
	# Don't like the use of global arguments here.
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
	service = ConeSearch( service_name, ivorn, ra, dec, radius, table_name, col_names, search_function_name ) 
	return service

def processImageLine( line, minimages, maximages, ra, dec, radius ):
	global FORMAT
	l = stripBrackets( line )
	bits = l.split( '|' )
	# save:
	# 0: short name for this service 
  # 1: its ivorn
	service_name = bits[0].strip()
	ivorn = bits[1].strip()
	service = SiapSearch( service_name, ivorn, ra, dec, radius, minimages, maximages, FORMAT )
	return service

#
# Process the input file of service ivorns.
#
# For each image source we wish to search, there is a list of alternative ivorns.
# For example, if we wished to search three image sources and there were
# two alternative services providing images for each source, that would
# make 6 ivorns altogether. We would return 3 ServiceGroups
#
# Returns a list of ServiceGroups
#
#
def processControlFile( ra, dec, radius, filePath ):
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
			if minimages == None and maximages == None:
				l = stripBrackets( l )
				bits = l.split()
				minimages = int( bits[0] )
				maximages = int( bits[1] )	
			else:
				serviceGroup.put( processImageLine( l, minimages, maximages, ra, dec, radius ) )
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


def dispatchSearch( service, dummyArg ):
	global TRACE
	if TRACE: print 'enter: dispatchSearch() for service ' + service.name
	service.searchAndRetrieve()
	if TRACE: print 'exit: dispatchSearch() for service ' + service.name
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
#
# Validate the input arguments passed to us...
( ra, dec, radius, ifile, odir ) = validateArgs()
if DEBUG: print ra, dec, radius, ifile, odir

#
# Retrieve ivorns for the sources we wish to search...
serviceGroups = processControlFile( ra, dec, radius, ifile )
#
# Create the overall search output directory...
if DEBUG: print( 'Output folder is called ' + odir )
os.mkdir( odir )
#
# Define the command to execute and the pool size
pool = easy_pool( dispatchSearch ) 
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
		input = ( service, None )
		if service != None:
			pool.put( input )
#
# Now observe the real work:
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
# We have to wait for threads to end to process the catalogue search results, 
# as the combined results have to be sorted on offset distance.
# The getHits() method does the sorting too...
starHits = stars.getHits()
galHits = gals.getHits()
#
# Write the files...
if len( starHits ) > 0:
	outputResults( odir, 'starHits.csv', starHits )
if len( galHits ) > 0:
	outputResults( odir, 'galaxyHits.csv', galHits )
#
if FEEDBACK: print( 'Finished at '  + time.strftime('%T') ) 
