;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Support for the `knowledgebase' for Quaestor
;;
;; Procedures defined here:
;;
;; kb:new            : create a new knowledgebase
;; kb:get            : retrieve a knowledgebase by name
;; kb:discard        : forget about a knowledgebase
;; kb:get-names      : returns the list of available knowledgebases
;; kb:knowledgebase? : tests whether something is a knowledgebase
;;
;; See below for more detailed documentation.
;;
;; A knowledgebase is a procedure which takes a subcommand argument, and further arguments.
;; These subcommands manipulate the knowledgebase.  See the documentation for (internal) procedure
;; MAKE-KB below.


(import s2j)

(require-library 'quaestor/jena)
(require-library 'quaestor/utils)
(import* utils
         jobject->list)
(require-library 'util/lambda-contract)

(require-library 'org/eurovotech/quaestor/scheme-wrapper-support)

(require-library 'sisc/libs/srfi/srfi-1)
(import* srfi-1
         filter
         remove!)

(import record)                         ;for define-struct
(import hashtable)

;; brain-dead assert macro
(define-syntax assert
  (syntax-rules ()
    ((_ expr)
     (if (not expr)
         (error "assertion failed: ~s" (quote expr))))))

;; (EVAL-WITH args -> (arg1 arg2 ...) body...)
;; Evaluate BODY with the elements of list ARGS assigned to argN.
;; Arguments (arg1 ...) can include the syntax of lambda/contract, eg ((arg test?) -> result?)
;; Use with: (put 'eval-with 'scheme-indent-function 3) to get good Emacs indentation
(define-syntax eval-with
  (syntax-rules (->)
    ((_ args -> (argn ...) form . forms)
     (if (let loop ((n (length args))
                    (l '(argn ...)))
           (cond ((= n 0) (or (= (length l) 0)
                              (eq? (car l) '->)))
                 (else (loop (- n 1) (cdr l)))))
         (apply (lambda/contract (argn ...) form . forms) args)
         (error 'knowledgebase
                "bad call to knowledgebase with args ~s (expecting ~s)"
                args '(argn ...))))
    ((_ args -> () form . forms)
     (if (= (length args) 0)
         ((lambda () form . forms))
         (error 'knowledgebase
                "bad call to knowledgebase with args ~s (expecting no args)"
                args)))))

(define (violated-contract fmt . args)
  (error (apply format (string-append "error calling knowledgebase function: " fmt) args)))



(module knowledgebase
  (kb:new
   kb:get
   kb:discard
   kb:get-names
   kb:knowledgebase?)

  (import jena)
  (import* utils
           is-java-type?
           jobject->list)
  (import* quaestor-support chatter)

  (define (jena-model? x)
    (is-java-type? x '|com.hp.hpl.jena.rdf.model.Model|))
  (define (jena-model-or-false? x)
    (or (not x)
        (is-java-type? x '|com.hp.hpl.jena.rdf.model.Model|)))
  (define (alist? x)
    (and (list? x)
         (let loop ((l x))
           (if (null? l)
               #t
               (and (pair? (car l))
                    (loop (cdr l)))))))
  (define (jstring? x)
    (is-java-type? x '|java.lang.String|))
  (define (uri? x)
    (is-java-type? x '|java.net.URI|))

  ;; A `knowledgebase' is a named model, consisting of a number of named
  ;; `submodels'.  When any of these are updated, the new submodel is
  ;; merged with the main model.
  ;;
  ;; The knowledgebase object that is returned is a function which takes
  ;; a number of subcommands.

  ;; Manage the set of known knowledgebases with the KNOWLEDGEBASE-LIST function,
  ;; which is a wrapper round java.util.HashMap.  (Hmm: wouldn't this more sensibly
  ;; be a SISC hashtable?)
  (define knowledgebase-list
    (let ((knowledgebase-list (java-new (java-class '|java.util.HashMap|))))

      (lambda (cmd . args)

        (define-generic-java-methods get put remove key-set iterator)

        (case cmd
          ((get)                        ; uri -> kb-or-false
           (eval-with args -> (uri)
             (let ((kb (get knowledgebase-list uri)))
               (and (not (java-null? kb))
                                  (java-unwrap kb)))))
          ((put)                        ; uri kb -> kb
           (eval-with args -> (uri kb)
             (java-synchronized knowledgebase-list
               (lambda ()
                 (put knowledgebase-list uri (java-wrap kb))
                 kb))))
          ((remove)                     ; uri -> kb
           (eval-with args -> (uri)
             (java-synchronized knowledgebase-list
               (lambda ()
                 (let ((prev-kb (remove knowledgebase-list uri)))
                   (and (not (java-null? prev-kb))
                        (java-unwrap prev-kb)))))))
          ((get-keys)                   ; -> list-of-uris
           (eval-with args -> ()
             (jobject->list (iterator (key-set knowledgebase-list)))))
          (else
           (error 'knowledgebase-list "Bad call: (~s ~s)" cmd args))))))

  ;; KB:GET : Java-URI -> knowledgebase or false
  ;; Retrieve the knowledgebase with the given name, which is
  ;; a Java URI.  Return #f if there is no KB with this name.
  (define/contract (kb:get (kb-name uri?)
                           -> (lambda (x)
                                (or (not x)
                                    (kb:knowledgebase? x))))
    (knowledgebase-list 'get kb-name))

  ;; KB:NEW : uri jena-model -> knowledgebase
  ;;
  ;; Create a new knowledgebase from scratch, registering it with the given
  ;; KB-NAME (a Java URI).  It must not already exist.
  ;; Return the new knowledgebase.  Either succeeds or throws an error.
  (define/contract (kb:new (kb-name uri?) (kb-metadata jena-model?) -> kb:knowledgebase?)
    (if (kb:get kb-name)
        (error 'kb:new
               "bad call to kb:new: knowledgebase ~a already exists" kb-name))
    (knowledgebase-list 'put kb-name (make-kb kb-name kb-metadata)))

  ;; KB:GET-NAMES : -> list-of-uris
  ;;
  ;; Returns a list of available knowledgebases.  Returns the names
  ;; as a list of URIs, or a null list if there are none.
  (define (kb:get-names)
    (knowledgebase-list 'get-keys))

  ;; KB:DISCARD : string -> knowledgebase/#f
  ;;
  ;; Remove a given knowledgebase from the list.  Returns it, or
  ;; returns #f if no such knowledgebase existed
  (define/contract (kb:discard (kb-name uri?)
                               -> (lambda (x)
                                    (or (not x)
                                        (kb:knowledgebase? x))))
    (knowledgebase-list 'remove kb-name))

  ;; Define a structure for abstracting submodels
  (define-struct submodel (name         ;string
                           tbox?        ;boolean
                           model        ;jena model
                           metadata))   ;jena model or #f

  ;; MAKE-KB : uri model -> knowledgebase
  ;;
  ;; Create a new knowledgebase.  The knowledgebase is a procedure.
  ;; The model and submodels may be retrieved, and new ones added, by
  ;; calling the procedure with a command as the second argument.
  ;;
  ;;    (kb 'add-abox!/tbox SUBMODEL-NAME SUBMODEL)
  ;;        Add or update a abox/tbox with the given name.
  ;;        SUBMODEL-NAME may be a string or a symbol.  Return #t on success
  ;;    (kb 'get-query-model)
  ;;        As with GET-MODEL, except that it is an inferencing model.
  ;;        Return #f on error.
  ;;    (kb 'get-model)
  ;;        Return the model, or #f if none exists.
  ;;    (kb 'get-model SUBMODEL-NAME)
  ;;        Return the named submodel, or #f if none exists.
  ;;        SUBMODEL-NAME may be a symbol or a string.
  ;;    (kb 'append-to-submodel! SUBMODEL-NAME MODEL)
  ;;        Append the given MODEL to the named SUBMODEL.
  ;;        SUBMODEL-NAME may be a symbol or a string.
  ;;    (kb 'remove-from-submodel! SUBMODEL-NAME STMT-LIST)
  ;;        Remove the list of statements from the named submodel
  ;;    (kb 'replace-submodel! SUBMODEL-NAME NEW-MODEL)
  ;;        Replace the named submodel with the statements from the NEW-MODEL.
  ;;    (kb 'get-model-abox/tbox)
  ;;        Return the merged abox/tbox submodels, or #f if there are none.
  ;;    (kb 'get-metadata [SUBMODEL-NAME])
  ;;        With an argument, returns the submodel metadata or #f
  ;;        Without an argument, returns the model metadata
  ;;    (kb 'get-name-as-uri)
  ;;        Return the knowledgebase's name, as a Java URI
  ;;    (kb 'get-name-as-resource)
  ;;        As get-name-as-uri, but returning the name as a Jena Resource
  ;;    (kb 'get-name-as-string)
  ;;        As get-name-as-uri, but returning the name as a Scheme string
  ;;    (kb 'has-model [SUBMODEL-NAME])
  ;;        Return true if the named (sub)model exists, that is, if
  ;;        (kb 'get-model [SUBMODEL-NAME]) would succeed.  Return #f otherwise.
  ;;    (kb 'drop-submodel! SUBMODEL-NAME)
  ;;        Forget about the named submodel.  
  ;;        Return the model, or #f if the model didn't exist
  ;;    ;(kb 'set-metadata jstream base-uri content-type)
  ;;    ;    Set the metadata to the arbitrary string INFO.
  ;;    (kb 'info)
  ;;        Return alist with info
  (define (make-kb kb-name metadata)
    (let (;(submodels '()) ;a list of (name tbox? . submodel) pseudo-lists
          (submodels (make-hashtable))
          (merged-model #f)             ;memo
          (merged-tbox #f)              ;memo
          (merged-abox #f)              ;memo
          (query-model #f)              ;memo
          (sync-object (->jstring "SYNC"))) ;a dummy Java object

      (define-syntax kb-synchronized
        (syntax-rules ()
          ((_ form . forms)
           (java-synchronized sync-object
             (lambda ()
               form . forms)))))

      (define-syntax memoized
        ;; (memoized settor memo-var)
        ;; Returns the value of the SETTOR expression, memoizing it in the variable MEMO-VAR
        (syntax-rules ()
          ((_ settor memo-var)
           (begin
             (if (not memo-var)
                 (set! memo-var settor))
             memo-var))))

      (define (clear-memos!)
        (set! merged-model #f)
        (set! merged-abox #f)
        (set! merged-tbox #f)
        (set! query-model #f))

      ;; Return the abox or tbox: BOXNAME is either 'tbox or 'abox.
      ;; Caches result in merged-abox/tbox.
      (define (get-box boxname)
        (kb-synchronized
         (let* ((tbox? (eq? boxname 'tbox))
                (models (filter (lambda (x) x)
                                (hashtable/map (lambda (k el)
                                                 (and (if tbox? (submodel-tbox? el) (not (submodel-tbox? el)))
                                                      (submodel-model el)))
                                               submodels))))
           (cond ((null? models)
                  #f)
                 (tbox?
                  (memoized (if (= (length models) 1)
                                (car models)
                                (rdf:merge-models models))
                            merged-tbox))
                 (else
                  (memoized (if (= (length models) 1)
                                (car models)
                                (rdf:merge-models models))
                            merged-abox))))))

      (define (sdb-description->model model-or-resource)
        (with/fc (lambda (m e)
                   ;; No SDB class found.
                   ;; Rethrow this error with an explanation.
                   (error "Cannot create SDB model -- no SDB support available in the classpath"))
          (lambda ()
            (define-generic-java-methods create connect-default-model read)
            (define-java-classes
              (<store-factory> |com.hp.hpl.jena.sdb.store.StoreFactory|)
              <com.hp.hpl.jena.sdb.store-desc>
              (<sdb-factory> |com.hp.hpl.jena.sdb.SDBFactory|))
            (connect-default-model (java-null <sdb-factory>)
                                   (create (java-null <store-factory>)
                                           (read (java-null <com.hp.hpl.jena.sdb.store-desc>)
                                                 model-or-resource))))))

      ;; Add the given submodel to the model.  BOXNAME is either 'tbox or 'abox.
      ;; If the model is actually a description of an SDB store, then instead
      ;; create the model which connects to that (this path is currently not covered by
      ;; any tests).
      (define (add-box name submodel boxname)
        (let ((store-resources (rdf:select-statements submodel #f "a" "http://jena.hpl.hp.com/2007/sdb#Store")))
          (let ((new-model (cond ((not (null? store-resources))
                                  (sdb-description->model submodel))
                                 (else submodel))))
            (kb-synchronized
             (hashtable/put! submodels
                             name
                             (make-submodel name (eq? boxname 'tbox) new-model #f))
             (clear-memos!)))))

      ;; Find the named submodel in the list of submodels.
      ;; Return the submodel, or #f if it's not present
      (define (get-submodel-from-list name)
        (cond ((hashtable/get submodels name)
               => submodel-model)
              (else
               #f)))

      ;; Return the merger of the submodels, performing and memoizing the merge if necessary
      (define (get-merged-model)
        (kb-synchronized
         (if (= (hashtable/size submodels) 0)
             #f
             (memoized (rdf:merge-models (hashtable/map (lambda (k v) (submodel-model v)) submodels))
                       merged-model))))

      ;; Return the model which is to be queried, using the settings in METADATA.
      ;; Return false on errors
      ;; If there is no TBox, or if the requiredReasoner->level
      ;; property is "none", then return simply the merged model
      (define (get-query-model)
        (let ((tbox (get-box 'tbox))
              (abox (get-box 'abox)))
          (define-java-classes
            (<factory> |com.hp.hpl.jena.rdf.model.ModelFactory|))
          (define-generic-java-methods
            create-inf-model to-string)
          (assert (and metadata kb-name))
          (let ((reasoner (rdf:get-reasoner metadata)))
            (cond ((not reasoner)
                   (error "level <~a> doesn't correspond to any reasoner I know about" level))
                  ((not (or abox tbox))
                   ;; There's nothing in this model at all.
                   ;; So return simply an empty model
                   (rdf:new-empty-model))
                  ((or (eq? reasoner 'none)
                       (not tbox))
                   (get-merged-model))
                  (abox
                   (create-inf-model (java-null <factory>)
                                     reasoner
                                     tbox
                                     abox))
                  (else
                   (create-inf-model (java-null <factory>)
                                     reasoner
                                     tbox))))))

      (lambda (cmd . args)

        (define-syntax unless
          (syntax-rules ()
            ((_ test expr)
             (if (not test)
                 expr))))

        (case cmd

          ;;;;;;;;;;
          ;; Commands which explicitly mutate the knowledgebase
          
          ((add-abox! add-tbox!)
           ;; (kb 'add-abox!/add-tbox! SUBMODEL-NAME SUBMODEL)
           (eval-with args -> ((submodel-name string?) (submodel jena-model?))
             (add-box submodel-name
                      submodel
                      (if (eq? cmd 'add-tbox!)
                          'tbox
                          'abox))
             #t))

          ((drop-submodel!)
           ;; (kb 'drop-submodel! SUBMODEL-NAME) -> submodel-or-false
           ;; Note that this does not close the submodel -- that must be done separately
           (eval-with args -> ((submodel-name string?) -> jena-model-or-false?)
            (unless (string? submodel-name)
                    (error 'make-kb
                           "bad call to drop-submodel: submodel name ~s is not a string"
                           submodel-name))
            (cond ((get-submodel-from-list submodel-name)
                   => (lambda (sm)
                        (kb-synchronized
                         (hashtable/remove! submodels submodel-name)
                         (clear-memos!)
                         sm)))
                  (else
                   #f))))

          ((append-to-submodel!)
           ;; (kb 'append-to-submodel! SUBMODEL-NAME MODEL) -> jena-model-or-false
           ;; Append the given MODEL to the named submodel.
           ;; Return the model, or #f if no such model exists.
           (eval-with args -> ((submodel-name string?) (new-model jena-model?) -> jena-model-or-false?)
             (cond ((get-submodel-from-list submodel-name)
                    => (lambda (submodel)
                         (define-generic-java-methods add)
                         (kb-synchronized
                          (add submodel new-model)
                          (clear-memos!)
                          submodel)))
                   (else                ;no such submodel
                    #f))))

          ((remove-from-submodel!)
           ;; (kb 'remove-from-submodel! SUBMODEL-NAME STMT-LIST)
           ;; Remove the list of statements from the named submodel
           (eval-with args -> ((submodel-name string?)
                               (stmt-list (and (list? stmt-list)
                                               (not (null? stmt-list))
                                               (is-java-type? (car stmt-list) '|com.hp.hpl.jena.rdf.model.Statement|))))
             (define-generic-java-methods remove)
             (define-java-classes <com.hp.hpl.jena.rdf.model.statement>)
             (cond ((get-submodel-from-list submodel-name)
                    => (lambda (sm)
                         (kb-synchronized
                          (remove sm (->jarray stmt-list <com.hp.hpl.jena.rdf.model.statement>))
                          (clear-memos!))))
                   (else #f))))

          ((replace-submodel!)
           ;; (kb 'replace-submodel! SUBMODEL-NAME NEW-MODEL)
           ;; Replace the named submodel with [the statements from] the NEW-MODEL.
           ;; Return the (new) model, or #f if it does not exist
           (eval-with args -> ((submodel-name string?) (new-model jena-model?) -> jena-model-or-false?)
             (cond
                  ((get-submodel-from-list submodel-name)
                    => (lambda (sm)
                         ;; The following is the most natural way to do this,
                         ;; but unfortunately it doesn't work for TDB models: removeAll throws an
                         ;; UnsupportedOperationException: remove not supported for this iterator
                         ;; (in TDB-0.5 at least)
;;                          (define-generic-java-methods add remove-all)
;;                          (kb-synchronized
;;                           (remove-all sm)
;;                           (add sm new-model)
;;                           (clear-memos!)
;;                           sm)
                         ;; ...but the following is barely more roundabout
                         (define-generic-java-methods add remove list-statements)
                         (define-java-classes <com.hp.hpl.jena.rdf.model.statement>)
                         (kb-synchronized
                          (remove sm
                                  (->jarray (jobject->list (list-statements sm))
                                            <com.hp.hpl.jena.rdf.model.statement>))
                          (add sm new-model)
                          (clear-memos!)
                          sm)))
                   (else                ;no such submodel
                    #f))))

          ((set-metadata!)
           ;; (kb 'set-metadata! submodel-name metadata-model)
           (eval-with args -> ((submodel-name string?) (metadata-model jena-model?))
             (let ((submodel (hashtable/get submodels submodel-name)))
               (if submodel
                   (set-submodel-metadata! submodel metadata-model)
                   (error 'set-metadata!
                          (format #f "Can't set metadata on a non-existent submodel ~a" submodel-name))))))

          ;;;;;;;;;;
          ;; Commands which implicitly mutate the knowledgebase, via memoisation

          ((get-model)
           ;; (kb 'get-model [SUBMODEL-NAME])
           ;; Return newly-merged model or #f if no models exist
           (case (length args)
             ((0)
              (get-merged-model))
             ((1)
              (get-submodel-from-list (car args)))
             (else
              (error 'make-kb
                     "Bad call to get: wrong number of args in ~a"
                     args))))

          ((get-query-model)
           ;; return #f on error
           (eval-with args -> ()
            (kb-synchronized
             (memoized (get-query-model)
                       query-model))))

          ((get-model-tbox get-model-abox)
           ;; (kb 'get-model-tbox/abox)
           ;; return merged models or #f if none
           (eval-with args -> ()
            (get-box (if (eq? cmd 'get-model-tbox)
                         'tbox
                         'abox))))

          ;;;;;;;;;;
          ;; Commands which do not mutate the knowledgebase

          ((has-model)
           ;; (kb 'has-model [SUBMODEL-NAME])
           ;; With an argument, returns true if the named submodel exists.
           ;; Without an argument, it returns true if any submodels exist.
           (case (length args)
             ((0)
              (> (hashtable/size submodels) 0))
             ((1)
              (get-submodel-from-list (car args)))
             (else
              (error 'make-kb
                     "Bad call to has-model: wrong number of arguments ~s" args))))

          ((get-metadata)
           ;; (kb 'get-metadata [SUBMODEL-NAME])
           ;; With an argument, returns the submodel metadata or #f
           ;; Without an argument, returns the model metadata
           (case (length args)
             ((0) metadata)
             ((1) (cond ((hashtable/get submodels (car args))
                         => submodel-metadata)
                        (else
                         #f)))
             (else
              (error 'get-metadata
                     "Bad call to get-metadata: wrong number of arguments ~s" args))))

          ((get-name-as-uri)
           (eval-with args -> (-> uri?)
             kb-name))

          ((get-name-as-string)
           (eval-with args -> (-> string?)
             (define-generic-java-methods to-string)
             (->string (to-string kb-name))))

          ((get-name-as-resource)
           (eval-with args -> ()
            (define-generic-java-methods create-resource to-string)
            (define-java-class <com.hp.hpl.jena.rdf.model.resource-factory>)
            (create-resource (java-null <com.hp.hpl.jena.rdf.model.resource-factory>)
                             (to-string kb-name))))

          ((info)
           (eval-with args -> (-> list?)
             (list (cons 'submodels
                         (hashtable/map (lambda (k sm)
                                          `((name . ,(submodel-name sm))
                                            (tbox . ,(submodel-tbox? sm))
                                            (namespaces . ,(get-model-namespaces (submodel-model sm)))))
                                        submodels)))))

          (else
           (error 'make-kb "impossible command for knowledgebase: ~s" cmd))))))

  ;; KB:KNOWLEDGEBASE? : object -> boolean
  ;;
  ;; Returns true if the object is a knowledgebase
  (define (kb:knowledgebase? object)
    (and (procedure? object)
         (with/fc
             (lambda (m e) #f)
           (lambda ()
             (uri? (object 'get-name-as-uri))))))

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;;
  ;; Helpers used above

  ;; Return an alist consisting of (prefix . namespace) mappings for
  ;; the given model.
  (define/contract (get-model-namespaces (model jena-model?)
                                         -> alist?)
    (define-generic-java-methods
      get-ns-prefix-map
      entry-set
      get-key
      get-value)
    (map (lambda (mapentry)
           (cons (->string (get-key mapentry))
                 (->string (get-value mapentry))))
         (jobject->list (entry-set (get-ns-prefix-map model)))))

  )
