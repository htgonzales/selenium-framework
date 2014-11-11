package com.hgdev777.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

/**
 * @author hernan
 * @version 1.0
 * 
 */
public class TestCaseExt {

	private static WebDriver _driver;
	private String baseUrl;
	private int timeout;
	private Dimension dimension;
	private Point point;

	private static final String SLASH = "/";

	protected static final int FOUND = 1;
	protected static final int NOT_FOUND = 0;
	protected static final int ERROR = -1;

	protected static final String LINK_TEXT = "linkText";
	protected static final String ID = "id";
	protected static final String XPATH = "xpath";
	protected static final String CSS_SELECTOR = "cssSelector";

	/*
	 * Constructor and Garbage Collector
	 */

	public TestCaseExt() {
	}

	public void finalize() {
		if (_driver != null)
			_driver.quit();
	}

	/*
	 * Getters
	 */

	public WebDriver getDriver() {
		return _driver;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public int getTimeout() {
		return timeout;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public Point getPoint() {
		return point;
	}

	/*
	 * Setters
	 */

	public boolean setDriver(WebDriver driver) {
		_driver = driver;
		return true;
	}

	public boolean setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		try {
			_driver.get(this.baseUrl + SLASH);
		} catch (Throwable e) {
			return false;
		}
		return true;
	}

	public boolean setTimeout(int timeout) {
		this.timeout = timeout;
		try {
			_driver.manage().timeouts()
					.implicitlyWait(this.timeout, TimeUnit.SECONDS);
		} catch (Throwable e) {
			return false;
		}
		return true;
	}

	public boolean setDimension(Dimension dimension) {
		this.dimension = dimension;
		try {
			_driver.manage().window().setSize(this.dimension);
		} catch (Throwable e) {
			return false;
		}
		return true;
	}

	public boolean setPoint(Point point) {
		this.point = point;
		try {
			_driver.manage().window().setPosition(this.point);
		} catch (Throwable e) {
			return false;
		}
		return true;
	}

	/**
	 * Private Helpers
	 */

	private String convertToRegEx(String text) {
		return "^[\\s\\S]*" + text + "[\\s\\S]*$";
	}

	private boolean isElementPresent(By by) {
		try {
			_driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isElementPresentById(String idStr) {
		try {
			return isElementPresent(By.id(idStr));
		} catch (Throwable e) {
			return false;
		}
	}

	private boolean isElementPresentBytLinkText(String linkTextStr) {
		try {
			return isElementPresent(By.linkText(linkTextStr));
		} catch (Throwable e) {
			return false;
		}
	}

	private boolean isElementPresentByXpath(String xpathStr) {
		try {
			return isElementPresent(By.xpath(xpathStr));
		} catch (Throwable e) {
			return false;
		}
	}

	private boolean isElementPresentByCssSelector(String cssSelectorStr) {
		try {
			return isElementPresent(By.cssSelector(cssSelectorStr));
		} catch (Throwable e) {
			return false;
		}
	}

	private boolean isTextPresentByCssSelector(String cssSelectorStr,
			String textStr) {
		try {
			return _driver.findElement(By.cssSelector(cssSelectorStr))
					.getText().matches(textStr);
		} catch (Throwable e) {
			return false;
		}
	}

	private boolean isTextPresentByXpath(String xpathStr, String textStr) {
		String textStrRegEx = this.convertToRegEx(textStr);
		try {
			return _driver.findElement(By.xpath(xpathStr)).getText()
					.matches(textStrRegEx);
		} catch (Throwable e) {
			return false;
		}
	}

	protected boolean isElementPresent(String type, String targetStr) {
		if (type.equals(LINK_TEXT)) {
			return isElementPresentBytLinkText(targetStr);
		} else if (type.equals(ID)) {
			return isElementPresentById(targetStr);
		} else if (type.equals(XPATH)) {
			return isElementPresentByXpath(targetStr);
		} else if (type.equals(CSS_SELECTOR)) {
			return isElementPresentByCssSelector(targetStr);
		} else {
			return false;
		}
	}

	protected boolean isTextPresent(String type, String targetStr,
			String locationStr) {
		if (type.equals(CSS_SELECTOR)) {
			return isTextPresentByCssSelector(locationStr, targetStr);
		} else if (type.equals(XPATH)) {
			return isTextPresentByXpath(locationStr, targetStr);
		}
		return false;
	}

	/*
	 * Operations and Actions
	 */

	/**
	 * 
	 * Click an element
	 * 
	 * @param type
	 *            "LINK_TEXT", "CSS_SELECTOR", "ID"
	 * @param target Locator
	 * @return int
	 */
	protected int click(String type, String target) {
		try {
			if (type.equals(LINK_TEXT)) {
				_driver.findElement(By.linkText(target)).click();
			} else if (type.equals(CSS_SELECTOR)) {
				_driver.findElement(By.cssSelector(target)).click();
			} else if (type.equals(ID)) {
				_driver.findElement(By.id(target)).click();
			} else {
				return NOT_FOUND;
			}
		} catch (Throwable e) {
			return ERROR;
		}
		return FOUND;
	}

	/**
	 * 
	 * Select value from select options
	 * 
	 * @param type "ID"
	 * @param id
	 *            unique id of the option
	 * @param target
	 *            text option value
	 * @return int
	 */
	protected int select(String type, String id, String target) {
		try {
			if (type.equals(ID)) {
				new Select(_driver.findElement(By.id(id)))
						.selectByVisibleText(target);
			} else {
				return NOT_FOUND;
			}
		} catch (Throwable e) {
			return ERROR;
		}
		return FOUND;
	}

	/**
	 * 
	 * Enter value in input field
	 * 
	 * @param type
	 *            "ID", "CSS_SELECTOR"
	 * @param target Locator
	 * @param text
	 *            value to be input
	 * @return int
	 */
	protected int input(String type, String target, String text) {
		try {
			if (type.equals(ID)) {
				_driver.findElement(By.id(target)).clear();
				_driver.findElement(By.id(target)).sendKeys(text);
			} else if (type.equals(CSS_SELECTOR)) {
				_driver.findElement(By.cssSelector(target)).clear();
				_driver.findElement(By.cssSelector(target)).sendKeys(text);
			} else {
				return NOT_FOUND;
			}
		} catch (Throwable e) {
			return ERROR;
		}
		return FOUND;
	}

	/**
	 * 
	 * Move cursor to a specific element
	 * 
	 * @param type
	 *            "ID", "LINK_TEXT", "CSS_SELECTOR", "XPATH"
	 * @param target Locator
	 * @return int
	 */
	protected int moveToElement(String type, String target) {
		Actions actions = new Actions(_driver);
		WebElement el;
		try {
			if (type.equals(ID)) {
				el = _driver.findElement(By.id(target));
				actions.moveToElement(el).perform();
			} else if (type.equals(LINK_TEXT)) {
				el = _driver.findElement(By.linkText(target));
				actions.moveToElement(el).perform();
			} else if (type.equals(CSS_SELECTOR)) {
				el = _driver.findElement(By.cssSelector(target));
				actions.moveToElement(el).perform();
			} else if (type.equals(XPATH)) {
				el = _driver.findElement(By.xpath(target));
				actions.moveToElement(el).perform();
			} else {
				return NOT_FOUND;
			}
		} catch (Throwable e) {
			return ERROR;
		}
		return FOUND;
	}

	/**
	 * 
	 * Get text from an element
	 * 
	 * @param type
	 *            "XPATH"
	 * @param target Locator
	 * @return String
	 */
	protected String getText(String type, String target) {
		String val = null;
		try {
			if (type.equals(XPATH)) {
				WebElement tmp = _driver.findElement(By.xpath(target));
				val = tmp.getText();
			} else {
				return null;
			}
		} catch (Throwable e) {
			return null;
		}
		return val;
	}

	/**
	 * 
	 * Get all options from select menu
	 * 
	 * @param type
	 *            "ID", "XPATH"
	 * @param target Locator
	 * @return List
	 */
	protected List<String> getSelectOptions(String type, String target) {
		WebElement el;
		List<WebElement> Options = new ArrayList<WebElement>();
		List<String> values = new ArrayList<String>();
		Select select;
		if (type.equals(ID)) {
			el = _driver.findElement(By.id(target));
			select = new Select(el);
			Options = select.getOptions();
		} else if (type.equals(XPATH)) {
			el = _driver.findElement(By.xpath(target));
			select = new Select(el);
			Options = select.getOptions();
		} else {
			return null;
		}
		for (WebElement option : Options) {
			values.add(option.getText());
		}
		return values;
	}

	/**
	 * 
	 * Get value from an element by attribute
	 * 
	 * @param type
	 *            "CSS_SELECTOR"
	 * @param target Locator
	 * @param attribute
	 *            Element attribute value
	 * @return String
	 */
	protected String getAttributeValue(String type, String target,
			String attribute) {
		String retVal = null;
		if (type.equals(CSS_SELECTOR)) {
			retVal = _driver.findElement(By.cssSelector(target)).getAttribute(
					attribute);
		} else {
			return retVal;
		}
		return retVal;
	}

}
