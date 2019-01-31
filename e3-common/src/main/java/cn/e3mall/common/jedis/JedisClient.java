package cn.e3mall.common.jedis;

import java.util.List;

public interface JedisClient {

	public String set(String key, String value);
	public String get(String key);
	public Boolean exists(String key);
	public Long expire(String key, int seconds);
	public Long ttl(String key);
	public Long incr(String key);
	public Long hset(String key, String field, String value);
	public String hget(String key, String field);
	public Long hdel(String key, String... field);
	public Boolean hexists(String key, String field);
	public List<String> hvals(String key);
	public Long del(String key);
	
}
