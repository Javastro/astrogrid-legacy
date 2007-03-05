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

(define sharp-bounds-n3 "
@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>.
@prefix utype: <http://example.ivoa.net/utypes#>.
@prefix myns: <http://example.org/utypes/1.0#>.
@prefix ivoa: <http://www.ivoa.net/ut/characterization#>.

myns:sharpBounds a utype:UType; 
    rdfs:subClassOf 
        ivoa:coverage-bounds . 
myns:verySharpBounds a utype:UType;
    rdfs:subClassOf
        myns:sharpBounds .")

(define charn-def "
@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>.
@prefix utype: <http://example.ivoa.net/utypes#>.
@prefix ivoa: <http://www.ivoa.net/ut/characterization#>.

ivoa:coverage-bounds a utype:UType.")

;; The NAMESPACE-SEEN? procedure is no longer exported from the 'knowledge'
;; module, so the following tests can't work.  The tests on that procedure
;; seem valueable, though, so at some point these should be moved to be 
;; internal to the 'knowledge' module.
;;
;; In fact, the other procedure we use here, *INGEST-UTYPE-DECLARATION/STREAM!,
;; is also internal to that module -- do I sense a theme?

;; (expect seen-no
;;         #f
;;         (namespace-seen?
;;          (mk-uri "http://example.org/utypes/1.0#anything")))
;; (expect seen-no
;;         #f
;;         (namespace-seen?
;;          (mk-uri "http://www.ivoa.net/ut/characterization#anything")))

;; ;; We haven't told the reasoner anything, yet -- no superclasses
;; (expect superclasses1
;;         #f
;;         (query-utype-superclasses
;;          (mk-uri "http://example.org/utypes/1.0#sharpBounds")))

;; Add the assertions
;;
;; These namespaces are not resolvable, so we have to fake that with
;; *INGEST-UTYPE-DECLARATION/STREAM!, adding them by hand
;; as if they had been resolved.
(*ingest-utype-declaration/stream! "http://example.org/utypes/1.0"
                                   (string->input-stream sharp-bounds-n3))
(*ingest-utype-declaration/stream! "http://www.ivoa.net/ut/characterization"
                                   (string->input-stream charn-def))

;; ;; Now check whether we know we've seen this namespace
;; ;; First, check we have logged this namespace
;; (expect seen-1
;;         #t
;;         (and (namespace-seen? (mk-uri "http://example.org/utypes/1.0#stuff"))
;;              #t))
;; ;; we still haven't 'seen' this namespace -- only mentioned incidentally
;; ;; to our ingesting the http://example.org/utypes/1.0 ns
;; (expect seen-no-2
;;         #f
;;         (namespace-seen?
;;          (mk-uri "http://www.ivoa.net/ut/characterization#anything")))

;; ;; Normalisation
;; ;;
;; ;; This URI is the same as the seen one after normalisation, but the 
;; ;; RDF spec says that namespaces should be compared as strings, and not
;; ;; in their normalised forms.
;; (expect seen-3
;;         #f
;;         (namespace-seen?
;;          (mk-uri "http://example.org/utypes/./1.0")))
;; (expect seen-4
;;         #f
;;         (namespace-seen?
;;          (mk-uri "http://example.org//utypes/1.0")))


;; The order of the returned superclasses is not specified below: this
;; happens to be correct (always?).  This also tests that the transitivity
;; reasoning is working.
(expect superclasses2
        '("http://example.org/utypes/1.0#sharpBounds"
          "http://www.ivoa.net/ut/characterization#coverage-bounds")

        (sort-list
         (query-utype-superclasses "http://example.org/utypes/1.0#verySharpBounds")
         string<=?))

;; Now add assertions concerning a completely different namespace.
;; The descriptions of these two namespaces should end up completely disjoint.
;; Unresolvable namespace -- see comments above.
(*ingest-utype-declaration/stream!
 "http://example.org/test"
 (string->input-stream "
@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>.
@prefix : <http://example.org/test#>.
:c1 a rdfs:Class.
:c2 a rdfs:Class;
    rdfs:subClassOf :c1.
"))

;; utilities
(define (show-model-as-triples model)
  (define-java-classes
    <java.io.string-writer>)
  (define-generic-java-methods
    write to-string)
  (let ((sw (java-new <java.io.string-writer>)))
    (write model sw (->jstring "N-TRIPLE"))
    (->string (to-string sw))))
(define (make-regexp-pattern regexp)
  (define-java-classes <java.util.regex.pattern>)
  (define-generic-java-methods compile)
  (compile (java-null <java.util.regex.pattern>)
           (->jstring regexp)))
(define (count-matches pattern string)
  (define-generic-java-methods matcher find end)
  (let ((m (matcher pattern (->jstring string))))
    (let loop ((n 0)
               (start (->jint 0)))
      (if (->boolean (find m start))
          (loop (+ n 1) (end m))
          n))))


(let ((utypes-pattern (make-regexp-pattern "(http://example.org/utypes/1.0)"))
      (test-pattern   (make-regexp-pattern "(http://example.org/test)"))
      (utypes-triples (show-model-as-triples
                       (get-namespace-description
                        "http://example.org/utypes/1.0")))
      (test-triples   (show-model-as-triples
                       (get-namespace-description
                        "http://example.org/test"))))
  (expect crossover-11
          5
          (count-matches utypes-pattern  utypes-triples))
  (expect crossover-12
          0
          (count-matches test-pattern utypes-triples))
  (expect crossover-21
          4
          (count-matches test-pattern test-triples))
  (expect crossover-22
          0
          (count-matches utypes-pattern  test-triples)))

(if (failures-in-block?)
    (format #t "Errors in knowledge.scm~%"))
