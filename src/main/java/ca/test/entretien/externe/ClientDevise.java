package ca.test.entretien.externe;

import io.netty.channel.ChannelOption;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Service
public class ClientDevise {
    private final WebClient webClient;

    public ClientDevise(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                /*.clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .responseTimeout(Duration.ofSeconds(5))   // Timeout de lecture
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)  // Timeout de connexion
                ))*/
                .baseUrl("http://localhost:8080/devise").build();
    }
    public Double getResult(Double prix)  {
        return webClient.get()
                .uri("/{val}", prix)
                .retrieve()
                .bodyToMono(Double.class)
                .block();
         }
}

