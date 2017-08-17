// Copyright (c) 1998-2017 Core Solutions Limited. All rights reserved.
// ============================================================================
// CURRENT VERSION CNT.5.0.1
// ============================================================================
// CHANGE LOG
// CNT.5.0.1 : 2017-XX-XX, simba.lin, creation
// ============================================================================
package radix;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
/**
 * @author simba.lin
 *
 */
public class TestRadixTransformation {

    @Test
    public void testNumberToBinaryString() {
        final String bs = Integer.toBinaryString(6);
        final String ls = Long.toBinaryString(6L);
        System.out.println("only integer/long can convert to binary");
        System.out.println("" + 6 + " to Binary:"+ bs);
        System.out.println("" + 6L + " to Binary:"+ ls);
        assertEquals("110",ls);
        assertEquals("110",bs);
    }

    @Test
    public void testBinaryStringToHexString() {
        final String hex = Integer.toHexString(100);
        System.out.println("100 to hex :"+ hex);
        assertEquals(hex, "64");
    }

    @Test
    public void testBinaryStringToOctString() {
        final String oct = Integer.toOctalString(20);
        final String octL =  Long.toOctalString(20L);
        System.out.println("20 to Oct:");
        System.out.println(oct);
        System.out.println(octL);
    }

    @Test
    public void testBinaryStringToNumber() {
        final int num = Integer.parseInt("110", 2);
        final long longNum = Long.parseLong("110", 2);
        assertEquals(num, 6);
        assertEquals(longNum, 6L);
    }

    @Test
    public void testUnsignedNumToBinary() {
        final String num = Integer.toBinaryString(-1);
        System.out.println("-1 to binary:" + num);
    }

}
