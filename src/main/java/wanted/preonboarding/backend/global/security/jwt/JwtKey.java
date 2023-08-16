package wanted.preonboarding.backend.global.security.jwt;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtKey {
    @Value("${custom.jwt.secretKey1}")
    private String secretKey1;

    @Value("${custom.jwt.secretKey2}")
    private String secretKey2;

    @Value("${custom.jwt.secretKey3}")
    private String secretKey3;

    private Map<String, String> SECRET_KEY_SET;
    private String[] KID_SET;
    private Random randomIndex;

    @PostConstruct
    public void init() {
        SECRET_KEY_SET = new HashMap<String, String>() {
            {
                put("key1", secretKey1);
                put("key2", secretKey2);
                put("key3", secretKey3);
            }
        };
        KID_SET = SECRET_KEY_SET.keySet().toArray(new String[0]);
        randomIndex = new Random();
    }

    /**
     * SECRET_KEY_SET에서 랜덤 key 가져오기
     *
     * @return kid, keyPair
     */
    public Map.Entry<String, Key> getRandomKey() {
        String kid = KID_SET[randomIndex.nextInt(KID_SET.length)];
        String secretKey = SECRET_KEY_SET.get(kid);
        return new HashMap.SimpleEntry<>(kid, Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * kid로 Key 찾기
     * @Param kid
     * @return Key
     */
    public Key getKey(String kid) {
        String key = SECRET_KEY_SET.getOrDefault(kid, null);
        if(key == null) {
            return null;
        }
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

}
