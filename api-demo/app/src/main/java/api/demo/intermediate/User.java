package api.demo.intermediate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
    private final String username;
    private final int tweetCount;
    private final long lastTweet;
    private final String [] interests;
}
