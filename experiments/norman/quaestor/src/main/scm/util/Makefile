# Relative path to masters of Scheme utilities, in Norman's CVS repository
SCM_UTILS=../../../../../../scm

# The module sexp-xml.scm contains $(SCM_UTILS)/sexp-xml.scm, somewhat edited.
# sexp->xml and escape-string-for-xml are prefixed with 'sexp-xml:',
# and the function PFORMAT is renamed back to FORMAT.

all:	decode-der-implementation.scm \
	xmlrpc-implementation.scm \
	lambda-contract.scm

# Don't do any of the possible ;@CONFIG comment stripping, here --
# handle that in the module file.
decode-der-implementation.scm: $(SCM_UTILS)/decode-der.scm
	cp $< $@
xmlrpc-implementation.scm: $(SCM_UTILS)/xmlrpc.scm
	cp $< $@
lambda-contract.scm: $(SCM_UTILS)/lambda-contract.scm
	cp $< $@
