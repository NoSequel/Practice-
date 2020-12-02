package io.github.nosequel.practice.handler;

public interface Handler {

    /**
     * Method called upon loading of all handlers
     */
    void load();

    /**
     * Method called upon unloading of all handlers
     */
    void unload();

}
