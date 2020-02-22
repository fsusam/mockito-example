package com.ait.retailers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BiggestBazarRetailTest {

    private final Inventory inventory = mock(Inventory.class);
    private final PublicAddressSystem pas = mock(PublicAddressSystem.class);
    private BiggestBazarRetail biggestBazarRetail;
    private Item item;

    @BeforeEach
    public void setup() {
        biggestBazarRetail = new BiggestBazarRetail(inventory, pas);
    }

    @Test
    public void discount_is_issued_on_one_item() {
        item = new Item("1111", "Item1", 100.0, 80.0);
        when(inventory.getItemsExpireInAMonth()).thenReturn(new ArrayList<Item>(Arrays.asList(item)));
        when(inventory.itemsUpdated()).thenReturn(1);
        final int updatedItems = biggestBazarRetail.issueDiscountForItemsExpireIn30Days(0.1);
        verify(inventory).update(item, 90);
        verify(pas).announce(any(Offer.class));
        assertEquals(updatedItems, 1);
    }

    @Test
    public void no_Item_qualifies_for_a_discount_if_the_discounted_price_is_less_than_the_base_price() {
        item = new Item("1111", "Item1", 100.0, 80.0);
        when(inventory.getItemsExpireInAMonth()).thenReturn(new ArrayList<Item>(Arrays.asList(item)));
        when(inventory.itemsUpdated()).thenReturn(1);
        final int updatedItems = biggestBazarRetail.issueDiscountForItemsExpireIn30Days(0.3);
        verify(inventory, never()).update(any(Item.class), any(Double.class));
        verify(pas, never()).announce(any(Offer.class));
        assertEquals(updatedItems, 1);
    }

    @Test
    public void two_Items_qualifies_for_a_discount() {
        final ArrayList<Item> items = new ArrayList<Item>();
        items.add(new Item("1111", "Item1", 100.0, 80.0));
        items.add(new Item("1111", "Item1", 100.0, 60.0));
        items.add(new Item("1111", "Item1", 100.0, 50.0));

        when(inventory.getItemsExpireInAMonth()).thenReturn(items);
        when(inventory.itemsUpdated()).thenReturn(3);
        final int updatedItems = biggestBazarRetail.issueDiscountForItemsExpireIn30Days(0.3);
        verify(inventory, times(2)).update(any(Item.class), any(Double.class));
        verify(pas, times(2)).announce(any(Offer.class));
        assertEquals(updatedItems, 3);
    }
}
