package dev.pedrohfreitas.javalib.infrastructure.google;

import dev.pedrohfreitas.javalib.domain.exception.ExternalServiceException;
import dev.pedrohfreitas.javalib.domain.exception.UnauthorizedException;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GoogleOAuth2ClientTest {

    @RegisterExtension
    WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .failOnUnmatchedRequests(false)
            .build();

    private GoogleOAuth2Client createClient() {
        String tokenInfoUrl = "http://localhost:" + wireMock.getPort() + "/tokeninfo";
        String userInfoUrl = "http://localhost:" + wireMock.getPort() + "/userinfo";
        var props = new GoogleOAuth2Properties(
                List.of("https://accounts.google.com", "accounts.google.com"),
                "test-client-id",
                userInfoUrl,
                tokenInfoUrl
        );

        WebClient webClient = WebClient.builder()
                .baseUrl(props.tokenInfoUrl())
                .build();

        return new GoogleOAuth2Client(webClient, props);
    }

    @Test
    @DisplayName("should validate a valid token")
    void shouldValidateValidToken() {
        wireMock.stubFor(get(urlPathEqualTo("/tokeninfo"))
                .withQueryParam("id_token", equalTo("valid-token"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {"iss":"https://accounts.google.com","aud":"test-client-id","sub":"sub123","email":"user@example.com","email_verified":true,"name":"John","picture":"https://pic.jpg"}
                                """)));

        var client = createClient();
        var result = client.validateToken("valid-token");

        assertThat(result.sub()).isEqualTo("sub123");
        assertThat(result.email()).isEqualTo("user@example.com");
        assertThat(result.emailVerified()).isTrue();
    }

    @Test
    @DisplayName("should throw UnauthorizedException for invalid issuer")
    void shouldThrowForInvalidIssuer() {
        wireMock.stubFor(get(urlPathEqualTo("/tokeninfo"))
                .withQueryParam("id_token", equalTo("bad-issuer-token"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {"iss":"https://evil.com","aud":"test-client-id","sub":"sub123","email":"user@example.com","email_verified":true}
                                """)));

        var client = createClient();

        assertThatThrownBy(() -> client.validateToken("bad-issuer-token"))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("issuer");
    }

    @Test
    @DisplayName("should throw UnauthorizedException for invalid audience")
    void shouldThrowForInvalidAudience() {
        wireMock.stubFor(get(urlPathEqualTo("/tokeninfo"))
                .withQueryParam("id_token", equalTo("bad-aud-token"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {"iss":"https://accounts.google.com","aud":"wrong-client","sub":"sub123","email":"user@example.com","email_verified":true}
                                """)));

        var client = createClient();

        assertThatThrownBy(() -> client.validateToken("bad-aud-token"))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("audience");
    }

    @Test
    @DisplayName("should throw ExternalServiceException on server error")
    void shouldThrowOnServerError() {
        wireMock.stubFor(get(urlPathEqualTo("/tokeninfo"))
                .willReturn(aResponse().withStatus(500)));

        var client = createClient();

        assertThatThrownBy(() -> client.validateToken("any-token"))
                .isInstanceOf(ExternalServiceException.class)
                .hasMessageContaining("Google OAuth");
    }

    @Test
    @DisplayName("should create client programmatically via factory method")
    void shouldCreateClientViaFactoryMethod() {
        String tokenInfoUrl = "http://localhost:" + wireMock.getPort() + "/tokeninfo";
        var props = new GoogleOAuth2Properties(
                List.of("https://accounts.google.com", "accounts.google.com"),
                "factory-client-id",
                "http://localhost:" + wireMock.getPort() + "/userinfo",
                tokenInfoUrl
        );

        wireMock.stubFor(get(urlPathEqualTo("/tokeninfo"))
                .withQueryParam("id_token", equalTo("factory-token"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {"iss":"https://accounts.google.com","aud":"factory-client-id","sub":"sub456","email":"factory@example.com","email_verified":true,"name":"Factory","picture":"https://pic.jpg"}
                                """)));

        var client = GoogleOAuth2Client.create(props);

        var result = client.validateToken("factory-token");
        assertThat(result.sub()).isEqualTo("sub456");
        assertThat(result.email()).isEqualTo("factory@example.com");
        assertThat(result.emailVerified()).isTrue();
    }
}
