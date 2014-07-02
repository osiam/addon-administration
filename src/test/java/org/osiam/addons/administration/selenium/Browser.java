package org.osiam.addons.administration.selenium;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.osiam.addons.administration.Element;
import org.osiam.addons.administration.Element.OauthLogin;
import org.osiam.addons.administration.controller.LoginController;

/**
 * This is an advanced {@link WebDriver}! This class contains more useful functions.
 */
public class Browser implements WebDriver {
    private final WebDriver delegate;

    private String baseURL;

    public Browser(WebDriver driver) {
        this.delegate = driver;
    }

    /**
     * Set the base-url. This url is used by {@link Browser#gotoPage(String)}!
     * 
     * @param baseURL
     *        The base-url of the administration-servlet.
     * @return this
     */
    public Browser setBaseURL(String baseURL) {
        this.baseURL = baseURL;

        return this;
    }

    /**
     * Go to a specific sub-page of the administration-servlet. This function requires that the base-url was previously
     * set by {@link Browser#setBaseURL(String)}.
     * 
     * @param page
     *        Where should be navigated.
     * @return this
     */
    public Browser gotoPage(String page) {
        String target = baseURL + "/" + page;
        target = target.replaceAll("/{2,}", "/");

        get(target);

        return this;
    }

    /**
     * Login via OAuth2-Mechanism from Osiam.
     * 
     * @param username
     *        Username that should be used.
     * @param password
     *        Password that should be used.
     * @return this
     */
    public Browser doOauthLogin(String username, String password) {
        gotoPage(LoginController.CONTROLLER_PATH);

        if (!getCurrentUrl().contains("osiam-auth-server")) {
            // always logged in
            return this;
        }

        try {
            fill(new Field(OauthLogin.Username, username),
                    new Field(OauthLogin.Password, password));

            click(OauthLogin.LoginButton);
        } catch (NoSuchElementException e) {
            // maybe we can it ignore, because osiam save it in his session
        }

        click(OauthLogin.AuthorizeButton);

        return this;
    }

    /**
     * Click on the given element.
     * 
     * @param element
     *        Element which should be clicked.
     * @return this
     */
    public Browser click(Element element) {
        findElement(element).click();

        return this;
    }

    /**
     * Return the element if it was found.
     * 
     * @param element
     * @return this
     */
    public WebElement findElement(Element element) {
        return findElement(element.by());
    }

    public Select findSelectElement(Element element) {
        return new Select(findElement(element));
    }
    
    public WebElement findSelectedOption(Element element) {
        return findSelectElement(element).getFirstSelectedOption();
    }

    /**
     * Fill all fields on the current page.
     * 
     * @param fields
     *        Fields that should be filled.
     * @return this
     */
    public Browser fill(List<Field> fields) {
        for (Field f : fields) {
            WebElement webElement = findElement(f.getElement());

            if ("select".equalsIgnoreCase(webElement.getTagName())) {
                selectOption(webElement, f.getValue());
            } else {
                webElement.clear();
                webElement.sendKeys(String.valueOf(f.getValue()));
            }
        }

        return this;
    }

    private void selectOption(WebElement selectElement, Object value) {
        Select select = new Select(selectElement);
        try {
            select.selectByValue(String.valueOf(value));
        } catch (NoSuchElementException e) {
            select.selectByVisibleText(String.valueOf(value));
        }
    }

    /**
     * {@link Browser#fill(List)}
     */
    public Browser fill(Field... fields) {
        return fill(Arrays.asList(fields));
    }

    /**
     * Is the access for the current page denied?
     * 
     * @return True if it so. Otherwise false.
     */
    public boolean isAccessDenied() {
        return isTextPresent("Access Denied");
    }

    /**
     * Check if the given text is shown on the current page.
     * 
     * @param text
     *        Text to check.
     * @return True if the text is present. Otherwise false.
     */
    public boolean isTextPresent(String text) {
        return findElements(
                By.xpath("//*[contains(normalize-space(text()), '" + text + "') or contains(normalize-space(.), '"
                        + text + "')]")).size() > 0;
    }

    // //
    // Delegate methods...
    // //

    public void close() {
        delegate.close();
    }

    public WebElement findElement(By arg0) {
        return delegate.findElement(arg0);
    }

    public List<WebElement> findElements(By arg0) {
        return delegate.findElements(arg0);
    }

    public void get(String arg0) {
        delegate.get(arg0);
    }

    public String getCurrentUrl() {
        return delegate.getCurrentUrl();
    }

    public String getPageSource() {
        return delegate.getPageSource();
    }

    public String getTitle() {
        return delegate.getTitle();
    }

    public String getWindowHandle() {
        return delegate.getWindowHandle();
    }

    public Set<String> getWindowHandles() {
        return delegate.getWindowHandles();
    }

    public Options manage() {
        return delegate.manage();
    }

    public Navigation navigate() {
        return delegate.navigate();
    }

    public void quit() {
        delegate.quit();
    }

    public TargetLocator switchTo() {
        return delegate.switchTo();
    }
}
