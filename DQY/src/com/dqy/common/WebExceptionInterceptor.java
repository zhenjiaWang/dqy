package com.dqy.common;


import com.google.inject.Injector;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.guiceside.web.action.ActionContext;
import org.guiceside.web.dispatcher.mapper.ActionMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2009-4-28
 * @since JDK1.5
 */
public class WebExceptionInterceptor implements MethodInterceptor {

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = null;
        try {
            result = invocation.proceed();
        } catch (Exception e) {
            ActionContext actionContext = (ActionContext) invocation.getArguments()[0];
            HttpServletRequest httpServletRequest = (HttpServletRequest) actionContext
                    .getActionContext().get(ActionContext.HTTPSERVLETREQUEST);
            ActionMapping actionMapping = (ActionMapping) actionContext.getActionContext().get(ActionContext.ACTIONMAPPING);
            ServletContext servletContext = (ServletContext) actionContext
                    .getActionContext().get(ActionContext.SERVLETCONTEXT);
            UserInfo userInfo = UserSession.getUserInfo(httpServletRequest);
            Injector injector = (Injector) servletContext.getAttribute(Injector.class.getName());
            StackTraceElement[] stackTraceElements = e.getCause() == null ? e.getStackTrace() : e.getCause().getStackTrace();
            String exceptionType = e.getCause() == null ? e.toString() : e.getCause().toString();
            StringBuilder stringBuilder = new StringBuilder();
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                stringBuilder.append(stackTraceElement.toString() + "\n");
            }
//			SysLogException sysLogException=new SysLogException();
//			sysLogException.setCreated(DateFormatUtil.getCurrentDate(true));
//			sysLogException.setCreatedBy("sys");
//			sysLogException.setUpdated(DateFormatUtil.getCurrentDate(true));
//			sysLogException.setUpdatedBy("sys");
//			sysLogException.setTempMessages(stringBuilder.toString());
//			sysLogException.setExceptionType(exceptionType);
//			sysLogException.setExeptionAction(actionMapping.getNamespace()+"/"+actionMapping.getName());
//			sysLogException.setExceptionMethod(actionMapping.getMethodName());
//			String empId=null;
//			try{
//			empId=userInfo.getHrEmployee().getEmpId();
//			}catch (Exception ee) {
//
//			}
//			sysLogException.setFromEmp(empId);
//			sysLogException.setUseYn("Y");
//			SysLogExceptionService sysLogExceptionService=injector.getInstance(SysLogExceptionService.class);
//			String[] clobPropertys=new String[1];
//			clobPropertys[0]="exceptionMessages";
//			String [] clobValues=new String[1];
//			clobValues[0]=sysLogException.getTempMessages();
//			sysLogExceptionService.saveByWithClob(sysLogException, clobPropertys, clobValues);
            throw e;
        }
        return result;
    }
}