CWM=PYTHONPATH=/Data/tools/cwm-cvs/lib/python2.3/site-packages /Volumes/Lagrange/Data/tools/cwm-cvs/bin/cwm
JENA=$T/Jena-2.3
#CP=`ls $(JENA)/lib/*.jar|sed -n '1,$$H;$${g;s/\n/:/gp;}'`
CP=$(JENA)/lib/antlr-2.7.5.jar:$(JENA)/lib/arq.jar:$(JENA)/lib/commons-logging.jar:$(JENA)/lib/concurrent.jar:$(JENA)/lib/icu4j_3_4.jar:$(JENA)/lib/jakarta-oro-2.0.8.jar:$(JENA)/lib/jena.jar:$(JENA)/lib/jenatest.jar:$(JENA)/lib/junit.jar:$(JENA)/lib/log4j-1.2.12.jar:$(JENA)/lib/stax-1.1.1-dev.jar:$(JENA)/lib/stax-api-1.0.jar:$(JENA)/lib/xercesImpl.jar:$(JENA)/lib/xml-apis.jar

TESTCASES=test-instance \
	sia-1.0 mssl-reg-authority mssl-reg-registry \
	organisation-ivoa organisation-mssl data-collection-1.0

# Don't reap intermediate files -- handy for debugging
.PRECIOUS: %.rdf %.xslt

%.rdf: %.xml ../code/STAMP
	@rm -f $@
	xsltproc --path ../code convert-root.xslt $< >$@.tmp
	@if grep 'UNKNOWN' $@.tmp; then echo "ERRORs in $@!!"; false; else :; fi && mv $@.tmp $@
# %.rdf: %.xml %.xslt
# 	rm -f $@
# 	xsltproc --path ../code ${<:.xml=.xslt} $< >$@.tmp && mv $@.tmp $@
# 	if grep 'UNKNOWN' $@; then echo "UNKNOWN in $@!!"; false; else :; fi

%.xslt: %.xml
	rm -f $@
	java -jar ../code/ExtractNamespaces.jar --generate-xslt --indent $< >$@

# Create a ntriples representation.
%.nt: %.rdf
	@rm -f $@
#	CLASSPATH=`ls $(JENA)/lib/*.jar|sed -n '1,$$H;$${g;s/\n/:/gp;}'` 
	@java -cp $(CP) jena.rdfcopy $< RDF/XML N-TRIPLE \
	  | ./normalise-ntriples >$@.tmp && mv $@.tmp $@
#	$(CWM) -pipe -rdf $< -ntriples | ./normalise-ntriples >$@

# ... and a normalised version, ready for diffing
%.nnt: %.nt
	sed 's/_:[0-9a-f]*/_:b/g' $< >$@
#%.nnt: %.nt
#	./normalise-ntriples --suppress-blank-nodes $< >$@

# Create .n3 representation (just for debugging)
%.n3: %.rdf
	rm -f $@
	CLASSPATH=`ls $(JENA)/lib/*.jar|sed -n '1,$$H;$${g;s/\n/:/gp;}'` \
	  java jena.rdfcopy $< RDF/XML N3 >$@.tmp && mv $@.tmp $@
#	$(CWM) -pipe -rdf $< -n3 >$@

# Run the RDF through Eyeball.  Exclude the URI check, which objects
# to ivo:... URIs (as indeed it should, grumble, grumble)
#	  -assume $(T)/eyeball-2.0/mirror/owl.rdf 
%.eyeballed: %.rdf
	rm -f $@
	../code/eyeball.sh \
	  -assume $(T)/eyeball-2.0/mirror/owl.rdf \
	  -assume ../rdfschemas/VOResource-1.0.owl \
	  -assume ../xschemas/SIA-v1.0.rdfs \
	  -assume ../xschemas/TabularDB-v0.3.rdfs \
	  -assume ../xschemas/VODataService-v1.0.rdfs \
	  -assume ../xschemas/VORegistry-v1.0.rdfs \
	  -exclude URI \
	  -check $< \
	  >$@.tmp 2>&1 && mv $@.tmp $@

# cat all the eyeballed problems, and diff the ntriples files against
# correct values.  If there's a problem with the latter, then leave the .diff
# file, and also create a .diffx file, which is the diff of the .ntriples files
# with the anonymous nodes all given the same identifier, which is easier
# to read.
check: $(TESTCASES:=.nt) # $(TESTCASES:=.eyeballed)
#	@for f in *.eyeballed; do echo "### $$f..."; cat $$f; done
	@rm -f *.diff
	@for f in $(TESTCASES); do \
	  if diff $$f.nt-correct $$f.nt >$$f.diff; then \
	    echo "$$f: ok"; \
	    rm $$f.diff; \
	  else \
	    echo "$$f: failed (maybe) -- see $$f.diff & $$f.diffx"; \
	    sed 's/_:[0-9a-f]*/_:X/g' $$f.nt >t.nt; \
	    sed 's/_:[0-9a-f]*/_:X/g' $$f.nt-correct >t.nt-correct; \
	    diff t.nt-correct t.nt >$$f.diffx; \
	    rm t.nt t.nt-correct; \
	  fi; \
	done

../code/STAMP:
	cd ../code; make STAMP

#test.xslt: test.xsd
#	cd ../code; make test.xslt
#	ln -sf ../code/test.xslt

clean:
	rm -f *.nt *.rdf *.diff *.diffx *.eyeballed
