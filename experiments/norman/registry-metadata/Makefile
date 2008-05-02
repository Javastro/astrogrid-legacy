# top-level makefile for the registry-metadata work
#
# This doesn't do much -- it's just here on a principle of least surprise

all:
	cd code; make
	cd xschemas; make

check:
	cd test; make check

clean:
	for d in code xschemas test; do (cd $$d; make clean); done
