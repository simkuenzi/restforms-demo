package com.github.simkuenzi.restforms.demo;

import com.github.simkuenzi.restforms.*;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.math.BigDecimal;
import java.util.stream.Stream;

public class DemoForm {

    private FormField<String> textField;
    private FormField<String> mandatoryTextField;
    private FormField<BigDecimal> decimalField;

    DemoForm() {
        this(new MultivaluedHashMap<>());
    }

    DemoForm(MultivaluedMap<String, String> rawForm) {
        this(
                new TextField(new FormValue("text", rawForm)),
                new MandatoryField(new TextField(new FormValue("mandatoryText", rawForm))),
                new DecimalField(new FormValue("decimal", rawForm)));
    }

    private DemoForm(FormField<String> textField, FormField<String> mandatoryTextField, FormField<BigDecimal> decimalField) {
        this.textField = textField;
        this.mandatoryTextField = mandatoryTextField;
        this.decimalField = decimalField;
    }

    DemoForm noValidation() {
        return new DemoForm(
                new AlwaysValidField<>(textField),
                new AlwaysValidField<>(mandatoryTextField),
                new AlwaysValidField<>(decimalField)
        );
    }

    public boolean valid() {
        return Stream.of(textField, mandatoryTextField, decimalField).allMatch(FormField::valid);
    }

    public FormField<String> getText() {
        return textField;
    }

    public FormField<String> getMandatoryText() {
        return mandatoryTextField;
    }

    public FormField<BigDecimal> getDecimal() {
        return decimalField;
    }

    public String message() {
        return valid() ? "" : "Invalid input";
    }
}
