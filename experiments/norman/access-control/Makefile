CVS_TOP=$(CT)
#include $(CVS_TOP)/text/webpages/misc/Makefile.webpages

VERSION=2008-08-20

PNG=asserted-hierarchy.png inferred-hierarchy.png
# include base.css, because the access-control-demo.html refers to a relative
# copy, so that the demo files can be standalone
EXTRA_FILES=access-control-demo.html $(PNG) demo.tar.gz quaestor-0.3.war

BIBTEX=bibtex
XSLTPROC=xsltproc
LX=java -cp $(CVS_TOP)/code/lx/lx.jar uk.me.nxg.lx.helpers.Transformer
TAR=tar

edit=sed -e 's/@VERSION\@/$(VERSION)/'

%.gz: %
	rm -f $@
	gzip --best $<

%.aux: %.xml structure.lx
	rm -f $@ $@.tmp
	$(LX) $< document-id=$< target=aux structure.lx >$@.tmp && mv $@.tmp $@
	printf '\\bibstyle{plainhtml}\n' >> $@

# Following relies on BIBINPUTS and BSTINPUTS being set OK.
%.bbl: %.aux detex.sed
	rm -f $@ $@.tmp
	$(BIBTEX) $*
	sed -e '1s+<dl>+<dl xmlns="http://www.w3.org/1999/xhtml">+' -f detex.sed $@ >$@.tmp && mv $@.tmp $@

%.xhtml: %.xml structure.lx
	rm -f $@ $@.tmp
	$(LX) document-id=$< $< structure.lx | $(edit) >$@.tmp && mv $@.tmp $@

%.html: %.xhtml identity-html.xslt
	rm -f $@ $@.tmp
	$(XSLTPROC) identity-html.xslt $< >$@.tmp && mv $@.tmp $@

# Default target

all: access-control.html
access-control.xhtml: access-control.xml access-control.bbl

demo/demo.html: demo/demo.xml

# The IVOA Note is actually in the directory ivoa-note/, but it's easier
# to build it here.
note.xhtml: note.xml note.bbl
note.xml: ivoa-note/note.xml
	cp ivoa-note/note.xml .

# star is a tar which doesn't include OS X file metadata
demo.tar: access-control-demo
	$(TAR) cf $@ access-control-demo

access-control-demo: demo/demo.html
	rm -Rf access-control-demo
	mkdir access-control-demo
	cp demo/demo.html access-control-demo/index.html
	cp $(CVS_TOP)/text/webpages/nxg.me.uk/style/base.css access-control-demo
	cd demo; cp access-control.owl *.rq *.n3 xmlrpc-*.xml ../access-control-demo

access-control-demo.html: demo/demo.html
	cp $< $@

webpage-tarball.tar: access-control.html $(EXTRA_FILES)
	rm -Rf distdir && mkdir distdir
	cp access-control.html distdir/index.html
	cp $(EXTRA_FILES) distdir
	echo "Built in $$PWD" >distdir/000README
	cd distdir; $(TAR) cf ../$@ *
	rm -Rf distdir

#install: $(WEB)/note/2006/access-control/index.html
#
#$(WEB)/note/2006/access-control/index.html: access-control.html $(EXTRA_FILES)
#	$(GENERAL_INSTALL_WEBPAGE)
#	D=`dirname $@`; \
#	  for f in $(EXTRA_FILES); do \
#	    rm -f $$D/$$f; \
#	    cp $$f $$D; \
#	  done

structure.lx identity-html.xslt detex.sed base.css:
	cp $(CVS_TOP)/text/webpages/misc/$@ .


tidy:
	rm -f *~

clean: tidy
	rm -Rf access-control-demo
	rm -f demo.tar demo.tar.gz access-control-demo.html demo/demo.html
	rm -f access-control.bbl access-control.blg access-control.html access-control.xhtml
	rm -f note.*
	rm -f base.css
