package com.collager.images.adapter;

import com.collager.images.property.ImaggaProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Getter
@Setter
@Component
public class ImaggaAdapter {

    WebClient client;


    public ImaggaAdapter(ImaggaProperties properties) {
        String auth = Base64Utils.encodeToString((properties.getKey() + ":" + properties.getSecret()).getBytes(UTF_8));

        this.client = WebClient.builder()
                .baseUrl(properties.getApiEndpoint())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Basic " + auth).build();
    }

    public String getObjects(String imageUrl) {
        String objs = "";
        Mono<ImaggaResponse> imaggaResp = client.get()
                .uri(builder -> builder.path("/tags")
                        .queryParam("image_url", imageUrl)
                        .build())
                .retrieve()
                .bodyToMono(ImaggaResponse.class);
        ImaggaResponse tagResponse = imaggaResp.block();
        if (tagResponse != null) {
            List<String> tags = tagResponse.result.tags
                    .stream()
                    .filter(item -> item.confidence > 65.00)
                    .map(t -> t.tag.get("en"))
                    .collect(Collectors.toList());
            objs = String.join(",", tags);
        }
        return objs;
    }

    @Getter
    @Setter
    @Entity
    private static class ImaggaResponse {
        private ImaggaResult result;
    }

    @Getter
    @Setter
    @Entity
    private static class ImaggaResult {
        private ArrayList<TagResult> tags;
    }

    @Getter
    @Setter
    @Entity
    private static class TagResult {
        private double confidence;
        private HashMap<String, String> tag;

    }
}
