import io.github.bonigarcia.wdm.config.DriverManagerType;
import io.github.bonigarcia.wdm.managers.FirefoxDriverManager;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Tests {

    static String userName = "sc1004641";
    static String password = "Sdg3WiVBtT";
    static String URL = "http://scloud.ru";


    public static WebDriver init(){
        FirefoxDriverManager.getInstance(DriverManagerType.FIREFOX).setup();
        WebDriver x = new FirefoxDriver();
        x.get("http://scloud.ru");
        x.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return x;
    }

    @Test
    public void currentSessionsActionTest()
    {
        WebDriver x = init();
        MainPage mp = new MainPage(x);
        mp.logIn(userName, password);
        mp.mainNavPanel.clickElement(mp.mainNavPanel.commonInformation);
        List<WebElement> quickOptions = mp.commonInfoPage.getAllQuickOptions();
        WebElement currentSessionsActn = quickOptions.get(0);
        currentSessionsActn.click();
        mp.waitForLoading();
        mp.popup.closePopUp();
        x.close();
    }

    @Test
    public void createAndDeleteEmptyFileDataBaseTest()
    {
        WebDriver x = init();
        MainPage mp = new MainPage(x);
        mp.logIn(userName, password);
        mp.mainNavPanel.clickElement(mp.mainNavPanel.administration);
        mp.adminPage.getDataBaseCreateBtn().click();
        mp.adminPage.createDBWizard.clickNext();
        mp.adminPage.createDBWizard.selectRadioItemById(1);
        mp.adminPage.createDBWizard.clickNext();
        mp.adminPage.createDBWizard.fillDBName("testDB");
        mp.adminPage.createDBWizard.clickNext();
        mp.popup.closeInfoDialog();
        mp.adminPage.clickContextMenuActnById("testDB", 1);
        mp.popup.confirm();
        x.close();
    }

    @Test
    public void createAndDeleteUserTest()
    {
        WebDriver x = init();
        MainPage mp = new MainPage(x);
        mp.logIn(userName, password);
        mp.mainNavPanel.clickElement(mp.mainNavPanel.administration);
        mp.waitForLoading();
        mp.adminPage.addDefaultUser();
        mp.adminPage.deleteDefaultUser();
        x.close();
    }

    @Test
    public void useInvalidPromoCodeTest(){
        WebDriver x = init();
        MainPage mp = new MainPage(x);
        mp.logIn(userName, password);
        mp.mainNavPanel.clickElement(mp.mainNavPanel.payment);
        mp.paymentPage.activatePromocode("testCode");
        System.out.println(mp.popup.getInfoText());
        mp.popup.closeInfoDialog();
        x.close();
    }

    @Test
    public void createRenameDeleteSafeFolderTest(){
        String folderName = "testFolder";
        WebDriver x = init();
        MainPage mp = new MainPage(x);
        mp.logIn(userName, password);
        mp.mainNavPanel.clickElement(mp.mainNavPanel.administration);
        mp.waitForLoading();
        mp.adminPage.addFolder(folderName);
        mp.adminPage.clickContextMenuActnById("testFolder", 1);
        mp.popup.fillField("Changed");
        mp.popup.clickNext();
        mp.popup.closeInfoDialog();
        mp.adminPage.deleteFolder(folderName+"Changed");
        x.close();
    }


    @Test
    public void AssignNewFolderToUserTest(){
        String folderName = "testFolder";
        WebDriver x = init();
        MainPage mp = new MainPage(x);
        mp.logIn(userName, password);
        mp.mainNavPanel.clickElement(mp.mainNavPanel.administration);
        mp.waitForLoading(15);
        mp.adminPage.addFolder(folderName);
        mp.adminPage.addDefaultUser();
        //Assign folder to user
        mp.adminPage.clickContextMenuActnById("dop", 1);
        mp.popup.clickCheckBox(folderName);
        mp.popup.clickNext();
        mp.waitForLoading(15);
        mp.popup.closeInfoDialog();

        mp.adminPage.deleteDefaultUser();
        mp.adminPage.deleteFolder(folderName);
        x.close();
    }
}
