package com.chex.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdUtilsTest {



    @Test
    void for_othre_id_return_appropriate_value() {

        assertEquals("EU.POL.MAL.KRK", IdUtils.extractId("EU.POL.MAL.KRK.00003"));
        assertEquals("EU.POL.MAL.KRK", IdUtils.extractId("EU.POL.MAL.KRK.00000"));
        assertEquals("EU.POL.MAL", IdUtils.extractId("EU.POL.MAL.000.00000"));
        assertEquals("EU.POL", IdUtils.extractId("EU.POL.000.000.00000"));
        assertEquals("EU", IdUtils.extractId("EU.000.000.000.00000"));
    }
}