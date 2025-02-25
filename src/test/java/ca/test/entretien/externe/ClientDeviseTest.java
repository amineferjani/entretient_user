package ca.test.entretien.externe;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;




import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

class ClientDeviseTest {
    private static WireMockServer wireMockServer;
    private static ClientDevise clientDevise;
    @BeforeAll
    static void setUp() {
        // Démarrer WireMock sur le port 8089 (au lieu de 8080 pour éviter les conflits)
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8089));
        wireMockServer.start();

        // Configurer WebClient pour pointer vers WireMock (localhost:8089)
        clientDevise = new ClientDevise(WebClient.builder().baseUrl("http://localhost:8089/devise"));

    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testGetResult() {
        // Simuler une réponse de l'API avec WireMock
        wireMockServer.stubFor(get(urlPathMatching("/devise/100.0"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("10.0")
                        .withStatus(200)));

        // Appeler la méthode et vérifier le résultat
        Double result = clientDevise.getResult(100.0);
        assertEquals(10.0, result);
    }

    @Test
    void testGetResultNotFound() {
        // Simuler une réponse 404
        wireMockServer.stubFor(get(urlPathMatching("/devise/999.0"))
                .willReturn(aResponse()
                        .withStatus(404)));

        // Vérifier que l'appel retourne une exception
        try {
            clientDevise.getResult(999.0);
        } catch (Exception e) {
            assertEquals("org.springframework.web.reactive.function.client.WebClientResponseException$NotFound", e.getClass().getName());
        }
    }
}
