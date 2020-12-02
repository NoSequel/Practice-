package io.github.nosequel.practice.queue;

import io.github.nosequel.practice.arena.Arena;
import io.github.nosequel.practice.kit.Kit;
import org.bukkit.entity.Player;

import java.util.List;

public interface Queue<T> {

    /**
     * Get all entries inside of the queue
     *
     * @return the entries
     */
    List<QueueEntry<T>> getEntries();

    /**
     * Get entries by kit
     *
     * @param kit the kit
     * @return the entries
     */
    List<QueueEntry<T>> getEntries(Kit kit);

    /**
     * Add an entry to the queue
     *
     * @param entry the entry
     */
    void addToQueue(T entry, Kit kit, Arena arena);

    /**
     * Remove an entry from the queue
     *
     * @param entry the entry
     */
    void removeEntry(QueueEntry<T> entry);

    /**
     * Remove a player from the queue
     *
     * @param player the player
     */
    void removeEntry(Player player);

    /**
     * Get the queue entry of a player
     *
     * @param player the player
     * @return the queue entry
     */
    QueueEntry<T> getEntry(Player player);

    /**
     * Get the type of the queue
     *
     * @return the class type
     */
    Class<T> getType();

}