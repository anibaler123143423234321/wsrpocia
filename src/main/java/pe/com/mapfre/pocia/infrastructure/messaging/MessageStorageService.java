package pe.com.mapfre.pocia.infrastructure.messaging;

import org.springframework.stereotype.Service;
import pe.com.mapfre.pocia.presentation.api.dto.AuthorDTO;
import pe.com.mapfre.pocia.presentation.api.dto.MessageResponseDTO;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageStorageService {

    // Store futures by authorId
    private final Map<Long, CompletableFuture<AuthorDTO>> pendingResponses = new ConcurrentHashMap<>();

    // Store message metadata by authorId
    private final Map<Long, MessageMetadata> messageMetadata = new ConcurrentHashMap<>();

    /**
     * Register a pending request for an author
     */
    public CompletableFuture<AuthorDTO> registerPendingRequest(Long authorId) {
        CompletableFuture<AuthorDTO> future = new CompletableFuture<>();
        pendingResponses.put(authorId, future);
        return future;
    }

    /**
     * Complete a pending request with the received data
     */
    public void completeRequest(Long authorId, AuthorDTO authorData) {
        CompletableFuture<AuthorDTO> future = pendingResponses.get(authorId);
        if (future != null) {
            future.complete(authorData);
            pendingResponses.remove(authorId);
        }
    }

    /**
     * Store message metadata for an author
     */
    public void storeMessageMetadata(Long authorId, String exchange, String name,
                                     String routingKey, String url, String objects) {
        MessageMetadata metadata = new MessageMetadata(exchange, name, routingKey, url, objects);
        messageMetadata.put(authorId, metadata);
    }

    /**
     * Get message metadata for an author
     */
    public MessageMetadata getMessageMetadata(Long authorId) {
        return messageMetadata.get(authorId);
    }

    /**
     * Check if there's a pending request for the given authorId
     */
    public boolean hasPendingRequest(Long authorId) {
        return pendingResponses.containsKey(authorId);
    }

    /**
     * Inner class to store message metadata
     */
    public static class MessageMetadata {
        private String exchange;
        private String name;
        private String routingKey;
        private String url;
        private String objects;

        public MessageMetadata(String exchange, String name, String routingKey, String url, String objects) {
            this.exchange = exchange;
            this.name = name;
            this.routingKey = routingKey;
            this.url = url;
            this.objects = objects;
        }

        // Getters
        public String getExchange() { return exchange; }
        public String getName() { return name; }
        public String getRoutingKey() { return routingKey; }
        public String getUrl() { return url; }
        public String getObjects() { return objects; }
    }
}