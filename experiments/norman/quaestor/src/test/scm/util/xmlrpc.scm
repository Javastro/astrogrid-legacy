;; test cases for the xmlrpc module

(import s2j)

(require-library 'util/xmlrpc)
(import xmlrpc)

(define (string-reader str)
  (define-java-class <java.io.string-reader>)
  (java-new <java.io.string-reader> (->jstring str)))

(expect xmlrpc-simple
        '(examples.getStateName
          "string")
        ((xmlrpc:new-call
         (string-reader
          "<methodCall><methodName>examples.getStateName</methodName>   <params>      <param>         <value>string</value>         </param>      </params>   </methodCall>"))
         'get)
)
