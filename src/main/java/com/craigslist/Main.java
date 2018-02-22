package com.craigslist;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

public class Main {

    private static ChromeOptions co;
    private static WebDriver driver;
    private static WebDriverWait wait;


    private static Scanner in = new Scanner(System.in);

    private static String CategoriesData[][];
    private static String CitiesData[][];

    private static HashMap<String, String> CitiesMap = new HashMap<String, String>();
    private static HashMap<String, String> CategoriesMap = new HashMap<String, String>();
    private static HashMap<String, Boolean> PhoneData = new HashMap<String, Boolean>();

    private static String City = "Randomword";
    private static String Category = "Randomword";
    private static String CityURL;
    private static String CategoryURL;

    private static LinkedList <String[]> Data;
    private static DBSaver ExcelFile;

    private static boolean AskUser(String outString) {
        outString += "(y/n)";
        char ind = 'a';
        while (ind != 'y' && ind != 'n') {
            System.out.println(outString);
            String Line = in.nextLine();
            if (Line.length() == 1) {
                ind = Character.toLowerCase(Line.charAt(0));
            }
        }
        return (ind == 'y');
    }

    private static void SaveCitiesData() {
        System.out.println("Saving Cities!");
        try {
            PrintWriter CitiesWriter = new PrintWriter("Data/Input/Cities.txt", "UTF-8");
            CitiesWriter.println(CitiesData.length);
            for (int i = 0; i < CitiesData.length; i++) {
                CitiesWriter.println(CitiesData[i][0] + Variables.Isolator + CitiesData[i][1]);
            }

            CitiesWriter.close();
        } catch (IOException e) {
            System.out.println("Error in SaveCitiesData()!");
        }
    }

    private static void ReadCitiesData() {
        System.out.println("Reading Cities!");
        try {
            File CitiesFile = new File("Data/Input/Cities.txt");
            Scanner inCities = new Scanner(CitiesFile);
            int cnt = inCities.nextInt();
            inCities.nextLine();
            CitiesData = new String[cnt][2];
            for (int i = 0; i < cnt; i++) {
                String[] Line = inCities.nextLine().split(Pattern.quote(Variables.Isolator));
                CitiesData[i][0] = Line[0];
                CitiesData[i][1] = Line[1];
            }
        } catch (IOException e) {
            System.out.println("Error in ReadCitiesData()!");
        }
    }

    private static void ScrapeCities() {
        System.out.println("Scraping Cities!");
        driver.navigate().to(Variables.CitiesURL);
        WebElement USContainer = driver.findElement(By.xpath(Variables.ContainerXpath));
        List<WebElement> Cities = USContainer.findElements(By.xpath(Variables.CitiesXpath));
        CitiesData = new String[Cities.size()][2];
        for (int i = 0; i < Cities.size(); i++) {
            WebElement City = Cities.get(i);
            CitiesData[i][0] = City.getText();
            CitiesData[i][1] = City.getAttribute("href");
        }
        System.out.println(Cities.size());
        SaveCitiesData();
    }

    private static void SaveCategoriesData() {
        System.out.println("Saving Categories!");
        try {
            PrintWriter CategoriesWriter = new PrintWriter("Data/Input/Categories.txt", "UTF-8");
            CategoriesWriter.println(CategoriesData.length);
            for (int i = 0; i < CategoriesData.length; i++) {
                CategoriesWriter.println(CategoriesData[i][0] + Variables.Isolator + CategoriesData[i][1]);
            }

            CategoriesWriter.close();
        } catch (IOException e) {
            System.out.println("Error in SaveCategoriesData()!");
        }
    }

    private static void ReadCategoriesData() {
        System.out.println("Reading Categories!");
        try {
            File CategoriesFile = new File("Data/Input/Categories.txt");
            Scanner inCategories = new Scanner(CategoriesFile);
            int cnt = inCategories.nextInt();
            inCategories.nextLine();
            CategoriesData = new String[cnt][2];
            for (int i = 0; i < cnt; i++) {
                String[] Line = inCategories.nextLine().split(Pattern.quote(Variables.Isolator));
                CategoriesData[i][0] = Line[0];
                CategoriesData[i][1] = Line[1];
            }
        } catch (IOException e) {
            System.out.println("Error in ReadCategoriesData()!");
        }
    }

