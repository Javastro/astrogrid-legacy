;; test cases for the utype-knowledge package

(import s2j)

(require-library 'utype-resolver/knowledge)
(import knowledge)

(define-java-class <uri> |java.net.URI|)

(define (mk-uri s)
  (java-new <uri> (->jstring s)))
(define u1 (mk-uri "http://example.org/a/b#f1"))

(expect simple1 #f (namespace-seen? u1))
(namespace-seen! u1)
(expect simple2 #t (namespace-seen? u1))

;; try with a URI differing only in fragment -- same namespace
(expect newfrag
        #t
        (namespace-seen? (mk-uri "http://example.org/a/b#f2")))
(expect newns
        #f
        (namespace-seen? (mk-uri "http://example.org/x#f2")))

;; Should the following test expect true or false?
;; This URI is the same as u1 after normalisation: should it be the same ns?

;; The following test is commented out, because it doesn't work.  This
;; should be the first sight of this namespace, but the implementation
;; of the function doesn't get this right.  See discussion there.
(expect denorm
        #f
        (namespace-seen? (mk-uri "http://example.org/a/./b#f1")))



(define (string->input-stream s)
  (define-java-class <java.io.byte-array-input-stream>)
  (define-generic-java-method get-bytes)
  (java-new <java.io.byte-array-input-stream>
            (get-bytes (->jstring s))))

(define sharp-bounds-n3 "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>.
@prefix myns: <http://example.org/utypes/1.0#>.
@prefix ivoa: <http://www.ivoa.net/ut/characterization#>.

myns:sharpBounds a rdfs:Class; 
    rdfs:subClassOf 
        ivoa:characterizationAxis-coverage-bounds . ")
(define char-bounds-n3 "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>.
@prefix ivoa: <http://www.ivoa.net/ut/characterization#>.

ivoa:characterizationAxis-coverage-bounds a rdfs:Class; 
    rdfs:subClassOf 
        ivoa:characterizationAxis-info . ")

;; We haven't told the reasoner anything, yet -- no superclasses
(expect superclasses1
        #f
        (query-utype-superclasses "http://example.org/utypes/1.0#sharpBounds"))

;; Add the first lot of assertions
(ingest-utype-declaration-from-stream! (string->input-stream sharp-bounds-n3))

(expect superclasses2
        '("http://www.ivoa.net/ut/characterization#characterizationAxis-coverage-bounds")

        (query-utype-superclasses "http://example.org/utypes/1.0#sharpBounds"))

;; more assertions
(ingest-utype-declaration-from-stream! (string->input-stream char-bounds-n3))

;; The order of the returned superclasses is not specified below: this
;; happens to be correct (always?)
(expect superclasses3
        '("http://www.ivoa.net/ut/characterization#characterizationAxis-coverage-bounds"
          "http://www.ivoa.net/ut/characterization#characterizationAxis-info")

        (sort-list
         (query-utype-superclasses "http://example.org/utypes/1.0#sharpBounds")
         string<=?))
