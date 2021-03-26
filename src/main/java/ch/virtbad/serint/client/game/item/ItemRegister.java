package ch.virtbad.serint.client.game.item;

import ch.virtbad.serint.client.game.GameContext;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Virt
 */
@Slf4j
public class ItemRegister {

    private HashMap<Integer, Item> items;

    private ArrayList<Integer> toInit;
    private ArrayList<Integer> toDestroy;

    private final GameContext context;

    public ItemRegister(GameContext context) {
        this.context = context;
        items = new HashMap<>();
        toInit = new ArrayList<>();
        toDestroy = new ArrayList<>();
    }

    /**
     * Adds a new item to the register
     *
     * @param item item to add
     *
     * @param id item id
     */


    public void add(Item item, int id) {
        log.info("Item with id {} added", id);
        item.setContext(context);
        items.put(id, item);
        toInit.add(id);
    }

    /**
     * Returns the item with the id
     *
     * @param id id of the item
     */

    public Item get(int id) {
        return items.get(id);
    }

    /**
     * Removes an item from the register
     *
     * @param id item to remove
     */

    public void remove(int id) {
        toDestroy.add(id);
    }

    /**
     * Updates all items
     *
     * @param delta delta time
     */

    public void update(float delta) {

        // Initialize uninitialized Things
        if (toInit.size() != 0) {
            for (Integer integer : toInit) {
                items.get(integer).init();
            }

            toInit.clear();
        }

        // Destroy uninitialized Things
        if (toDestroy.size() != 0) {
            for (Integer integer : toDestroy) {
                items.remove(integer).destroy();
            }

            toDestroy.clear();
        }

        // Update Objects
        for (Item value : items.values()) {
            value.update(delta);
        }
    }

    public void draw() {
        for (Item value : items.values()) {
            value.draw();
        }
    }

}
