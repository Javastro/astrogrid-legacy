;; Functions to handle XML-RPC
;; See spec at <http://www.xmlrpc.com/spec>

(require-library 'sisc/libs/srfi/srfi-6)  ;basic string ports
(require-library 'sisc/libs/srfi/srfi-13) ;string libraries

(module xmlrpc
    (xmlrpc:new-call
     xmlrpc:method-name
     xmlrpc:method-param
     xmlrpc:method-param-list
     xmlrpc:number-of-params
     xmlrpc:call?
     xmlrpc:create-response
     xmlrpc:create-fault)

  (import* srfi-13 string-downcase)
  (import srfi-6)

  (include "xmlrpc-implementation.scm")

  )
