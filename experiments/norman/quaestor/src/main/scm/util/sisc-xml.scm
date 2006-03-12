;; Utility module -- SAX->sexp using the Java XML parser

(import s2j)

(module sisc-xml
(xml->sexp/file
 xml->sexp/string)

(define-java-classes
  <org.xml.sax.content-handler>)
(define (null-method name) #f)
(define (new-ssax-accumulator)
  (let ((stack '((*TOP*))))
    (lambda (op . args)                    ;op a symbol, args list of strings
      (cond ((eq? op 'start-element)
             (let ((el (car args))
                   (attlist (get-sax-atts-list (cadr args))))
               (if attlist
                   (set! stack
                         (cons `((@ ,@attlist)
                                 ,(string->symbol el))
                               stack))
                   (set! stack
                         (cons (list (string->symbol el))
                               stack)))))
            ((eq? op 'end-element)
             (set! stack `((,(reverse (car stack)) . ,(cadr stack))
                           . ,(cddr stack))))
            ((eq? op 'characters)
             (set-car! stack
                       (cons (car args) (car stack))))
            ((eq? op 'processing-instruction)
             (set-car! stack
                       `((*PI* ,(car args) ,(cadr args)) . ,(car stack))))
            ((eq? op '*TOP*)
             (reverse (car stack)))
            (else
             (error 'new-ssax-accumulator "Unknown op: ~a" op))))))

                                        ;Given a SAX Attributes object, return
                                        ;the list of attributes as a list of
                                        ;(symbol string) lists.  If there are
                                        ;no attributes, return #f, not '()
(define (get-sax-atts-list sax-atts)
  (define-generic-java-methods
    get-length
    get-local-name
    get-value)
  (let ((natts (->number (get-length sax-atts))))
    (if (= natts 0)
        #f
        (let loop ((al '())
                   (n 0))
          (if (>= n natts)
              al
              (let ((name (->string (get-local-name sax-atts (->jint n))))
                    (value (->string (get-value sax-atts (->jint n)))))
                (loop (cons (list (string->symbol name)
                                  value)
                            al)
                      (+ n 1))))))))

                                        ;Given a java string, return scheme #t
                                        ;if the string matches all-whitespace
(define string-all-whitespace?
  (let ((pattern #f))
    (define-java-class <java.util.regex.pattern>)
    (define-generic-java-methods compile matcher matches)
    (lambda (jstr)
      (or pattern
          (set! pattern (compile (java-null <java.util.regex.pattern>)
                                 (->jstring "^\\s*$"))))
      (->boolean (matches (matcher pattern jstr))))))

                                        ;Make a proxy SAX ContentHandler
                                        ;intereface, using the given
                                        ;ssax-accumulator.  If SKIP-WHITESPACE?
                                        ;is true, then omit any all-whitespace
                                        ;strings.
(define-java-proxy (make-content-handler-proxy ssax-accumulator
                                               skip-whitespace?)
  (<org.xml.sax.content-handler>)
  (define (characters h ch start length)
    (define-java-class <java.lang.string>)
    (let ((js (java-new <java.lang.string> ch start length)))
      (if (and skip-whitespace?
               (string-all-whitespace? js))
          (null-method 'characters)
          (ssax-accumulator 'characters (->string js)))))
  (define (end-document h)
    (null-method 'end-document))
  (define (end-element h namespace-uri local-name q-name)
    (ssax-accumulator 'end-element))
  (define (end-prefix-mapping h prefix)
    (null-method 'end-prefix-mapping))
  (define (ignorable-whitespace h ch start length)
    (if skip-whitespace?
        (null-method 'ignorable-whitespace)
        (ssax-accumulator 'characters 
                          (->string
                           (java-new <java.lang.string> ch start length)))))
  (define (processing-instruction h target data)
    (ssax-accumulator 'processing-instruction (->string target) (->string data))
    (null-method 'processing-instruction))
  (define (set-document-locator h locator)
    (null-method 'set-document-locator))
  (define (skipped-entity h name)
    (null-method 'skipped-entity))
  (define (start-document h)
    (null-method 'start-document))
  (define (start-element h namespace-uri local-name q-name atts)
    (ssax-accumulator 'start-element (->string local-name) atts))
  (define (start-prefix-mapping h prefix uri)
    (null-method 'start-prefix-mapping)))

(define (xml->sexp/file filename)
  (define-java-classes
    <org.xml.sax.input-source>
    <java.io.file-reader>)
  (xml->sexp/input-source (java-new <org.xml.sax.input-source>
                                    (java-new <java.io.file-reader>
                                              (->jstring filename)))))

(define (xml->sexp/string string)
  (define-java-classes
    <org.xml.sax.input-source>
    <java.io.string-reader>)
  (xml->sexp/input-source (java-new <org.xml.sax.input-source>
                                    (java-new <java.io.string-reader>
                                              (->jstring string)))))

(define (xml->sexp/input-source source)
  (define-generic-java-method parse)
  (let* ((sa (new-ssax-accumulator))
         (xml-reader (get-xml-reader-with-proxy
                      (make-content-handler-proxy sa #t))))
    ;; Complicated call to (parse xml-reader source), to unpack and rethrow any
    ;; java.lang.reflect.UndeclaredThrowableException.
    ;; This error unpacking isn't quite right, but it does seem to excavate
    ;; the underlying error message somehow...
    (with/fc (lambda (m e)
               (define-generic-java-methods
                 get-cause
                 get-undeclared-throwable
                 get-message)
               (let ((cause (get-cause (error-message m))))
                 (error 'xml->sexp/input-source "reader error: ~s/~s/~s"
                        cause
                        (get-undeclared-throwable (error-message m))
                        (if (java-null? cause)
                            "null cause"
                            (get-message cause)))))
       (lambda () (parse xml-reader source)))
    (sa '*TOP*)))

(define (get-xml-reader-with-proxy content-handler-proxy)
  (define-java-classes
    (<sax-parser-factory> |javax.xml.parsers.SAXParserFactory|))
  (define-generic-java-methods
    new-instance
    (new-sax-parser |newSAXParser|)
    (get-xml-reader |getXMLReader|)
    set-content-handler
    set-feature)
  (let ((reader (get-xml-reader
                 (new-sax-parser
                  (new-instance (java-null <sax-parser-factory>))))))
    (set-content-handler reader
                         content-handler-proxy)
    ;; the following should be the defaults
    (set-feature reader
                 (->jstring "http://xml.org/sax/features/namespaces")
                 (->jboolean #t))
    (set-feature reader
                 (->jstring "http://xml.org/sax/features/namespace-prefixes")
                 (->jboolean #f))
    reader))

)
