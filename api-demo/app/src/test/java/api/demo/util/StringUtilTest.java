package api.demo.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {


    @Test
    void testStringToJson() {
        //When
        String res = StringUtil.stringToJson("key", "value");

        assertThat(res).isEqualTo("{\"key\":\"value\"}");
    }

    @Test
    void testStringToJsonArray() {
        //When
        String res = StringUtil.stringToJsonArray("key", "value", "key", "value");

        assertThat(res).isEqualTo("[{\"key\":\"value\"},{\"key\":\"value\"}]");
    }
}