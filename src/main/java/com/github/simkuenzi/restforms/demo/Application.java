package com.github.simkuenzi.restforms.demo;

import java.util.Collections;
import java.util.Set;

public class Application extends javax.ws.rs.core.Application {
    @Override
    public Set<Object> getSingletons() {
        return Collections.singleton(new DemoResource());
    }
}
