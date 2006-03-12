;; Utility module -- SAX->sexp using the Java XML parser
;;
;; Exports three procedures.
;;
;; sisc-xml:xml->sexp/file FILENAME
;; Given a file name, parse it and return a SSAX-style sexp representing it. 
;;
;; sisc-xml:xml->sexp/string STRING
;; Parse the XML in the given string.
;;
;; sisc-xml:skip-whitespace? BOOLEAN
;; Set the behaviour of the conversion to the sexp.  If the boolean is set
;; true, then all-whitespace content is skipped (so that
;; "<doc> <p>content  </p> </doc>" would be parsed as (doc (p "content  ")),
;; but if it is false, it is all included verbatim, so that the above would
;; parse to (doc " " (p "content  ") " ").  This is a SRFI-39 parameter, 
;; so may be set inside a PARAMETERIZE special form.

(import s2j)

(require-library 'sisc/libs/srfi/srfi-39) ;parameter objects

(module sisc-xml
(sisc-xml:xml->sexp/file
 sisc-xml:xml->sexp/string
 sisc-xml:skip-whitespace?)

(import srfi-39)

(define-java-classes
  <org.xml.sax.content-handler>)

;; Procedure NULL-METHOD does nothing
(define (null-method name) #f)

;; Create a ssax-accumulator.  This returns a function which is carried about
;; by the SAX ContentHandler below.
(define (new-ssax-accumulator)
  (let ((stack '((*TOP*)))
        (currently-parsing? #f))
    (lambda (op . args)                    ;op a symbol, args list of strings
      (cond ((eq? op 'start-element)
             (let ((ns (car args))         ;args: ns-or-false
                   (el (cadr args))        ;element name
                   (attlist (get-sax-atts-list (caddr args)))) ;attlist
               (let ((elx (if ns
                              (cons (string->symbol ns)
                                    (string->symbol el))
                              (string->symbol el))))
                 (if attlist
                     (set! stack
                           (cons `((@ ,@attlist)
                                   ,elx)
                                 stack))
                     (set! stack
                           (cons (list elx)
                                 stack))))))
            ((eq? op 'end-element)
             (set! stack `((,(reverse (car stack)) . ,(cadr stack))
                           . ,(cddr stack))))
            ((eq? op 'characters)
             (set-car! stack
                       (cons (car args) (car stack))))
            ((eq? op 'processing-instruction)
             (set-car! stack
                       `((*PI* ,(car args) ,(cadr args)) . ,(car stack))))
            ((eq? op 'start-document)
             (set! currently-parsing? #t))
            ((eq? op 'end-document)
             (set! currently-parsing? #f))
            ((eq? op '*TOP*)
             (if currently-parsing?
                 (error 'new-ssax-accumulator "Can't retrieve *TOP* mid-parse")
                 (reverse (car stack))))
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
    get-value
    (get-uri |getURI|))
  (let ((natts (->number (get-length sax-atts))))
    (if (= natts 0)
        #f
        (let loop ((al '())
                   (n 0))
          (if (>= n natts)
              (reverse al)
              (let ((nj (->jint n)))
                (let ((ns (->string (get-uri sax-atts nj)))
                      (name (string->symbol
                             (->string (get-local-name sax-atts nj))))
                      (value (->string (get-value sax-atts nj))))
                  (loop (cons (list (if (> (string-length ns) 0)
                                        (cons (string->symbol ns) name)
                                        name)
                                    value)
                              al)
                        (+ n 1)))))))))

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
(define-java-proxy (make-content-handler-proxy ssax-accumulator)
  (<org.xml.sax.content-handler>)
  (define (characters h ch start length)
    (define-java-class <java.lang.string>)
    (let ((js (java-new <java.lang.string> ch start length)))
      (if (and (sisc-xml:skip-whitespace?)
               (string-all-whitespace? js))
          (null-method 'characters)
          (ssax-accumulator 'characters (->string js)))))
  (define (end-document h)
    (ssax-accumulator 'end-document))
  (define (end-element h namespace-uri local-name q-name)
    (ssax-accumulator 'end-element))
  (define (end-prefix-mapping h prefix)
    (null-method 'end-prefix-mapping))
  (define (ignorable-whitespace h ch start length)
    (if (sisc-xml:skip-whitespace?)
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
    (ssax-accumulator 'start-document))
  (define (start-element h namespace-uri local-name q-name atts)
    (let ((ns (->string namespace-uri)))
      (ssax-accumulator 'start-element
                        (and (> (string-length ns) 0)
                             ns)
                        (->string local-name)
                        atts)))
  (define (start-prefix-mapping h prefix uri)
    (null-method 'start-prefix-mapping)))

(define (sisc-xml:xml->sexp/file filename)
  (define-java-classes
    <org.xml.sax.input-source>
    <java.io.file-reader>)
  (xml->sexp/input-source (java-new <org.xml.sax.input-source>
                                    (java-new <java.io.file-reader>
                                              (->jstring filename)))))

(define (sisc-xml:xml->sexp/string string)
  (define-java-classes
    <org.xml.sax.input-source>
    <java.io.string-reader>)
  (xml->sexp/input-source (java-new <org.xml.sax.input-source>
                                    (java-new <java.io.string-reader>
                                              (->jstring string)))))

;; Procedure: XML->SEXP/INPUT-SOURCE
;; Read XML from the given SAX InputSource, returning a SExp.
(define (xml->sexp/input-source input-source)
  (define-generic-java-method parse)
  (let* ((sa (new-ssax-accumulator))
         (xml-reader (get-xml-reader-with-proxy
                      (make-content-handler-proxy sa))))
    ;; Complicated call to (parse xml-reader input-source), to unpack and
    ;; rethrow any errors.  I think there are circumstances where the
    ;; proxy can throw java.lang.reflect.UndeclaredThrowableException, which
    ;; needs special handling, but I can't reproduce this right now, nor
    ;; find the details in the SISC docs.
    (with/fc (lambda (m e)
               (define-generic-java-methods
                 get-message
                 get-system-id
                 get-line-number
                 get-column-number)
               (let ((sax-exception (error-message m)))
                 (let ((sysid   (get-system-id sax-exception))
                       (line-no (->number (get-line-number sax-exception)))
                       (col-no  (->number (get-column-number sax-exception))))
                   (error 'xml->sexp/input-source
                          "parse error:~a:~a:~a: ~a"
                          (if (java-null? sysid)
                              "?"
                              (->string (get-system-id sax-exception)))
                          (if (< line-no 0) "?" line-no)
                          (if (< col-no 0)  "?" col-no)
                          (->string (get-message sax-exception))))))
       (lambda () (parse xml-reader input-source)))
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

;; Parameter procedure: SISC-XML:SKIP-WHITESPACE?
;; This is a SRFI-39 parameter.  It is a procedure which returns
;; a boolean value indicating whether all-whitespace content read within
;; the input XML is skipped (if true) or included verbatim (if #f).  Since
;; it is a parameter, it may be manipulated using the SRFI-39 PARAMETERIZE
;; special form.
(define sisc-xml:skip-whitespace?
  (make-parameter #t))

)
