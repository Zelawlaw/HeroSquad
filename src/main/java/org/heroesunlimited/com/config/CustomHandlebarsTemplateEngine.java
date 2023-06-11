package org.heroesunlimited.com.config;

import com.github.jknack.handlebars.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.TemplateEngine;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;

import java.io.IOException;
import java.util.Map;

public class CustomHandlebarsTemplateEngine extends TemplateEngine {
    private static final Logger logger = LoggerFactory.getLogger(CustomHandlebarsTemplateEngine.class);

    private Handlebars handlebars;

    public CustomHandlebarsTemplateEngine() {

        handlebars = new Handlebars(new ClassPathTemplateLoader("/templates"));

        // Register the ifEquals helper

        handlebars.registerHelper("ifEquals", (Helper<Object>) (ctx, options) -> {
            Object value1 = options.param(0);
            Object value2 = options.param(1);
            if (value1 != null && value1.equals(value2)) {
                return options.fn();
            } else {
                return options.inverse();
            }
        });

        logRegisteredHelpers();


    }

    @Override
    public String render(ModelAndView modelAndView) {
        try {
            Template template = handlebars.compile(modelAndView.getViewName());
            return template.apply(modelAndView.getModel());
        } catch (IOException e) {
            throw new RuntimeException("Error rendering Handlebars template", e);
        }
    }

    public void logRegisteredHelpers() {
        handlebars.helpers().forEach((name) -> {
            logger.info("Registered Helper: {}", name);
        });
    }
}


