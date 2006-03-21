;; SISC library module for Quaestor
;; Interactions with Jena

(import s2j)

(require-library 'sisc/libs/srfi/srfi-1)
(import* srfi-1
         reduce)


(module jena
( rdf:new-empty-model
  rdf:ingest-from-stream
  rdf:merge-models
  ;rdf:language-ok?
  rdf:mime-type->language
  rdf:language->mime-type
  rdf:mime-type-list
  rdf:get-reasoner)

 ;; Return a new empty model
 (define (rdf:new-empty-model)
   (define-java-class <com.hp.hpl.jena.rdf.model.model-factory>)
   ((generic-java-method '|createDefaultModel|)
    (java-null <com.hp.hpl.jena.rdf.model.model-factory>)))

 ;; Given a URI, this reads in the RDF within it, and returns the
 ;; resulting model.  The format of the input file is determined using
 ;; GET-RDF-LANGUAGE-FROM-EXTENSION.
 (define (rdf:ingest-from-uri uri)
   (define-generic-java-methods
     read
     open-stream)
   (define-java-classes
     <java.io.file-input-stream>
     <java.lang.string>
     (<URL> |java.net.URL|))
   (let ((model (rdf:new-empty-model))
         (full-uri (->jstring (normalise-uri uri))))
     (chatter "Ingesting RDF from ~a" (->string full-uri))
     (read model
           (open-stream (java-new <URL> full-uri))
           full-uri
           (or (get-rdf-language-from-extension uri)
               (java-null <java.lang.string>)))
     model))

 ;; Given a list of RDF models, merge them into a single one, and return it
 (define (rdf:merge-models models)
   (define (model-union m1 m2)           ; union of two models
     ((generic-java-method 'union) m1 m2))
   (reduce model-union
           (rdf:new-empty-model)
           models))

 ;; Given a language LANG, which may be a Jstring, scheme string or #f,
 ;; return true if it is one of the allowed RDF languages, "RDF/XML[-ABBREV]",
 ;; "N-TRIPLE" and "N3".  Returns #f if it is not a legal language.
;;  (define (rdf:language-ok? lang)
;;    (or (not lang)
;;        (member (as-scheme-string lang)
;;                '("RDF/XML" "RDF/XML-ABBREV" "N-TRIPLE" "N3"))))

 ;; The set of mappings from MIME types to RDF languages.
 ;; Used in both directions.
 (define mime-lang-mappings
   '(;; default type -- leave this first, so rdf:mime-type-list can strip it
     ("*/*"                 . "RDF/XML")
     ("application/n3"      . "N3")
     ;; http://infomesh.net/2002/notation3/#mimetype
     ("text/rdf+n3"         . "N3")
     ;; http://www.w3.org/DesignIssues/Notation3.html
     ("application/rdf+xml" . "RDF/XML")
     ;; http://www.w3.org/TR/rdf-syntax-grammar/#section-MIME-Type
     ;; Generic RDF MIME type: http://www.ietf.org/rfc/rfc3870.txt
     ("text/plain"          . "N-TRIPLE")
     ;; http://www.dajobe.org/2001/06/ntriples/
     ))

 ;; Given a MIME type, return an RDF language, as one of the strings accepted
 ;; by RDF:LANGUAGE-OK?.  If the MIME type isn't recognised, return #f.
 (define (rdf:mime-type->language s)
   (let ((p (assoc s mime-lang-mappings)))
     (and p (cdr p))))

 ;; Map RDF language to MIME type.  This is the inverse of
 ;; RDF:MIME-TYPE->LANGUAGE.  Return #f if LANG is not a legal language.
 (define (rdf:language->mime-type lang)
   (let loop ((l (cdr mime-lang-mappings))) ;strip initial "*/*"
     (cond ((null? l)
            #f)
           ((not (string? lang))        ;non-string languages are not allowed!
            #f)
           ((string=? lang (cdar l))
            (caar l))
           (else
            (loop (cdr l))))))

 ;; Return a list of allowed mime-types.  This does not include "*/*".
 (define (rdf:mime-type-list)
   (map car (cdr mime-lang-mappings)))

 ;; Given a Java STREAM, this reads in the RDF within it, and returns
 ;; the resulting model.  MIME-TYPE may be #f, in which case we use the
 ;; default language for the Model read function.  If it is given, it
 ;; must be one of the mime-types which is handled by RDF:MIME-TYPE->LANGUAGE.
 ;; 
 ;; It is not clear what the read method does if the language parameter
 ;; is not one of these three, but just to be sure, we check the language, and
 ;; return #f if it is not valid.
 (define (rdf:ingest-from-stream stream mime-type)
   (define-generic-java-method
     read)
   (define-java-class <java.lang.string>)
   (let ((language                      ;set to valid language (jstring or null)
          (if mime-type                 ;...or #f
                       (let ((l (rdf:mime-type->language mime-type)))
                         (and l (->jstring l)))
                       (java-null <java.lang.string>))))
     (and language
          (let ((model (rdf:new-empty-model)))
            (read model
                  stream
                  (->jstring "")        ;base -- let the serial'n handle this
                  (or language ))))))

 ;; Return the object S, which should be either a Java or Scheme string,
 ;; as a Scheme string.
 (define (as-scheme-string s)
   (cond ((string? s)
          s)
         ((java-object? s)              ;should be a jstring
          (->string s))
         (else
          #f)))

;; Return a new Reasoner object
(define (rdf:get-reasoner)

  (define (get-dig-reasoner)
    (define-java-classes
      ;;(<registry> |com.hp.hpl.jena.reasoner.ReasonerRegistry|)
      <com.hp.hpl.jena.reasoner.reasoner-registry>
      <com.hp.hpl.jena.rdf.model.model-factory>
      ;;(<factory> |com.hp.hpl.jena.reasoner.dig.DIGReasonerFactory|)
      ;;<com.hp.hpl.jena.rdf.model.resource>
      )
    (define-generic-java-methods
      the-registry
      (create-with-owl-axioms |createWithOWLAxioms|)
      get-factory
      create-resource
      create-default-model
      add-property)

    (let* ((config-model (create-default-model
                          (java-null <com.hp.hpl.jena.rdf.model.model-factory>)))
           (conf (create-resource config-model)))
      (add-property conf
                    (java-retrieve-static-object
                     '|com.hp.hpl.jena.vocabulary.ReasonerVocabulary.EXT_REASONER_URL|)
                    (create-resource config-model
                                     (->jstring (dig-uri))))
      ;(chatter "Connecting to DIG reasoner at ~a" (dig-uri))
      (create-with-owl-axioms
       (get-factory
        (the-registry (java-null <com.hp.hpl.jena.reasoner.reasoner-registry>))
        (java-retrieve-static-object
         '|com.hp.hpl.jena.reasoner.dig.DIGReasonerFactory.URI|))
       conf)))

  (define (get-owl-reasoner)
    (define-java-classes
      (<registry> |com.hp.hpl.jena.reasoner.ReasonerRegistry|))
    (define-generic-java-methods
      (get-owl-reasoner |getOWLReasoner|))
    ;(chatter "Creating OWL reasoner")
    (get-owl-reasoner (java-null <registry>)))

  (define (get-rdfs-reasoner)
    (define-java-class
      <com.hp.hpl.jena.reasoner.reasoner-registry>)
    (define-generic-java-methods
      (get-rdfs-reasoner |getRDFSReasoner|))
    ;(chatter "Creating RDFS reasoner")
    (get-rdfs-reasoner (java-null <com.hp.hpl.jena.reasoner.reasoner-registry>)))

  (get-owl-reasoner))

)
