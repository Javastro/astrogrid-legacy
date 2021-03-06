;; test cases for the jena package

(require-library 'quaestor/jena)
(import jena)

(require-library 'quaestor/utils)
(import* utils
         jobject->list
         java-class-present
         is-java-type?)

(require-library 'util/misc)
(import* misc sort-list)

(import s2j)

;(import debugging)                      ;for suppressed-stack-trace-source-kinds

(define-java-classes (<uri> |java.net.URI|))

;; The following list does not include tests for */* -- that's the province of
;; any code elsewhere which does 'acceptable-header' parsing, whereas here
;; we are simply concerned to provide the information on 'default mime type'
;; which that processing will rely on.
(let ((maps '(("application/n3"                      . "TURTLE")
              ("text/rdf+n3"                         . "TURTLE")
              ("text/rdf+n3; charset=utf-8"          . "TURTLE")
              ("application/x-turtle"                . "TURTLE")
              ("text/turtle"                         . "TURTLE")
              ("application/rdf+xml"                 . "RDF/XML")
              ("application/rdf+xml; charset=wibble" . "RDF/XML")
              (#f                                    . "RDF/XML")
              ("text/plain"                          . "N-TRIPLE")
              ("wibble"                              . #f))))
  (expect mime-type-1
          (map cdr maps)
          (map rdf:mime-type->language
               (map car maps))))

;; Remove this -- rdf:language-ok? is not currently exported from jena.scm
;; (expect mime-type-2
;;         '(#t #t #t #f #f)
;;         (map (lambda (l) (not (not (rdf:language-ok? l))))
;;              '("RDF/XML"
;;                "TURTLE"
;;                "N-TRIPLE"
;;                "OWL"                    ;for example -- shouldn't be recognised
;;                #f)))

(let ((maps '(("RDF/XML"  . "application/rdf+xml")
              ("TURTLE"       . "text/rdf+n3")
              ("N-TRIPLE" . "text/plain")
              ("wibble"   . #f))))
  (expect mime-type-3
          (map cdr maps)
          (map rdf:language->mime-type
               (map car maps))))

;; Confirm that the 'default' MIME type is RDF/XML.
;; The tests after this confirm the consistency of this with other functions.
(expect mime-type-default
        "RDF/XML"
        (rdf:mime-type->language #f))
(expect mime-type-default-inverse
        ;; check the two operations agree when given argument #f
        (rdf:language->mime-type (rdf:mime-type->language #f))
        (rdf:language->mime-type #f))
(expect mime-type-list-default
        (rdf:language->mime-type #f)
        (car (rdf:mime-type-list)))

;; Given a string containing Turtle, return the Jena model corresponding to it
(define turtle->model rdf:ingest-from-string/turtle)

(define (print-model-statements model)
  (let ((pu (java-null (java-class '|com.hp.hpl.jena.util.PrintUtil|))))
    (define-generic-java-methods
      list-statements
      print)
    (sort-list
     (map (lambda (stmt)
            (->string (print pu stmt)))
          (jobject->list (list-statements model)))
     string<=?)))

(expect ingest
        '("(urn:example#MyClass rdf:type rdfs:Class)"
          "(urn:example#MyClass rdfs:subClassOf urn:example#MySuperClass)"
          "(urn:example#norman http://purl.org/dc/elements/1.1/name 'Norman')")
        (print-model-statements
         (turtle->model
          "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. <urn:example#MyClass> a rdfs:Class; rdfs:subClassOf <urn:example#MySuperClass>. <urn:example#norman> <http://purl.org/dc/elements/1.1/name> \"Norman\".")))

(expect-failure ingest-error
                (turtle->model "prefix : <urn:example>.")) ;missing '@' -- invalid

(let ((models (map turtle->model
                   '("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. <urn:example#MyClass> a rdfs:Class; rdfs:subClassOf <urn:example#MySuperClass>."
                     "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. <urn:example#MySuperClass> a rdfs:Class; rdfs:subClassOf <urn:example#MySuperDuperClass>."
                     "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. <urn:example#MySuperDuperClass> a rdfs:Class."
                     ))))
  (expect merge-three
          '("(urn:example#MyClass rdf:type rdfs:Class)"
            "(urn:example#MyClass rdfs:subClassOf urn:example#MySuperClass)"
            "(urn:example#MySuperClass rdf:type rdfs:Class)"
            "(urn:example#MySuperClass rdfs:subClassOf urn:example#MySuperDuperClass)"
            "(urn:example#MySuperDuperClass rdf:type rdfs:Class)")
          (print-model-statements (rdf:merge-models models)))
  (expect merge-three-1
          '("(urn:example#MyClass rdf:type rdfs:Class)"
            "(urn:example#MyClass rdfs:subClassOf urn:example#MySuperClass)")
          (print-model-statements (car models)))
  (expect merge-three-2
          '("(urn:example#MySuperClass rdf:type rdfs:Class)"
            "(urn:example#MySuperClass rdfs:subClassOf urn:example#MySuperDuperClass)")
          (print-model-statements (cadr models)))
  (expect merge-three-3
          '("(urn:example#MySuperDuperClass rdf:type rdfs:Class)")
          (print-model-statements (caddr models))))
;; (expect merge
;;         '("(urn:example#MyClass rdf:type rdfs:Class)"
;;           "(urn:example#MyClass rdfs:subClassOf urn:example#MySuperClass)"
;;           "(urn:example#MySuperClass rdf:type rdfs:Class)"
;;           "(urn:example#MySuperClass rdfs:subClassOf urn:example#MySuperDuperClass)"
;;           "(urn:example#MySuperDuperClass rdf:type rdfs:Class)")
;;         (let ((models (map turtle->model
;;                            '("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. <urn:example#MyClass> a rdfs:Class; rdfs:subClassOf <urn:example#MySuperClass>."
;;                              "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. <urn:example#MySuperClass> a rdfs:Class; rdfs:subClassOf <urn:example#MySuperDuperClass>."
;;                              "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. <urn:example#MySuperDuperClass> a rdfs:Class."
;;                              ))))
;;           (print-model-statements (rdf:merge-models models))))

(let ((test-model
       (turtle->model "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. <http://example.org#MyClass> a rdfs:Class; rdfs:subClassOf <http://example.org#MySuperClass>. <http://example.org#norman> <http://purl.org/dc/elements/1.1/name> \"Norman\"."))
       (norman-string "http://example.org#norman")
       (norman-jstring (->jstring "http://example.org#norman")))
  (define-generic-java-methods
    create-resource create-property create-typed-literal to-string)
  (define (node->string n)
    (->string (to-string n)))
  (let ((res  (create-resource test-model norman-jstring))
        (class-resource (create-resource test-model (->jstring "http://www.w3.org/2000/01/rdf-schema#Class")))
        (name (create-property test-model (->jstring "http://purl.org/dc/elements/1.1/name")))
        (norman (create-typed-literal test-model (->jstring "Norman")))
        (norman-uri (java-new <uri> norman-jstring)))

    ;; Test-true calls, with no slots wildcarded
    (expect select-ask-1
            #f
            (null? (rdf:select-statements test-model res name norman)))
    (expect select-ask-2
            #t
            (null? (rdf:select-statements test-model
                                          res
                                          name
                                          (create-typed-literal test-model (->jstring "Aloysius")))))

    ;; Calls with a single #f argument
    (expect select-subjects
            (list norman-string) ; '("http://example.org#norman")
            (map node->string
                 (rdf:select-statements test-model #f name norman)))
    (expect select-subjects-literal     ;predicate is string, object is jstring
            (list norman-string) ;'("http://example.org#norman")
            (map node->string
                 (rdf:select-statements test-model
                                        #f
                                        "http://purl.org/dc/elements/1.1/name"
                                        (->jstring "Norman"))))
    (expect select-subjects-literal2    ;the same, but predicate is jstring
            (list norman-string);'("http://example.org#norman")
            (map node->string
                 (rdf:select-statements test-model
                                        #f
                                        (->jstring "http://purl.org/dc/elements/1.1/name")
                                        (->jstring "Norman"))))
    (expect select-subjects-literal-string ;object is scheme string
            (list norman-string) ;'("http://example.org#norman")
            (map node->string
                 (rdf:select-statements test-model
                                        #f
                                        "http://purl.org/dc/elements/1.1/name"
                                        "Norman")))
    (expect select-subjects-resource    ;object is resource
            '("http://example.org#MyClass")
            (map node->string
                 (rdf:select-statements
                  test-model
                  #f
                  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
                  class-resource)))
    (expect select-subjects-resource-string ;object is http: scheme string
            '("http://example.org#MyClass")
            (map node->string
                 (rdf:select-statements
                  test-model
                  #f
                  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
                  "http://www.w3.org/2000/01/rdf-schema#Class")))
    (expect select-subjects-resource-string-a ;same, but predicate is "a"
            '("http://example.org#MyClass")
            (map node->string
                 (rdf:select-statements
                  test-model
                  #f
                  "a"
                  "http://www.w3.org/2000/01/rdf-schema#Class")))
    (expect select-predicates           ;query over predicate
            '("http://purl.org/dc/elements/1.1/name")
            (map node->string
                 (rdf:select-statements test-model res #f norman)))
    (expect select-predicates-literal   ;subject is string and object is jstring
            '("http://purl.org/dc/elements/1.1/name")
            (map node->string
                 (rdf:select-statements test-model
                                        norman-string
                                        #f
                                        (->jstring "Norman"))))
    (expect select-objects              ;subject and predicate are RDFNodes
            '("Norman")
            (map node->string
                 (rdf:select-statements test-model res name #f)))
    (expect select-objects-literal      ;subject and predicate are strings
            '("Norman")
            (map node->string
                 (rdf:select-statements test-model
                                        norman-string
                                        "http://purl.org/dc/elements/1.1/name"
                                        #f)))
    (expect select-objects-literal2 ;as before, but with jstring as subject
            '("Norman")
            (map node->string
                 (rdf:select-statements test-model
                                        norman-jstring
                                        "http://purl.org/dc/elements/1.1/name"
                                        #f)))
    (expect select-objects-literal3     ;as before, but with URI as subject
            '("Norman")
            (map node->string
                 (rdf:select-statements test-model
                                        norman-uri
                                        "http://purl.org/dc/elements/1.1/name"
                                        #f)))
    (expect select-objects-none         ;no matches
            '()
            (rdf:select-statements test-model
                                   norman-string 
                                   "http://example.org/wibble"
                                   #f))

    ;; selecting multiple statements
    (let ((one-stmt (list (format #f "~a ~a ~a" norman-string "http://purl.org/dc/elements/1.1/name" "Norman"))))
      (define (format-statement stmt)
        (define-generic-java-methods get-subject get-predicate get-object)
        (format #f "~a ~a ~a"
                (->string (to-string (get-subject stmt)))
                (->string (to-string (get-predicate stmt)))
                (->string (to-string (get-object stmt)))))
      (expect select-multiple-1
              one-stmt
              (map format-statement
                   (rdf:select-statements test-model #f "http://purl.org/dc/elements/1.1/name" #f)))
      (expect select-multiple-2
              one-stmt
              (map format-statement
                   (rdf:select-statements test-model #f #f "Norman")))
      (expect select-multiple-3
              one-stmt
              (map format-statement
                   (rdf:select-statements test-model norman-string #f #f)))
      (expect select-multiple-4
              (sort-list (append
                          (list "http://example.org#MyClass http://www.w3.org/2000/01/rdf-schema#subClassOf http://example.org#MySuperClass"
                                "http://example.org#MyClass http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.w3.org/2000/01/rdf-schema#Class")
                          one-stmt)
                         string<=?)
              (sort-list (map format-statement
                              (rdf:select-statements test-model #f #f #f))
                         string<=?)))


    ;; now a couple of similar queries using rdf:select-object/string
    (expect select-objects-string       ;object is a literal
            "Norman"
            (rdf:select-object/string test-model
                                      norman-string
                                      "http://purl.org/dc/elements/1.1/name"))
    (expect select-objects-string-resource ;object is a resource
            "http://example.org#MySuperClass"
            (rdf:select-object/string test-model
                                      "http://example.org#MyClass"
                                      "http://www.w3.org/2000/01/rdf-schema#subClassOf"))
    (expect select-objects-string-none  ;no such object
            #f
            (rdf:select-object/string test-model
                                      norman-string
                                      "http://purl.org/dc/elements/1.1/wibble"))
    (expect select-object-string-any    ;wildcard subject
            "Norman"
            (rdf:select-object/string test-model
                                      #f
                                      "http://purl.org/dc/elements/1.1/name"))

    (expect select-properties-property  ;predicate is Property
            '("Norman")
            (map node->string
                 (rdf:get-properties-on-resource res name)))
    (expect select-properties-string    ;predicate is string
            '("Norman")
            (map node->string
                 (rdf:get-properties-on-resource
                  res
                  "http://purl.org/dc/elements/1.1/name")))
    (expect select-properties-none      ;no matches
            '()
            (rdf:get-properties-on-resource res "http://example.org/wibble"))

    (expect select-property             ;object is RDFNode
            "Norman"
            (node->string (rdf:get-property-on-resource res name)))
    (expect select-property-none        ;object is string
            #f
            (rdf:get-property-on-resource res "http://example.org/wibble"))))

;; Tests of rdf:get-reasoner
(define-java-classes (<reasoner> |com.hp.hpl.jena.reasoner.Reasoner|))
(expect reasoner-none
        #f
        (rdf:get-reasoner "none"))
(expect-failure reasoner-bad
        (rdf:get-reasoner "wibble"))
(expect reasoner-owl
        #t
        (is-java-type? (rdf:get-reasoner "defaultOWL") <reasoner>))
(expect reasoner-owl-from-model
        #t
        (is-java-type? (rdf:get-reasoner
                        (turtle->model
                         "@prefix q: <http://ns.eurovotech.org/quaestor#>. [] q:requiredReasoner [ q:level \"defaultOWL\" ]."))
                       <reasoner>))
(expect reasoner-none-from-model
        #f
        (rdf:get-reasoner
         (turtle->model
          "@prefix q: <http://ns.eurovotech.org/quaestor#>. [] q:requiredReasoner [ q:level \"none\" ].")))
(expect reasoner-absent-from-model
        #f
        (rdf:get-reasoner
         (turtle->model
          "@prefix q: <http://ns.eurovotech.org/quaestor#>. [] q:NOTrequiredReasoner \"foo\".")))
(expect-failure reasoner-bad-from-model
                (rdf:get-reasoner
                 (turtle->model
                  "@prefix q: <http://ns.eurovotech.org/quaestor#>. [] q:requiredReasoner [ q:level \"wibble\" ].")))
(expect reasoner-strings
        #t
        (let loop ((s (rdf:get-reasoner 'reasoner-list)))
          ;; check this is a list of strings
          (cond ((null? s) #t)
                ((string? (car s)) (loop (cdr s)))
                (else #f))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Tests of TDB factory.  This is a bit complicated, because we need more
;; set-up and tear-down.  Specifically, we need to make a temporary directory,
;; and create various configuration models.  And we need to skip the whole thing if
;; class com.hp.hpl.jena.tdb.TDBFactory isn't present.

;; DELETE-FILE-OR-DIRECTORY-TREE : <java.io.file> -> void
;; Delete the FILE, whether it is a file or a directory tree. 
(define (delete-file-or-directory-tree file)
  (define-java-classes <java.io.file>)
  (define-generic-java-methods delete list-files directory?)
  (if (->boolean (directory? file))
      (for-each delete-file-or-directory-tree (->list (list-files file))))
  (delete file))

;; MAKE-TEMPORARY-DIRECTORY : -> <java.io.file>
;; Like java.io.File.createTempFile, except creating a directory.
;; There's a potential race condition between deleting one and creating the other
(define (make-temporary-directory)
  (define-java-classes <java.io.file>)
  (define-generic-java-methods delete mkdirs create-temp-file)
  (let ((tempfile (create-temp-file (java-null <java.io.file>) (->jstring "quaestor-test") (->jstring ".workdir"))))
    (delete tempfile)
    (mkdirs tempfile)
    tempfile))

;; If the TDB support is present, then check we can create a persistent model,
;; and read it.

(if (java-class-present '|com.hp.hpl.jena.tdb.TDBFactory|)
    (let* ((persistence-dir (make-temporary-directory))
           (persistence-factory
            (rdf:create-persistent-model-factory
             (turtle->model (format #f "[] <~a> \"~a\"."
                                    (rdf:make-quaestor-resource "persistenceDirectory")
                                    (->string persistence-dir))))))
      (define-generic-java-methods create-resource create-property add-literal get-string close)
      (dynamic-wind
          (lambda () (chatter "Test persistence directory ~s" persistence-dir))
          (lambda ()
            (let* ((p1-model-name "http://example.org/persistent/p1")
                   (p1-model (rdf:ingest-from-string/turtle (format #f "<~a> a <~a>."
                                                                    p1-model-name
                                                                    (rdf:make-quaestor-resource "PersistentModel")))))
              (let ((m (persistence-factory p1-model)))
                (add-literal m
                             (create-resource m (->jstring p1-model-name))
                             (create-property m (->jstring "http://purl.org/dc/elements/1.1/") (->jstring "description"))
                             (->jstring "A persistent model"))
                (close m))
              ;; now open a different instance of the same model,
              ;; which should have this information already in it
              (let ((m2 (persistence-factory p1-model)))
                (add-literal m2
                             (create-resource m2 (->jstring p1-model-name))
                             (create-property m2 (->jstring "http://purl.org/dc/elements/1.1/") (->jstring "description"))
                             (->jstring "Another persistent model"))
                (expect persistent-persistence
                        '("A persistent model" "Another persistent model")
                        (map (lambda (node) (->string (get-string node)))
                             (rdf:select-statements m2
                                                    p1-model-name
                                                    "http://purl.org/dc/elements/1.1/description"
                                                    #f)))
                (close m2))))
          (lambda ()
            (delete-file-or-directory-tree persistence-dir))))
    (format #t "TDBFactory not found -- persistence tests skipped~%"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Talis changesets
(let ((model (turtle->model "
@prefix dc: <http://purl.org/dc/elements/1.1/>.
@prefix : <urn:example#>.
:x dc:title \"Title1\"."))
      (cs-add (turtle->model "
@prefix cs:      <http://purl.org/vocab/changeset/schema#> .
@prefix rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix dc: <http://purl.org/dc/elements/1.1/>.
@prefix : <urn:example#>.

  [] rdf:type cs:ChangeSet ;
      cs:subjectOfChange :x;
      cs:addition
              [ rdf:type rdf:Statement ;
                rdf:subject :x;
                rdf:predicate dc:title ;
                rdf:object \"Title2\" ;
              ].
"))
      (cs-replace (turtle->model "
@prefix cs:      <http://purl.org/vocab/changeset/schema#> .
@prefix rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix dc: <http://purl.org/dc/elements/1.1/>.
@prefix : <urn:example#>.

  [] rdf:type cs:ChangeSet ;
      cs:subjectOfChange :x;
      cs:addition
              [ rdf:type rdf:Statement ;
                rdf:subject :x;
                rdf:predicate dc:title;
                rdf:object \"Title3\" ;
              ] ;
      cs:removal
              [ rdf:type rdf:Statement ;
                rdf:subject :x;
                rdf:predicate dc:title ;
                rdf:object \"Title1\" ;
              ] .
"))
      (cs-remove (turtle->model "
@prefix cs:      <http://purl.org/vocab/changeset/schema#> .
@prefix rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix dc: <http://purl.org/dc/elements/1.1/>.
@prefix : <urn:example#>.

  [] rdf:type cs:ChangeSet ;
      cs:subjectOfChange :x;
      cs:removal
              [ rdf:type rdf:Statement ;
                rdf:subject :x;
                rdf:predicate dc:title ;
                rdf:object \"Title2\" ;
              ].
"))
      (cs-no-subject (turtle->model "
@prefix cs:      <http://purl.org/vocab/changeset/schema#> .
@prefix rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix dc: <http://purl.org/dc/elements/1.1/>.
@prefix : <urn:example#>.

  [] rdf:type cs:ChangeSet ;
      cs:removal
              [ rdf:type rdf:Statement ;
                rdf:subject :x;
                rdf:predicate dc:title ;
                rdf:object \"Title1\" ;
              ].
"))
      (cs-bad-subject (turtle->model "
@prefix cs:      <http://purl.org/vocab/changeset/schema#> .
@prefix rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix dc: <http://purl.org/dc/elements/1.1/>.
@prefix : <urn:example#>.

  [] rdf:type cs:ChangeSet ;
      cs:subjectOfChange :y;
      cs:removal
              [ rdf:type rdf:Statement ;
                rdf:subject :x;
                rdf:predicate dc:title ;
                rdf:object \"Title1\" ;
              ].
")))
  ;; check we start off OK
  (expect changeset-model0
          '("(urn:example#x http://purl.org/dc/elements/1.1/title 'Title1')")
          (print-model-statements model))

  ;; various failure modes:
  ;; no changeset mentions
  (expect-failure changeset-no-changeset
                  (rdf:mutate/changeset! model (turtle->model "<urn:example2#foo> <http://purl.org/dc/elements/1.1/title> \"boo!\".")))
  ;; two changesets
  (expect-failure changeset-two-changesets
                  (rdf:mutate/changeset! model (turtle->model "@prefix cs: <http://purl.org/vocab/changeset/schema#> . <urn:example#1> a cs:ChangeSet. <urn:example#2> a cs:ChangeSet.")))
  ;; must mention subjectOfChange
  (expect-failure changeset-no-subject
                  (rdf:mutate/changeset! model cs-no-subject))
  ;; and each changeset must refer to subjectOfChange
  (expect-failure changeset-bad-subject
                  (rdf:mutate/changeset! model cs-bad-subject))
  ;; each removal must refer to a statement in the model
  (expect-failure changeset-missing-statement
                  (rdf:mutate/changeset! model (turtle->model "
@prefix cs: <http://purl.org/vocab/changeset/schema#>.
@prefix rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix dc: <http://purl.org/dc/elements/1.1/>.
@prefix : <urn:example#>.
  [] a cs:ChangeSet;
     cs:subjectOfChange :x;
     cs:removal [ rdf:type rdf:Statement;
                  rdf:subject :x;
                  rdf:predicate dc:title;
                  rdf:object \"MISSING TITLE\"; # there is no such statement
                ].
")))

  ;; now a sequence of additions and removals
  (let ((model1 (rdf:mutate/changeset! model cs-add)))
    (expect changeset-model1
            '("(urn:example#x http://purl.org/dc/elements/1.1/title 'Title1')"
              "(urn:example#x http://purl.org/dc/elements/1.1/title 'Title2')")
            (print-model-statements model1))
    (let ((model2 (rdf:mutate/changeset! model1 cs-replace)))
      (expect changeset-model2
              '("(urn:example#x http://purl.org/dc/elements/1.1/title 'Title2')"
                "(urn:example#x http://purl.org/dc/elements/1.1/title 'Title3')")
              (print-model-statements model2))
      (let ((model3 (rdf:mutate/changeset! model2 cs-remove)))
        (expect changeset-model3
                '("(urn:example#x http://purl.org/dc/elements/1.1/title 'Title3')")
                (print-model-statements model3))
        ;; model2 is changed (is the same as model3 in fact)
        (expect changeset-model3-2
                '("(urn:example#x http://purl.org/dc/elements/1.1/title 'Title3')")
                (print-model-statements model2)))))
  )


;; Other functions
(let ((dummy-model (turtle->model "<http://nxg.me.uk/norman/> a <urn:example#Person>.")))
  (define-java-classes <java.io.file> <com.hp.hpl.jena.rdf.model.resource>)
  (define-generic-java-methods create-resource create-property create-literal to-string
    get-absolute-file)
  (let ((this-file (get-absolute-file (java-new <java.io.file> (->jstring "jena.scm")))))
    (expect mappings-to-uri
          `("urn:example#foo1"
            ,(string-append "file:" (->string (to-string this-file)))
            "urn:example#foo2"
            "urn:example#property"
            #f
            "urn:example#foo3"
            "urn:example#foo4")
          (map (lambda (u)
                 (and (is-java-type? u <uri>)
                      (->string (to-string u))))
               (map rdf:->uri
                    (list (java-new <uri> (->jstring "urn:example#foo1"))
                          this-file
                          (create-resource dummy-model (->jstring "urn:example#foo2"))
                          (create-property dummy-model
                                           (->jstring "urn:example#")
                                           (->jstring "property"))
                          (create-literal dummy-model
                                          (->jstring "wibble")
                                          (->jboolean #f))
                          (->jstring "urn:example#foo3")
                          "urn:example#foo4"))))))
