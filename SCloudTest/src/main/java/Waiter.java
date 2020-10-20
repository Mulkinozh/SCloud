import org.openqa.selenium.WebElement;

public class Waiter {
    public static void waitForInvisibility(WebElement element, int timeout){
        Long startTime = System.currentTimeMillis();
        try {
            while (System.currentTimeMillis() - startTime < timeout * 1000 && element.isDisplayed()) {}
        } catch (Exception e) {
            return;
        }
    }

    public static void waitForVisibility(WebElement element, int timeout){
        Long startTime = System.currentTimeMillis();
        try {
            while (System.currentTimeMillis() - startTime < timeout * 1000 && !element.isDisplayed()) { }
        } catch (Exception e) {}
        return;
    }
}