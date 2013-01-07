package org.etp.portalKit.common.service;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.etp.portalKit.common.util.JSONUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * The purpose of this class is to intercept exceptions that throw
 * within controllers, and convert it to a jackson lib allowed format.
 */
public class FailurecallbackExceptionResolver extends SimpleMappingExceptionResolver {

    @Override
    public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        boolean isJsonformat = false;
        Annotation[] declaredAnnotations = ((HandlerMethod) handler).getMethod().getDeclaredAnnotations();
        for (Annotation anno : declaredAnnotations) {
            if (anno.annotationType() == ResponseBody.class) {
                isJsonformat = true;
                break;
            }
        }
        //view returned
        if (!isJsonformat) {//normal views  
            return super.doResolveException(request, response, handler, ex);
        }

        //JSON returned  
        Map<String, String> model = new HashMap<String, String>();
        //exception
        model.put("type", ex.getClass().getName());
        model.put("message", ex.getMessage());
        applyStatusCodeIfPossible(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        try {
            response.getWriter().write(JSONUtils.toJSON(model));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ModelAndView();

    }
}
