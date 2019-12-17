/*
 * Copyright 2019 Hydra Indonesia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package id.hydra.utils;

import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;

/**
 * Parse and query currency information provided from web
 *
 * @author ShinyDove
 */
public class CurrencyParser {

    /**
     * The string contains the web to visit. The hash symbol must included as
     * "####" after URL and before ".JSON" file extension.
     */
    public static final String CURRENCY_EXCHANGE_JSON_ADDRESS = "http://www.floatrates.com/daily/####.json";

    /**
     * Locate the JSON Currency Exchange JSON Address.
     * <br>To override this method,
     *
     * @param jsonFileName the file name of JSON Currency Exchange (usually
     * local currency format)
     * @return located JSON Currency Exchange JSON file name URL
     */
    public static String locateJSONfile(String jsonFileName) {
        return JsonParser.locateJSONfile(CURRENCY_EXCHANGE_JSON_ADDRESS, jsonFileName);
    }

    /**
     * Display Currency Exchange Table from JSON for a currency by displaying
     * the exchange rate of source currency for another currency in ArrayList.
     *
     * @param sourceCurrencyCode the source currency code to show in currency
     * exchange table
     * @throws Exception if any parse failure occurred
     * @return Currency exchange list of foreign currency for local/source currency
     */
    public static ArrayList<Currency> getCurrencyExchangeLists(String sourceCurrencyCode) throws Exception {
        // Query source currency conversion rate via floatrates
        String s = locateJSONfile(sourceCurrencyCode);
        URL url = new URL(s);

        // Query from the upstreamed json files
        Scanner scan = new Scanner(url.openStream());
        String str = new String();
        while (scan.hasNext()) {
            str += scan.nextLine();
        }
        scan.close();

        // Define and iterates keys
        JSONObject obj = new JSONObject(str);
        Iterator<String> keys = obj.keys();
        ArrayList<Currency> currLists = new ArrayList<>();

        while (keys.hasNext()) {
            // Iterate each keys
            JSONObject currJSON = obj.getJSONObject(keys.next());

            // Gather properties
            String target_target_curr_name = currJSON.getString("name").replaceAll("\t", "").replaceAll("convertible ", "");
            String target_curr_code = currJSON.getString("code");
            Double curr_rate = currJSON.getDouble("rate");
            Double curr_rrate = currJSON.getDouble("inverseRate");
            String source_curr_code = sourceCurrencyCode.toUpperCase();
            Currency curr = new Currency(target_target_curr_name, source_curr_code, target_curr_code, curr_rate, curr_rrate);

            currLists.add(curr);
        }

        scan.close();
        return currLists;
    }

    /**
     * Get the currency exchange rate from source currency to target currency
     *
     * @param sourceCurrencyCode source currency code
     * @param targetCurrencyCode destination currency code
     * @throws Exception if any parse error occurred
     * @return currency exchange rate between source currency and target currency
     */
    public static Currency getBetweenCurrencyExchangeRate(String sourceCurrencyCode, String targetCurrencyCode) throws Exception {
        // Query source currency conversion rate via floatrates
        String s = locateJSONfile(sourceCurrencyCode);
        URL url = new URL(s);

        // Query from the upstreamed json files
        Scanner scan = new Scanner(url.openStream());
        String str = new String();
        while (scan.hasNext()) {
            str += scan.nextLine();
        }
        scan.close();

        // Define and search for target currency code in source currency json file
        JSONObject obj = new JSONObject(str);
        JSONObject currJSON = obj.getJSONObject(targetCurrencyCode.toLowerCase());
        String target_target_curr_name = currJSON.getString("name");
        Double curr_rate = currJSON.getDouble("rate");
        Double curr_rrate = currJSON.getDouble("inverseRate");

        String source_curr_code = sourceCurrencyCode.toUpperCase();
        String target_curr_code = targetCurrencyCode.toUpperCase();

        scan.close();
        return new Currency(target_target_curr_name, source_curr_code, target_curr_code, curr_rate, curr_rrate);
    }

