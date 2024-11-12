package com.ib.imagebord_test.configuration_app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class confPropsCookies {
    @Value(value = "${cookie.id}")
    private String cookie_id;

    public String getCookie_id() {
        return cookie_id;
    }

    public void setCookie_id(String cookie_id) {
        this.cookie_id = cookie_id;
    }
}
