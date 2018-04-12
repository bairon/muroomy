package com.alsa.muroomy.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by alsa on 27.02.2018.
 */
public class RoomTest {
    private Room room;

    @Before
    public void setUp() throws Exception {
        room = new Room(5);
    }

    @Test
    public void reset() throws Exception {
        room.reset();
        System.out.println(room);
    }

    @Test
    public void layoutCards() throws Exception {
        room.reset();
        room.layoutCards();
        System.out.println(room);
    }

}