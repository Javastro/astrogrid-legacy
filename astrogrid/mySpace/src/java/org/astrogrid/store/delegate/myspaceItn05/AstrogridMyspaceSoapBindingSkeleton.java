/**
 * AstrogridMyspaceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.myspace.delegate;

public class AstrogridMyspaceSoapBindingSkeleton implements org.astrogrid.myspace.delegate.Manager, org.apache.axis.wsdl.Skeleton {
    private org.astrogrid.myspace.delegate.Manager impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getBytes", _params, new javax.xml.namespace.QName("", "getBytesReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:Kernel", "KernelResults"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:Kernel", "getBytes"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getBytes") == null) {
            _myOperations.put("getBytes", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getBytes")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getString", _params, new javax.xml.namespace.QName("", "getStringReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:Kernel", "KernelResults"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:Kernel", "getString"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getString") == null) {
            _myOperations.put("getString", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getString")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
        };
        _oper = new org.apache.axis.description.OperationDesc("heartBeat", _params, new javax.xml.namespace.QName("", "heartBeatReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:Kernel", "heartBeat"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("heartBeat") == null) {
            _myOperations.put("heartBeat", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("heartBeat")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getEntriesList", _params, new javax.xml.namespace.QName("", "getEntriesListReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:Kernel", "KernelResults"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:Kernel", "getEntriesList"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getEntriesList") == null) {
            _myOperations.put("getEntriesList", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getEntriesList")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("putString", _params, new javax.xml.namespace.QName("", "putStringReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:Kernel", "KernelResults"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:Kernel", "putString"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("putString") == null) {
            _myOperations.put("putString", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("putString")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("putBytes", _params, new javax.xml.namespace.QName("", "putBytesReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:Kernel", "KernelResults"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:Kernel", "putBytes"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("putBytes") == null) {
            _myOperations.put("putBytes", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("putBytes")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("putUri", _params, new javax.xml.namespace.QName("", "putUriReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:Kernel", "KernelResults"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:Kernel", "putUri"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("putUri") == null) {
            _myOperations.put("putUri", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("putUri")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("createContainer", _params, new javax.xml.namespace.QName("", "createContainerReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:Kernel", "KernelResults"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:Kernel", "createContainer"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("createContainer") == null) {
            _myOperations.put("createContainer", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("createContainer")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("copyFile", _params, new javax.xml.namespace.QName("", "copyFileReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:Kernel", "KernelResults"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:Kernel", "copyFile"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("copyFile") == null) {
            _myOperations.put("copyFile", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("copyFile")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("moveFile", _params, new javax.xml.namespace.QName("", "moveFileReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:Kernel", "KernelResults"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:Kernel", "moveFile"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("moveFile") == null) {
            _myOperations.put("moveFile", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("moveFile")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deleteFile", _params, new javax.xml.namespace.QName("", "deleteFileReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:Kernel", "KernelResults"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:Kernel", "deleteFile"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("deleteFile") == null) {
            _myOperations.put("deleteFile", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deleteFile")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("extendLifetime", _params, new javax.xml.namespace.QName("", "extendLifetimeReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:Kernel", "KernelResults"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:Kernel", "extendLifetime"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("extendLifetime") == null) {
            _myOperations.put("extendLifetime", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("extendLifetime")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("changeOwner", _params, new javax.xml.namespace.QName("", "changeOwnerReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:Kernel", "KernelResults"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:Kernel", "changeOwner"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("changeOwner") == null) {
            _myOperations.put("changeOwner", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("changeOwner")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("createAccount", _params, new javax.xml.namespace.QName("", "createAccountReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:Kernel", "KernelResults"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:Kernel", "createAccount"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("createAccount") == null) {
            _myOperations.put("createAccount", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("createAccount")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deleteAccount", _params, new javax.xml.namespace.QName("", "deleteAccountReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:Kernel", "KernelResults"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:Kernel", "deleteAccount"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("deleteAccount") == null) {
            _myOperations.put("deleteAccount", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deleteAccount")).add(_oper);
    }

    public AstrogridMyspaceSoapBindingSkeleton() {
        this.impl = new org.astrogrid.myspace.delegate.AstrogridMyspaceSoapBindingImpl();
    }

    public AstrogridMyspaceSoapBindingSkeleton(org.astrogrid.myspace.delegate.Manager impl) {
        this.impl = impl;
    }
    public org.astrogrid.myspace.delegate.KernelResults getBytes(java.lang.String in0, boolean in1) throws java.rmi.RemoteException
    {
        org.astrogrid.myspace.delegate.KernelResults ret = impl.getBytes(in0, in1);
        return ret;
    }

    public org.astrogrid.myspace.delegate.KernelResults getString(java.lang.String in0, boolean in1) throws java.rmi.RemoteException
    {
        org.astrogrid.myspace.delegate.KernelResults ret = impl.getString(in0, in1);
        return ret;
    }

    public java.lang.String heartBeat() throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.heartBeat();
        return ret;
    }

    public org.astrogrid.myspace.delegate.KernelResults getEntriesList(java.lang.String in0, boolean in1) throws java.rmi.RemoteException
    {
        org.astrogrid.myspace.delegate.KernelResults ret = impl.getEntriesList(in0, in1);
        return ret;
    }

    public org.astrogrid.myspace.delegate.KernelResults putString(java.lang.String in0, java.lang.String in1, int in2, int in3, boolean in4) throws java.rmi.RemoteException
    {
        org.astrogrid.myspace.delegate.KernelResults ret = impl.putString(in0, in1, in2, in3, in4);
        return ret;
    }

    public org.astrogrid.myspace.delegate.KernelResults putBytes(java.lang.String in0, byte[] in1, int in2, int in3, boolean in4) throws java.rmi.RemoteException
    {
        org.astrogrid.myspace.delegate.KernelResults ret = impl.putBytes(in0, in1, in2, in3, in4);
        return ret;
    }

    public org.astrogrid.myspace.delegate.KernelResults putUri(java.lang.String in0, java.lang.String in1, int in2, int in3, boolean in4) throws java.rmi.RemoteException
    {
        org.astrogrid.myspace.delegate.KernelResults ret = impl.putUri(in0, in1, in2, in3, in4);
        return ret;
    }

    public org.astrogrid.myspace.delegate.KernelResults createContainer(java.lang.String in0, boolean in1) throws java.rmi.RemoteException
    {
        org.astrogrid.myspace.delegate.KernelResults ret = impl.createContainer(in0, in1);
        return ret;
    }

    public org.astrogrid.myspace.delegate.KernelResults copyFile(java.lang.String in0, java.lang.String in1, boolean in2) throws java.rmi.RemoteException
    {
        org.astrogrid.myspace.delegate.KernelResults ret = impl.copyFile(in0, in1, in2);
        return ret;
    }

    public org.astrogrid.myspace.delegate.KernelResults moveFile(java.lang.String in0, java.lang.String in1, boolean in2) throws java.rmi.RemoteException
    {
        org.astrogrid.myspace.delegate.KernelResults ret = impl.moveFile(in0, in1, in2);
        return ret;
    }

    public org.astrogrid.myspace.delegate.KernelResults deleteFile(java.lang.String in0, boolean in1) throws java.rmi.RemoteException
    {
        org.astrogrid.myspace.delegate.KernelResults ret = impl.deleteFile(in0, in1);
        return ret;
    }

    public org.astrogrid.myspace.delegate.KernelResults extendLifetime(java.lang.String in0, long in1, boolean in2) throws java.rmi.RemoteException
    {
        org.astrogrid.myspace.delegate.KernelResults ret = impl.extendLifetime(in0, in1, in2);
        return ret;
    }

    public org.astrogrid.myspace.delegate.KernelResults changeOwner(java.lang.String in0, java.lang.String in1, boolean in2) throws java.rmi.RemoteException
    {
        org.astrogrid.myspace.delegate.KernelResults ret = impl.changeOwner(in0, in1, in2);
        return ret;
    }

    public org.astrogrid.myspace.delegate.KernelResults createAccount(java.lang.String in0, boolean in1) throws java.rmi.RemoteException
    {
        org.astrogrid.myspace.delegate.KernelResults ret = impl.createAccount(in0, in1);
        return ret;
    }

    public org.astrogrid.myspace.delegate.KernelResults deleteAccount(java.lang.String in0, boolean in1) throws java.rmi.RemoteException
    {
        org.astrogrid.myspace.delegate.KernelResults ret = impl.deleteAccount(in0, in1);
        return ret;
    }

}
