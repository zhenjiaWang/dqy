package com.dqy.common;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.guiceside.commons.collection.RequestData;
import org.guiceside.web.action.ActionContext;
import org.guiceside.web.dispatcher.mapper.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2008-11-6
 * @since JDK1.5
 */
public class ActionInterceptor implements MethodInterceptor {

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = null;

        Object[] args = invocation.getArguments();

        ActionContext actionContext = (ActionContext) args[0];
        ActionMapping actionMapping = (ActionMapping) actionContext
                .getActionContext().get(ActionContext.ACTIONMAPPING);
        HttpServletRequest httpServletRequest = (HttpServletRequest) actionContext.getActionContext().get(ActionContext.HTTPSERVLETREQUEST);
        HttpServletResponse httpServletResponse = (HttpServletResponse) actionContext.getActionContext().get(ActionContext.HTTPSERVLETRESPONSE);
        RequestData requestData = (RequestData) actionContext.getActionContext().get(ActionContext.REQUESTDATA);
        if (actionMapping.getNamespace().equals("/common")) {
            if (actionMapping.getName().equals("login")) {
                if (actionMapping.getMethodName().equals("execute")||actionMapping.getMethodName().equals("validateUser")) {
                    result = invocation.proceed();
                    return result;
                }

            }
        }
        UserInfo userInfo = null;
        try {
            userInfo = UserSession.getUserInfo(httpServletRequest);
        } catch (UserSessionException e) {
            httpServletResponse.sendRedirect("/view/common/urlGoLogin.html");
            return null;
        } catch (Exception e) {

        }
        result = invocation.proceed();
        return result;
    }
}