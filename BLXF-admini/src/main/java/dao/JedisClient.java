package dao;

/**
 * @Auther: blxf
 * @Date: 2019-08-08 21:55
 * @Description:
 */
public interface JedisClient {
    public String get(String key);

    public String set(String key, String value);

    public String hget(String hkey, String key);

    public long hset(String hkey, String key, String value);

    public long incr(String key);

    public long expire(String key, int second);

    public long ttl(String key);

    public long del(String key);

    public long hdel(String hkey, String key);

    public void cleanCache();
}
