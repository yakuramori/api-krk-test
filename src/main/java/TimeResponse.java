import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;

class TimeResponse {
    @JsonProperty("error")
    List<String> error = Arrays.asList();
    @JsonProperty("result")
    TimeResult result;
}
