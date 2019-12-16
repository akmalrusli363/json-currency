package id.hydra.utils;

import java.util.Comparator;

/**
 * The Currency class that stores target currency and local currency information such as exchange rate and currency codes
 * @author ShinyDove
 */
public class Currency {
    
    private final String targetCurrencyName, localCurrencyCode, targetCurrencyCode;
    private final double exchangeRate, reverseExchangeRate;
    
    /**
     * Create Currency Conversion object with set of name of currency, 
     * @param targetCurrencyName The target currency name
     * @param localCurrencyCode The local currency name
     * @param targetCurrencyCode The destination currency code
     * @param exchangeRate The local currency to destination currency rate
     * @param reverseExchangeRate The destination currency to local currency rate
     */
    public Currency(String targetCurrencyName, String localCurrencyCode, String targetCurrencyCode, double exchangeRate, double reverseExchangeRate){
        this.targetCurrencyName = targetCurrencyName;
        this.localCurrencyCode = localCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.exchangeRate = exchangeRate;
        this.reverseExchangeRate = reverseExchangeRate;
    }
    
    /**
     * Get Target Currency name
     * @return Name of target currency
     */
    public String getTargetCurrencyExchangeName(){
        return targetCurrencyName;
    }
    
    /**
     * Get Target Currency code
     * @return Name of target currency
     */
    public String getTargetCurrencyCode(){
        return targetCurrencyCode;
    }
    
    /**
     * Get Local Currency code
     * @return Name of target currency
     */
    public String getLocalCurrencyCode(){
        return localCurrencyCode;
    }
    
    /**
     * Get local currency to target currency rate
     * @param reverse true if want to obtain target currency to local currency rate
     * @return Name of target currency
     */
    public Double getCurrencyExchangeRate(boolean reverse){
        return (reverse == true) ? reverseExchangeRate : exchangeRate;
    }
    
    /**
     * Sort by currency name, ascending
     */
    public static Comparator<Currency> CurrencyNameComparator = new Comparator<Currency>() {

        @Override
	public int compare(Currency s1, Currency s2) {
	   String Currency1 = s1.getTargetCurrencyExchangeName().toUpperCase();
	   String Currency2 = s2.getTargetCurrencyExchangeName().toUpperCase();

	   //ascending order
	   return Currency1.compareTo(Currency2);
    }};
    
    /**
     * Sort by currency name, descending
     */
    public static Comparator<Currency> ReverseCurrencyNameComparator = new Comparator<Currency>() {

        @Override
	public int compare(Currency s1, Currency s2) {
	   String Currency1 = s1.getTargetCurrencyExchangeName().toUpperCase();
	   String Currency2 = s2.getTargetCurrencyExchangeName().toUpperCase();

	   //descending order
	   return Currency2.compareTo(Currency1);
    }};
    
    /**
     * Sort by currency code, ascending
     */
    public static Comparator<Currency> CurrencyCodeComparator = new Comparator<Currency>() {

        @Override
	public int compare(Currency s1, Currency s2) {
	   String Currency1 = s1.getTargetCurrencyCode().toUpperCase();
	   String Currency2 = s2.getTargetCurrencyCode().toUpperCase();

	   //ascending order
	   return Currency1.compareTo(Currency2);
    }};
    
    /**
     * Sort by currency name, descending
     */
    public static Comparator<Currency> ReverseCurrencySymbolComparator = new Comparator<Currency>() {

        @Override
	public int compare(Currency s1, Currency s2) {
	   String Currency1 = s1.getTargetCurrencyCode().toUpperCase();
	   String Currency2 = s2.getTargetCurrencyCode().toUpperCase();

	   //descending order
	   return Currency2.compareTo(Currency1);
    }};
}
