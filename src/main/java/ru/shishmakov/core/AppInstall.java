package ru.shishmakov.core;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.Locale.ENGLISH;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.lowerCase;

public class AppInstall {
    private static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", ENGLISH);
    private static final String INSTALL_TIME = "install_time";
    private static final String APP_ID = "app_id";
    private static final String COUNTRY_CODE = "country_code";
    private static final Set<String> KEYS = Sets.newHashSet(INSTALL_TIME, APP_ID, COUNTRY_CODE);

    private final long stamp;
    private final String appId;
    private final String country;
    private int quantity;

    private AppInstall(long stamp, String appId, String country) {
        this.stamp = stamp;
        this.appId = appId;
        this.country = country;
    }

    public long getStamp() {
        return stamp;
    }

    public String getAppId() {
        return appId;
    }

    public String getCountry() {
        return country;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static AppInstall build(String query) throws ParseException {
        Map<String, String> parameters = buildParameters(query);
        String installTime = parameters.get(INSTALL_TIME);
        String appId = lowerCase(parameters.get(APP_ID));
        String country = lowerCase(parameters.getOrDefault(COUNTRY_CODE, EMPTY));

        LocalDateTime dateTime = LocalDateTime.parse(installTime, PATTERN);
        long stamp = dateTime.withMinute((dateTime.getMinute() / 5) * 5)
                .withSecond(0).withNano(0)
                .toInstant(ZoneOffset.UTC).toEpochMilli();
        return new AppInstall(stamp, appId, country);
    }

    private static Map<String, String> buildParameters(String query) {
        Map<String, String> parameters = new HashMap<>(KEYS.size());
        String[] pairs = query.split("&");
        try {
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                String key = lowerCase(URLDecoder.decode(pair.substring(0, idx), "UTF-8"));
                String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                if (KEYS.contains(key)) parameters.put(key, value);
            }
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Parse query error", e);
        }
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppInstall that = (AppInstall) o;
        return Objects.equals(stamp, that.stamp) &&
                Objects.equals(appId, that.appId) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stamp, appId, country);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("stamp", stamp)
                .add("appId", appId)
                .add("country", country)
                .add("quantity", quantity)
                .toString();
    }
}
