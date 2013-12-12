/**
 * XmapiImplService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.dqy.email;

public interface XmapiImplService extends javax.xml.rpc.Service {
    public String getxmapiAddress();

    public com.dqy.email.XmapiImpl getxmapi() throws javax.xml.rpc.ServiceException;

    public com.dqy.email.XmapiImpl getxmapi(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
