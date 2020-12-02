package io.github.nosequel.practice.queue;

import io.github.nosequel.practice.handler.Handler;
import io.github.nosequel.practice.queue.type.SoloQueue;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class QueueHandler implements Handler {

    private final List<Queue<?>> queues = new ArrayList<>();

    @Override
    public void load() {
        this.queues.add(new SoloQueue());
    }

    @Override
    public void unload() {

    }

    /**
     * Find a queue by a class
     *
     * @param clazz the class of the queue
     * @param <T>   the type of the queue
     * @return the queue or null
     */
    @SuppressWarnings("unchecked")
    public <T> Queue<T> findQueue(Class<? extends T> clazz) {
        return (Queue<T>) this.queues.stream()
                .filter(queue -> queue.getType().equals(clazz))
                .findFirst().orElse(null);
    }

    /**
     * Unqueue a player
     *
     * @param player the player to unqueue
     */
    public void unqueue(Player player) {
        this.getQueue(player).getQueue().removeEntry(player);
    }

    /**
     * Get the queue of a player
     *
     * @param player the player
     * @return the queue found or null
     */
    public QueueEntry<?> getQueue(Player player) {
        return this.queues.stream()
                .map(queue -> queue.getEntry(player))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }
}
