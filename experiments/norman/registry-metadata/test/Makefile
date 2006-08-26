CWM=PYTHONPATH=/Volumes/Lagrange/Data/tools/cwm-cvs/lib/python2.3/site-packages /Volumes/Lagrange/Data/tools/cwm-cvs/bin/cwm

TESTCASES=siap-results

%.rdf: %.xml
	rm -f $@
	xsltproc --path ../code rdf-all.xslt $< >$@.tmp && mv $@.tmp $@

# Create a normalised ntriples representation.
%.ntriples: %.rdf
	rm -f $@
	$(CWM) -pipe -rdf $< -ntriples | ./normalise-ntriples >$@

# Run the RDF through Eyeball.  Exclude the URI check, which objects
# to ivo:... URIs (as indeed it should, grumble, grumble)
%.eyeballed: %.rdf
	rm -f $@
	../code/eyeball.sh -check $< \
	  -assume ../rdfschemas/VOResource-1.0.owl \
	  -exclude URI >$@.tmp && mv $@.tmp $@

check: $(TESTCASES:=.ntriples) $(TESTCASES:=.eyeballed)
	@for f in *.eyeballed; do echo "### $$f..."; cat $$f; done
	@rm -f *.diff
	@for f in $(TESTCASES); do \
	  if diff $$f.ntriples-correct $$f.ntriples >$$f.diff; then \
	    echo "$$f: ok"; \
	    rm $$f.diff; \
	  else \
	    echo "$$f: failed -- see $$f.diff"; \
	  fi; \
	done

clean:
	rm -f *.ntriples *.rdf *.diff *.eyeballed
