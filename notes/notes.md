

### Do not forget
- Sync conversations to the backend, when user moves to a new device they have the same conversions and participants. Note, these things can change.

### Where the "Real" Crypto TODOs live
Since you are connecting the pieces, keep these three "Anchor Points" in your notes for when we revisit E2EE:

- Auth Flow: When the user logs in, we'll need to generate their Identity Keys and upload Pre-keys to the server.
- Conversation Start: When a new chat is created, the SendMessageUseCase will need to check if a session exists and trigger the Handshake if it doesn't.
- Database: We will eventually need a SessionEntity table in SQLDelight to store the "Ratchet State" (the moving keys) for every conversation.

### Back-off for failed sync
Include the incremental backoff later.
ChatDatabase.sq
```sqldelight
-- Move this to the Entity proper
ALTER TABLE MessageEntity ADD COLUMN retryCount INTEGER NOT NULL DEFAULT 0;
ALTER TABLE MessageEntity ADD COLUMN lastRetryAt INTEGER;

-- Update the query to only get messages that aren't "cooling down"
getPendingMessages:
SELECT * FROM MessageEntity
WHERE status = 'SENDING'
AND (lastRetryAt IS NULL OR lastRetryAt < :currentTime);
```

Kotlin
```kotlin
catch (cause: Exception) {
    val currentRetry = entity.retryCount + 1
    val nextRetryAt = Clock.System.now() + (pow(2, currentRetry)).seconds // Exponential 2, 4, 8, 16...
    
    database.transaction {
        queries.updateRetryMetadata(
            localId = messageId,
            retryCount = currentRetry,
            lastRetryAt = nextRetryAt.toEpochMilliseconds()
        )
    }
    return DomainResult.Failed(...)
}
```