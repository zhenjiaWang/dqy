package com.dqy.common;

import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import org.aopalliance.intercept.MethodInterceptor;
import org.guiceside.guice.strategy.AbstractInterceptorStrategy;
import org.guiceside.web.annotation.ActionInterceptor;
import org.guiceside.web.annotation.BindingGuice;

import java.lang.reflect.Method;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2009-4-28
 * @since JDK1.5
 */
public class WebExceptionInterceptorModel extends AbstractInterceptorStrategy {

    public Matcher<? super Class<?>> getClassMatcher() {

        return Matchers.annotatedWith(BindingGuice.class);
    }

    public MethodInterceptor getMethodInterceptor() {

        return new WebExceptionInterceptor();
    }

    public Matcher<? super Method> getMethodMatcher() {

        return Matchers.annotatedWith(ActionInterceptor.class);
    }
}