    private static String ProcessCategory(String Text) {
        String rtn = "" + Text.charAt(0);
        for (int i = 1; i < Text.length() - 1; i++) {
            if (Text.charAt(i) == ' ' && (Text.charAt(i - 1) == '/' || Text.charAt(i + 1) == '/')) {
                continue;
            }
            rtn += Text.charAt(i);
        }
        rtn += Text.charAt(Text.length() - 1);
        return rtn;
    }

    private static void ScrapeCategories() {
        System.out.println("Scraping Categories!");
        driver.navigate().to(Variables.CategoriesURL);
        int cnt = 0;
        for (int i = 0; i < Variables.CategoryColumnsXpath.length; i++) {
            WebElement CurColumn = driver.findElement(By.xpath(Variables.CategoryColumnsXpath[i]));
            List<WebElement> Elements = CurColumn.findElements(By.xpath(".//li/a"));
            cnt += Elements.size();
        }
        cnt += 3;
        CategoriesData = new String[cnt][2];
        WebElement WebEl = driver.findElement(By.xpath(Variables.HousingXpath));
        CategoriesData[cnt - 3][0] = ProcessCategory(WebEl.getText());
        CategoriesData[cnt - 3][1] = WebEl.getAttribute("href").substring(Variables.LinkThreshhold);
        WebEl = driver.findElement(By.xpath(Variables.ForSaleXpath));
        CategoriesData[cnt - 2][0] = ProcessCategory(WebEl.getText());
        CategoriesData[cnt - 2][1] = WebEl.getAttribute("href").substring(Variables.LinkThreshhold);
        WebEl = driver.findElement(By.xpath(Variables.JobsXpath));
        CategoriesData[cnt - 1][0] = ProcessCategory(WebEl.getText());
        CategoriesData[cnt - 1][1] = WebEl.getAttribute("href").substring(Variables.LinkThreshhold);
        cnt = 0;
        for (int i = 0; i < Variables.CategoryColumnsXpath.length; i++) {
            WebElement CurColumn = driver.findElement(By.xpath(Variables.CategoryColumnsXpath[i]));
            List<WebElement> Elements = CurColumn.findElements(By.xpath(".//li/a"));
            for (int j = 0; j < Elements.size(); j++, cnt++) {
                CategoriesData[cnt][0] = ProcessCategory(Elements.get(j).getText());
                CategoriesData[cnt][1] = Elements.get(j).getAttribute("href").substring(Variables.LinkThreshhold);
            }
        }
        SaveCategoriesData();
    }

    private static void FillCitiesMap() {
        for (int i = 0; i < CitiesData.length; i++) {
            System.out.print(CitiesData[i][0] + "; ");
            CitiesMap.put(CitiesData[i][0].toLowerCase(), CitiesData[i][1]);
        }
        System.out.println();
    }

    private static void FillCategoriesMap() {
        for (int i = 0; i < CategoriesData.length; i++) {
            System.out.print(CategoriesData[i][0] + "; ");
            CategoriesMap.put(CategoriesData[i][0].toLowerCase(), CategoriesData[i][1]);
        }
        System.out.println();
    }

    private static void InitCitiesData() {
        File Cities = new File("Data/Input/Cities.txt");
        if (!Cities.isFile()) {
            System.out.println("Creating Cities.txt");
            ScrapeCities();
        } else {
            if (AskUser("Do you want to Update the City list?")) {
                System.out.println("Updating Cities.txt");
                Cities.delete();
                ScrapeCities();
            }
        }
        if (CitiesData == null) {
            ReadCitiesData();
        }
        FillCitiesMap();
    }

