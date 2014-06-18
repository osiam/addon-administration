package org.osiam.addons.administration.config;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.osiam.addons.administration.selenium.Browser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration is responsible for creating a {@link Browser} instance.
 */
@Configuration
public class SeleniumFactory {

    @Inject
    Browser browser;
    
    @Bean
    Browser browser(){
        WebDriver driver;
        
        try{
            driver = new InternetExplorerDriver();
        }catch(Exception e){
            try{
                driver = new SafariDriver();
            }catch(Exception e1){
                try{
                    driver = new FirefoxDriver();
                }catch(Exception e2){
                    driver = new HtmlUnitDriver(true);
                }
            }
        }
        
        Browser browser = new Browser(driver);
        return browser;
    }
    
    @PreDestroy
    void closeBrowser(){
        browser.quit();
    }
}
