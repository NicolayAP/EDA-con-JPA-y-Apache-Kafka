package co.edu.uptc.edakafka.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@RestController
public class GatewayController {

    private final WebClient webClient;

    @Value("${spring.gateway.backend-url:http://edakafka:8080}")
    private String backendUrl;

    public GatewayController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @RequestMapping(value = {"/customer/**", "/api/logins/**"}, method = {
            RequestMethod.GET,
            RequestMethod.POST,
            RequestMethod.PUT,
            RequestMethod.DELETE,
            RequestMethod.PATCH
    })
    public Mono<ResponseEntity<byte[]>> proxy(ServerWebExchange exchange,
                                              @RequestBody(required = false) Mono<byte[]> body) {

        HttpMethod method = exchange.getRequest().getMethod();
        String path = exchange.getRequest().getURI().getRawPath();
        String query = exchange.getRequest().getURI().getRawQuery();
        String url = backendUrl + path + (query != null ? "?" + query : "");

        if (method == HttpMethod.GET || method == HttpMethod.DELETE) {
            return webClient.method(method)
                    .uri(url)
                    .headers(headers -> copyHeaders(exchange.getRequest().getHeaders(), headers))
                    .exchangeToMono(clientResponse -> clientResponse.toEntity(byte[].class));
        }

        Mono<byte[]> requestBody = (body != null ? body : Mono.<byte[]>empty());
        return requestBody
                .defaultIfEmpty(new byte[0])
                .flatMap(payload -> webClient.method(method)
                        .uri(url)
                        .headers(headers -> copyHeaders(exchange.getRequest().getHeaders(), headers))
                        .bodyValue(payload)
                        .exchangeToMono(clientResponse -> clientResponse.toEntity(byte[].class)));
    }

    private void copyHeaders(HttpHeaders source, HttpHeaders target) {
        source.forEach((key, values) -> {
            if (!HttpHeaders.HOST.equalsIgnoreCase(key)) {
                target.put(key, values);
            }
        });
    }
}
