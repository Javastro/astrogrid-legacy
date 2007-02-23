;; test cases for the utype-knowledge package

(import s2j)

(require-library 'utype-resolver/knowledge)
(import knowledge)

(define-java-class <uri> |java.net.URI|)

(define (mk-uri s)
  (java-new <uri> (->jstring s)))

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
        ivoa:coverage-bounds . 
myns:verySharpBounds a rdfs:Class;
    rdfs:subClassOf
        myns:sharpBounds .")

(expect seen-no
        #f
        (namespace-seen?
         (mk-uri "http://example.org/utypes/1.0#anything")))
(expect seen-no
        #f
        (namespace-seen?
         (mk-uri "http://www.ivoa.net/ut/characterization#anything")))

;; We haven't told the reasoner anything, yet -- no superclasses
(expect superclasses1
        #f
        (query-utype-superclasses
         (mk-uri "http://example.org/utypes/1.0#sharpBounds")))

;; Add the assertions
(ingest-utype-declaration-from-stream! "http://example.org/utypes/1.0"
                                       (string->input-stream sharp-bounds-n3))

;; Now check whether we know we've seen this namespace
;; First, check we have logged this namespace
(expect seen-1
        #t
        (and (namespace-seen? (mk-uri "http://example.org/utypes/1.0#stuff"))
             #t))
;; we still haven't 'seen' this namespace -- only mentioned incidentally
;; to our ingesting the http://example.org/utypes/1.0 ns
(expect seen-no-2
        #f
        (namespace-seen?
         (mk-uri "http://www.ivoa.net/ut/characterization#anything")))

;; Normalisation
;;
;; This URI is the same as the seen one after normalisation, but the 
;; RDF spec says that namespaces should be compared as strings, and not
;; in their normalised forms.
(expect seen-3
        #f
        (namespace-seen?
         (mk-uri "http://example.org/utypes/./1.0")))
(expect seen-4
        #f
        (namespace-seen?
         (mk-uri "http://example.org//utypes/1.0")))


;; The order of the returned superclasses is not specified below: this
;; happens to be correct (always?).  This also tests that the transitivity
;; reasoning is working.
(expect superclasses2
        '("http://example.org/utypes/1.0#sharpBounds"
          "http://www.ivoa.net/ut/characterization#coverage-bounds")

        (sort-list
         (query-utype-superclasses "http://example.org/utypes/1.0#verySharpBounds")
         string<=?))

(if (failures-in-block?)
    (format #t "Errors in knowledge.scm~%"))
