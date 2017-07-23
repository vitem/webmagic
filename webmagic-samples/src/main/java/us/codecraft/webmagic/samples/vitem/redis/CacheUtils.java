package us.codecraft.webmagic.samples.vitem.redis;


import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public final class CacheUtils {

    public static final int EXPIRE_SECOND_DEFAULT = 3600*24*3;

    private static JedisPool pool;
    
    static {
        String ip = "127.0.0.1";
        //String pwd = AppConfig.getTrimProperty("redis.password");
        int port = 6379;
        int timeout = 1000;
        int maxActive = 50;
        int maxIdle = 10;
        JedisPoolConfig jpc =   new JedisPoolConfig();
        jpc.setMaxTotal(maxActive);
        jpc.setMaxIdle(maxIdle);
        pool = new JedisPool(jpc, ip,port,timeout,null);
    }

    /**
     * put 如果Key已经存在，则覆盖
     * @param key
     * @param fieldVal{
     * 				   Map.Entry<String,Object>,
     * 				   String,
     * 				   List<Object>,
     *                 set
     *                 }
     * @param expireTime
     */
    public static void put(final String key,String field,Object fieldVal,Integer expireTime){
    	
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Pipeline pipeline = jedis.pipelined();
            
            if(fieldVal instanceof Map){
            	Map map = (Map)fieldVal;
            	Iterator<Map.Entry<String,Object>> it = map.entrySet().iterator();
            	Map.Entry<String,Object> me;
                String tmpKey;
                Object val;
                String sVal;
            	while (it.hasNext()){
                    me = it.next();
                    tmpKey = String.valueOf(me.getKey());
                    val = me.getValue();
                    sVal = JSON.toJSONString(val);
                    pipeline.hset(key, tmpKey, sVal);
                }
        	}else if(fieldVal instanceof String){
        		pipeline.set(key,String.valueOf(fieldVal));
        	}else if(fieldVal instanceof List){
        		List list = (List)fieldVal;
        		for(int i=0;i<list.size();i++){
        			pipeline.rpush(key, JSON.toJSONString(list.get(i)));
        		}
        	}else if(fieldVal instanceof Object){
        		if(StringUtils.isEmpty(field)){
        			pipeline.set(key,JSON.toJSONString(fieldVal));
        		}else{
        			pipeline.hset(key,field,JSON.toJSONString(fieldVal));
        		}
        	}
            pipeline.sync();
            if(expireTime==null){
            	expireTime = EXPIRE_SECOND_DEFAULT;
            }
            pipeline.expire(key,expireTime);
        } finally {
            if(jedis != null){
                pool.returnResource(jedis);
            }
        }
    }
    
    /**
     * tianjia
     * @param key
     * @param field
     * @param fieldVal
     */
    public static void put(final String key,String field,Object fieldVal){
    	put(key,field, fieldVal,null);
    }
    
    /**
     * tianjia
     * @param key
     * @param fieldVal
     */
    public static void put(final String key,Object fieldVal){
    	put(key,null, fieldVal,null);
    }
    /**
     * tianjia
     * @param key
     * @param fieldVal
     * @param expireTime  时间
     */
    public static void put(final String key,Object fieldVal,Integer expireTime){
    	put(key,null, fieldVal,null);
    }
    /**
     * 更新
     * @param fieldVal{
     * 				   Map.Entry<String,Object> ---要想整体跟新:update(String key,Map<?,?> map),要想跟新key下域中的值：update(String key,String field,Object fieldVal);
     * 				   String---update(String key,Object fieldVal)
     * 				   List<Object>,update(String key,List<?>)
     *                 set
     *                 }
     * @param key  
     * @param field       域
     * @param fieldVal    值
     */
    public static void update(final String key,String field,Object fieldVal,Integer expireTime){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Pipeline pipeline = jedis.pipelined();
            if(fieldVal instanceof Map<?,?>){
            	if(StringUtils.isEmpty(field)){
            		Map map = (Map)fieldVal;
                	Iterator<Map.Entry<String,Object>> it = map.entrySet().iterator();
                	Map.Entry<String,Object> me;
                    String tmpKey;
                    Object val;
                    String sVal;
                	while (it.hasNext()){
                        me = it.next();
                        tmpKey = String.valueOf(me.getKey());
                        val = me.getValue();
                        sVal = JSON.toJSONString(val);
                        pipeline.hset(key, tmpKey, sVal);
                    }
            	}else{
                	pipeline.hset(key, field, JSON.toJSONString(fieldVal));
                }
        	}else if(fieldVal instanceof String){
        		String value = (String)fieldVal;
        		pipeline.set(key,value);
        	}else if(fieldVal instanceof List){
        		remove(key);
        		List list = (List)fieldVal;
        		for(int i=0;i<list.size();i++){
        			pipeline.rpush(key, JSON.toJSONString(list.get(i)));
        		}
        	}else if(fieldVal instanceof Object){
        		if(StringUtils.isEmpty(field)){
        			pipeline.set(key,JSON.toJSONString(fieldVal));
        		}else{
        			pipeline.hset(key,field,JSON.toJSONString(fieldVal));
        		}
        	}
            pipeline.sync();
            if(expireTime!=null){
            	expireTime = EXPIRE_SECOND_DEFAULT;
            }
            pipeline.expire(key,expireTime);
        } finally {
            if(jedis != null){
                pool.returnResource(jedis);
            }

        }
    }
    /**
     * 更新
     * @param key
     * @param field       域
     * @param fieldVal    值
     */
    public static void update(final String key,String field,Object fieldVal){
    	update(key, field, fieldVal,null);
    }
    
    /**
     * 更新
     * @param key
     * @param fieldVal
     */
    public static void update(final String key,Object fieldVal){
    	update(key, null, fieldVal,null);
    }
    
    /**
     * 更新
     * @param key
     * @param fieldVal    值
     * @param expireTime  时间
     */
    public static void update(final String key,Object fieldVal,Integer expireTime){
    	update(key, null, fieldVal,expireTime);
    }
    /**
     * 删除一系列的key
     * 时间复杂度中的N表示删除的Key数量。从数据库删除中参数中指定的keys，如果指定键不存在，则直接忽略。
     * 还需要另行指出的是，如果指定的Key关联的数据类型不是String类型，而是List、Set、Hashes和Sorted Set等容器类型，
     * 该命令删除每个键的时间复杂度为O(M)，其中M表示容器中元素的数量。而对于String类型的Key，其时间复杂度为O(1)
     * @param keys
     */
    public  static void remove(final String... keys){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.del(keys);
        } finally {
            if(jedis != null){
                pool.returnResource(jedis);
            }
        }
    }
    
    /**
     * 删除map中的key下某些域中的值
     * 该命令删除每个键的时间复杂度为O(M)，其中M表示容器中元素的数量
     * @param key
     * @param fields
     */
    public  static void remove(final String key, final String fields){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.hdel(key,fields);
        } finally {
            if(jedis != null){
                pool.returnResource(jedis);
            }
        }
    }
    
    /**
     * 确认一个key是否存在
     * 只能查询key是否存在，不能查询域是否存在，要更新域中的值不必判断，直接更新udate即可
     * @param key
     */
    public  static boolean containsKey(final String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.exists(key);
        } finally {
            if(jedis != null){
                pool.returnResource(jedis);
            }
        }
    }
    
    /**
     * 时间复杂度中的N表示数据库中Key的数量。获取所有匹配pattern参数的Keys。
     * 需要说明的是，在我们的正常操作中应该尽量避免对该命令的调用，
     * 因为对于大型数据库而言，该命令是非常耗时的，对Redis服务器的性能打击也是比较大的。pattern支持glob-style的通配符格式，
     * 如*表示任意一个或多个字符，?表示任意字符，[abc]表示方括号中任意一个字母。
     * @param pattern
     * @return
     */
    public static Set<String> keys(final String pattern){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.keys(pattern);
        } finally {
            if(jedis != null){
                pool.returnResource(jedis);
            }

        }
    }

   
    
    /**
     * 返回值类型   none (key不存在) 
     * 			 string (字符串) , 返回值String，返回 key 所关联的字符串值。当 key 不存在时，返回 nil ，否则，返回 key 的值
     * 		     list (列表) , 返回值List<String>，返回列表 key 中指定区间内的元素,注意：在这个地方时获取了key下全部的的List中元素， 要获取key下某些index的元素，请使用get(key,start,end)方法
     *           hash (哈希表) 返回值Map<String,String>，返回哈希表 key 中，所有的域和值。在返回值里，紧跟每个域名(field name)之后是域的值(value)， 所以返回值的长度是哈希表大小的两倍。
     * remark
     * @param key
     * @return
     */
    public static Object get(final String key){
    	Object obj = null;
        Jedis jedis = null;
        jedis = pool.getResource();
        String keyType = jedis.type(key);
        try {
        	if(keyType.equals("string")){
        		String string = jedis.get(key);
        		obj = string.equals("nil")?null:string;
        	}else if(keyType.equals("list")){
        		//返回值List<String> ，如果传入的是List<Object> 请使用反序列化
        		obj = jedis.lrange(key, 0, -1);
        	}else if(keyType.equals("hash")){
        		//返回值Map<String, String>，如果你的是Map<String, Object>请自行转换
        		obj = jedis.hgetAll(key);
        	}
        	return obj;
        } finally {
            if(jedis != null){
                pool.returnResource(jedis);
            }
        }
    }
    
    /**
     * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。超出范围的下标 超出范围的下标值不会引起错误。
	 * 假如你有一个包含一百个元素的列表，对该列表执行 LRANGE list 0 10 ，结果是一个包含11个元素的列表，这表明 stop 下标也在 LRANGE 命令的取值范围之内(闭区间)，这和某些语言的区间函数可能不一致，比如Ruby的 Range.new 、 Array#slice 和Python的 range() 函数。
	 * 如果 start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大，那么 LRANGE 返回一个空列表。
	 * 如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end 。
     * 时间复杂度:O(S+N)， S 为偏移量 start ， N 为指定区间内元素的数量
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static List<String> get(final String key,int start,int end){
    	Jedis jedis = null;
    	jedis = pool.getResource();
    	try {
    		return jedis.lrange(key, start, end);
        } finally {
            if(jedis != null){
                pool.returnResource(jedis);
            }
        }
    	
    }
    
    /**
     * HMGET key field [field ...]返回哈希表 key 中，一个或多个给定域的值。
     *如果给定的域不存在于哈希表，那么返回一个 nil 值。
     *因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 nil 值的表
     * @param key
     * @param field
     * @return
     */
    public static String get(final String key,final String field){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String type = jedis.type(key);
            String string = jedis.hget(key,field);
            return "niu".equals(string)?null:string;
        } finally {
            if(jedis != null){
                pool.returnResource(jedis);
            }

        }
    }
    
    /**
     * HMGET key field [field]返回哈希表 key 中，一个或多个给定域的值。
     *如果给定的域不存在于哈希表，那么返回一个 nil 值。
     *因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 nil 值的表
     * @param key
     * @param fields
     * @return
     */
    public static List<String> get(final String key,final String[] fields){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hmget(key,fields);
        } finally {
            if(jedis != null){
                pool.returnResource(jedis);
            }

        }
    }
	/**
	 * ZADD key score member [[score member] [score member] ...]
	 * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
	 * 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值，并通过重新插入这个 member 元素，来保证该 member 在正确的位置上。
	 * score 值可以是整数值或双精度浮点数。 如果 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
	 * 当 key 存在但不是有序集类型时，返回一个错误。对有序集的更多介绍请参见 sorted set 
	 * 时间复杂度:(M*log(N))， N 是有序集的基数， M 为成功添加的新成员的数量。
	 * @param key
	 * @param score
	 * @param member
	 * @return
	 */
    public  static Long zadd(final String key,double score, Object member){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key,score,String.valueOf(member));
        } finally {
            if(jedis != null){
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列。
	 * 具有相同 score 值的成员按字典序(lexicographical order)来排列(该属性是有序集提供的，不需要额外的计算)。
	 * 可选的 LIMIT 参数指定返回结果的数量及区间(就像SQL中的 SELECT LIMIT offset, count )，注意当 offset 很大时，定位 offset 的操作可能需要遍历整个有序集，此过程最坏复杂度为 O(N) 时间。
	 * 可选的 WITHSCORES 参数决定结果集是单单返回有序集的成员，还是将有序集成员及其 score 值一起返回。
	 * min 和 max 可以是 -inf 和 +inf ，这样一来，你就可以在不知道有序集的最低和最高 score 值的情况下，使用 ZRANGEBYSCORE 这类命令。
	 * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
	 * 举个例子:ZRANGEBYSCORE zset (1 5返回所有符合条件 1 < score <= 5 的成员，而ZRANGEBYSCORE zset (5 (10   则返回所有符合条件 5 < score < 10 的成员。
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Set<String> zrangeByScore(final String key,long start, long end){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrangeByScore(key, start, end);
        } finally {
            if(jedis != null){
                pool.returnResource(jedis);
            }
        }
    }
    /**
     * ZREMRANGEBYRANK key start stop
	 * 移除有序集 key 中，指定排名(rank)区间内的所有成员。
	 * 区间分别以下标参数 start 和 stop 指出，包含 start 和 stop 在内。
	 * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
	 * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
	 * 可用版本：>= 2.0.0时间复杂度:O(log(N)+M)， N 为有序集的基数，而 M 为被移除成员的数量。返回值:被移除成员的数量。
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Long zremrangeByScore(final String key,long start, long end){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zremrangeByScore(key, start, end);
        } finally {
            if(jedis != null){
                pool.returnResource(jedis);
            }
        }
    }

	/**
	 * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。
	 * 当 key 存在但不是有序集类型时，返回一个错误。    
	 * 时间复杂度:O(M*log(N))， N 为有序集的基数， M 为被成功移除的成员的数量。
	 * @param key
	 * @param members
	 * @return
	 */
    public static Long zrem(final String key,String... members){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrem(key, members);
        } finally {
            if(jedis != null){
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * HINCRBY key field increment 为哈希表 key 中的域 field 的值加上增量 increment 。
	 * 增量也可以为负数，相当于对给定域进行减法操作。
	 * 如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
	 * 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。
	 * 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。
	 * 本操作的值被限制在 64 位(bit)有符号数字表示之内。
     * @param key
     * @param field
     * @param val
     * @return
     */
    public static Long hincrby(final  String key,final String field,final long val){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hincrBy(key,field,val);
        } finally {
            if(jedis != null){
                pool.returnResource(jedis);
            }
        }
    }
    
	 

    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {

        }
        return null;
    }

    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {

        }
        return null;
    }

    public static void putObject(final String key,String field,Object fieldVal,Integer expireTime){

        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Pipeline pipeline = jedis.pipelined();

            if(StringUtils.isEmpty(field)){
                pipeline.set(key.getBytes(),serialize(fieldVal));
            }else{
                pipeline.hset(key.getBytes(),field.getBytes(),serialize(fieldVal));
            }
            pipeline.sync();
            if(expireTime!=null){
                expireTime = EXPIRE_SECOND_DEFAULT;
            }
            pipeline.expire(key,expireTime);
        } finally {
            if(jedis != null){
                pool.returnResource(jedis);
            }
        }
    }

    public static Object getObject(final String key,final String field){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            byte[] val;
            if(StringUtils.isEmpty(field)){
                val = jedis.get(key.getBytes());
            }else{
                val = jedis.hget(key.getBytes(),field.getBytes());
            }
            return unserialize(val);
        } finally {
            if(jedis != null){
                pool.returnResource(jedis);
            }

        }
    }


    public static void main(String[] args) {
        String key = "aaaaa";
        CacheUtils.put(key,"okokok");

        System.out.println(CacheUtils.get(key));
    }


}
