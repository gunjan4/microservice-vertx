package Microservice;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.core.eventbus.Message;
import rx.Single;

import java.util.concurrent.TimeUnit;

public class HelloMicroservice extends AbstractVerticle {

    @Override
    public void start() {
        vertx.createHttpServer()
            .requestHandler(
                req -> {
                    Single<JsonObject> obs1 = vertx.eventBus().
                        <JsonObject>rxSend("hello", "Luke")
                        .subscribeOn(RxHelper.scheduler(vertx))
                        .timeout(3, TimeUnit.SECONDS)
                        .retry((i, t) -> {
                            System.out.println("Retrying... because of " + t.getMessage());
                            return true;
                        })
                        .map(Message::body);
                        
                    Single<JsonObject> obs2 = vertx.eventBus().
                        <JsonObject>rxSend("hello", "Leia")
                        .subscribeOn(RxHelper.scheduler(vertx))
                        .timeout(3, TimeUnit.SECONDS)
                        .retry((i, t) -> {
                            System.out.println("Retrying... because of " + t.getMessage());
                            return true;
                        })
                        .map(Message::body);

                    Single
                        .zip(obs1, obs2, (luke, leia) ->
                            new JsonObject()
                                .put("Luke", luke.getString("message")
                                    + " from " + luke.getString("served-by"))
                                .put("Leia", leia.getString("message")
                                    + " from " + leia.getString("served-by"))
                        )
                        .subscribe(
                            x -> req.response().end(x.encodePrettily()),
                            t -> req.response().setStatusCode(500).end(t.getMessage())
                        );
                })
            .listen(8083);
    }

}

