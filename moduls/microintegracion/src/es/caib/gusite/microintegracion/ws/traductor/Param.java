/**
 * Param.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package  es.caib.gusite.microintegracion.ws.traductor;


public class Param  implements java.io.Serializable {
    private java.lang.String txtValue;

    private byte[] binValue;

    private java.lang.String name;  // attribute

    private java.lang.String value;  // attribute

    public Param() {
    }

    public Param(
           java.lang.String txtValue,
           byte[] binValue,
           java.lang.String name,
           java.lang.String value) {
           this.txtValue = txtValue;
           this.binValue = binValue;
           this.name = name;
           this.value = value;
    }


    /**
     * Gets the txtValue value for this Param.
     * 
     * @return txtValue
     */
    public java.lang.String getTxtValue() {
        return txtValue;
    }


    /**
     * Sets the txtValue value for this Param.
     * 
     * @param txtValue
     */
    public void setTxtValue(java.lang.String txtValue) {
        this.txtValue = txtValue;
    }


    /**
     * Gets the binValue value for this Param.
     * 
     * @return binValue
     */
    public byte[] getBinValue() {
        return binValue;
    }


    /**
     * Sets the binValue value for this Param.
     * 
     * @param binValue
     */
    public void setBinValue(byte[] binValue) {
        this.binValue = binValue;
    }


    /**
     * Gets the name value for this Param.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this Param.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the value value for this Param.
     * 
     * @return value
     */
    public java.lang.String getValue() {
        return value;
    }


    /**
     * Sets the value value for this Param.
     * 
     * @param value
     */
    public void setValue(java.lang.String value) {
        this.value = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Param)) return false;
        Param other = (Param) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.txtValue==null && other.getTxtValue()==null) || 
             (this.txtValue!=null &&
              this.txtValue.equals(other.getTxtValue()))) &&
            ((this.binValue==null && other.getBinValue()==null) || 
             (this.binValue!=null &&
              java.util.Arrays.equals(this.binValue, other.getBinValue()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              this.value.equals(other.getValue())));
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
        if (getTxtValue() != null) {
            _hashCode += getTxtValue().hashCode();
        }
        if (getBinValue() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBinValue());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBinValue(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getValue() != null) {
            _hashCode += getValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Param.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://lucysoftware.com/ws", "param"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("value");
        attrField.setXmlName(new javax.xml.namespace.QName("", "value"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("txtValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "txtValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("binValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "binValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
