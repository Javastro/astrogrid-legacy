# top-level makefile for the registry-metadata work
#
# This doesn't do much -- it's just here on a principle of least surprise

VERSION=0.1
RELEASEDATE=2008 May 03
DIST=registry-metadata-$(VERSION)

# TAR is a tar command which doesn't store HFS+ extended attributes
TAR=star
#TAR=COPY_EXTENDED_ATTRIBUTES_DISABLE=1 tar

edit=sed -e s/@VERSION\@/$(VERSION)/ \
  -e s/@RELEASEDATE\@/'$(RELEASEDATE)'/

%.gz: %
	rm -f $@
	gzip --best $<

all:
	cd code; make
	cd xschemas; make

dist: $(DIST).tar.gz

$(DIST).tar: $(DIST)/README
	rm -f $@
	$(TAR) cf $@ $(DIST)

$(DIST)/README:
	if test -d $(DIST); then rm -R $(DIST); fi
	mkdir $(DIST) $(DIST)/xslt $(DIST)/schemas
	cd code;       make DISTDIR=../$(DIST)/xslt    install
	cd xschemas;   make DISTDIR=../$(DIST)/schemas install
	cd rdfschemas; make DISTDIR=../$(DIST)/schemas install
	$(edit) README >$(DIST)/README

# This doesn't do a very rigourous check, but it's better than nothing.
check:
	cd test; make check

clean:
	for d in code xschemas test; do (cd $$d; make clean); done
	rm -Rf $(DIST) $(DIST).tar.gz
