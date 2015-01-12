/**
 * Task.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.gusite.microintegracion.ws.traductor;

public class Task  implements java.io.Serializable {
    private es.caib.gusite.microintegracion.ws.traductor.Param[] inputParams;

    private es.caib.gusite.microintegracion.ws.traductor.Param[] outputParams;

    private boolean verbose;  // attribute

    private java.lang.String ID;  // attribute

    private java.lang.String service;  // attribute

    private java.lang.String state;  // attribute

    private java.lang.String prio;  // attribute

    private java.lang.String created;  // attribute

    private java.lang.String running;  // attribute

    private java.lang.String done;  // attribute

    private java.lang.String removed;  // attribute

    private java.lang.String creator;  // attribute

    private java.lang.String engine;  // attribute

    private java.lang.String removeReason;  // attribute

    public Task() {
    }

    public Task(
           es.caib.gusite.microintegracion.ws.traductor.Param[] inputParams,
           es.caib.gusite.microintegracion.ws.traductor.Param[] outputParams,
           boolean verbose,
           java.lang.String ID,
           java.lang.String service,
           java.lang.String state,
           java.lang.String prio,
           java.lang.String created,
           java.lang.String running,
           java.lang.String done,
           java.lang.String removed,
           java.lang.String creator,
           java.lang.String engine,
           java.lang.String removeReason) {
           this.inputParams = inputParams;
           this.outputParams = outputParams;
           this.verbose = verbose;
           this.ID = ID;
           this.service = service;
           this.state = state;
           this.prio = prio;
           this.created = created;
           this.running = running;
           this.done = done;
           this.removed = removed;
           this.creator = creator;
           this.engine = engine;
           this.removeReason = removeReason;
    }


    /**
     * Gets the inputParams value for this Task.
     * 
     * @return inputParams
     */
    public es.caib.gusite.microintegracion.ws.traductor.Param[] getInputParams() {
        return inputParams;
    }


    /**
     * Sets the inputParams value for this Task.
     * 
     * @param inputParams
     */
    public void setInputParams(es.caib.gusite.microintegracion.ws.traductor.Param[] inputParams) {
        this.inputParams = inputParams;
    }


    /**
     * Gets the outputParams value for this Task.
     * 
     * @return outputParams
     */
    public es.caib.gusite.microintegracion.ws.traductor.Param[] getOutputParams() {
        return outputParams;
    }


    /**
     * Sets the outputParams value for this Task.
     * 
     * @param outputParams
     */
    public void setOutputParams(es.caib.gusite.microintegracion.ws.traductor.Param[] outputParams) {
        this.outputParams = outputParams;
    }


    /**
     * Gets the verbose value for this Task.
     * 
     * @return verbose
     */
    public boolean isVerbose() {
        return verbose;
    }


    /**
     * Sets the verbose value for this Task.
     * 
     * @param verbose
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }


    /**
     * Gets the ID value for this Task.
     * 
     * @return ID
     */
    public java.lang.String getID() {
        return ID;
    }


    /**
     * Sets the ID value for this Task.
     * 
     * @param ID
     */
    public void setID(java.lang.String ID) {
        this.ID = ID;
    }


    /**
     * Gets the service value for this Task.
     * 
     * @return service
     */
    public java.lang.String getService() {
        return service;
    }


    /**
     * Sets the service value for this Task.
     * 
     * @param service
     */
    public void setService(java.lang.String service) {
        this.service = service;
    }


    /**
     * Gets the state value for this Task.
     * 
     * @return state
     */
    public java.lang.String getState() {
        return state;
    }


    /**
     * Sets the state value for this Task.
     * 
     * @param state
     */
    public void setState(java.lang.String state) {
        this.state = state;
    }


    /**
     * Gets the prio value for this Task.
     * 
     * @return prio
     */
    public java.lang.String getPrio() {
        return prio;
    }


    /**
     * Sets the prio value for this Task.
     * 
     * @param prio
     */
    public void setPrio(java.lang.String prio) {
        this.prio = prio;
    }


    /**
     * Gets the created value for this Task.
     * 
     * @return created
     */
    public java.lang.String getCreated() {
        return created;
    }


    /**
     * Sets the created value for this Task.
     * 
     * @param created
     */
    public void setCreated(java.lang.String created) {
        this.created = created;
    }


    /**
     * Gets the running value for this Task.
     * 
     * @return running
     */
    public java.lang.String getRunning() {
        return running;
    }


    /**
     * Sets the running value for this Task.
     * 
     * @param running
     */
    public void setRunning(java.lang.String running) {
        this.running = running;
    }


    /**
     * Gets the done value for this Task.
     * 
     * @return done
     */
    public java.lang.String getDone() {
        return done;
    }


    /**
     * Sets the done value for this Task.
     * 
     * @param done
     */
    public void setDone(java.lang.String done) {
        this.done = done;
    }


    /**
     * Gets the removed value for this Task.
     * 
     * @return removed
     */
    public java.lang.String getRemoved() {
        return removed;
    }


    /**
     * Sets the removed value for this Task.
     * 
     * @param removed
     */
    public void setRemoved(java.lang.String removed) {
        this.removed = removed;
    }


    /**
     * Gets the creator value for this Task.
     * 
     * @return creator
     */
    public java.lang.String getCreator() {
        return creator;
    }


    /**
     * Sets the creator value for this Task.
     * 
     * @param creator
     */
    public void setCreator(java.lang.String creator) {
        this.creator = creator;
    }


    /**
     * Gets the engine value for this Task.
     * 
     * @return engine
     */
    public java.lang.String getEngine() {
        return engine;
    }


    /**
     * Sets the engine value for this Task.
     * 
     * @param engine
     */
    public void setEngine(java.lang.String engine) {
        this.engine = engine;
    }


    /**
     * Gets the removeReason value for this Task.
     * 
     * @return removeReason
     */
    public java.lang.String getRemoveReason() {
        return removeReason;
    }


    /**
     * Sets the removeReason value for this Task.
     * 
     * @param removeReason
     */
    public void setRemoveReason(java.lang.String removeReason) {
        this.removeReason = removeReason;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Task)) return false;
        Task other = (Task) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.inputParams==null && other.getInputParams()==null) || 
             (this.inputParams!=null &&
              java.util.Arrays.equals(this.inputParams, other.getInputParams()))) &&
            ((this.outputParams==null && other.getOutputParams()==null) || 
             (this.outputParams!=null &&
              java.util.Arrays.equals(this.outputParams, other.getOutputParams()))) &&
            this.verbose == other.isVerbose() &&
            ((this.ID==null && other.getID()==null) || 
             (this.ID!=null &&
              this.ID.equals(other.getID()))) &&
            ((this.service==null && other.getService()==null) || 
             (this.service!=null &&
              this.service.equals(other.getService()))) &&
            ((this.state==null && other.getState()==null) || 
             (this.state!=null &&
              this.state.equals(other.getState()))) &&
            ((this.prio==null && other.getPrio()==null) || 
             (this.prio!=null &&
              this.prio.equals(other.getPrio()))) &&
            ((this.created==null && other.getCreated()==null) || 
             (this.created!=null &&
              this.created.equals(other.getCreated()))) &&
            ((this.running==null && other.getRunning()==null) || 
             (this.running!=null &&
              this.running.equals(other.getRunning()))) &&
            ((this.done==null && other.getDone()==null) || 
             (this.done!=null &&
              this.done.equals(other.getDone()))) &&
            ((this.removed==null && other.getRemoved()==null) || 
             (this.removed!=null &&
              this.removed.equals(other.getRemoved()))) &&
            ((this.creator==null && other.getCreator()==null) || 
             (this.creator!=null &&
              this.creator.equals(other.getCreator()))) &&
            ((this.engine==null && other.getEngine()==null) || 
             (this.engine!=null &&
              this.engine.equals(other.getEngine()))) &&
            ((this.removeReason==null && other.getRemoveReason()==null) || 
             (this.removeReason!=null &&
              this.removeReason.equals(other.getRemoveReason())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getInputParams() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getInputParams());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getInputParams(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getOutputParams() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOutputParams());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOutputParams(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += (isVerbose() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getID() != null) {
            _hashCode += getID().hashCode();
        }
        if (getService() != null) {
            _hashCode += getService().hashCode();
        }
        if (getState() != null) {
            _hashCode += getState().hashCode();
        }
        if (getPrio() != null) {
            _hashCode += getPrio().hashCode();
        }
        if (getCreated() != null) {
            _hashCode += getCreated().hashCode();
        }
        if (getRunning() != null) {
            _hashCode += getRunning().hashCode();
        }
        if (getDone() != null) {
            _hashCode += getDone().hashCode();
        }
        if (getRemoved() != null) {
            _hashCode += getRemoved().hashCode();
        }
        if (getCreator() != null) {
            _hashCode += getCreator().hashCode();
        }
        if (getEngine() != null) {
            _hashCode += getEngine().hashCode();
        }
        if (getRemoveReason() != null) {
            _hashCode += getRemoveReason().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Task.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://lucysoftware.com/ws", "task"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("verbose");
        attrField.setXmlName(new javax.xml.namespace.QName("", "verbose"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("ID");
        attrField.setXmlName(new javax.xml.namespace.QName("", "ID"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("service");
        attrField.setXmlName(new javax.xml.namespace.QName("", "service"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("state");
        attrField.setXmlName(new javax.xml.namespace.QName("", "state"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("prio");
        attrField.setXmlName(new javax.xml.namespace.QName("", "prio"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("created");
        attrField.setXmlName(new javax.xml.namespace.QName("", "created"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("running");
        attrField.setXmlName(new javax.xml.namespace.QName("", "running"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("done");
        attrField.setXmlName(new javax.xml.namespace.QName("", "done"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("removed");
        attrField.setXmlName(new javax.xml.namespace.QName("", "removed"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("creator");
        attrField.setXmlName(new javax.xml.namespace.QName("", "creator"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("engine");
        attrField.setXmlName(new javax.xml.namespace.QName("", "engine"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("removeReason");
        attrField.setXmlName(new javax.xml.namespace.QName("", "removeReason"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inputParams");
        elemField.setXmlName(new javax.xml.namespace.QName("", "InputParams"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://lucysoftware.com/ws", "param"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "Param"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("outputParams");
        elemField.setXmlName(new javax.xml.namespace.QName("", "OutputParams"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://lucysoftware.com/ws", "param"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "Param"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
