package io.github.nosequel.practice.player;

import io.github.nosequel.practice.handler.Handler;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class ProfileHandler implements Handler {

    private final Map<UUID, Profile> profiles = new HashMap<>();

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    /**
     * Find a profile by their unique identifier
     *
     * @param uuid the unique identifier to find the profile with
     * @return the profile or null
     */
    public Profile findProfile(UUID uuid) {
        return this.profiles.get(uuid);
    }
}
