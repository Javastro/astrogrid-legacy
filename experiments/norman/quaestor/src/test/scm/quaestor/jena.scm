;; test cases for the jena package

(require-library 'quaestor/jena)
(import jena)

(require-library 'quaestor/utils)
(import* utils iterator->list)

(require-library 'util/misc)
(import* misc sort-list)


(expect mime-type-1
        '("N3"
          "N3"
          "RDF/XML"
          "N3"
          "N3"
          "N-TRIPLE"
          #f)
        (map rdf:mime-type->language
             '("application/n3"
               "text/rdf+n3"
               "application/rdf+xml"
               "*/*"
               #f
               "text/plain"
               "wibble")))

;; Remove this -- rdf:language-ok? is not currently exported from jena.scm
;; (expect mime-type-2
;;         '(#t #t #t #f #f)
;;         (map (lambda (l) (not (not (rdf:language-ok? l))))
;;              '("RDF/XML"
;;                "N3"
;;                "N-TRIPLE"
;;                "OWL"                    ;for example -- shouldn't be recognised
;;                #f)))

(expect mime-type-3
        '("application/rdf+xml"
          "text/rdf+n3"
          "text/plain"
          #f)
        (map rdf:language->mime-type
             '("RDF/XML"
               "N3"
               "N-TRIPLE"
               "wibble")))

;; Given a string containing N3, return the Jena model corresponding to it
(define n3->model rdf:ingest-from-string/n3)

(define (print-model-statements model)
  (let ((pu (java-null (java-class '|com.hp.hpl.jena.util.PrintUtil|))))
    (define-generic-java-methods
      list-statements
      print)
    (sort-list
     (map (lambda (stmt)
            (->string (print pu stmt)))
          (iterator->list (list-statements model)))
     string<=?)))

(expect ingest
        '("(urn:example#MyClass rdf:type rdfs:Class)"
          "(urn:example#MyClass rdfs:subClassOf urn:example#MySuperClass)"
          "(urn:example#norman http://purl.org/dc/elements/1.1/name 'Norman')")
        (print-model-statements
         (n3->model
          "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. <urn:example#MyClass> a rdfs:Class; rdfs:subClassOf <urn:example#MySuperClass>. <urn:example#norman> <http://purl.org/dc/elements/1.1/name> \"Norman\".")))

(expect-failure ingest-error
                (n3->model "prefix : <urn:example>.")) ;missing '@' -- invalid

(let ((models (map n3->model
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
;;         (let ((models (map n3->model
;;                            '("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. <urn:example#MyClass> a rdfs:Class; rdfs:subClassOf <urn:example#MySuperClass>."
;;                              "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. <urn:example#MySuperClass> a rdfs:Class; rdfs:subClassOf <urn:example#MySuperDuperClass>."
;;                              "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. <urn:example#MySuperDuperClass> a rdfs:Class."
;;                              ))))
;;           (print-model-statements (rdf:merge-models models))))

(let ((test-model
       (n3->model "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. <http://example.org#MyClass> a rdfs:Class; rdfs:subClassOf <http://example.org#MySuperClass>. <http://example.org#norman> <http://purl.org/dc/elements/1.1/name> \"Norman\".")))
  (define-generic-java-methods
    create-resource create-property create-typed-literal to-string)
  (define (node->string n)
    (->string (to-string n)))
  (let ((res  (create-resource test-model
                               (->jstring "http://example.org#norman")))
        (class-resource
         (create-resource
          test-model
          (->jstring "http://www.w3.org/2000/01/rdf-schema#Class")))
        (name
         (create-property test-model
                          (->jstring "http://purl.org/dc/elements/1.1/name")))
        (norman (create-typed-literal test-model
                                      (->jstring "Norman"))))

    (expect-failure select-failure      ;error: no #f slot
                    (rdf:select-statements test-model res name norman))

    (expect select-subjects
            '("http://example.org#norman")
            (map node->string
                 (rdf:select-statements test-model #f name norman)))
    (expect select-subjects-literal     ;predicate is string, object is jstring
            '("http://example.org#norman")
            (map node->string
                 (rdf:select-statements test-model
                                        #f
                                        "http://purl.org/dc/elements/1.1/name"
                                        (->jstring "Norman"))))
    (expect select-subjects-literal2    ;the same, but predicate is jstring
            '("http://example.org#norman")
            (map node->string
                 (rdf:select-statements test-model
                                        #f
                                        (->jstring "http://purl.org/dc/elements/1.1/name")
                                        (->jstring "Norman"))))
    (expect select-subjects-literal-string ;object is scheme string
            '("http://example.org#norman")
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
                                        "http://example.org#norman"
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
                                        "http://example.org#norman"
                                        "http://purl.org/dc/elements/1.1/name"
                                        #f)))

    (expect select-objects-none         ;no matches
            '()
            (rdf:select-statements test-model
                                   "http://example.org#norman"
                                   "http://example.org/wibble"
                                   #f))

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
