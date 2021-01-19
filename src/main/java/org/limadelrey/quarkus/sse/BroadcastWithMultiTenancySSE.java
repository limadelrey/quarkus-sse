package org.limadelrey.quarkus.sse;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Path("/api/v1/broadcast-sse-multi-tenancy")
@Singleton
public class BroadcastWithMultiTenancySSE {

    private Sse sse;
    private Map<String, SseBroadcaster> sseBroadcasterMap = new HashMap<>();

    @Context
    public void setSse(Sse sse) {
        this.sse = sse;
    }

    @POST
    public Response produce(@HeaderParam("X-Tenant") String tenant) {
        if (!sseBroadcasterMap.isEmpty()) {
            final OutboundSseEvent sseEvent = sse.newEventBuilder()
                    .id(UUID.randomUUID().toString())
                    .name("EVENT TYPE")
                    .data("EVENT DATA")
                    .reconnectDelay(3000)
                    .build();

            sseBroadcasterMap.get(tenant).broadcast(sseEvent);
        }

        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void consume(@Context SseEventSink sseEventSink,
                        @HeaderParam("X-Tenant") String tenant) {
        if (!sseBroadcasterMap.containsKey(tenant)) {
            sseBroadcasterMap.put(tenant, sse.newBroadcaster());
        }

        sseBroadcasterMap.get(tenant).register(sseEventSink);
    }

}
