;; Library module for UType-resolver

(import s2j)

(require-library 'sisc/libs/srfi/srfi-1)

(require-library 'util/lambda-contract)
(require-library 'quaestor/utils)
(require-library 'quaestor/jena)

;; (define-syntax self-test     ;in this file, discard all self-test content
;;   (syntax-rules ()
;;     ((_ form . forms)
;;      (define dummy #f))))

(module knowledge
    (first-sight-of-namespace
     ingest-utype-declaration-from-uri!
     ingest-utype-declaration-from-stream!
     query-utype-superclasses)

(import* srfi-1 remove)
(import* utils
         is-java-type?)
(import jena)

;; Useful classes
(define-java-classes
  (<jstring> |java.lang.String|)
  (<uri> |java.net.URI|)
  <java.io.input-stream>)

;; predicates used in contracts
(define (uri? x)
  (is-java-type? x <uri>))
(define (java-stream? x)
  (is-java-type? x <java.io.input-stream>))

;; (self-test ; dummy test
;;  (expect #t (and #t #t)))

;; uri->namespace : uri -> uri
;; Given a URI, returns the corresponding namespace as a new URI.
(define (uri->namespace uri)
  ;; the following follows the procedure implied by the class identies
  ;; documented at the top of the java.net.URI javadocs
  (define-java-class <uri> |java.net.URI|)
  (define-generic-java-methods
    get-scheme get-authority get-path get-query)
  (java-new <uri>
            (get-scheme uri)
            (get-authority uri)
            (get-path uri)
            (get-query uri)
            (java-null <jstring>)))

;; first-sight-of-namespace : URI -> boolean
;; Given a URI, return #t if this is the first time this URI has been seen,
;; and #f otherwise
;; (simple-minded linear search: use a hash-table instead?)
;;
;; The namespace comparison is done on normalised URIs, because that's
;; what URI.equals() seems to do.  It shouldn't (FIXME), since
;; <http://www.w3.org/TR/rdf-concepts/#section-Graph-URIref> says that
;; URIs are comparable if and only if they are equal character by
;; character.  There are other discussions of URI-equivalence
;; mentioned there, including
;; <http://www.w3.org/2001/tag/issues.html#URIEquivalence-15> and
;; <http://www.textuality.com/tag/uri-comp-4>.  Leave it as this for
;; the moment, since there's some cleverness going on either with
;; java.net.URI.equals() or with implicit normalisation in URI
;; constructors or accessors, which I can't track down just now.
(define first-sight-of-namespace
  (let ((seen-namespaces '()))
    (define (memqx o l =?)           ;like memq, but with explicit predicate
      (cond ((null? l)
             #f)
            ((=? o (car l)))
            (else
             (memqx o (cdr l) =?))))
    (lambda/contract ((uri uri?) -> boolean?)
      (let ((ns (uri->namespace uri)))
        (define-generic-java-method equals)
        (cond ((memqx ns seen-namespaces equals)
               #f)
              (else
               (set! seen-namespaces (cons ns seen-namespaces))
               #t))))))

;; (self-test
;;  (define (mk-uri s)
;;    (java-new <uri> (->jstring s)))
;;  (define u1 (mk-uri "http://example.org/a/b#f1"))
;;  (expect simple1 #t (first-sight-of-namespace u1))
;;  (expect simple2 #f (first-sight-of-namespace u1))
;;  ;; try with a URI differing only in fragment -- same namespace
;;  (expect newfrag
;;          #f
;;          (first-sight-of-namespace (mk-uri "http://example.org/a/b#f2"))))

;; utype-model : -> model
;; The model which holds information about UTypes
(define utype-model
  (let ((model #f))
    (define (create-inferencing-model)
      (define-java-classes
        (<factory> |com.hp.hpl.jena.rdf.model.ModelFactory|))
      (define-generic-java-methods create-inf-model)
      (create-inf-model (java-null <factory>)
                        (rdf:get-reasoner "transitive")
                        (rdf:new-empty-model)))
    (lambda ()
      (if (not model)
          (set! model (create-inferencing-model)))
      model)))

;; ingest-utype-declaration-from-uri! : uri -> unspecified
;; Given a URI, ingest it as RDF.  Either succeeds or throws an error,
;; of the type expected by MAKE-FC
(define/contract (ingest-utype-declaration-from-uri! (uri uri?))
  (define-generic-java-methods add)
  (add (utype-model) (rdf:ingest-from-uri uri)))

;; ingest-utype-declaration-from-stream! : stream -> unspecified
;; Read N3 from a stream (this should be used much less often than the above)
(define/contract (ingest-utype-declaration-from-stream! (s java-stream?))
  (define-generic-java-methods add)
  (add (utype-model) (rdf:ingest-from-stream/language s "" "N3")))

;; query-utype-superclasses : string -> list-of-strings or false
;; Given a string representing a subject, find all the classes of
;; which it is a subclass, returning them as string if there are some,
;; or #f if there are none.
(define/contract (query-utype-superclasses (utype string?)
                                           -> (lambda (res)
                                                (or (not res)
                                                    (and (list? res)
                                                         (not (null? res))
                                                         (string? (car res))))))
  (define-generic-java-methods resource? to-string)
  (let ((results
         (remove (lambda (s) (string=? utype s))
                 (map (lambda (node)
                        (or (resource? node)
                            (report-exception 'query-utype-superclasses
                                              |SC_INTERNAL_SERVER_ERROR|
                                              "Class ~a has 'superclass' ~a, which is not a resource!"
                                              utype (to-string node)))
                        (->string (to-string node)))
                      (rdf:select-statements
                       (utype-model)
                       utype
                       "http://www.w3.org/2000/01/rdf-schema#subClassOf"
                       #f)))))
    (if (null? results)
        #f
        results)))
;;   (let ((results (rdf:select-statements
;;                   (utype-model)
;;                   utype
;;                   "http://www.w3.org/2000/01/rdf-schema#subClassOf"
;;                   #f)))
;;     (define-generic-java-methods resource? to-string)
;;     (if (null? results)
;;         #f
;;         (remove (lambda (s) (string=? utype))
;;                 (map (lambda (node)
;;                        (if (resource? node)
;;                            (->string (to-string node)) ;normal case
;;                            (report-exception 'query-utype-superclasses
;;                                              |SC_INTERNAL_SERVER_ERROR|
;;                                              "Class ~a has 'superclass' ~a, which is not a resource!"
;;                                              utype (to-string node))))
;;                      results))))

)
