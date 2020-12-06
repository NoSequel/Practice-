package io.github.nosequel.practice.match;

import java.util.UUID;

public interface MatchParticipant<T> {

    /**
     * Get the name of a match participant
     *
     * @return the name
     */
    String getName();

    /**
     * Get the unique identifier of the match participant
     *
     * @return the unique identifier
     */
    UUID getUniqueId();

    /**
     * Get the match participant's original object
     *
     * @return the original object
     */
    T get();

}
