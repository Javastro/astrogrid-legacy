;; test cases for the jena package

(require-library 'quaestor/jena)
(import jena)

(require-library 'quaestor/utils)
(import* utils jobject->list)

(require-library 'util/misc)
(import* misc sort-list)

(import s2j)
(define-java-classes (<uri> |java.net.URI|))


(let ((maps '(("application/n3"                      . "TURTLE")      
              ("text/rdf+n3"                         . "TURTLE")      
              ("text/rdf+n3; charset=utf-8"          . "TURTLE")      
              ("application/x-turtle"                . "TURTLE")
              ("text/turtle"                         . "TURTLE")
              ("application/rdf+xml"                 . "RDF/XML") 
              ("application/rdf+xml; charset=wibble" . "RDF/XML") 
              ("*/*"                                 . "TURTLE")      
              (#f                                    . "TURTLE")
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
        (class-resource
         (create-resource
          test-model
          (->jstring "http://www.w3.org/2000/01/rdf-schema#Class")))
        (name
         (create-property test-model
                          (->jstring "http://purl.org/dc/elements/1.1/name")))
        (norman (create-typed-literal test-model
                                      (->jstring "Norman")))
        (norman-uri (java-new <uri> norman-jstring)))

    (expect-failure select-failure      ;error: no #f slot
                    (rdf:select-statements test-model res name norman))

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
              (append
               (list "http://example.org#MyClass http://www.w3.org/2000/01/rdf-schema#subClassOf http://example.org#MySuperClass"
                     "http://example.org#MyClass http://www.w3.org/1999/02/22-rdf-syntax-ns#type http://www.w3.org/2000/01/rdf-schema#Class")
               one-stmt)
              (map format-statement
                   (rdf:select-statements test-model #f #f #f))))


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
