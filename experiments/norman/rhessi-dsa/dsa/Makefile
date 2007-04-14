# prefix must be set on command-line
prefix=/no/such/directory

# distribution details
SOURCEWAR=astrogrid-dsa-catalog-2007.1.01pl
DISTBASE=http://www.astrogrid.org/maven/org.astrogrid/wars/
MYSQL_JDBC=mysql-connector-java-5.0.5-bin.jar
# Download connector by hand from <http://dev.mysql.com/downloads>
# It can also be obtained from
# <http://www.mirrorservice.org/sites/ftp.mysql.com>, but extracting the 
# jar file from the source-and-binaries download would be too errorprone
# to be worth trying automatically.

# configuration details
DSASERVERBASE=http://www.astro.gla.ac.uk
WEBAPP_NAME=rhessi-dsa
DBUSER=dsaaccess
DBPASS=
DBHOST=bern.astro.gla.ac.uk
DBNAME=dsa
METADOC=rhessi-metadoc.xml
# version string should be overridden by the parent makefile
VERSION_STRING=rhessi-dsa-vxxx

edit=sed \
	-e 's,@BASEURI\@,$(DSASERVERBASE)/$(WEBAPP_NAME)/,' \
	-e 's,@DBHOST\@,$(DBHOST),' \
	-e 's,@DBNAME\@,$(DBNAME),' \
	-e 's,@DBUSER\@,$(DBUSER),' \
	-e 's,@DBPASS\@,$(DBPASS),' \
	-e 's,@METADOC\@,$(METADOC),' \
	-e 's,@VERSION_STRING\@,$(VERSION_STRING),'

all: $(WEBAPP_NAME).war

$(WEBAPP_NAME).war: $(WEBAPP_NAME)
	cd $(WEBAPP_NAME); jar cf ../$@ *

$(SOURCEWAR).war:
	wget $(DISTBASE)$(SOURCEWAR).war
$(MYSQL_JDBC):
	@echo "Component missing! Download $(MYSQL_JDBC) from dev.mysql.com"
	@false

$(WEBAPP_NAME): $(SOURCEWAR).war $(MYSQL_JDBC) \
		rhessi-dsa.properties $(METADOC)
	rm -Rf $(WEBAPP_NAME)
	mkdir $(WEBAPP_NAME)
	cd $(WEBAPP_NAME); jar xf ../$(SOURCEWAR).war
	cp $(MYSQL_JDBC) $(WEBAPP_NAME)/WEB-INF/lib
	cp rhessi-dsa.properties $(WEBAPP_NAME)/WEB-INF/classes/default.properties
	cp gu-logo-tiny.gif $(WEBAPP_NAME)/logo.gif
	cp $(METADOC) $(WEBAPP_NAME)/WEB-INF/classes

rhessi-dsa.properties: rhessi-dsa.properties.in Makefile
	rm -f $@ $@.tmp
	$(edit) -e 's,@GENERATED@,DO NOT EDIT -- Generated from rhessi-dsa.properties.in,' \
	  $< >$@.tmp && mv $@.tmp $@

install: $(WEBAPP_NAME).war
	test -d $(prefix) || { echo "No directory $(prefix)"; false; }
	test -d $(prefix)/lib || mkdir $(prefix)/lib
	cp $(WEBAPP_NAME).war $(prefix)/lib

clean:
	rm -Rf $(WEBAPP_NAME) $(WEBAPP_NAME).war
	rm -f rhessi-dsa.properties
