MYURL=http://www.ivoa.net/Document/Note-xxx

# Rules for creating note.html from note.xml

%.aux: %.xml structure.xslt
	rm -f $@
	xsltproc --stringparam document-id $< --stringparam target aux \
	  structure.xslt $< >tmp && mv tmp $@
	echo '\bibstyle{plainhtml}' >> $@

# Following relies on BIBINPUTS and BSTINPUTS being set OK.
%.bbl: %.aux
	bibtex $<
	sed '1s+<dl>+<dl xmlns="http://www.w3.org/1999/xhtml">+; /% *$$/{N;s/% *\n//;}; s/[{}]//g; s,\([^/]\)~,\1 ,g; s+\\,c+ç+g; s,\\'"'"'e,é,g; s,\\'"'"'a,á,g; s,\\\^o,ô,g' $@ >$(@:=.tmp)
	mv $(@:=.tmp) $@

note.html: note.xml note.bbl structure.xslt
	rm -f $@
	xsltproc --stringparam document-id note.xml \
	  structure.xslt note.xml >tmp && mv tmp $@

# Check the RDFa content of note.html
# (note, this refers to a local copy of the script RDFa2RDFXML.xsl available from
# http://ns.inria.fr/grddl/rdfa/)
note.rdf: note.html
	xsltproc /checkouts/me/text/webpages/misc/RDFa2RDFXML.xsl note.html >note.rdf
note.ttl: note.rdf
	rapper --output turtle note.rdf $(MYURL) >note.ttl

# The following extracts from my compsci.bib database those entries which are 
# referred to in note.aux.  To use this, change the <?bibliography?> line to 
# refer to "compsci", then make note.html, then make this target, then
# change <?bibliography?> to refer again to "rm-refs".
# It doesn't work completely, because bibtool for some reason fails to pick up a 
# crossref, but it's a start.
rm-refs.bib:
	echo "new.entry.type = {electronic}" >bibtool.rsc
	echo "new.entry.type = {webpage}" >>bibtool.rsc
	/Data/tools/BibTool-2.46/bin/bibtool -r bibtool.rsc -x note.aux -o rm-refs.bib

clean:
	rm -f note.html note.aux note.bbl note.blg note.rdf note.ttl tmp
