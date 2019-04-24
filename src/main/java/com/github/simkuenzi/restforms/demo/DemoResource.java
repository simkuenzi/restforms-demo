package com.github.simkuenzi.restforms.demo;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.Map;

@Path("")
public class DemoResource {

    @javax.ws.rs.core.Context
    private ServletContext servletContext;
    @javax.ws.rs.core.Context
    private HttpServletRequest servletRequest;
    @javax.ws.rs.core.Context
    private HttpServletResponse servletResponse;

    @GET
    public String get() {
        Map<String, Object> vars = new HashMap<>();
        vars.put("form", new DemoForm().noValidation());
        return render(vars);
    }

    @POST
    public String post(MultivaluedMap<String, String> rawForm) {
        DemoForm form = new DemoForm(rawForm);
        if (form.valid()) {
            System.out.println("Form is valid");
        } else {
            System.out.println("Form is invalid");
        }

        Map<String, Object> vars = new HashMap<>();
        vars.put("form", form);

        return render(vars);
    }

    private String render(Map<String, Object> vars) {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheable(false);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        WebContext context = new WebContext(servletRequest, servletResponse, servletContext, servletRequest.getLocale(), vars);
        return templateEngine.process("form", context);
    }
}
