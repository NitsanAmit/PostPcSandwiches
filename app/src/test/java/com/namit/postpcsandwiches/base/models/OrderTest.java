package com.namit.postpcsandwiches.base.models;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
public class OrderTest {

    @Test
    public void when_incrementingOrDecrementPickles_shouldNotifyListenerIfAttached() {
        Order order = new Order();
        OnOrderChangedListener listener = Mockito.mock(OnOrderChangedListener.class);
        order.setOnPicklesChangedListener(listener);
        Mockito.verify(listener, Mockito.never()).onChange();
        order.incrementPickles();
        Mockito.verify(listener, Mockito.times(1)).onChange();
        order.decrementPickles();
        Mockito.verify(listener, Mockito.times(2)).onChange();
    }

    @Test
    public void when_attemptToIncrementPicklesAboveTen_shouldNotIncrement() {
        Order order = new Order();
        order.pickles = 9;
        order.incrementPickles();
        Assert.assertEquals(10, order.getPickles());
        order.incrementPickles();
        Assert.assertEquals(10, order.getPickles());
        order.incrementPickles();
        order.incrementPickles();
        order.incrementPickles();
        Assert.assertEquals(10, order.getPickles());
    }

}