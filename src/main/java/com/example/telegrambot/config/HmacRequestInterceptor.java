package com.example.telegrambot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Component
public class HmacRequestInterceptor implements ClientHttpRequestInterceptor {

    @Value("${security.hmac.key}")
    private String SECRET_KEY;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        try {
            long timestamp = Instant.now().getEpochSecond();
            String signedData = timestamp + "." + new String(body, StandardCharsets.UTF_8);
            String signature = generateSignature(signedData);

            HttpHeaders headers = request.getHeaders();
            headers.set("X-Signature", signature);
            headers.set("X-Timestamp", String.valueOf(timestamp));

        } catch (Exception e) {
            throw new IOException("Erro ao gerar assinatura HMAC", e);
        }

        return execution.execute(request, body);
    }

    private String generateSignature(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }
}

