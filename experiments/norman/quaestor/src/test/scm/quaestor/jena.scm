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
(define (n3->model n3)
  (define-java-class <java.io.string-reader>)
  (rdf:ingest-from-stream (java-new <java.io.string-reader> (->jstring n3))
                          "text/rdf+n3"))

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

(expect merge
        '("(urn:example#MyClass rdf:type rdfs:Class)"
          "(urn:example#MyClass rdfs:subClassOf urn:example#MySuperClass)"
          "(urn:example#MySuperClass rdf:type rdfs:Class)"
          "(urn:example#MySuperClass rdfs:subClassOf urn:example#MySuperDuperClass)"
          "(urn:example#MySuperDuperClass rdf:type rdfs:Class)")
        (let ((models (map n3->model
                           '("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. <urn:example#MyClass> a rdfs:Class; rdfs:subClassOf <urn:example#MySuperClass>."
                             "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. <urn:example#MySuperClass> a rdfs:Class; rdfs:subClassOf <urn:example#MySuperDuperClass>."
                             "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. <urn:example#MySuperDuperClass> a rdfs:Class."
                             ))))
          (print-model-statements (rdf:merge-models models))))

