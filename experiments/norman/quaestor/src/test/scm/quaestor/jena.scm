;; test cases for the jena package

(require-library 'quaestor/jena)
(import jena)

(expect mime-type-1
        '("N3"
          "N3"
          "RDF/XML"
          "RDF/XML"
          "N-TRIPLE"
          #f
          #f)
        (map rdf:mime-type->language
             '("application/n3"
               "text/rdf+n3"
               "application/rdf+xml"
               "*/*"
               "text/plain"
               "wibble"
               #f)))

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
          "application/n3"
          "text/plain"
          #f
          #f)
        (map rdf:language->mime-type
             '("RDF/XML"
               "N3"
               "N-TRIPLE"
               "wibble"
               #f)))