    /**
     * Get the currency exchange rate from source currency to target currency
     *
     * @param sourceCurrencyCode source currency code
     * @param targetCurrencyCode destination currency code
     * @throws Exception if any parse error occurred
     * @see getCalculatedCurrencyExchangeRate(String sourceCurrencyCode, String targetCurrencyCode, double rates)
     */
    private static void getCurrencyExchangeByJSON(String sourceCurrencyCode, String targetCurrencyCode) throws Exception {
        // Take a query for in-between currency exchange rate from currency exchange site
        Currency curr = getBetweenCurrencyExchangeRate(sourceCurrencyCode, targetCurrencyCode);

        String target_curr_name = curr.getTargetCurrencyExchangeName();
        Double curr_rate = curr.getCurrencyExchangeRate(false);
        Double curr_rrate = curr.getCurrencyExchangeRate(true);
        String source_curr_code = sourceCurrencyCode.toUpperCase();
        String target_curr_code = targetCurrencyCode.toUpperCase();

        // Display the conversion rate between source and destination currency
        System.out.println(source_curr_code + " <--> " + target_curr_code + " exchange information:");
        System.out.println("---------------------------------------------------");
        System.out.println("[!] Target currency : " + target_curr_name);
        System.out.printf("[B] %s to %s rate : %s %15.5f%n", source_curr_code, target_curr_code, target_curr_code, curr_rate);
        System.out.printf("[S] %s to %s rate : %s %15.5f%n", target_curr_code, source_curr_code, source_curr_code, curr_rrate);
    }

    /**
     * Calculate the currency exchange rate from source currency to target
     * currency with specified price
     *
     * @param sourceCurrencyCode source currency code
     * @param targetCurrencyCode destination currency code
     * @param rates specified currency exchange rate
     * @throws Exception if any parse error occurred
     * @see getCurrencyExchangeByJSON(String sourceCurrencyCode, String targetCurrencyCode)
     */
    private static void getCalculatedCurrencyExchangeRate(String sourceCurrencyCode, String targetCurrencyCode, double rates) throws Exception {
        // Take a query for in-between currency exchange rate from currency exchange site
        Currency curr = getBetweenCurrencyExchangeRate(sourceCurrencyCode, targetCurrencyCode);

        String target_curr_name = curr.getTargetCurrencyExchangeName();
        String source_curr_code = sourceCurrencyCode.toUpperCase();
        String target_curr_code = targetCurrencyCode.toUpperCase();

        Double calculatedRate = curr.calculateExchangeRate(rates);

        // Display the conversion rate between source and destination currency
        System.out.printf("%s %.2f to %s exchange rate:%n", source_curr_code, rates, target_curr_code);
        System.out.println("---------------------------------------------------");
        System.out.println("[!] Target currency : " + target_curr_name);
        System.out.printf("[B] %s to %s rate : %s %15.5f%n", source_curr_code, target_curr_code, target_curr_code, calculatedRate);
    }

    /**
     * Display Currency Exchange Table from JSON (unsorted) for a currency by
     * displaying the exchange rate for another currency when the currency rate
     * would offered for another and the another currency rate offered with that
     * currency.
     *
     * @param sourceCurrencyCode the source currency code to show in currency
     * exchange table
     * @throws Exception if any parse failure occurred
     * @see getWholeSortedCurrencyExchangeTableFromJSON(String sourceCurrencyCode,
     *   boolean local, boolean sortByCurrencyCode)
     */
    private static void getWholeCurrencyExchangeTableFromJSON(String sourceCurrencyCode) throws Exception {
        // Take a query for currency exchange table from currency exchange site
        ArrayList<Currency> currencyExchanges = getCurrencyExchangeLists(sourceCurrencyCode);

        System.out.println(sourceCurrencyCode.toUpperCase() + " exchange information:");
        System.out.println("---------------------------------------------------");
        
        for (Currency curr : currencyExchanges) {
            // Gather & print the properties
            String target_curr_name = curr.getTargetCurrencyExchangeName();
            String target_curr_code = curr.getTargetCurrencyCode();
            Double curr_rate = curr.getCurrencyExchangeRate(false);
            Double curr_rrate = curr.getCurrencyExchangeRate(true);
            String source_curr_code = curr.getLocalCurrencyCode();

            System.out.printf("%-30s : %s %10.3f | %s %10.5f%n", target_curr_name, source_curr_code, curr_rrate, target_curr_code, curr_rate);
        }
    }

