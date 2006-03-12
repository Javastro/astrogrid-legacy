;; Functions to handle XML-RPC
;; See spec at <http://www.xmlrpc.com/>

(require-library 'util/sisc-xml)
(require-library 'sisc/libs/srfi/srfi-13) ;string libraries

(module xmlrpc
(xmlrpc:new-call)

(import sisc-xml)
(import* srfi-13 string-downcase)

(define (xmlrpc:new-call reader)
  (let ((call-sexp (sisc-xml:xml->sexp/reader reader)))
    (format #t "call-sexp=~s~%" call-sexp)
    (or (and call-sexp
             (eq? (car call-sexp) '*TOP*))
        (error 'xmlrpc:new-call "Malformed request: ~s" call-sexp))
    (let ((call (evaluate-call-sexp (cadr call-sexp))))
      (lambda (op . args)
        (cond ((eq? op 'get)
               call)
              (else
               (error 'xmlrpc:new-call "Unrecognised operation ~s" op)))))))

(define (evaluate-call-sexp sexp)
  (format #t "Evaluating: ~s~%" sexp)
  ((eval `(lambda (methodCall
                   methodName
                   params
                   param
                   value)
            ,sexp)
         (scheme-report-environment 5)
         )
   do-method-call
   do-method-name
   do-params
   do-param
   do-value))

;; <?xml version="1.0"?>
;; <methodCall>
;;    <methodName>examples.getStateName</methodName>
;;    <params>
;;       <param>
;;          <value><i4>41</i4></value>
;;          </param>
;;       </params>
;;    </methodCall>
(define (do-method-call name params)
  (format #t "method-call: name=~s  params=~s~%" name params)
  (cons (string->symbol (string-downcase name))
        params))
(define (do-method-name content)
  content)
(define (do-params . content)
  content)
(define (do-param value)
  value)
(define (do-value content)
  content)

)
