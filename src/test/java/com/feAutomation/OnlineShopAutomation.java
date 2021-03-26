package com.feAutomation;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.util.List;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OnlineShopAutomation {
	
	static ChromeDriver driver;
	private static String path = "D:\\chromeDriver\\chromedriver_win32\\chromedriver.exe";
	private static String id = null;
	private static String amount = null;
	
	@SuppressWarnings({ "static-access", "deprecation" })
	public static void initialization(){
		
		System.setProperty("webdriver.chrome.driver",path );
		DesiredCapabilities capabilities = new DesiredCapabilities().chrome();
		
		try {
        driver = new ChromeDriver(capabilities);
		}catch(Exception e) {
			System.out.println("Exception while instantiating driver.");
		}
	}
	
	public static void navigateHomePage() throws InterruptedException {
		driver.manage().window().maximize();
		driver.get("https://www.demoblaze.com/index.html");
		Thread.sleep(8000);
	}
	
	public static void getCategory(String category) throws InterruptedException {
		List<WebElement> categories =  driver.findElementsByXPath("//*[@id=\"itemc\"]");
		for (WebElement webElement : categories) {
			if(webElement.getText().equals(category)) {
				webElement.click();
				return;
			}
		}
	}
	
	
	public static void getLaptopByName(String name) throws InterruptedException {
		
		List<WebElement> categories =  driver.findElementsByXPath("//*[@id=\"tbodyid\"]/div");
		for (WebElement webElement : categories) {
			WebElement element = webElement.findElement(By.xpath(".//*[@class=\"card-title\"]"));
			
			if(element.getText().equals(name)) {
				element.click();
				
				Thread.sleep(5000);
				driver.findElementByXPath("//*[@class=\"btn btn-success btn-lg\"]").click();
				
				Thread.sleep(5000);
				driver.switchTo().alert().accept();
				return;
			}
		}
	}
	
	public static void deleteProductByName(String name) throws InterruptedException {
		List<WebElement> categories =  driver.findElementsByXPath("//*[@id=\"tbodyid\"]/tr");
		for (WebElement webElement : categories) {
			WebElement element = webElement.findElement(By.xpath("./td[2]"));
			if(element.getText().equals(name)) {
				webElement.findElement(By.xpath("./td[4]/a")).click();
				return;
			}
		}
	}
	
	public static void scrollAndClick(By by)
	{
	   WebElement element = driver.findElement(by);
	   int elementPosition = element.getLocation().getY();
	   String js = String.format("window.scroll(0, %s)", elementPosition);
	   ((JavascriptExecutor)driver).executeScript(js);
	   element.click();
	}
	
	public static void placeOrder() throws InterruptedException {
		driver.findElementByXPath("//*[@class=\"btn btn-success\"]").click();
		Thread.sleep(5000);
		
		driver.findElementById("name").sendKeys("Aayush Jain");
		driver.findElementById("country").sendKeys("India");
		driver.findElementById("city").sendKeys("Delhi");
		driver.findElementById("card").sendKeys("xxxx-xxxx-xxxx");
		driver.findElementById("month").sendKeys("August");
		driver.findElementById("year").sendKeys("2020");
		
		Thread.sleep(5000);
		scrollAndClick(By.xpath("//*[@id=\"orderModal\"]/div/div/div[3]/button[2]"));
		
		Thread.sleep(5000);
		String data = driver.findElementByXPath("/html/body/div[10]/p").getText();
		String[] info = data.split("\\r?\\n");
		
		for (String string : info) {
			String[] attributes = string.split(":");
			if(attributes[0].equals("Id"))
				id = attributes[1];
			else if(attributes[0].equals("Amount"))
				amount = attributes[1].trim();
		}
		AssertJUnit.assertEquals(amount, "790 USD");
		
		Thread.sleep(3000);
		driver.findElementByXPath("/html/body/div[10]/div[7]/div/button").click();
		
	}

	public static void addLaptop(String name) throws InterruptedException {
		
		getCategory("Laptops");
		Thread.sleep(3000);
		
		getLaptopByName(name);
		Thread.sleep(5000);
		
		driver.findElementByXPath("//*[@id=\"navbarExample\"]/ul/li[1]/a").click();
		Thread.sleep(5000);
	}
	
	@Test
	public void run() throws InterruptedException {
		
		initialization();
		navigateHomePage();
		
		addLaptop("Sony vaio i5");
		addLaptop("Dell i7 8gb");
		
		driver.findElementById("cartur").click();
		Thread.sleep(5000);
		
		deleteProductByName("Dell i7 8gb");
		Thread.sleep(5000);
		
		placeOrder();
		Thread.sleep(5000);
		
		if (driver != null) {
			driver.close();
            driver.quit();
        }
	}
}
