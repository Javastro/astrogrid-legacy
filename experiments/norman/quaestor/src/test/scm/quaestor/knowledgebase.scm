;; test cases for the knowledgebase module

(require-library 'quaestor/knowledgebase)
(import knowledgebase)

(require-library 'quaestor/jena)
(import* jena
         rdf:new-empty-model
         rdf:ingest-from-string/n3)


(expect-true kb-create-1
             (kb:new "try1"))
(expect-true kb-create-2
             (kb:new "try2"))
(expect-failure kb-create-duplicate     ;can't create duplicates
                (kb:new "try1"))

(expect kb-predicate
        '(#t #f #f)
        (map kb:knowledgebase?
             `(,(kb:get "try1")
               "hello"
               ,(lambda () "hello"))))

(expect kb-names
        '("try2" "try1")                ;order isn't significant
        (kb:get-names))

(expect-true kb-get-1
             (kb:get "try1"))
(expect-true kb-get-2
             (kb:get "try2"))
(expect kb-get-3
        #f
        (kb:get "try3"))

(expect kb-discard
        #t
        (kb:knowledgebase? (kb:discard "try2")))
(expect-true kb-reget-1
             (kb:get "try1"))
(expect kb-reget-2
        #f
        (kb:get "try2"))

(let ((m (kb:get "try1")))
  (expect kb-retrieve-empty
          #f
          (m 'get-model))
  (expect kb-has-empty
          #f
          (m 'has-model))
  (m 'add-abox "s1" (rdf:new-empty-model))
  (expect-true kb-has-non-empty
               (m 'has-model))
  (expect-true kb-retrieve-non-empty
               ((kb:get "try1") 'get-model))

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
  (rdf:ingest-from-string/n3 "
@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>.
@prefix : <urn:example1#>.
:c1 a rdfs:Class.
:c2 a rdfs:Class;
    rdfs:subClassOf :c1.
"))

(define model-2
  (rdf:ingest-from-string/n3 "
@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>.
@prefix : <urn:example2#>.
:c1 a rdfs:Class.
:c2 a rdfs:Class;
    rdfs:subClassOf :c1.
"))

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

(let ((multimodel (kb:new "multi"))
      (example1-pattern (make-regexp-pattern "(urn:example1)"))
      (example2-pattern (make-regexp-pattern "(urn:example2)")))
  (expect multimodel-add1
          #t
          (multimodel 'add-tbox "model1" model-1))
  (expect multimodel-add2
          #t
          (multimodel 'add-tbox "model2" model-2))

  (let ((example1-string (show-model-as-triples
                          (multimodel 'get-model "model1")))
        (example2-string (show-model-as-triples
                          (multimodel 'get-model "model2"))))
;;     (format #t "### example1: ~a~%" example1-string)
;;     (format #t "### example2: ~a~%" example2-string)

    ;; Expect to find precisely four mentions of "urn:example1" in the example1
    ;; string, and no mentions of "urn:example2"
    (expect model-mix-11
            4
            (count-matches example1-pattern example1-string))
    (expect model-mix-12
            0
            (count-matches example2-pattern example1-string))
    ;; ...and vice versa
    (expect model-mix-21
            4
            (count-matches example2-pattern example2-string))
    (expect model-mix-22
            0
            (count-matches example1-pattern example2-string)))

  ;; test the merged version
  (let ((merged-string (show-model-as-triples
                        (multimodel 'get-model))))
    (expect model-mix-merge1
            4
            (count-matches example1-pattern merged-string))
    (expect model-mix-merge2
            4
            (count-matches example2-pattern merged-string)))

  ;; and retest the individual ones (this is a direct copy of the previous test)
  (let ((example1-string (show-model-as-triples
                          (multimodel 'get-model "model1")))
        (example2-string (show-model-as-triples
                          (multimodel 'get-model "model2"))))

    ;; Expect to find precisely four mentions of "urn:example1" in the example1
    ;; string, and no mentions of "urn:example2"
    (expect model-mix-11-bis
            4
            (count-matches example1-pattern example1-string))
    (expect model-mix-12-bis
            0
            (count-matches example2-pattern example1-string))
    ;; ...and vice versa
    (expect model-mix-21-bis
            4
            (count-matches example2-pattern example2-string))
    (expect model-mix-22-bis
            0
            (count-matches example1-pattern example2-string))))
