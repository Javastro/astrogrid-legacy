;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Support for the `knowledgebase' for Quaestor

(import s2j)

(require-library 'quaestor/jena)
(require-library 'quaestor/utils)
(import* utils
         iterator->list)
(require-library 'util/lambda-contract)

(require-library 'sisc/libs/srfi/srfi-1)
(import* srfi-1
         filter
         remove!)

;; brain-dead assert macro
(define-syntax assert
  (syntax-rules ()
    ((_ expr)
     (if (not expr)
         (error "assertion failed: ~s" (quote expr))))))

(module knowledgebase
  (kb:new
   kb:get
   kb:discard
   kb:get-names
   kb:knowledgebase?)

  (import jena)
  (import* utils
           is-java-type?
           collection->list)

  (define (in-quaestor-namespace fragment)
    (let ((ns "http://ns.eurovotech.org/quaestor#"))
      (string-append ns fragment)))

  (define (jena-model? x)
    (is-java-type? x '|com.hp.hpl.jena.rdf.model.Model|))
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

  ;; Manage the set of known knowledgebases with the MODEL-LIST function,
  ;; which is a wrapper round java.util.HashMap
  (define model-list
    (let ((model-list (java-new (java-class '|java.util.HashMap|))))

      (lambda (cmd . args)

        ;; (call-with-args (arg arg ...) body...)
        (define-syntax call-with-args
          (syntax-rules ()
            ((_ (arg1 . moreargs) form . forms)
             (if (= (length args) (length '(arg1 . moreargs)))
                 (apply (lambda (arg1 . moreargs) form . forms) args)
                 (error 'knowledgebase
                        "bad call to knowledgebase with args ~s (expecting ~s)"
                        args (cons (car args) '(arg1 . moreargs)))))
            ((_ () form . forms)
             (if (= (length args) 0)
                 ((lambda () form . forms))
                 (error 'knowledgebase
                        "bad call to knowledgebase with args ~s (expecting no args)"
                        args)))))

        (define-generic-java-methods get put remove key-set iterator)

        (case cmd
          ((get)                        ; uri -> kb-or-false
           (call-with-args (uri)
                           (let ((kb (get model-list uri)))
                             (and (not (java-null? kb))
                                  (java-unwrap kb)))))
          ((put)                        ; uri kb -> kb
           (call-with-args (uri kb)
                           (java-synchronized model-list
                             (lambda ()
                               (put model-list uri (java-wrap kb))
                               kb))))
          ((remove)                     ; uri -> kb
           (call-with-args (uri)
                           (java-synchronized model-list
                             (lambda ()
                               (let ((prev-kb (remove model-list uri)))
                                 (and (not (java-null? prev-kb))
                                      (java-unwrap prev-kb)))))))
          ((get-keys)                   ; -> list-of-uris
           (call-with-args ()
                           (iterator->list (iterator (key-set model-list)))))
          (else
           (error 'model-list "Bad call: (~s ~s)" cmd args))))))

  ;; KB:GET : Java-URI -> knowledgebase or false
  ;; Retrieve the knowledgebase with the given name, which is
  ;; a Java URI.  Return #f if there is no KB with this name.
  (define/contract (kb:get (kb-name uri?)
                           -> (lambda (x)
                                (or (not x)
                                    (kb:knowledgebase? x))))
    (model-list 'get kb-name))

  ;; KB:NEW : uri jena-model -> knowledgebase
  ;;
  ;; Create a new knowledgebase from scratch, registering it with the given
  ;; KB-NAME (a Java URI).  It must not already exist.
  ;; Return the new knowledgebase.  Either succeeds or throws an error.
  (define/contract (kb:new (kb-name uri?) (kb-metadata jena-model?) -> kb:knowledgebase?)
    (if (kb:get kb-name)
        (error 'kb:new
               "bad call to kb:new: knowledgebase ~a already exists" kb-name))
    (model-list 'put kb-name (make-kb kb-name kb-metadata)))

  ;; KB:GET-NAMES : -> list-of-uris
  ;;
  ;; Returns a list of available knowledgebases.  Returns the names
  ;; as a list of URIs, or a null list if there are none.
  (define (kb:get-names)
    (model-list 'get-keys))

  ;; KB:DISCARD : string -> knowledgebase/#f
  ;;
  ;; Remove a given knowledgebase from the list.  Returns it, or
  ;; returns #f if no such knowledgebase existed
  (define/contract (kb:discard (kb-name uri?)
                               -> (lambda (x)
                                    (or (not x)
                                        (kb:knowledgebase? x))))
    (model-list 'remove kb-name))

  ;; ADD-SUBMODEL : list symbol jena-model boolean -> list
  ;; Add a new submodel to the model.  Returns the original or an updated
  ;; submodel list, or #f on any errors (there's nothing which triggers #f
  ;; at present, but it's documented to do this just in case)
  (define/contract (add-submodel (submodel-list      list?)
                                 (new-submodel-name  string?)
                                 (new-submodel-model jena-model?)
                                 (tbox?              boolean?)
                                 -> list?)
    (java-synchronized (java-wrap submodel-list) ;_model-list-lock-object
      (lambda ()
        (cond ((assoc new-submodel-name submodel-list)
               => (lambda (sm-list)     ;already exists
                    (set-cdr! sm-list
                              (cons tbox? new-submodel-model))
                    submodel-list))
              (else                     ;create a new one by consing
                                        ;an entry to the front
               (cons `(,new-submodel-name ,tbox? . ,new-submodel-model)
                     submodel-list))))))


  ;; MAKE-KB : uri model -> knowledgebase
  ;;
  ;; Create a new knowledgebase.  The knowledgebase is a procedure.
  ;; The model and submodels may be retrieved, and new ones added, by
  ;; calling the procedure with a command as the second argument.
  ;;
  ;;    (kb 'add-abox/tbox SUBMODEL-NAME SUBMODEL)
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
  ;;    (kb 'get-model-abox/tbox)
  ;;        Return the merged abox/tbox submodels, or #f if there are none.
  ;;    (kb 'get-metadata)
  ;;        Return the model's metadata, as a Jena Model.
  ;;    (kb 'get-name-as-uri)
  ;;        Return the knowledgebase's name, as a Java URI
  ;;    (kb 'get-name-as-resource)
  ;;        As get-name-as-uri, but returning the name as a Jena Resource
  ;;    (kb 'get-name-as-string)
  ;;        As get-name-as-uri, but returning the name as a Scheme string
  ;;    (kb 'has-model [SUBMODEL-NAME])
  ;;        Return true if the named (sub)model exists, that is, if
  ;;        (kb 'get-model [SUBMODEL-NAME]) would succeed.  Return #f otherwise.
  ;;    (kb 'drop-submodel SUBMODEL-NAME)
  ;;        Forget about the named submodel
  ;;    ;(kb 'set-metadata jstream base-uri content-type)
  ;;    ;    Set the metadata to the arbitrary string INFO.
  ;;    (kb 'info)
  ;;        Return alist with info
  (define (make-kb kb-name metadata)
    (let ((submodels '()) ;a list of (name tbox? . submodel) pseudo-lists
          (merged-model #f)             ;memo
          (merged-tbox #f)              ;memo
          (merged-abox #f)              ;memo
          (query-model #f)              ;memo
          (sync-object (->jstring ""))) ;a dummy Java object

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
                (models (filter (if tbox?
                                    (lambda (x) (cadr x))
                                    (lambda (x) (not (cadr x))))
                                submodels)))
           (cond ((null? models)
                  #f)
                 (tbox?
                  (memoized (if (= (length models) 1)
                                (cddar models)
                                (rdf:merge-models (map cddr models)))
                            merged-tbox))
                 (else
                  (memoized (if (= (length models) 1)
                                (cddar models)
                                (rdf:merge-models (map cddr models)))
                            merged-abox))))))

      (define (sdb-description->model model-or-resource)
        (define-generic-java-methods create connect-default-model read)
        (define-java-classes
          (<store-factory> |com.hp.hpl.jena.sdb.store.StoreFactory|)
          <com.hp.hpl.jena.sdb.store-desc>
          (<sdb-factory> |com.hp.hpl.jena.sdb.SDBFactory|))
        (connect-default-model (java-null <sdb-factory>)
                       (create (java-null <store-factory>)
                               (read (java-null <com.hp.hpl.jena.sdb.store-desc>)
                                     model-or-resource))))

      ;; Add the given submodel to the model.  BOXNAME is either 'tbox or 'abox.
      ;; If the model is actually a description of an SDB store, then instead
      ;; create the model which connects to that (this path is currently not covered by
      ;; any tests).
      (define (add-box name submodel boxname)
        (let ((store-resources (rdf:select-statements submodel #f "a" "http://jena.hpl.hp.com/2007/sdb#Store")))
          (let ((new-model
                 (cond ((not (null? store-resources))
                        (sdb-description->model submodel ;(car store-resources)
                                                ))
                       (else submodel))))
            (kb-synchronized
             (set! submodels
                   (add-submodel submodels
                                 name
                                 new-model
                                 (eq? boxname 'tbox)))
             (clear-memos!)))))

      ;; Return the merger of the submodels, performing and memoizing the merge if necessary
      (define (get-merged-model)
        (kb-synchronized
         (if (null? submodels)
             #f
             (memoized (if (= (length submodels) 1)
                           (cddar submodels)
                           (rdf:merge-models (map cddr submodels)))
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
          (let* ((levelres (rdf:select-statements metadata
                                                  kb-name
                                                  (in-quaestor-namespace "requiredReasoner")
                                                  #f))
                 (level (cond ((and (not (null? levelres))
                                    (rdf:get-property-on-resource (car levelres)
                                                                  (in-quaestor-namespace "level")))
                               => (lambda (levelp)
                                    (->string (to-string levelp))))
                              (else
                               "none"))))
            (chatter "Level for ~s is ~s" kb-name level)
            (let ((reasoner (rdf:get-reasoner level)))
              (cond ((not reasoner)
                     (error "level <~a> doesn't correspond to any reasoner I know about" level))
                    ((and (not tbox)
                          (not abox))
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
                                       tbox)))))))

      (lambda (cmd . args)

        ;; First, some helpful syntax...

        ;; (call-with-args (arg arg ...) body...)
        (define-syntax call-with-args
          (syntax-rules ()
            ((_ (arg1 . moreargs) form . forms)
             (if (= (length args) (length '(arg1 . moreargs)))
                 (apply (lambda (arg1 . moreargs) form . forms) args)
                 (error 'knowledgebase
                        "bad call to knowledgebase with args ~s (expecting ~s)"
                        args (cons (car args) '(arg1 . moreargs)))))
            ((_ () form . forms)
             (if (= (length args) 0)
                 ((lambda () form . forms))
                 (error 'knowledgebase
                        "bad call to knowledgebase with args ~s (expecting no args)"
                        args)))))
        (define-syntax unless
          (syntax-rules ()
            ((_ test expr)
             (if (not test)
                 expr))))

        (case cmd

          ;;;;;;;;;;
          ;; Commands which explicitly mutate the knowledgebase
          
          ((add-abox add-tbox)
           ;; (kb 'add-abox/tbox SUBMODEL-NAME SUBMODEL)
           (call-with-args
            (submodel-name submodel)
            (unless (string? submodel-name)
                    (error 'make-kb
                           "Bad call to add-abox/tbox: submodel name ~s is not a string"
                           submodel-name))
            (add-box submodel-name
                     submodel
                     (if (eq? cmd 'add-tbox)
                         'tbox
                         'abox))
            #t))

          ((drop-submodel)
           ;; (kb 'drop-submodel SUBMODEL-NAME) -> boolean
           (call-with-args
            (submodel-name)
            (unless (string? submodel-name)
                    (error 'make-kb
                           "bad call to drop-submodel: submodel name ~s is not a string"
                           submodel-name))
            (cond ((assoc submodel-name submodels)
                   (kb-synchronized
                    (set! submodels
                          (remove! (lambda (sm)
                                     (string=? submodel-name (car sm)))
                                   submodels))
                    (clear-memos!))
                   #t)
                  (else
                   #f))))

;;           ((set-metadata)
;;            ;; (kb 'set-metadata jinput-stream base-uri content-type-string)
;;            ;; (kb 'set-metadata jstring base-uri <anything>)
;;            ;; Set model metadata to the Model read from the given reader.
;;            ;; If the first argument is a string, then create a model with
;;            ;; this as a dc:description.
;;            (call-with-args
;;             (input base-uri content-type)
;;             (let ((m (get-metadata-from-source input base-uri content-type)))
;;               (kb-synchronized
;;                (set! myuri base-uri)
;;                (set! metadata m)))))

          ;;;;;;;;;;
          ;; Commands which implicitly mutate the knowledgebase, via memoisation

          ((get-model)
           ;; (kb 'get-model [SUBMODEL-NAME])
           ;; Return newly-merged model or #f if no models exist
           (case (length args)
             ((0)
              (get-merged-model))
             ((1)
              (let ((sm (assoc (car args) submodels)))
                (and sm (cddr sm))))
             (else
              (error 'make-kb
                     "Bad call to get: wrong number of args in ~a"
                     args))))

          ((get-query-model)
           ;; return #f on error
           (call-with-args 
            ()
            (kb-synchronized
             (memoized (get-query-model)
                       query-model))))

          ((get-model-tbox get-model-abox)
           ;; (kb 'get-model-tbox/abox)
           ;; return merged models or #f if none
           (call-with-args
            ()
            (get-box (if (eq? cmd 'get-model-tbox)
                         'tbox
                         'abox))))

          ;;;;;;;;;;
          ;; Commands which do not mutate the knowledgebase

          ((has-model)
           ;; (kb 'has-model [SUBMODEL-NAME])
           ;; With an argument, returns true if the named submodel exists.
           ;; Without an argument, it returns true if any submodels exist.
           (cond ((= (length args) 0)
                  (not (null? submodels)))
                 ((= (length args) 1)
                  (assoc (car args) submodels))
                 (else
                  (error 'make-kb
                         "Bad call to has-model: wrong no. args ~s" args))))

          ((get-metadata)
           (call-with-args () metadata))

          ((get-name-as-uri)
           (call-with-args () kb-name))

          ((get-name-as-string)
           (call-with-args ()
                           (define-generic-java-methods to-string)
                           (->string (to-string kb-name))))

          ((get-name-as-resource)
           (call-with-args
            ()
            (define-generic-java-methods create-resource to-string)
            (define-java-class <com.hp.hpl.jena.rdf.model.resource-factory>)
            (create-resource (java-null <com.hp.hpl.jena.rdf.model.resource-factory>)
                             (to-string kb-name))))

          ((info)
           (call-with-args
            ()
            (list (cons 'submodels
                        (map (lambda (sm)
                               `((name . ,(car sm))
                                 (tbox . ,(cadr sm))
                                 (namespaces
                                  . ,(get-model-namespaces (cddr sm)))))
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
         (collection->list (entry-set (get-ns-prefix-map model)))))

  )
