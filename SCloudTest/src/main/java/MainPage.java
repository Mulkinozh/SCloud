import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;

import java.util.List;



public class MainPage {

    WebDriver driver;

    private WebElement header;
    public MainNavigationPanel mainNavPanel = new MainNavigationPanel();
    public CommonInformationPage commonInfoPage = new CommonInformationPage();
    public AdministrationPage adminPage = new AdministrationPage();
    public PaymentPage paymentPage = new PaymentPage();
    public Popup popup = new Popup();

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getHeader() {
        if (header == null) {
            header = driver.findElement(By.xpath("//header[@class='header']"));
        }
        return header;
    }

    public void clickEnterAction() {
        WebElement element = getHeader().findElement(By.xpath("//button[@data-sprite-btn='loginPopup']"));
        element.click();
    }

    public WebElement getLoginField() {
        return getHeader().findElement(By.xpath("//input[@name='USER_LOGIN']"));
    }

    public WebElement getPassword() {
        return getHeader().findElement(By.xpath("//input[@name='USER_PASSWORD']"));
    }

    public WebElement getLoginButton() {
        return getHeader().findElement(By.className("js-btnLogin"));
    }

    public void logIn(String UserName, String Password) {
        clickEnterAction();
        getLoginField().sendKeys(UserName);
        getPassword().sendKeys(Password);
        getLoginButton().click();
    }

    public void waitForLoading() {
        waitForLoading(10);
    }
    public void waitForLoading(int timeOut) {
        Waiter.waitForInvisibility(driver.findElement(By.className("cssload-flex-container")), timeOut);
    }

    public WebElement getContextMenu(WebElement parent, String itemName){
        WebElement dbItem = parent.findElement(By.xpath("//*[contains(text(), '" + itemName + "')]")).findElement(By.xpath("../.."));
        dbItem.findElement(By.className("objectMenu__button")).click();
        return dbItem.findElement(By.className("objectMenu__list"));
    }

    public class MainNavigationPanel {
        String commonInformation = "index";
        String administration = "administration";
        String payment = "pay";

        public WebElement getMainNavPane() {
            WebElement desktopMenu = driver.findElement(By.className("desktopMenu"));
            Waiter.waitForVisibility(desktopMenu, 10);
            return desktopMenu;
        }

        public WebElement getElementByLink(String link) {
            return getMainNavPane().findElement(By.xpath("//a[@href='/cabinet/" + link + "']"));
        }

        public void clickElement(String link) {
            getElementByLink(link).click();
        }
    }

    public class CommonInformationPage {
        private WebElement quickOptionsPanel;

        public WebElement getQuickOptionsPanel() {
            if (quickOptionsPanel == null)
                quickOptionsPanel = driver.findElement(By.className("quickOptions"));
            return quickOptionsPanel;
        }


        public List<WebElement> getAllQuickOptions() {
            return getQuickOptionsPanel().findElements(By.className("quickOptions__quickOption"));
        }
    }

    public class AdministrationPage {
        private WebElement adminPage;
        public CreateDBWizard createDBWizard = new CreateDBWizard();

        public class CreateDBWizard extends Popup {
            public void fillDBName(String name) {
                fillField("//input[@name='baseName']", name);
            }
        }


        public WebElement getAdminPage() {
            return driver.findElement(By.className("administrationPage"));
        }

        public WebElement getDataBaseCreateBtn() {
            return getXXXBtn("base_create");
        }

        public WebElement getUserCreateBtn() {
            return getXXXBtn("createUserBtn");
        }

        public WebElement getSafeFolderCreateBtn(){
            return getXXXBtn("secure_folder_create");
        }

        public WebElement getXXXBtn(String symCode){
            return getAdminPage().findElement(By.xpath("//app-formbutton[@symbolcode='"+symCode+"']")).findElement(By.className("btn-white-orange-mini"));
        }

        public WebElement getMenu(String itemName) {
            return getContextMenu(getAdminPage(), itemName);
        }

        public void clickContextMenuActnById(String itemName, int idx) {
            WebElement actn = getMenu(itemName).findElements(By.className("objectMenu__item")).get(idx);
            Waiter.waitForVisibility(actn, 20);
            actn.click();
        }

        public void selectSubMenuItemById(int idx){
            getAdminPage().findElements(By.className("el")).get(idx).click();
        }

        public void addFolder(String folderName){
            selectSubMenuItemById(2);
            getSafeFolderCreateBtn().click();
            popup.fillField(folderName);
            popup.clickNext();
            popup.closeInfoDialog();
        }

        public void deleteFolder(String folderName){
            selectSubMenuItemById(2);
            clickContextMenuActnById(folderName, 3);
            popup.confirm();
            waitForLoading(15);
            popup.closeInfoDialog();
        }

        public void addDefaultUser(){
            selectSubMenuItemById(0);
            getUserCreateBtn().click();
            popup.clickNext();
            waitForLoading(15);
            popup.closeInfoDialog();
        }

        public void deleteDefaultUser(){
            clickContextMenuActnById("dop", 5);
            popup.confirm();
            waitForLoading(15);
            popup.closeInfoDialog();
        }
    }

    public class PaymentPage{
        public WebElement getPaymentPage(){
            return driver.findElement(By.className("main-content"));
        }

        public void activatePromocode(String code){
            WebElement promoField = getPaymentPage().findElement(By.xpath("//input[@name='promotionalCode']"));
            promoField.click();
            promoField.sendKeys(code);
            WebElement activateBtn = getPaymentPage().findElement(By.className("btn-white-orange"));
            activateBtn.click();
        }
    }

    public class Popup {
        public WebElement getPopUpDialog() {
            return driver.findElement(By.className("popup"));
        }

        public void closePopUp() {
            WebElement closeBtn = getPopUpDialog().findElement(By.className("close"));
            closeBtn.click();
        }

        public WebElement getRadioGroup() {
            return getPopUpDialog().findElement(By.className("popupCreateBase__choice"));
        }

        public void selectRadioItemById(int idx) {
            getRadioGroup().findElements(By.tagName("label")).get(idx).click();
        }

        public void clickNext() {
            getPopUpDialog().findElement(By.className("btn-white-orange")).click();
        }
        public void fillField(String locator, String value){
            WebElement field = getPopUpDialog().findElement(By.xpath(locator));
            field.click();
            field.sendKeys(value);
        }
        public void fillField(String value){
            fillField("//input[@type='text']", value);
        }

        public void confirm() {
            fillField("//input[@name='actionConfirmation']", "ПОДТВЕРДИТЬ");
            clickNext();
        }

        public void clickCheckBox(String caption){
            WebElement cb = getPopUpDialog().findElement(By.xpath("//label[@for='"+caption+"']"));
            Waiter.waitForVisibility(cb, 2);
            cb.click();
        }

        public String getInfoText(){
            return driver.findElement(By.className("swal2-html-container")).getText();
        }

        public void closeInfoDialog()
        {
            driver.findElement(By.className("informationPopupCloseButton")).click();
        }
    }

}
