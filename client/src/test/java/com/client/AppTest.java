package com.client;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */

    @Test
    public void testAppWithParamsServiceBorrow() {
        // Arrange
        String[] args = { "-s", "Borrow", "--isbn", "ASD233D", "-c", "1" };

        // Act
        App.main(args);

        // Assert
        assertTrue(true);
    }

    @Test
    public void testAppWithParamsService() {
        // Arrange
        String[] args = { "-s", "Borrow", "--isbn", "ASD233D", "-c", "1" };

        // Act
        App.main(args);

        // Assert
        assertTrue(true);
    }
}
