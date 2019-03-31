import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

class TimeResult {
    @JsonProperty("unixtime")
    long unixTime;
    @JsonProperty("rfc1123")
    ZonedDateTime rfc1123;

    public void setRfc1123(String dateTime) {
        rfc1123 = ZonedDateTime.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(dateTime));
    }
}

