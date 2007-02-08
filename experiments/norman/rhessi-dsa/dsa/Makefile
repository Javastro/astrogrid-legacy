# distribution details
SOURCEWAR=astrogrid-dsa-catalog-2006.3.05pl
DISTBASE=http://www.astrogrid.org/maven/org.astrogrid/wars/
MYSQL_JDBC=mysql-connector-java-5.0.4-bin.jar
# Download connector by hand from <http://dev.mysql.com/downloads>
# It can also be obtained from
# <http://www.mirrorservice.org/sites/ftp.mysql.com>, but extracting the 
# jar file from the source-and-binaries download would be too errorprone
# to be worth trying automatically.

# configuration details
DSASERVERBASE=http://www.astro.gla.ac.uk:8080
WEBAPP_NAME=rhessi-dsa
DBUSER=dsaaccess
DBPASS=
DBHOST=bern.astro.gla.ac.uk
DBNAME=dsa
METADOC=rhessi-metadoc.xml

edit=sed \
	-e 's,@BASEURI\@,$(DSASERVERBASE)/$(WEBAPP_NAME)/,' \
	-e 's,@DBHOST\@,$(DBHOST),' \
	-e 's,@DBNAME\@,$(DBNAME),' \
	-e 's,@DBUSER\@,$(DBUSER),' \
	-e 's,@DBPASS\@,$(DBPASS),' \
	-e 's,@METADOC\@,$(METADOC),'

all: $(WEBAPP_NAME).war

$(WEBAPP_NAME).war: $(WEBAPP_NAME)
	cd $(WEBAPP_NAME); jar cf ../$@ *

$(SOURCEWAR).war:
	wget $(DISTBASE)$(SOURCEWAR).war

$(WEBAPP_NAME): $(SOURCEWAR).war $(MYSQL_JDBC) \
		rhessi-dsa.properties $(METADOC)
	rm -Rf $(WEBAPP_NAME)
	mkdir $(WEBAPP_NAME)
	cd $(WEBAPP_NAME); jar xf ../$(SOURCEWAR).war
	cp $(MYSQL_JDBC) $(WEBAPP_NAME)/WEB-INF/lib
	cp rhessi-dsa.properties $(WEBAPP_NAME)/WEB-INF/classes/default.properties
	cp $(METADOC) $(WEBAPP_NAME)/WEB-INF/classes

rhessi-dsa.properties: rhessi-dsa.properties.in Makefile
	rm -f $@ $@.tmp
	$(edit) $< >$@.tmp && mv $@.tmp $@

clean:
	rm -Rf $(WEBAPP_NAME) $(WEBAPP_NAME).war
	rm -f rhessi-dsa.properties
