package io.github.nosequel.katakuna.button.impl.pagination;

import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.menu.paginated.PaginatedMenu;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.function.BiConsumer;

@Getter
public class PaginationButton implements Button {

    private int index;

    private final int amount = 1;
    private final String displayName;
    private final Material material;
    private final BiConsumer<ClickType, Player> action;
    private final byte data;

    /**
     * Constructor for creating a new PaginationButton
     *
     * @param index the index
     * @param type the type of pagination
     * @param menu the menu
     */
    public PaginationButton(int index, PaginationType type, PaginatedMenu menu) {
        this.data = 0x0;
        this.index = index;
        this.displayName = type.equals(PaginationType.NEXT) ? ChatColor.GRAY + "Next Page" : ChatColor.GRAY + "Previous Page";
        this.material = Material.CARPET;

        this.action = (clickType, humanEntity) -> {
            if (type.equals(PaginationType.NEXT)) {
                menu.setPage(menu.getPage() + 1);
            } else {
                menu.setPage(menu.getPage() - 1);
            }

            menu.updateMenu();
        };
    }

    @Override
    public void index(int index) {
        this.index = index;
    }
}
