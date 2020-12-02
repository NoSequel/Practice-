package io.github.nosequel.practice.queue;

import io.github.nosequel.practice.arena.Arena;
import io.github.nosequel.practice.kit.Kit;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class QueueEntry<T> {

    private final T object;
    private final Queue<?> queue;
    private final Arena arena;
    private final Kit kit;

}