    private static void InitCategoriesData() {
        File Categories = new File("Data/Input/Categories.txt");
        if (!Categories.isFile()) {
            System.out.println("Creating Categories.txt");
            ScrapeCategories();
        } else {
            if (AskUser("Do you want to Update the Categories list?")) {
                System.out.println("Updating Categories.txt");
                Categories.delete();
                ScrapeCategories();
            }
        }
        if (CategoriesData == null) {
            ReadCategoriesData();
        }
        FillCategoriesMap();
    }

    private static void InitInput() {
        System.out.println("Checking Data/Input Directory");
        File InputDir = new File("Data/input");
        if (InputDir.isDirectory()) {
            System.out.println("Data/Input Directory Exists!");
        } else {
            System.out.println("Creating Data/Input Directory");
            InputDir.mkdir();
        }

        InitCitiesData();

        InitCategoriesData();
    }

    private static void InitOutput() {
        System.out.println("Checking Data/Output Directory");
        File OutputDir = new File("Data/Output");
        if (OutputDir.isDirectory()) {
            System.out.println("Data/Output Directory Exists!");
        } else {
            System.out.println("Creating Data/Output Directory");
            OutputDir.mkdir();
        }
    }

    private static void InitWork() {
        System.out.println("Checking Data Directory");
        File DataDir = new File("Data");
        if (DataDir.isDirectory()) {
            System.out.println("Data Directory Exists!");
        } else {
            System.out.println("Creating Data Directory");
            DataDir.mkdir();
        }
        InitInput();
        InitOutput();
        System.out.println();
    }

    private static void GetCityName() {
        System.out.println();
        while (!CitiesMap.containsKey(City)) {
            System.out.println("Please enter the correct name of the city!");
            City = in.nextLine().toLowerCase();
        }
        CityURL = CitiesMap.get(City);
    }

    private static void GetCategoryName() {
        System.out.println();
        while (!CategoriesMap.containsKey(Category)) {
            System.out.println("Please enter the correct name of the category!");
            Category = in.nextLine().toLowerCase();
        }
        CategoryURL = CategoriesMap.get(Category);
    }

    private static String ExtractFromContainer(String Container) {
        String rtnVal = "";
        for (int i = 0; i < Container.length(); i++) {
            if (Container.charAt(i) >= '0' && Container.charAt(i) <= '9') {
                rtnVal += Container.charAt(i);
            }
        }
        return rtnVal;
    }

    private static int LinkJumper (String Text, int ind) {
        Text.substring(ind, ind + 4);
        if (Text.substring(ind, ind + 4).equals("http")) {
            for (int i = ind; i < Text.length(); i ++) {
                if (Text.charAt(i) == ' ') {
                    return i;
                }
            }
            return Text.length() - 1;
        }
        return ind;
    }

    private static String GetPhone() {
        String Container_1 = driver.findElement(By.xpath(Variables.Content1Xpath)).getText();
        String Container_2 = driver.findElement(By.xpath(Variables.Content2Xpath)).getText();
        String Text = Container_1 + "" + Container_2;
        String newText = "";
        for (int i = 0; i < Text.length(); i++) {
            char curChar = Text.charAt(i);
            if (curChar == '-' || curChar == '.' || curChar == ' ' || curChar == '(' || curChar == ')') {
                continue;
            }
            newText += curChar;
        }
        Text = newText.toLowerCase();
        String Container = "";
        int difficulty = Variables.PhoneMinDifficulty;
        int cnt = Variables.PhoneMinLength - 1;
        for (int i = 0; i < Text.length() - Variables.PhoneContainerLength; i++) {
            int curCnt = 0, interval = 0, curDifficulty = 0;
            for (int j = i; j < i + Variables.PhoneContainerLength; j++) {
                char curChar = Text.charAt(j);
                if (curChar >= '0' && curChar <= '9') {
                    curCnt++;
                    interval++;
                } else {
                    int JumpLength = LinkJumper (Text, i);
                    if (JumpLength != i) {
                        i = JumpLength;
                        break;
                    }
                    curDifficulty += interval * interval;
                    interval = 0;
                    if (curDifficulty > 0 && (curChar == '\"' || curChar == '\'' || curChar == ':' || curChar == '$')) {
                        break;
                    }
                }
            }
            if (curCnt > cnt && curCnt < Variables.PhoneMaxLength && curDifficulty >= difficulty) {
                Container = Text.substring(i, i + Variables.PhoneContainerLength);
                cnt = curCnt;
                difficulty = curDifficulty;
            }
        }
        if (Container.equals("")) {
            return "";
        }

        return ExtractFromContainer(Container);
    }

