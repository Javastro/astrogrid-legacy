;; sisc script

(import s2j)

(define (main . args)
  ;; (define-generic-java-method get-registry-query)
;;   (display (get-registry-query (get-acr-service (->jstring "ivoa.siap"))))
  (for-each (lambda (q)
              ;(format #t "query <~a> --> ~%" q)
              (display-dom (make-registry-adql-query q)))
            (cdr args)))

;; make-registry-adql-query string -> DOM
;; Given a string containing an ADQL query, make that query of a
;; registry, and return a DOM showing the results
(define (make-registry-adql-query query-string)
  (define-generic-java-method adql-search)
  (adql-search (get-acr-service
                (java-class '|org.astrogrid.acr.astrogrid.Registry|))
               (->jstring query-string)))

;; make-registry-keyword-query string -> DOM
(define (make-registry-keyword-query keyword)
  (define-generic-java-method keyword-search)
  (keyword-search (get-acr-service
                   (java-class '|org.astrogrid.acr.astrogrid.Registry|))
                  (->jstring keyword)
                  (->jboolean #t)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; ACR utility functions

;; acr: <nothing> -> ACR
(define (acr)
  (define-java-classes
    <org.astrogrid.acr.finder>
    ;; (<ACR> |org.astrogrid.acr.builtin.ACR|)
    )
  (define-generic-java-method find)
  (find (java-new <org.astrogrid.acr.finder>)))

;; get-acr-service: jstring -> jobject
;; get-acr-service: jclass  -> jobject
;; Given a service specification (which can be a jstring or a jclass), return
;; an object implementing that.
(define (get-acr-service service)
  (define-generic-java-method get-service)
  (get-service (acr) service))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Useful functions

;; display-dom DOM -> <undefined>
;; Given a DOM, dump a serialisation of it on stdout.
(define (display-dom dom)
  (define-java-classes
    (<dom-source> |javax.xml.transform.dom.DOMSource|)
    (<stream-result> |javax.xml.transform.stream.StreamResult|)
    <javax.xml.transform.transformer>
    <javax.xml.transform.transformer-factory>)
  (define-generic-java-methods
    new-instance
    new-transformer
    transform)
  (transform (new-transformer
              (new-instance
               (java-null <javax.xml.transform.transformer-factory>)))
             (java-new <dom-source> dom)
             (java-new <stream-result> (system-out))))

;; Return System.out
(define (system-out)
  ((generic-java-field-accessor '|out|)
   (java-null (java-class '|java.lang.System|))))
