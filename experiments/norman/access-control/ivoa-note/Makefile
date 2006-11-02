# Rules for creating note.html from note.xml

%.aux: %.xml structure.xslt
	rm -f $@
	xsltproc --stringparam document-id $< --stringparam target aux \
	  structure.xslt $< >tmp && mv tmp $@
	echo '\bibdata{compsci}' >> $@
	echo '\bibstyle{plainhtml}' >> $@

# Following relies on BIBINPUTS and BSTINPUTS being set OK.
%.bbl: %.aux
	bibtex $<
	sed '/% *$$/{N;s/% *\n//;}; s/[{}]//g; s,\([^/]\)~,\1 ,g' $@ >$(@:=.tmp)
	mv $(@:=.tmp) $@

note.html: note.xml note.bbl structure.xslt
	rm -f $@
	xsltproc --stringparam document-id note.xml \
	  structure.xslt note.xml >tmp && mv tmp $@

clean:
	rm -f note.html tmp
