package com.ddcoding.framework.console.exception;

import lombok.Setter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 */
public class DefaultHandlerExceptionResolver implements HandlerExceptionResolver {

    @Setter
    private String defaultView;

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception exception) {
        handleException(httpServletRequest, exception);
        if (!(handler instanceof HandlerMethod)) {
            return getDefaultView();
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        ExceptionForward methodExceptionForward = handlerMethod.getMethodAnnotation(ExceptionForward.class);
        ExceptionForward beanExceptionForward = handlerMethod.getBeanType().getDeclaredAnnotation(ExceptionForward.class);
        StringBuffer stringBuffer = new StringBuffer("forward:");
        if (methodExceptionForward != null) {
            stringBuffer.append(methodExceptionForward.value());
            return new ModelAndView(stringBuffer.toString());
        }
        if (beanExceptionForward != null) {
            stringBuffer.append(beanExceptionForward.value());
            return new ModelAndView(stringBuffer.toString());
        }
        return getDefaultView();
    }

    protected ModelAndView getDefaultView() {
        return new ModelAndView("forward:" + defaultView);
    }

    protected void handleException(HttpServletRequest httpServletRequest, Exception exception) {
        httpServletRequest.setAttribute("message", "<div class=\"alert alert-error alert-block\">" +
                " <a class=\"close\" data-dismiss=\"alert\" href=\"#\">×</a>" +
                "<h4 class=\"alert-heading\">Error!</h4>" + exception.getClass().getName() + ":" +exception.getMessage() + "</div>");
    }

}
