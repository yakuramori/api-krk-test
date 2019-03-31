import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;

class TickerInfo {
    @JsonProperty("a")
    List<Number> a = Arrays.asList();
    @JsonProperty("b")
    List<Number> b = Arrays.asList();
    @JsonProperty("c")
    List<Number> c = Arrays.asList();
    @JsonProperty("v")
    List<Number> v = Arrays.asList();
    @JsonProperty("p")
    List<Number> p = Arrays.asList();
    @JsonProperty("t")
    List<Number> t = Arrays.asList();
    @JsonProperty("l")
    List<Number> l = Arrays.asList();
    @JsonProperty("h")
    List<Number> h = Arrays.asList();
    @JsonProperty("o")
    Number o;
}
