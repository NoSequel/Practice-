package io.github.nosequel.practice.match.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public class ItemElement {

    private final int index;
    private final ItemStack itemStack;

}