    private static int GetPageNumber (int cnt) {
        int ind = 1000000;
        while (ind < 0 || ind > cnt) {
            System.out.println("Enter valid starting page number!");
            ind = Variables.AdsPerPage * (in.nextInt() - 1);
        }
        return ind;
    }

    private static void ScrapeAds() {
        ExcelFile = new DBSaver(City + " " + Category.replace('/', '-'));
        String URL = CityURL + CategoryURL;
        driver.navigate().to(URL);


        Data = new LinkedList<String[]>();
        int cnt = 0;
        try {
            cnt = Integer.parseInt(driver.findElement(By.className("totalcount")).getText());
        } catch (Exception e) {
            System.out.println("There aren't any results, just hit enter  to finish!");
            in.nextLine();
            driver.close();
            System.exit(0);
        }
        System.out.println("Found " + cnt + " results!");
        int ind = GetPageNumber (cnt);
        URL += "?s=" + ind;
        driver.navigate().to(URL);

        while (ind != cnt) {
            try {
                List<WebElement> Ads = driver.findElements(By.xpath(Variables.Ads));
                System.out.println(Ads.size());
                for (int i = 0; i < Ads.size() && ind + i < cnt; i++) {
                    try {
                        String AdXpath = Variables.AdPrefix + "[" + (i + 1) + "]/p/a";
                        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(AdXpath)));
                        WebElement Ad = driver.findElement(By.xpath(AdXpath));
                        Ad.sendKeys(Keys.RETURN);

                        WebElement Header = driver.findElement(By.xpath(Variables.AdHeader));
                        System.out.println((ind + i + 1) + ") " + Header.getText());
                        System.out.println(driver.getCurrentUrl());
                        String Phone = GetPhone();
                        if (Phone.length() == 10 && !PhoneData.containsKey(Phone)) {
                            System.out.println("Found advertisement with phone number!");
                            PhoneData.put(Phone, true);
                            String row[] = {Header.getText(), driver.getCurrentUrl(), GetPhone()};
                            Data.add(row);
                        }
                    } catch (NoSuchElementException e) {
                        System.out.println ("Couldn't get This link");
                    }
                    driver.navigate().back();
                }
                SaveData(ind);
                ind += Ads.size();
                System.out.println(ind);
                if (ind >= cnt) {
                    break;
                }
                WebElement Next = driver.findElement(By.xpath(Variables.NextXpath));
                Next.sendKeys(Keys.RETURN);
                if (ind%(Variables.AdsPerPage*Variables.NewChromeDriverRate) == 0){
                    String Temp = driver.getCurrentUrl();
                    driver.close();
                    driver = new ChromeDriver(co);
                    wait = new WebDriverWait(driver, 5);
                    driver.navigate().to(Temp);
                }
            } catch (TimeoutException e) {
                break;
            }
        }
    }

    private static void SaveData(int ind) {
        System.out.println("Data from " + ind + " to " + (ind + Variables.AdsPerPage) + " is being updated in " + "\"" + ExcelFile.Name + "!\"");
        ExcelFile.UpdateData (Data, ind);
    }

    public static void main(String[] args) {
        //Sets variable to local chromedriver's path
        System.setProperty("webdriver.chrome.driver", "chromedriver");

        HashMap<String, Object> images = new HashMap<String, Object>();
        images.put("images", 2);
        HashMap<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values", images);


        co = new ChromeOptions();
        co.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(co);
        wait = new WebDriverWait(driver, 5);

        InitWork();

        GetCityName();
        GetCategoryName();
        ScrapeAds();


        driver.close();
        System.out.println("Scraper finished working, just hit enter  to finish!");
        in.nextLine();
    }
}