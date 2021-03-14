package Microservice;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;
import io.vertx.rxjava.ext.web.client.HttpRequest;
import io.vertx.rxjava.ext.web.client.HttpResponse;
import io.vertx.rxjava.ext.web.client.WebClient;
import io.vertx.rxjava.ext.web.codec.BodyCodec;
import rx.Single;

public class Consumer extends AbstractVerticle {

    private WebClient client;

    @Override
    public void start() {
        client = WebClient.create(vertx);

        Router router = Router.router(vertx);
        router.get("/").handler(this::invokeMyFirstMicroservice);

        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(8085);
    }

    private void invokeMyFirstMicroservice(RoutingContext rc) {
        HttpRequest<JsonObject> requestG = client
            .get(8080, "localhost", "/Gunjan")
            .as(BodyCodec.jsonObject());

        HttpRequest<JsonObject> requestR = client
            .get(8080, "localhost", "/Rahul")
            .as(BodyCodec.jsonObject());

        Single<HttpResponse<JsonObject>> s1 = requestG.rxSend();
        Single<HttpResponse<JsonObject>> s2 = requestR.rxSend();

        Single.zip(s1, s2, (gunjan, rahul) -> {
            return new JsonObject()
                .put("gk", gunjan.body().getString("message"))
                .put("rk", rahul.body().getString("message"));
        }).subscribe( //
            x -> {
                rc.response().end(x.encode());
            },
            t -> {
                rc.response().end(new JsonObject().encodePrettily());
            });
    }

}