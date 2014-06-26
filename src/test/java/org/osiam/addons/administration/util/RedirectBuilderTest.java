package org.osiam.addons.administration.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class RedirectBuilderTest {

    @Test
    public void path() {
        String result = new RedirectBuilder().setPath("/path/").build();

        assertEquals("redirect:/path/", result);
    }

    @Test
    public void pathAndQuery() {
        String result = new RedirectBuilder()
                .setPath("/path/")
                .setQuery("param=value")
                .build();

        assertEquals("redirect:/path/?param=value", result);
    }

    @Test
    public void destination() {
        String result = new RedirectBuilder()
                .setDestination("http://localhost:8080/osiam")
                .build();

        assertEquals("redirect:http://localhost:8080/osiam", result);
    }
}