    /**
     * Display Currency Exchange Table from JSON for a currency by displaying
     * the exchange rate for another currency when the currency rate would
     * offered for another and the another currency rate offered with that
     * currency
     *
     * @param sourceCurrencyCode the source currency code to show in currency
     * exchange table
     * @param local show column buy another currency first instead of sell it
     * for another currency?
     * @param sortByCurrencyCode sort the currency exchange table by currency
     * code?
     * @throws Exception if any parse failure occurred
     */
    private static void getWholeSortedCurrencyExchangeTableFromJSON(String sourceCurrencyCode, boolean local, boolean sortByCurrencyCode) throws Exception {
        // Take a query for currency exchange table from currency exchange site
        ArrayList<Currency> currLists = getCurrencyExchangeLists(sourceCurrencyCode);

        // Sort using selected comparator (currency code if true or currency name if false)
        if (sortByCurrencyCode == true) {
            currLists.sort(Currency.CurrencyCodeComparator);
        } else {
            currLists.sort(Currency.CurrencyNameComparator);
        }

        // Display the exchange rate table
        Iterator<Currency> extractInfo = currLists.iterator();
        int index = 0;
        System.out.println(sourceCurrencyCode.toUpperCase() + " exchange information: " + ((local) ? "(buy / sell)" : "(sell / buy)"));
        System.out.println("--------------------------------------------------------------------------");
        try {
            while (extractInfo.hasNext()) {
                if (local == true) {
                    System.out.printf("%-30s : %s %15.5f | %s %15.5f%n", currLists.get(index).getTargetCurrencyExchangeName(),
                            currLists.get(index).getLocalCurrencyCode(), currLists.get(index).getCurrencyExchangeRate(true),
                            currLists.get(index).getTargetCurrencyCode(), currLists.get(index).getCurrencyExchangeRate(false));
                } else {
                    System.out.printf("%-30s : %s %15.5f | %s %15.5f%n", currLists.get(index).getTargetCurrencyExchangeName(),
                            currLists.get(index).getTargetCurrencyCode(), currLists.get(index).getCurrencyExchangeRate(false),
                            currLists.get(index).getLocalCurrencyCode(), currLists.get(index).getCurrencyExchangeRate(true));
                }
                index++;
            }
        } catch (Exception e) {
            System.out.println("Done!");
        }
    }

    /**
     * Display Currency Exchange Table from JSON with column to sell local
     * currency for another currency prices or buy another currency using local
     * currency price (sorted by currency name).
     *
     * @param sourceCurrencyCode the source currency code to show the currency
     * table
     * @param buys show column to buy another currency instead of sell it for
     * another currency?
     * @throws Exception if any parse failure occurred
     */
    private static void getWholeCurrencyBuysSellsColumnFromJSON(String sourceCurrencyCode, boolean buys) throws Exception {
        // Call another getWholeCurrencyBuysSellsColumnFromJSON() method but sorted by currency name
        getWholeCurrencyBuysSellsColumnFromJSON(sourceCurrencyCode, buys, false);
    }

    /**
     * Display Currency Exchange Table from JSON with column to sell local
     * currency for another currency prices or buy another currency using local
     * currency price (sorted by currency name or currency code).
     *
     * @param sourceCurrencyCode the source currency code to show the currency
     * table
     * @param buys show column to buy another currency instead of sell it for
     * another currency?
     * @param sortByCurrencyCode sort the currency exchange rate by currency
     * code?
     * @throws Exception if any parse failure occurred
     */
    private static void getWholeCurrencyBuysSellsColumnFromJSON(String sourceCurrencyCode, boolean buys, boolean sortByCurrencyCode) throws Exception {
        // Take a query for currency exchange table from currency exchange site
        ArrayList<Currency> currLists = getCurrencyExchangeLists(sourceCurrencyCode);

        // Sort using selected comparator (currency code if true or currency name if false)
        if (sortByCurrencyCode == true) {
            currLists.sort(Currency.CurrencyCodeComparator);
        } else {
            currLists.sort(Currency.CurrencyNameComparator);
        }

        // Display the buy/sell table
        Iterator<Currency> extractInfo = currLists.iterator();
        int index = 0;
        System.out.println(sourceCurrencyCode.toUpperCase() + " currency " + ((buys == true) ? "buy" : "sell") + " information:");
        System.out.println("----------------------------------------------------");
        try {
            while (extractInfo.hasNext()) {
                if (buys == true) {
                    System.out.printf("%-30s : %s %15.5f%n", currLists.get(index).getTargetCurrencyExchangeName(),
                            currLists.get(index).getLocalCurrencyCode(), currLists.get(index).getCurrencyExchangeRate(true));
                } else {
                    System.out.printf("%-30s : %s %15.5f%n", currLists.get(index).getTargetCurrencyExchangeName(),
                            currLists.get(index).getTargetCurrencyCode(), currLists.get(index).getCurrencyExchangeRate(false));
                }
                index++;
            }
        } catch (Exception e) {
            // Finished, because no more data to parse it..
            System.out.println("---------------------------------------------------");
            System.out.println("Done!");
        }
    }

