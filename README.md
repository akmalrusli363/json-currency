# json-currency
**json-currency** is a JSON currency command line to obtain currency exchange table and rates from JSON currency exchange site.

> **NOTICE**: This is an incomplete version of json-currency, so you can contribute for extra things or corrections related for the project repository.

## Requirement & Installation
Java 8 or newer is required to use JSON Currency. Also, JSON-Java jar archive is included for fetching any information provided from JSON file.

**How to download & use?**
First, clone the master repository using `git clone` to your favorite directory
```
$ git clone https://github.com/akmalrusli363/json-currency.git (yourDir)
```
> Or download it directly from GitHub as .zip and unzip the downloaded repository at your favorite directory

Locate executable directory located at `dist/` from this repository.
```
$ cd json-currency
$ cd dist
```
You can copy the dist directory as executable jar directory for json-currency (don't forget for libs too!) and simply execute it using `java -jar jsoncurrency.jar` inside the directory.
```
$ java -jar jsoncurrency.jar
```
> WARNING: Don't attempt to run the jar directly, it would impossible to show without `java -jar` prefixes!


## Execute & Running
To execute JSON Currency, locate the directory from root repository to `./dist/` (or locate the executable jar directory you've copied it) and execute `java -jar jsoncurrency.jar` inside the directory.

Basically, if you run the `.jar` without arguments, it will show the help guide how to use the `jsoncurrency.jar` file.


## Usage/Parameters
There are two basic command to comply in jsoncurrency.jar with addtional two commands:
1. `java -jar jsoncurrency.jar` or `java -jar jsoncurrency.jar help` - help guides for `jsoncurrency.jar`
2. `java -jar jsoncurrency.jar demo` to demonstrate how to use `jsoncurrency.jar` with usage `usd -sortByCurrencyCode:true`
3. `java -jar jsoncurrency.jar <currencyCode> (-buy/-sell/-showInLocalCurrency:[true/false]) (-sortByCurrencyCode:[true/false])` - display exchange rate table information for a currency
4. `java -jar jsoncurrency.jar <currencyFrom> <currencyTo> (values)` - display exchange rate between local and target currency.

### Currency exchange information for a currency
```
$ java -jar jsoncurrency.jar <currencyCode> (-showInLocalCurrency) (-sortByCurrencyCode)
```
For currency exchange information about the currency, type `jsoncurrency.jar <currencyCode>` for displaying the currency exchange table of `<currencyCode>`
> Example: `java -jar jsoncurrency.jar usd` to display currency exchange table for USD.

**Output for USD currency exchange table**
```
USD exchange information: (sell / buy)
--------------------------------------------------------------------------
Afghan afghani                 : AFN        77.77778 | USD         0.01286
Albanian lek                   : ALL       109.30246 | USD         0.00915
Algerian Dinar                 : DZD       118.59293 | USD         0.00843
Angolan kwanza                 : AOA       466.26829 | USD         0.00214
...                            : ...             ... | ...             ...
Yemeni rial                    : YER       246.59142 | USD         0.00406
Zambian kwacha                 : ZMW        14.23985 | USD         0.07023
---------------------------------------------------
Done!
```

### Currency buy/sell information for a currency
```
$ java -jar jsoncurrency.jar <currencyCode> -buy/-sell (-sortByCurrencyCode)
```
For currency buy/sell information about the currency, type `jsoncurrency.jar <currencyCode> -buy/-sell` for displaying the currency buy/sell table of `<currencyCode>`
> Example: `java -jar jsoncurrency.jar usd -sell` to display currency sell table for USD.

**Output for USD currency sell table (not to compare!)**
```
USD currency exchange information:
----------------------------------------------------
Afghan afghani                 : AFN        77.77778
Albanian lek                   : ALL       109.30246
Algerian Dinar                 : DZD       118.59293
Angolan kwanza                 : AOA       466.26829
...                            : ...             ...
Yemeni rial                    : YER       246.59142
Zambian kwacha                 : ZMW        14.23985
---------------------------------------------------
Done!
```

> Another example: `java -jar jsoncurrency.jar idr -buy` to display currency buy table for IDR.

**Output for IDR currency buy table (not to compare!)**
```
IDR currency buy information:
----------------------------------------------------
Afghan afghani                 : IDR       179.70526
Albanian lek                   : IDR       127.87522
Algerian Dinar                 : IDR       117.31416
Angolan kwanza                 : IDR        29.97647
...                            : ...             ...
Yemeni rial                    : IDR        56.68111
Zambian kwacha                 : IDR       981.54648
---------------------------------------------------
Done!
```

### Currency exchange information for two currencies
```
$ java -jar jsoncurrency.jar <currencyFrom> <currencyTo>
```
For currency exchange information between two currency, type `jsoncurrency.jar <currencyFrom> <currencyTo>` to display the currency exchange rate between `<currencyFrom>` and `<currencyTo>`
> Example: `java -jar jsoncurrency.jar usd eur` to display currency exchange table for USD and EUR.

**Output for currency exchange between USD and EUR (not to compare!)**
```
USD <--> EUR exchange information:
---------------------------------------------------
[!] Target currency : Euro
[B] USD to EUR rate : EUR         0.89779
[S] EUR to USD rate : USD         1.11385
```

### Calculate currency exchange rate for specified local currency rate
```
$ java -jar jsoncurrency.jar <currencyFrom> <currencyTo> <localCurrencyRate>
```
For currency exchange information between two currency, type `jsoncurrency.jar <currencyFrom> <currencyTo> <localCurrencyRate>` to display the currency exchange rate between `<currencyFrom>` and `<currencyTo>`
> Example: `java -jar jsoncurrency.jar usd eur 20` to display currency exchange table for USD and EUR.

**Output for currency exchange rate for USD 20 to EUR (not to compare!)**
```
USD 20.00 to EUR exchange rate:
---------------------------------------------------
[!] Target currency : Euro
[B] USD to EUR rate : EUR        17.95581
```

### Advanced parameters
To check more advanced parameters, see `jsoncurrency.jar (help)` or just type `jsoncurrency.jar` for more information.

## Contribution
To contribute with this project, use **Apache NetBeans 8** or newer by open the project into your workspace. The repository may or would need to clean up before decided to take directly to git from Team Git option.
