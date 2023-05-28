package net.brickverse.test;

import lombok.SneakyThrows;
import okhttp3.Headers;
import okhttp3.Response;
import javax.ws.rs.core.Response.Status;

public class ResponseConverter {

    @SneakyThrows
    public static javax.ws.rs.core.Response convert(okhttp3.Response okhttpResponse) {
        Status responseStatus = Status.fromStatusCode(okhttpResponse.code());
        javax.ws.rs.core.Response.StatusType statusType = new javax.ws.rs.core.Response.StatusType() {
            @Override
            public int getStatusCode() {
                return responseStatus.getStatusCode();
            }

            @Override
            public Status.Family getFamily() {
                return responseStatus.getFamily();
            }

            @Override
            public String getReasonPhrase() {
                return responseStatus.getReasonPhrase();
            }
        };

        javax.ws.rs.core.Response.ResponseBuilder responseBuilder = javax.ws.rs.core.Response
                .status(statusType)
                .entity(okhttpResponse.body().string());

        // Copy response headers from OkHttp response to JAX-RS response
        Headers headers = okhttpResponse.headers();
        for (int i = 0, size = headers.size(); i < size; i++) {
            String name = headers.name(i);
            String value = headers.value(i);
            responseBuilder.header(name, value);
        }

        // Close the OkHttp response body
        okhttpResponse.close();

        return responseBuilder.build();
    }
}