    /**
     * The main method to display the help guide or parse it with command line
     * arguments
     *
     * @param args arguments to pass to provide the JSON Currency Information
     * using inputted parameters
     */
    public static void main(String[] args) {
        boolean showInLocales, sortByCurrencyCode;
        try {
            if (args.length == 0 || args[0].equals("help")) {
                // Give me an helpful help lists
                throw new NoSuchFieldException("Help");
            } else if (args.length == 1 && args[0].equals("demo")) {
                // Demonstrate USD currency table by 
                getWholeSortedCurrencyExchangeTableFromJSON("USD", false, true);
            } else if (args.length == 2 && !(args[1].startsWith("-showInLocalCurrency") || args[1].startsWith("-sortByCurrencyCode") || args[1].startsWith("-buy") || args[1].startsWith("-sell"))) {
                // Two parameters but for currency codes
                getCurrencyExchangeByJSON(args[0], args[1]); //money conversion
            } else if (args.length == 3 && !(args[1].startsWith("-showInLocalCurrency") || args[1].startsWith("-sortByCurrencyCode") || args[1].startsWith("-buy") || args[1].startsWith("-sell"))) {
                // Buy local to target with selected price
                try {
                    getCalculatedCurrencyExchangeRate(args[0], args[1], Double.parseDouble(args[2]));
                } catch (Exception e) {
                    throw new Exception("Parsing error!");
                }
            } else if (args.length == 3) {
                // If displays in buy-sell table (-showInLocalCurrency and -sortByCurrencyCode or vice-versa)
                if ((args[1].equals("-showInLocalCurrency") || args[1].equals("-showInLocalCurrency:true") || args[1].equals("-showInLocalCurrency:false"))
                        && (args[2].equals("-sortByCurrencyCode") || args[2].equals("-sortByCurrencyCode:true") || args[2].equals("-sortByCurrencyCode:false"))) {
                    showInLocales = (args[1].equals("-showInLocalCurrency")) ? true : Boolean.parseBoolean(args[1].split(":")[1]);
                    sortByCurrencyCode = (args[2].equals("-sortByCurrencyCode")) ? true : Boolean.parseBoolean(args[2].split(":")[1]);
                    getWholeSortedCurrencyExchangeTableFromJSON(args[0], showInLocales, sortByCurrencyCode); //Sort by currency code and View Local
                }
                else if ((args[2].equals("-showInLocalCurrency") || args[2].equals("-showInLocalCurrency:true") || args[2].equals("-showInLocalCurrency:false"))
                        && (args[1].equals("-sortByCurrencyCode") || args[1].equals("-sortByCurrencyCode:true") || args[1].equals("-sortByCurrencyCode:false"))) {
                    showInLocales = (args[2].equals("-showInLocalCurrency")) ? true : Boolean.parseBoolean(args[2].split(":")[1]);
                    sortByCurrencyCode = (args[1].equals("-sortByCurrencyCode")) ? true : Boolean.parseBoolean(args[1].split(":")[1]);
                    getWholeSortedCurrencyExchangeTableFromJSON(args[0], showInLocales, sortByCurrencyCode); //Sort by currency code and View Local
                }

                // If displays currency buy or sell table ([-buy or -sell] and -sortByCurrencyCode)
                else if (args[1].equals("-buy") && (args[2].equals("-sortByCurrencyCode") || args[2].equals("-sortByCurrencyCode:true") || args[2].equals("-sortByCurrencyCode:false"))) {
                    sortByCurrencyCode = (args[2].equals("-sortByCurrencyCode")) ? true : Boolean.parseBoolean(args[2].split(":")[1]);
                    getWholeCurrencyBuysSellsColumnFromJSON(args[0], true, sortByCurrencyCode); //Sort the currency buy table by currency code
                }
                else if (args[1].equals("-sell") && (args[2].equals("-sortByCurrencyCode") || args[2].equals("-sortByCurrencyCode:true") || args[2].equals("-sortByCurrencyCode:false"))) {
                    sortByCurrencyCode = (args[2].equals("-sortByCurrencyCode")) ? true : Boolean.parseBoolean(args[2].split(":")[1]);
                    getWholeCurrencyBuysSellsColumnFromJSON(args[0], false, sortByCurrencyCode); //Sort the currency sell table by currency code
                }
            } else if (args.length == 2) {
                switch (args[1]) {
                    // If buy the target currency with local currency price
                    case "-buy":
                        getWholeCurrencyBuysSellsColumnFromJSON(args[0], true); //View currency buy rate
                        break;
                    // If sell/offer local currency for target currency price
                    case "-sell":
                        getWholeCurrencyBuysSellsColumnFromJSON(args[0], false); //View currency sell rate
                        break;
                    // Prioritize to show in local currency display first
                    case "-showInLocalCurrency":
                    case "-showInLocalCurrency:true":
                    case "-showInLocalCurrency:false":
                        showInLocales = (args[1].equals("-showInLocalCurrency")) ? true : Boolean.parseBoolean(args[1].split(":")[1]);
                        getWholeSortedCurrencyExchangeTableFromJSON(args[0], showInLocales, false); //View Local
                        break;
                    // Sort by currency code
                    case "-sortByCurrencyCode":
                    case "-sortByCurrencyCode:true":
                    case "-sortByCurrencyCode:false":
                        sortByCurrencyCode = (args[1].equals("-sortByCurrencyCode")) ? true : Boolean.parseBoolean(args[1].split(":")[1]);
                        getWholeSortedCurrencyExchangeTableFromJSON(args[0], false, sortByCurrencyCode); //Sort by currency code
                        break;
                }
            } else if (args.length == 1) {
                // Show currency table (Defaultly, display offered price first and sorted by currency name)
                getWholeSortedCurrencyExchangeTableFromJSON(args[0], false, false);
            } else {
                throw new Exception("Invalid command");
            }

        } catch (NoSuchFieldException ex) {
            showHelp();
        } catch (Exception ex) {
            Logger.getLogger(CurrencyParser.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Invalid command line parameter specified!");
            System.err.println("Please try with \'jsoncurrency.jar\' for parameters and further information!");
        }
    }
    
    /**
     * Display JSON Currency Parser command line informations
     */
    private static void showHelp() {
        System.out.println("JSON Currency Parser");
            System.out.println("------------------------------------------");
            System.out.println("Basic command line parameters:");
            System.out.println("\'jsoncurrency.jar (help)\':\n"
                    + " - Get help about JSON Currency Parser (you can empty the parameters or type 'help')");
            System.out.println("");

            System.out.println("\'jsoncurrency.jar demo\':\n"
                    + " - Show demo from JSON Currency Parser (show USD conversion table, sorted in currency code)");
            System.out.println("");

            System.out.println("\'jsoncurrency.jar <currencyCode>\':\n"
                    + " - Show currency exchange table from a currency <currencyCode>\n"
                    + " - Example : \'jsoncurrency.jar usd\' -> shows USD exchange table");
            System.out.println("");

            System.out.println("\'jsoncurrency.jar <currencyCode> -buy/-sell\':\n"
                    + " - Show currency buy/sell table rate from a currency <currencyCode>\n"
                    + " - Example : \'jsoncurrency.jar usd -sell\' -> shows USD sell table");
            System.out.println("");

            System.out.println("\'jsoncurrency.jar <currencyFrom> <currencyTo>\':\n"
                    + " - Show currency exchange rate from local currency <currencyFrom> to target currency <currencyTo>\n"
                    + " - Example : \'jsoncurrency.jar usd idr\' -> shows USD to IDR exchange rate");
            System.out.println("");

            System.out.println("\'jsoncurrency.jar <currencyFrom> <currencyTo> <localCurrencyRate>\':\n"
                    + " - Calculate price of local currency <currencyFrom> to target currency <currencyTo>\n"
                    + " - Example : \'jsoncurrency.jar usd idr 25\' -> calculate USD 25 to IDR");
            System.out.println("");

            System.out.println("Addtional Parameters for <currencyCodeExchanges>:");
            System.out.println(" >> {\'-buy\' or \'-sell\' or \'-showInLocalCurrency:[true/false]\'}, \'-sortByCurrencyCode:[true/false]\'");
            System.out.println("----------------------------------------------------------------------------------");

            System.out.println("Currency buy/sell rate:");
            System.out.println("\'-buy\': Show currency buy rate. Cannot paired with \'-showInLocalCurrency:[true/false]\'");
            System.out.println("\'-sell\': Show currency sell rate. Cannot paired with \'-showInLocalCurrency:[true/false]\'");
            System.out.println("");

            System.out.println("\'-showInLocalCurrency:[true/false]\': Show currency exchange table by display local currency first (def: false)");
            System.out.println(">> If true, display local currency first. Else, display target currency first. Cannot paired with \'-buy\' or \'-sell\'");
            System.out.println("");

            System.out.println("\'-sortByCurrencyCode:[true/false]\': Show currency exchange table by sort the currency code (def: false)");
            System.out.println(">> If true, sort by currency code. Else, sort by country name.");
            System.out.println("");
    }
    
}
