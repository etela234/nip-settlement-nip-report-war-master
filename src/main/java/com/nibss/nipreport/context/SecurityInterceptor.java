/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.context;

import com.nibss.nipreport.admin.entity.UserIdentity;
import com.nibss.nipreport.bean.ProfileBean;
import java.lang.reflect.Method;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.interception.PostMatchContainerRequestContext;
import org.jboss.resteasy.plugins.providers.html.View;

/**
 *
 * @author oogunjimi
 */
@Provider
public class SecurityInterceptor implements javax.ws.rs.container.ContainerRequestFilter {

//    @Override
//    public void filter(ContainerRequestContext requestContext) throws IOException {
//
//        ResourceMethodInvoker methodInvoker = null;
//        if (requestContext instanceof PostMatchContainerRequestContext) {
//            methodInvoker = ((PostMatchContainerRequestContext) requestContext).getResourceMethod();
//        }
//        if (methodInvoker == null) {
//            Object property = requestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
//            if (property instanceof ResourceMethodInvoker) {
//                methodInvoker = (ResourceMethodInvoker) property;
//            }
//        }
//
//        Method method = methodInvoker == null ? null : methodInvoker.getMethod();
////        Annotation[] methodAnnotations = methodInvoker.getMethodAnnotations();
////        for (Annotation annotation : methodAnnotations) {
////            if(annotation.equals(PermitAll.class))
////
////        }
//        if (method == null || method.isAnnotationPresent(PermitAll.class)) {
//            return;
//        }
//
//        if (method.isAnnotationPresent(DenyAll.class)) {
//            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
//                    .entity("User cannot access the resource.")
//                    .build());
//        }
//        if (method.isAnnotationPresent(RolesAllowed.class)) {
//            RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
//            String[] roles = rolesAnnotation.value();
//            for (String role : roles) {
//                if (role != null) {
//
//                }
//            }
//            Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
//
//            //Is user valid?
//            if (!isUserAllowed(username, password, rolesSet)) {
//                return ACCESS_DENIED;
//            }
//        }
//    }
    //new ServerResponse("Nobody can access this resource", 403, new Headers<Object>());
    //new ServerResponse("Access denied for this resource", 401, new Headers<Object>());
//    private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED).entity("Access denied for this resource").build();
//    private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN).entity("Nobody can access this resource").build();
//    private static final Response SERVER_ERROR = Response.serverError().entity("INTERNAL SERVER ERROR").build();
    private static final String RESOURCE_METHOD_INVOKER = "org.jboss.resteasy.core.ResourceMethodInvoker";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        ResourceMethodInvoker methodInvoker = null;
        if (requestContext instanceof PostMatchContainerRequestContext) {
            methodInvoker = ((PostMatchContainerRequestContext) requestContext).getResourceMethod();
        }
        if (methodInvoker == null) {
            Object property = requestContext.getProperty(RESOURCE_METHOD_INVOKER);
            if (property instanceof ResourceMethodInvoker) {
                methodInvoker = (ResourceMethodInvoker) property;
            }
        }
        Method method = methodInvoker == null ? null : methodInvoker.getMethod();
        if (method == null) {
            requestContext.abortWith(createResponse(Response.Status.INTERNAL_SERVER_ERROR));
            return;
        }
        if (method.isAnnotationPresent(PermitAll.class)) {
            return;
        }
        if (method.isAnnotationPresent(DenyAll.class)) {
            requestContext.abortWith(createResponse(Response.Status.UNAUTHORIZED));
            return;
        }
        if (method.isAnnotationPresent(RolesAllowed.class)) {
            if (!isRolesAllowed(method.getAnnotation(RolesAllowed.class).value())) {
                requestContext.abortWith(createResponse(Response.Status.UNAUTHORIZED));
            }
            return;
        }
        //class check
        Class<?> resourceClass = methodInvoker == null ? null : methodInvoker.getResourceClass();
        if (resourceClass != null) {
            if (method.isAnnotationPresent(PermitAll.class)) {
                return;
            }
            if (resourceClass.isAnnotationPresent(DenyAll.class)) {
                requestContext.abortWith(createResponse(Response.Status.UNAUTHORIZED));
                return;
            }
            if (resourceClass.isAnnotationPresent(RolesAllowed.class)) {
                if (!isRolesAllowed(resourceClass.getAnnotation(RolesAllowed.class).value())) {
                    requestContext.abortWith(createResponse(Response.Status.UNAUTHORIZED));
                }
            }
        }

    }

    private boolean isRolesAllowed(String... roles) {
        ProfileBean profileBean = ProfileBean.instance();
        if (roles != null && profileBean != null) {
            for (String role : roles) {
                if (role != null && profileBean.hasRole(role)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Response createResponse(Response.Status status) {
        ProfileBean profileBean = ProfileBean.instance();
        return Response.status(status).entity(profileBean.errorView(status.getStatusCode())).type(MediaType.TEXT_HTML_TYPE).build();
    }

}
