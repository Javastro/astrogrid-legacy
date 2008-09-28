;; test cases for the knowledgebase module

(require-library 'quaestor/knowledgebase)
(import knowledgebase)
(import s2j)

(require-library 'quaestor/jena)
(import* jena
         rdf:new-empty-model
         rdf:ingest-from-string/turtle)

(require-library 'quaestor/utils)
(import* utils
         is-java-type?
         sort-list)

(define-java-classes
  (<uri> |java.net.URI|)
  (<model> |com.hp.hpl.jena.rdf.model.Model|)
  (<resource-factory> |com.hp.hpl.jena.rdf.model.ResourceFactory|))

(define-generic-java-methods to-string)

(define (jena-model? x)
  (is-java-type? x <model>))

(define try1 (java-new <uri> (->jstring "urn:example/try1")))
(define try2 (java-new <uri> (->jstring "urn:example/try2")))
(define try3 (java-new <uri> (->jstring "urn:example/try3")))
(define try-md (rdf:new-empty-model))

(expect-true kb-create-1
             (kb:new try1 try-md))
(expect-true kb-create-2
             (kb:new try2 try-md))
(expect-failure kb-create-duplicate     ;can't create duplicates
                (kb:new try1 try-md))

(expect kb-predicate
        '(#t #f #f)
        (map kb:knowledgebase?
             `(,(kb:get try1)
               "hello"
               ,(lambda () "hello"))))

(expect kb-names
        (list try2 try1)                ;order isn't significant
        (kb:get-names))

(expect-true kb-get-1
             (kb:get try1))
(expect-true kb-get-2
             (kb:get try2))
(expect kb-get-3
        #f
        (kb:get try3))

(expect kb-name-as-uri
        try1
        ((kb:get try1) 'get-name-as-uri))
(expect kb-name-as-resource
        (to-string try1)
        (to-string ((kb:get try1) 'get-name-as-resource)))

(expect kb-discard
        #t
        (kb:knowledgebase? (kb:discard try2)))
(expect-true kb-reget-1
             (kb:get try1))
(expect kb-reget-2
        #f
        (kb:get try2))

(let ((m (kb:get try1)))
  (expect kb-retrieve-empty
          #f
          (m 'get-model))
  (expect kb-has-empty
          #f
          (m 'has-model))
  (m 'add-abox! "s1" (rdf:new-empty-model))
  (expect-true kb-has-non-empty
               (m 'has-model))
  (expect-true kb-retrieve-non-empty
               ((kb:get try1) 'get-model))

  (expect-true kb-has-model-string
               (m 'has-model "s1"))
  ;; (expect-true kb-has-model-symbol
  ;;              (m 'has-model 's1))
  (expect-true kb-get-model-string
               (m 'get-model "s1"))
  ;; (expect-true kb-get-model-symbol
  ;;              (m 'get-model 's1))
  (expect kb-has-no-submodel
          #f
          (m 'has-model "s2")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Submodel tests
;;
;; Want to check, primarily, that the submodels are distinct, and that
;; assertions added to one don't end up appearing in the other.

(define model-1
  (rdf:ingest-from-string/turtle "
@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>.
@prefix : <urn:example1#>.
:c1 a rdfs:Class.
:c2 a rdfs:Class;
    rdfs:subClassOf :c1.
"))

(define model-2
  (rdf:ingest-from-string/turtle "
@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>.
@prefix : <urn:example2#>.
:c1 a rdfs:Class.
:c2 a rdfs:Class;
    rdfs:subClassOf :c1.
"))

(define (get-model-statements model)
  (define-generic-java-methods list-statements next-statement get-subject get-predicate get-object to-string)
  ;; For two triples ("subject" "predicate" "object"), define an ordering
  (define (statement-triples<=? a b)
    (if (string=? (car a) (car b))
        (if (string=? (cadr a) (cadr b))
            (string<=? (caddr a) (caddr b))
            (string<=? (cadr a) (cadr b)))
        (string<=? (car a) (car b))))
  (sort-list (map (lambda (stmt)
                    (map (lambda (rdfnode) (->string (to-string rdfnode)))
                         (list (get-subject stmt)
                               (get-predicate stmt)
                               (get-object stmt))))
                  (jobject->list (list-statements model)))
             statement-triples<=?))

;; (define (show-model-as-triples model)
;;   (define-java-classes
;;     <java.io.string-writer>)
;;   (define-generic-java-methods
;;     write to-string)
;;   (let ((sw (java-new <java.io.string-writer>)))
;;     (write model sw (->jstring "N-TRIPLE"))
;;     (->string (to-string sw))))

(let ((multimodel (kb:new (java-new <uri> (->jstring "urn:example/multi")) try-md)))

  (expect multimodel-add1
          #t
          (multimodel 'add-tbox! "model1" model-1))
  (expect multimodel-add2
          #t
          (multimodel 'add-tbox! "model2" model-2))

  ;; metadata
  (let ((metadata-model (rdf:ingest-from-string/turtle "@prefix dc: <http://purl.org/dc/elements/1.1/>.
<urn:example/multi> dc:description \"Simple model metadata\"; dc:date \"2008-09-24\".")))
    (multimodel 'set-metadata! "model1" metadata-model))

  (expect model-mix-1
          '(("urn:example1#c1" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example1#c2" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example1#c2" "http://www.w3.org/2000/01/rdf-schema#subClassOf" "urn:example1#c1"))
          (get-model-statements (multimodel 'get-model "model1")))

  (expect model-mix-2
          '(("urn:example2#c1" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example2#c2" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example2#c2" "http://www.w3.org/2000/01/rdf-schema#subClassOf" "urn:example2#c1"))
          (get-model-statements (multimodel 'get-model "model2")))
  (expect model-mix-3                   ;the two above models together
          '(("urn:example1#c1" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example1#c2" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example1#c2" "http://www.w3.org/2000/01/rdf-schema#subClassOf" "urn:example1#c1")
            ("urn:example2#c1" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example2#c2" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example2#c2" "http://www.w3.org/2000/01/rdf-schema#subClassOf" "urn:example2#c1"))
          (get-model-statements (multimodel 'get-model)))
  ;; now re-test the model1 and model2, exactly as before,
  ;; to check that the merge hasn't affected the individual ones
  (expect model-mix-1-bis
          '(("urn:example1#c1" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example1#c2" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example1#c2" "http://www.w3.org/2000/01/rdf-schema#subClassOf" "urn:example1#c1"))
          (get-model-statements (multimodel 'get-model "model1")))
  (expect model-mix-2-bis
          '(("urn:example2#c1" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example2#c2" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example2#c2" "http://www.w3.org/2000/01/rdf-schema#subClassOf" "urn:example2#c1"))
          (get-model-statements (multimodel 'get-model "model2")))

  ;; now append to one of the models, and check that the new triple appears in
  ;; both the submodel and the main merged one
  (multimodel 'append-to-submodel! "model1" 
              (rdf:ingest-from-string/turtle "<urn:example1#c3> a <http://www.w3.org/2000/01/rdf-schema#Class>."))
  (expect model-append-1
          '(("urn:example1#c1" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example1#c2" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example1#c2" "http://www.w3.org/2000/01/rdf-schema#subClassOf" "urn:example1#c1")
            ("urn:example1#c3" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class"))
          (get-model-statements (multimodel 'get-model "model1")))
  ;; ...and the new statement should appear in the merged model, too
  (expect model-append-2
          '(("urn:example1#c1" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example1#c2" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example1#c2" "http://www.w3.org/2000/01/rdf-schema#subClassOf" "urn:example1#c1")
            ("urn:example1#c3" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example2#c1" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example2#c2" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example2#c2" "http://www.w3.org/2000/01/rdf-schema#subClassOf" "urn:example2#c1"))
          (get-model-statements (multimodel 'get-model)))

  ;; Now remove some statements from model1
  (let ((deletions (rdf:ingest-from-string/turtle "@prefix e: <urn:example1#>. @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. e:c1 a rdfs:Class. e:c2 rdfs:subClassOf e:c1.")))
    (multimodel 'remove-from-submodel! "model1" (rdf:select-statements deletions #f #f #f))
  (expect model-remove-1
          '(("urn:example1#c2" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example1#c3" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class"))
          (get-model-statements (multimodel 'get-model "model1")))
  ;; ...and the new statement should appear in the merged model, too
  (expect model-remove-2
          '(("urn:example1#c2" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example1#c3" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example2#c1" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example2#c2" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example2#c2" "http://www.w3.org/2000/01/rdf-schema#subClassOf" "urn:example2#c1"))
          (get-model-statements (multimodel 'get-model))))

  ;; Now replace the contents of model1
  (expect model-replace-missing
          #f
          (multimodel 'replace-submodel!
                      "INVALID"
                      (rdf:ingest-from-string/turtle "<urn:example1#x1> a <http://www.w3.org/2000/01/rdf-schema#Class>.")))
  (expect-true model-replace-1
               (multimodel 'replace-submodel!
                           "model1"
                           (rdf:ingest-from-string/turtle "<urn:example1#x1> a <http://www.w3.org/2000/01/rdf-schema#Class>.")))
  (expect model-replace-2
          '(("urn:example1#x1" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class"))
          (get-model-statements (multimodel 'get-model "model1")))
  ;; ... and check that this appears in the merged model
  (expect model-replace-3
          '(("urn:example1#x1" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example2#c1" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example2#c2" "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" "http://www.w3.org/2000/01/rdf-schema#Class")
            ("urn:example2#c2" "http://www.w3.org/2000/01/rdf-schema#subClassOf" "urn:example2#c1"))
          (get-model-statements (multimodel 'get-model)))

  ;; check that the metadata is retrievable, and was undisturbed by all of that
  (expect model-metadata-1
          '(("urn:example/multi" "http://purl.org/dc/elements/1.1/date" "2008-09-24")
            ("urn:example/multi" "http://purl.org/dc/elements/1.1/description" "Simple model metadata"))
          (get-model-statements (multimodel 'get-metadata "model1")))

  ;; drop models
  (expect drop-no-model
          #f
          (multimodel 'drop-submodel! "bad-model"))
  (expect-true drop-1
               (and (multimodel 'has-model "model1")
                    (multimodel 'has-model "model2")))
  (expect-true drop-model1
               (multimodel 'drop-submodel! "model1"))
  (expect drop-2
          #f
          (multimodel 'has-model "model1"))
  (expect-true drop-3
               (multimodel 'has-model "model2"))
  (expect-true drop-model2
               (multimodel 'drop-submodel! "model2"))
  (expect drop-4
          #f
          (multimodel 'has-model "model2"))
  )

;; reasoning support
(let ((uri1 (java-new <uri> (->jstring "urn:example/simple1")))
      (uri2 (java-new <uri> (->jstring "urn:example/simple2")))
      (uri3 (java-new <uri> (->jstring "urn:example/simple3"))))
  (let (;; metadata with a simpleRDFS reasoner
        (simple-md1 (rdf:ingest-from-string/turtle "
@prefix dc: <http://purl.org/dc/elements/1.1/>.
@prefix quaestor: <http://ns.eurovotech.org/quaestor#>.

<> dc:creator \"Norman\";
  quaestor:requiredReasoner [
    quaestor:level \"simpleRDFS\"
  ].
" uri1))
        ;; metadata with no reasoner
        (simple-md2 (rdf:ingest-from-string/turtle "
@prefix dc: <http://purl.org/dc/elements/1.1/>.
@prefix quaestor: <http://ns.eurovotech.org/quaestor#>.

<> dc:creator \"Norman\";
  quaestor:requiredReasoner [
    quaestor:level \"none\"
  ].
" uri2))
        ;; metadata with invalid reasoner
        (simple-md3 (rdf:ingest-from-string/turtle "
@prefix dc: <http://purl.org/dc/elements/1.1/>.
@prefix quaestor: <http://ns.eurovotech.org/quaestor#>.

<> dc:creator \"Norman\";
  quaestor:requiredReasoner [
    quaestor:level \"invalid\"
  ].
" uri3)))

    (let ((simple-kb (expect-true kb-md-1
                                  (kb:new uri1 simple-md1))))
      (expect-true kb-md1-query
                   (jena-model? (simple-kb 'get-query-model))))
  
;;     (expect-true kb-md-1
;;                  (kb:new uri1 simple-md1))
    (let ((kb (kb:get uri1)))
      (expect-true kb-tbox-1
                   (kb 'add-tbox! "model1" model-1))
      (expect-true kb-query-1
                   (jena-model? (kb 'get-query-model))))

    (expect-true kb-md-2
                 (kb:new uri2 simple-md2))
    (let ((kb (kb:get uri2)))
      (expect-true kb-tbox-2
                   (kb 'add-tbox! "model1" model-1))
      (expect-true kb-query-2
                   (jena-model? (kb 'get-query-model))))

    (expect-true kb-md-3
                 (kb:new uri3 simple-md3))
    (let ((kb (kb:get uri3)))
      (expect-true kb-tbox-3
                   (kb 'add-tbox! "model1" model-1))
      (expect-failure kb-query-3
                      (kb 'get-query-model)))
  
    ;; we could potentially test that the reasoner works, but...
    ))

;; Tests of SDB support.
;; Unfortunately, these tests either are or are not run depending on whether 
;; SDB support is available -- I can't see a straightforward way to have both run.
(if (java-class-present '|com.hp.hpl.jena.sdb.SDBFactory|)
    (format #t "No SDBFactory tests currently defined!~%")
    (let ((sdb-config (rdf:ingest-from-string/turtle
                       "[] <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://jena.hpl.hp.com/2007/sdb#Store>."))
          (kb (kb:new (java-new <uri> (->jstring "urn:example/sdb")) try-md)))
      ;; No SDB library support -- creating a model backed
      ;; by an SDB store should therefore fail neatly.
      (format #t "SDBFactory not found -- SDB tests skipped~%")
      (expect-failure sdb-no-sdb
                      (kb 'add-abox! "model1" sdb-config))))

