package org.limadelrey.quarkus.sse;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;
import java.util.UUID;

@Path("/api/v1/sse")
@Singleton
public class SimpleSSE {

    private Sse sse;
    private SseEventSink sseEventSink = null;

    @Context
    public void setSse(Sse sse) {
        this.sse = sse;
    }

    @POST
    public Response produce() {
        if (sseEventSink != null) {
            final OutboundSseEvent sseEvent = sse.newEventBuilder()
                    .id(UUID.randomUUID().toString())
                    .name("EVENT TYPE")
                    .data("EVENT DATA")
                    .reconnectDelay(3000)
                    .build();

            sseEventSink.send(sseEvent);
        }

        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void consume(@Context SseEventSink sseEventSink) {
        this.sseEventSink = sseEventSink;
    }

}
