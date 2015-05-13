package org.osiam.addons.administration.elements;

import org.openqa.selenium.By;

/**
 * This contains all elements that we can access via selenium.
 */
public interface Element {
    By by();

    String name();
}
