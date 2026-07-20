package com.priceTracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.webmvc.test.autoconfigure.MockMvcBuilderCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/** Attaches the admin credential to every MockMvc request by default, since controller
 * integration tests exercise both public GET endpoints and admin-only write endpoints. */
@Component
public class MockMvcAdminAuthCustomizer implements MockMvcBuilderCustomizer {

    private final String adminUsername;
    private final String adminPassword;

    public MockMvcAdminAuthCustomizer(@Value("${app.admin.username}") String adminUsername,
                                       @Value("${app.admin.password}") String adminPassword) {
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @Override
    public void customize(ConfigurableMockMvcBuilder<?> builder) {
        builder.defaultRequest(get("/").with(httpBasic(adminUsername, adminPassword)));
    }
}