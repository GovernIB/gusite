/**
 * AutomaticTranslationServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package  es.caib.gusite.microintegracion.ws.traductor;

public class AutomaticTranslationServiceLocator extends org.apache.axis.client.Service implements es.caib.gusite.microintegracion.ws.traductor.AutomaticTranslationService {

    public AutomaticTranslationServiceLocator() {
    }


    public AutomaticTranslationServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AutomaticTranslationServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for AutomaticTranslationPort
    private java.lang.String AutomaticTranslationPort_address = "http://localhost:18080/jaxws-AutomaticTranslationService/AutoTranslate";

    public java.lang.String getAutomaticTranslationPortAddress() {
        return AutomaticTranslationPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AutomaticTranslationPortWSDDServiceName = "AutomaticTranslationPort";

    public java.lang.String getAutomaticTranslationPortWSDDServiceName() {
        return AutomaticTranslationPortWSDDServiceName;
    }

    public void setAutomaticTranslationPortWSDDServiceName(java.lang.String name) {
        AutomaticTranslationPortWSDDServiceName = name;
    }

    public es.caib.gusite.microintegracion.ws.traductor.AutomaticTranslation getAutomaticTranslationPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AutomaticTranslationPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAutomaticTranslationPort(endpoint);
    }

    public es.caib.gusite.microintegracion.ws.traductor.AutomaticTranslation getAutomaticTranslationPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
        	es.caib.gusite.microintegracion.ws.traductor.AutomaticTranslationPortBindingStub _stub = new es.caib.gusite.microintegracion.ws.traductor.AutomaticTranslationPortBindingStub(portAddress, this);
            _stub.setPortName(getAutomaticTranslationPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAutomaticTranslationPortEndpointAddress(java.lang.String address) {
        AutomaticTranslationPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.caib.gusite.microintegracion.ws.traductor.AutomaticTranslation.class.isAssignableFrom(serviceEndpointInterface)) {
            	es.caib.gusite.microintegracion.ws.traductor.AutomaticTranslationPortBindingStub _stub = new es.caib.gusite.microintegracion.ws.traductor.AutomaticTranslationPortBindingStub(new java.net.URL(AutomaticTranslationPort_address), this);
                _stub.setPortName(getAutomaticTranslationPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("AutomaticTranslationPort".equals(inputPortName)) {
            return getAutomaticTranslationPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://lucysoftware.com/ws", "AutomaticTranslationService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://lucysoftware.com/ws", "AutomaticTranslationPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("AutomaticTranslationPort".equals(portName)) {
            setAutomaticTranslationPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
