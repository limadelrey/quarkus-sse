package org.limadelrey.quarkus.sse;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/api/v1/broadcast-sse-event-id")
@Singleton
public class BroadcastWithEventIdSSE {

    private Sse sse;
    private SseBroadcaster sseBroadcaster = null;

    private final List<OutboundSseEvent> events = new ArrayList<>();

    @Context
    public void setSse(Sse sse) {
        this.sse = sse;
    }

    @POST
    public Response produce() {
        final OutboundSseEvent event = createEvent(events.size() + 1);

        sseBroadcaster.broadcast(event);
        events.add(event);

        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void consume(@Context SseEventSink sseEventSink,
                        @HeaderParam(HttpHeaders.LAST_EVENT_ID_HEADER) @DefaultValue("-1") int lastEventId) {
        if (sseBroadcaster == null) {
            sseBroadcaster = sse.newBroadcaster();
        }

        sseBroadcaster.register(sseEventSink);

        if (lastEventId >= 0) {
            sendPreviousEvents(sseEventSink, lastEventId);
        }
    }

    // Private methods
    private OutboundSseEvent createEvent(int eventId) {
        return sse.newEventBuilder()
                .id(String.valueOf(eventId))
                .name("EVENT TYPE")
                .data("EVENT DATA")
                .reconnectDelay(3000)
                .build();
    }

    private void sendPreviousEvents(SseEventSink sseEventSink, int lastEventId) {
        for (int i = lastEventId; i < events.size(); i++) {
            sseEventSink.send(events.get(i));
        }
    }

}
