package com.github.simkuenzi.restforms.demo;

import com.github.simkuenzi.restforms.*;
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
import javax.ws.rs.core.MultivaluedHashMap;
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
        return render(form(new MultivaluedHashMap<>()).nonValidating());
    }

    @POST
    public String post(MultivaluedMap<String, String> rawForm) {
        MapForm form = form(rawForm);
        if (form.valid()) {
            System.out.println("Form is valid");
        } else {
            System.out.println("Form is invalid");
        }

        return render(form);
    }

    private String render(MapForm form) {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheable(false);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Map<String, Object> vars = new HashMap<>();
        vars.put("form", form);

        WebContext context = new WebContext(servletRequest, servletResponse, servletContext, servletRequest.getLocale(), vars);
        return templateEngine.process("form", context);
    }

    private MapForm form(MultivaluedMap<String, String> rawForm) {
        return new MapForm("Invalid Input",
                new TextField(new FormValue("text", rawForm)),
                new MandatoryField(
                        new TextField(new FormValue("mandatoryText", rawForm)), "This field is mandatory"),
                new DecimalField(new FormValue("decimal", rawForm),"Must be a valid decimal number")
        );
    }
}
