prefix=$(HOME)/rhessi-dsa
VERSION_STRING=rhessi-dsa-1.0

all:
	cd dsa; make
	cd scripts; make

install:
	cd dsa; make prefix=$(prefix) "VERSION_STRING=$(VERSION_STRING)" install
	cd scripts; make prefix=$(prefix) "VERSION_STRING=$(VERSION_STRING)" install

clean:
	cd dsa; make clean
	cd scripts; make clean
