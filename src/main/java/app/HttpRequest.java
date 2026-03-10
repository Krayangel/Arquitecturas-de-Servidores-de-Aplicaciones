package app;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private Map<String, String> queryParams = new HashMap<>();

    public HttpRequest(String queryString) {
        if (queryString != null && !queryString.isEmpty()) {
            for (String pair : queryString.split("&")) {
                String[] kv = pair.split("=");
                if (kv.length > 1) {
                    queryParams.put(kv[0], kv[1]);
                } else {
                    queryParams.put(kv[0], "");
                }
            }
        }
    }

    public String getValues(String name) {
        return queryParams.get(name);
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }
}
