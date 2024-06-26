package io.quarkus.oidc.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.htmlunit.SilentCssErrorHandler;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusUnitTest;

public class SecurityDisabledTestCase {

    private static Class<?>[] testClasses = {
            UnprotectedResource.class
    };

    @RegisterExtension
    static final QuarkusUnitTest test = new QuarkusUnitTest()
            .withApplicationRoot((jar) -> jar
                    .addClasses(testClasses)
                    .addAsResource("application-security-disabled.properties", "application.properties"));

    @Test
    public void testAccessUnprotectedResource() throws IOException, InterruptedException {
        try (final WebClient webClient = createWebClient()) {

            HtmlPage page = webClient.getPage("http://localhost:8081/unprotected");
            assertEquals("unprotected", page.getBody().asNormalizedText());
            webClient.getCookieManager().clearCookies();
        }
    }

    private WebClient createWebClient() {
        WebClient webClient = new WebClient();
        webClient.setCssErrorHandler(new SilentCssErrorHandler());
        return webClient;
    }
}
