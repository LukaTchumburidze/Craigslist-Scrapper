package com.craigslist;

import java.awt.*;

public  class Variables {

    public static final int NewChromeDriverRate = 2;

    public static final int PhoneMinLength = 7;
    public static final int PhoneMaxLength = 12;
    public static final int PhoneContainerLength = 12;
    public static final int PhoneMinDifficulty = 25;//3^2+4^2
    public static final int AdsPerPage = 120;
    public static final int LinkThreshhold = 32;

    public static final String[] CategoryColumnsXpath = {
            "//*[@id=\"hhh0\"]", "//*[@id=\"sss0\"]", "//*[@id=\"sss1\"]", "//*[@id=\"bbb0\"]",
            "//*[@id=\"bbb1\"]", "//*[@id=\"jjj0\"]", "//*[@id=\"ggg0\"]", "//*[@id=\"ggg1\"]"
    };
    public static final String CategoriesURL = "https://rockford.craigslist.org/";

    public static String Isolator = "+";
    public static final String CitiesURL = "https://www.craigslist.org/about/sites#US";
    public static final String ContainerXpath = "/html/body/article/section/div[3]";
    public static final String CitiesXpath = ".//div/ul/li/a";
    public static final String HousingXpath = "//*[@id=\"hhh\"]/h4/a";
    public static final String ForSaleXpath = "//*[@id=\"sss\"]/h4/a";
    public static final String JobsXpath = "//*[@id=\"jjj\"]/h4/a";
    public static final String Ads = "//*[@id=\"sortable-results\"]/ul/li/a";
    public static final String AdPrefix = "//*[@id=\"sortable-results\"]/ul/li";
    public static final String AdHeader = "/html/body/section/section/h2/span[2]";
    public static final String RangeXpath = "//*[@id=\"searchform\"]/div[3]/div[3]/span[2]/span[3]/span[2]";
    public static final String NextXpath = "//*[@id=\"searchform\"]/div[3]/div[3]/span[2]/a[3]";
    public static final String CntClass = "totalcount";
    public static final String Content1Xpath = "//*[@id=\"postingbody\"]";
    public static final String Content2Xpath = "/html/body/section/section/section/ul";
    public static final String DateLink = "https://www.timeanddate.com/worldclock/georgia/tbilisi";
    public static final String DateXpath = "//*[@id=\"ctdat\"]";

    public static final Color HeaderColor = new Color(185, 66, 244);
    public static final String ExcelFilePath = "Data/Output/";


}
