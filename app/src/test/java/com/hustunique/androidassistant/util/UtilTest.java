package com.hustunique.androidassistant.util;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by CoXier on 17-6-15.
 */
public class UtilTest {
    @Test
    public void constructTimestamp() throws Exception {
        assertEquals("20175",Util.constructTimestamp());
    }
}