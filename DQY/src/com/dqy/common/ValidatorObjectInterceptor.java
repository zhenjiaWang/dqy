package com.dqy.common;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.guiceside.web.action.ActionContext;
import org.guiceside.web.dispatcher.mapper.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2008-11-6
 * @since JDK1.5
 */
public class ValidatorObjectInterceptor implements MethodInterceptor {

    public Object invoke(MethodInvocation invocation) throws Throwable {

        Object result = "wangzhenjia";
        Object[] args = invocation.getArguments();
        ActionContext actionContext = (ActionContext) args[0];
        ActionMapping actionMapping = (ActionMapping) actionContext
                .getActionContext().get(ActionContext.ACTIONMAPPING);
        HttpServletRequest httpServletRequest = (HttpServletRequest) actionContext.getActionContext().get(ActionContext.HTTPSERVLETREQUEST);
        if (actionMapping.getActionObject().getClass().getName().startsWith("cn.net.unison.common")) {
            result = invocation.proceed();
        } else {
            UserInfo userInfo = UserSession.getUserInfo(httpServletRequest);
            if (userInfo != null) {
                if (userInfo.isLoggedIn()) {
                    if (userInfo.isAuthorize()) {
                        result = invocation.proceed();
                    } else {
                    }
                }
            }
        }
        if (result != null && result.equals("wangzhenjia")) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) actionContext.getActionContext().get(ActionContext.HTTPSERVLETRESPONSE);
            httpServletResponse.sendRedirect("/view/common/wangzhenjia.jsp");
        }
        return result;
    }

}