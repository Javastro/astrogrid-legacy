CVS_TOP=../..
include $(CVS_TOP)/text/webpages/misc/Makefile.webpages

PNG=asserted-hierarchy.png inferred-hierarchy.png
# include base.css, because the access-control-demo.html refers to a relative
# copy, so that the demo files can be standalone
EXTRA_FILES=access-control-demo.html $(PNG) demo.tar.gz quaestor-0.3.war \
	base.css


access-control.html: access-control.xml access-control.bbl
demo/demo.html: demo/demo.xml

%.gz: %
	rm -f $@
	gzip --best $<

# star is a tar which doesn't include OS X file metadata
demo.tar: access-control-demo
	star cf $@ access-control-demo

access-control-demo: demo/demo.html
	rm -Rf access-control-demo
	mkdir access-control-demo
	cp demo/demo.html access-control-demo/index.html
	cp $(CVS_TOP)/text/webpages/nxg.me.uk/style/base.css access-control-demo
	cd demo; \
	  cp access-control.owl *.rq *.n3 xmlrpc-*.xml ../access-control-demo

access-control-demo.html: demo/demo.html
	cp $< $@

base.css: $(CVS_TOP)/text/webpages/nxg.me.uk/style/base.css
	cp $< $@

install: $(WEB)/note/2006/access-control/access-control.html

$(WEB)/note/2006/access-control/access-control.html: \
		access-control.html $(EXTRA_FILES)
	$(GENERAL_INSTALL_WEBPAGE)
	D=`dirname $@`; \
	  for f in $(EXTRA_FILES); do \
	    rm -f $$D/$$f; \
	    cp $$f $$D; \
	  done

tidy:
	rm -f *~

clean: tidy
	rm -Rf access-control-demo
	rm -f demo.tar demo.tar.gz access-control-demo.html demo/demo.html
	rm -f access-control.bbl access-control.blg access-control.html
	rm -f base.css
