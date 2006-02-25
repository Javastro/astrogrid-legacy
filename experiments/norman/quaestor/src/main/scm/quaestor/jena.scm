;; SISC library module for Quaestor
;; Interactions with Jena

(import s2j)

(module jena
 (new-empty-model
  rdf-ingest-from-stream)

 ;; Return a new empty model
 (define (new-empty-model)
   (define-java-class <com.hp.hpl.jena.rdf.model.model-factory>)
   ((generic-java-method '|createDefaultModel|)
    (java-null <com.hp.hpl.jena.rdf.model.model-factory>)))

 ;; Given a URI, this reads in the RDF within it, and returns the
 ;; resulting model.  The format of the input file is determined using
 ;; GET-RDF-LANGUAGE-FROM-EXTENSION.
 (define (rdf-ingest-from-uri uri)
   (define-generic-java-methods
     read
     open-stream)
   (define-java-classes
     <java.io.file-input-stream>
     <java.lang.string>
     (<URL> |java.net.URL|))
   (let ((model (new-empty-model))
         (full-uri (->jstring (normalise-uri uri))))
     (chatter "Ingesting RDF from ~a" (->string full-uri))
     (read model
           (open-stream (java-new <URL> full-uri))
           full-uri
           (or (get-rdf-language-from-extension uri)
               (java-null <java.lang.string>)))
     model))

 ;; Given a Java STREAM, this reads in the RDF within it, and returns
 ;; the resulting model.
 (define (rdf-ingest-from-stream stream)
   (define-generic-java-method
     read)
   (let ((model (new-empty-model)))
     (read model
           stream
           (->jstring ""))))     ;base -- let the serial'n handle this

)
