package io.github.nosequel.practice.handler;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public class HandlerManager {

    private final Map<Class<? extends Handler>, Handler> handlers = new HashMap<>();

    /**
     * Constructor for making a new handler manager instance and automatically registering all handlers provided in the parameters
     *
     * @param handlers the handlers to register
     */
    public HandlerManager(Handler... handlers) {
        Arrays.stream(handlers).forEach(handler -> this.handlers.put(handler.getClass(), handler));
    }

    /**
     * Load all registered handlers
     */
    public void load() {
        this.handlers.values().forEach(Handler::load);
    }

    /**
     * Unload all registered handlers
     */
    public void unload() {
        this.handlers.values().forEach(Handler::unload);
    }

    /**
     * Method for finding a {@link Handler} from a {@link Class}
     *
     * @param clazz the class
     * @param <T>   the type of the handler
     * @return the found handler or null
     */
    public <T extends Handler> T find(Class<? extends T> clazz) {
        return clazz.cast(this.handlers.get(clazz));
    }

    /**
     * Method for registering a {@link Handler}
     *
     * @param handler the handler to register
     * @param <T>     the type of the handler to register
     */
    public <T extends Handler> void register(T handler) {
        this.handlers.put(handler.getClass(), handler);
    }
}