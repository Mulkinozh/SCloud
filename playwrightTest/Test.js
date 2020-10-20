const { assert } = require('console');
const { chromium } = require('playwright');

const userName = "sc1009259";
const password = "H0MiLy5Jn3";
const URL = "https://scloud.ru/";

class Browser{
  constructor(browser)
  {
    this.browser = browser
  }

  get = async function(){
    if (this.process == undefined)
      this.process = await this.browser.launch({ headless: false, slowMo: 50 });
    return this.process;
  }

  close = async function () {
    if (this.process != undefined && this.process._isClosedOrClosing != true)
      await this.process.close();
  }
}

class Page{
  constructor(browser, url){
    this.browser = browser;
    this.url = url;
  }

  navigate = async function(){
    let page = await this.get();
    await page.goto(this.url);
  }

  get = async function(){
    if (this.page == undefined){
      let browser = await this.browser.get()
      this.page = await browser.newPage();
    }
    return this.page;
  }
}

class Control{
  constructor(parent, locator){
    this.parent = parent;
    this.locator = locator;
  }

  click = async function () {
    let pageProc = await this.parent.page.get()
    await pageProc.click(this.locator)
  }

  fill = async function (text) {
    let pageProc = await this.parent.page.get();
    await pageProc.fill(this.locator, text);
  }

  checkAttrib = async function  (attr, expectedValue) {
    let pageProc = await this.parent.page.get();
    let actualvalue = await pageProc.getAttribute(this.locator, attr);
    assert(expectedValue === actualvalue);
  }
}

class HeaderPanel{
  constructor(page)
  {
    this.page = page
  }

  login = async function (){
    await this.loginMenuButton.click();
    await this.loginEdit.fill(userName);
    await this.passwordEdit.fill(password);
    await this.loginButton.click();
  }

  loginMenuButton = new Control(this, 'button[class="btn btnIcoAndText ptCaption headerTop__loginBtn "]');
  loginEdit = new Control(this, 'input[name="USER_LOGIN"]');
  passwordEdit = new Control(this, 'input[name="USER_PASSWORD"]');
  loginButton = new Control(this, 'button[class="btn btnBackground  js-btnLogin "]');
} 

class MainNavigationPanel{
  constructor(page)
  {
    this.page = page
  }

  commonInformationBtnLocator = "index";
  my1SBasesLocator = "bases";

  clickBtn = async function (button) {
    let btn = new Control(this, `a[href="/cabinet/${button}"]`);
    await btn.click();
  }
}

class CommonInformationForm{
  constructor(page)
  {
    this.page = page
  }

  accessTo1cBtn = new Control(this, 'a[analyticscode="accessTo1cBtn"');
  otherAccessAct = new Control(this, 'button[class="btn btn-show-full"]');

  AccessBtns = ["Windows", "Mac", "iOS", "Android", "Linux"]   
  clickAccessBtn = async function (btnId){
    const actn = new Control(this, 'text='+this.AccessBtns[btnId])
    await actn.click();
  }
  
}

async function AccessTo1cDownloadLink (browserConst) {
  browser = ObjectsFactory.getBrowser(browserConst);
  mainPage = ObjectsFactory.getMainPage(browser);
  var headerPanel = new HeaderPanel(mainPage);
  var mainNavigationPanel = new MainNavigationPanel(mainPage);
  var commonInformationForm = new CommonInformationForm(mainPage);
  
  await mainPage.navigate();
  await headerPanel.login();
  await mainNavigationPanel.clickBtn(mainNavigationPanel.commonInformationBtnLocator);
  await commonInformationForm.accessTo1cBtn.checkAttrib("href", "/distrib/1C-Scloud.ru-Setup.exe");
  await browser.close();
};

async function CheckOtherAccessActions (browserConst) {
  browser = ObjectsFactory.getBrowser(browserConst);
  mainPage = ObjectsFactory.getMainPage(browser);
  var headerPanel = new HeaderPanel(mainPage);
  var mainNavigationPanel = new MainNavigationPanel(mainPage);
  var commonInformationForm = new CommonInformationForm(mainPage);
  
  await mainPage.navigate();
  await headerPanel.login();
  await mainNavigationPanel.clickBtn(mainNavigationPanel.commonInformationBtnLocator);
  await commonInformationForm.otherAccessAct.click();
  await (async () => {
    for (let i = 0; i< commonInformationForm.AccessBtns.length; i++){
      await commonInformationForm.clickAccessBtn(i)}})();
  await browser.close();
};

ObjectsFactory ={
  getBrowser: (browserConst) => new Browser(browserConst),
  getMainPage: (browser) => new Page(browser, URL)
};

(async () => {
  await AccessTo1cDownloadLink(chromium)
  await CheckOtherAccessActions(chromium) 
})();

