/**
 * WsManagerUser.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.dqy.email;

public class WsManagerUser  implements java.io.Serializable {
    private String account;

    private String domain;

    private Integer id;

    private String ip;

    private String key;

    private String product;

    private Integer status;

    public WsManagerUser() {
    }

    public WsManagerUser(
           String account,
           String domain,
           Integer id,
           String ip,
           String key,
           String product,
           Integer status) {
           this.account = account;
           this.domain = domain;
           this.id = id;
           this.ip = ip;
           this.key = key;
           this.product = product;
           this.status = status;
    }


    /**
     * Gets the account value for this WsManagerUser.
     *
     * @return account
     */
    public String getAccount() {
        return account;
    }


    /**
     * Sets the account value for this WsManagerUser.
     *
     * @param account
     */
    public void setAccount(String account) {
        this.account = account;
    }


    /**
     * Gets the domain value for this WsManagerUser.
     *
     * @return domain
     */
    public String getDomain() {
        return domain;
    }


    /**
     * Sets the domain value for this WsManagerUser.
     *
     * @param domain
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }


    /**
     * Gets the id value for this WsManagerUser.
     *
     * @return id
     */
    public Integer getId() {
        return id;
    }


    /**
     * Sets the id value for this WsManagerUser.
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }


    /**
     * Gets the ip value for this WsManagerUser.
     *
     * @return ip
     */
    public String getIp() {
        return ip;
    }


    /**
     * Sets the ip value for this WsManagerUser.
     *
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }


    /**
     * Gets the key value for this WsManagerUser.
     *
     * @return key
     */
    public String getKey() {
        return key;
    }


    /**
     * Sets the key value for this WsManagerUser.
     *
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }


    /**
     * Gets the product value for this WsManagerUser.
     *
     * @return product
     */
    public String getProduct() {
        return product;
    }


    /**
     * Sets the product value for this WsManagerUser.
     *
     * @param product
     */
    public void setProduct(String product) {
        this.product = product;
    }


    /**
     * Gets the status value for this WsManagerUser.
     *
     * @return status
     */
    public Integer getStatus() {
        return status;
    }


    /**
     * Sets the status value for this WsManagerUser.
     *
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof WsManagerUser)) return false;
        WsManagerUser other = (WsManagerUser) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
            ((this.account==null && other.getAccount()==null) ||
             (this.account!=null &&
              this.account.equals(other.getAccount()))) &&
            ((this.domain==null && other.getDomain()==null) ||
             (this.domain!=null &&
              this.domain.equals(other.getDomain()))) &&
            ((this.id==null && other.getId()==null) ||
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.ip==null && other.getIp()==null) ||
             (this.ip!=null &&
              this.ip.equals(other.getIp()))) &&
            ((this.key==null && other.getKey()==null) ||
             (this.key!=null &&
              this.key.equals(other.getKey()))) &&
            ((this.product==null && other.getProduct()==null) ||
             (this.product!=null &&
              this.product.equals(other.getProduct()))) &&
            ((this.status==null && other.getStatus()==null) ||
             (this.status!=null &&
              this.status.equals(other.getStatus())));
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
        if (getAccount() != null) {
            _hashCode += getAccount().hashCode();
        }
        if (getDomain() != null) {
            _hashCode += getDomain().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getIp() != null) {
            _hashCode += getIp().hashCode();
        }
        if (getKey() != null) {
            _hashCode += getKey().hashCode();
        }
        if (getProduct() != null) {
            _hashCode += getProduct().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WsManagerUser.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://module.ma.net263.com", "WsManagerUser"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("account");
        elemField.setXmlName(new javax.xml.namespace.QName("", "account"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("domain");
        elemField.setXmlName(new javax.xml.namespace.QName("", "domain"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ip");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ip"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("key");
        elemField.setXmlName(new javax.xml.namespace.QName("", "key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("product");
        elemField.setXmlName(new javax.xml.namespace.QName("", "product"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
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
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
