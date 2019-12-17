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

import org.json.JSONException;

/**
 * The JSON Parser Class, contains some of utility set for gather JSON files
 * @author ShinyDove
 */
public final class JsonParser {
    
    /**
     * The hash symbol for complying the template of JSON URL Address.
     * Must be placed after URL and before ".JSON" file extension
     */
    private static final String JSON_FILENAME_PATTERN = "####";
    
    /**
     * Convert patterned URL to selected JSON File Name.
     * <br> Example: "http://example.com/####.json" compiled as "http://example.com/myfile.json"
     *  using <code>matchJsonURLAddress("http://example.com/####.json", "myfile", "####")</code>
     *  where "####" denotes as pattern and "myfile" as JSON filename (without extension)
     * 
     * @param URL JSON Pattern URL to match
     * @param JSONfilename JSON filename (no extension) to match the patterned URL
     * @param JSONfilenamePattern JSON filename pattern to be match
     * @return JSON matched URL with JSON filename replace the pattern.
     */
    public static String matchJsonURLAddress(String URL, String JSONfilename, String JSONfilenamePattern) throws JSONException {
        if ((URL.startsWith("http://") || URL.startsWith("https://")) 
                && URL.contains(String.format("/%s.json", JSONfilenamePattern))
                && !URL.contains(String.format("//%s.json", JSONfilenamePattern))) {
            return URL.replace(JSONfilenamePattern, JSONfilename);        
        } else {
            throw new JSONException("JSON URL path or patter mismatches! Required valid URL address and filename pattern must be in a URL");
        }
    }
    
    /**
     * Convert patterned URL to selected JSON File Name using generic hash symbol pattern "####".
     * <br> Example: "http://example.com/####.json" compiled as "http://example.com/myfile.json"
     *  using <code>locateJSONfile("http://example.com/####.json", "myfile")</code>
     *  where "####" denotes as pattern and "myfile" as JSON filename (without extension)
     * 
     * @param URL JSON Pattern URL to match
     * @param jsonFileName JSON filename (no extension) to match the patterned URL with "####"
     * @return JSON matched URL with JSON filename replace the pattern.
     */
    public static String locateJSONfile(String URL, String jsonFileName) {
        return matchJsonURLAddress(URL, jsonFileName, JSON_FILENAME_PATTERN);
    }
    
    
}
