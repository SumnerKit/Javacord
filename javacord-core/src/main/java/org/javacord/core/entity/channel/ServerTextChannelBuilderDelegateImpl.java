package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.internal.ServerTextChannelBuilderDelegate;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerTextChannelBuilderDelegate}.
 */
public class ServerTextChannelBuilderDelegateImpl extends ServerChannelBuilderDelegateImpl
        implements ServerTextChannelBuilderDelegate {

    /**
     * The topic of the channel.
     */
    private String topic = null;

    /**
     * The category of the channel.
     */
    private ChannelCategory category = null;

    /**
     * Creates a new server text channel builder delegate.
     *
     * @param server The server of the server text channel.
     */
    public ServerTextChannelBuilderDelegateImpl(ServerImpl server) {
        super(server);
    }

    @Override
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public void setCategory(ChannelCategory category) {
        this.category = category;
    }

    @Override
    public CompletableFuture<ServerTextChannel> create() {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        body.put("type", 0);
        super.prepareBody(body);
        if (topic != null) {
            body.put("topic", topic);
        }
        if (category != null) {
            body.put("parent_id", category.getIdAsString());
        }
        return new RestRequest<ServerTextChannel>(server.getApi(), RestMethod.POST, RestEndpoint.SERVER_CHANNEL)
                .setUrlParameters(server.getIdAsString())
                .setBody(body)
                .setAuditLogReason(reason)
                .execute(result -> server.getOrCreateServerTextChannel(result.getJsonBody()));
    }


}
