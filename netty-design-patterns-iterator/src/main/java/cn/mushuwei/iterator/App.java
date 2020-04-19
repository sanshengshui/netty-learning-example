package cn.mushuwei.iterator;

import cn.mushuwei.iterator.bst.BstIterator;
import cn.mushuwei.iterator.bst.TreeNode;
import cn.mushuwei.iterator.list.Item;
import cn.mushuwei.iterator.list.ItemType;
import cn.mushuwei.iterator.list.TreasureChest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static cn.mushuwei.iterator.list.ItemType.*;

/**
 * @author james mu
 * @date 2020/4/18 21:59
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private static final TreasureChest TREASURE_CHEST = new TreasureChest();

    private static void demonstrateTreasureChestIteratorForType(ItemType itemType) {
        LOGGER.info("------------------------");
        LOGGER.info("Item Iterator for ItemType " + itemType + ": ");
        Iterator<Item> itemIterator = TREASURE_CHEST.iterator(itemType);
        while (itemIterator.hasNext()) {
            LOGGER.info(itemIterator.next().toString());
        }
    }

    private static void demonstrateBstIterator() {
        LOGGER.info("------------------------");
        LOGGER.info("BST Iterator: ");
        TreeNode<Integer> root = buildIntegerBst();
        BstIterator<Integer> bstIterator = new BstIterator<Integer>(root);
        while (bstIterator.hasNext()) {
            LOGGER.info("Next node: " + bstIterator.next().getVal());
        }
    }

    private static TreeNode<Integer> buildIntegerBst() {
        TreeNode<Integer> root = new TreeNode<>(8);

        root.insert(3);
        root.insert(10);
        root.insert(1);
        root.insert(6);
        root.insert(14);
        root.insert(4);
        root.insert(7);
        root.insert(13);

        return root;
    }

    /**
     * Program entry point.
     *
     * @param args command line args
     */
    public static void main(String[] args) {
        demonstrateTreasureChestIteratorForType(RING);
        demonstrateTreasureChestIteratorForType(POTION);
        demonstrateTreasureChestIteratorForType(WEAPON);
        demonstrateTreasureChestIteratorForType(ANY);

        demonstrateBstIterator();
    }
}
