package com.example.topgoback.Tools;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
public class JwtIdChecker {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Before("@annotation(JwtCheckAnnotation)")
    public void checkId(JoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String authorization = request.getHeader("Authorization");
        String jwtToken;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            jwtToken = authorization.substring(7);
        }
        else{
            throw new AccessDeniedException("Access denied!");
        }
        String id ;
        int idIndex = -1;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof PathVariable && ((PathVariable) annotation).value().equals("id")) {
                    idIndex = i;
                    break;
                }
            }
        }
        id = String.valueOf(joinPoint.getArgs()[idIndex]);
        String jwtId = String.valueOf(jwtTokenUtil.getUserIdFromToken(jwtToken));
        if(!id.equals(jwtId)){
            throw new AccessDeniedException("Access denied!");
        }

    }
}
