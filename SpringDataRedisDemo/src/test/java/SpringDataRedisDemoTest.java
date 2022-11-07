import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzy.springdataredisdemo.SpringDataRedisDemoApplication;
import com.lzy.springdataredisdemo.entity.Article;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * The type Spring data redis demo test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringDataRedisDemoApplication.class)
public class SpringDataRedisDemoTest {

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * The String value operations.
     */
    ValueOperations<String, String> stringValueOperations;
    /**
     * The Hash operations.
     */
    HashOperations<String, String, Article> hashOperations;
    /**
     * The List operations.
     */
    ListOperations<Object, Object> listOperations;
    /**
     * The Set operations.
     */
    SetOperations<String, String> setOperations;
    /**
     * The Z set operations.
     */
    ZSetOperations<String,String> zSetOperations;

    /**
     * Init.
     */
    @Before
    public void init() {
        stringValueOperations = stringRedisTemplate.opsForValue();

        //设置hashKey的序列化方式
        stringRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //设置hashValue的序列化方式
        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jsonRedisSerializer.setObjectMapper(mapper);
        stringRedisTemplate.setHashValueSerializer(jsonRedisSerializer);
        hashOperations = stringRedisTemplate.opsForHash();

        listOperations = redisTemplate.opsForList();

        setOperations = stringRedisTemplate.opsForSet();

        zSetOperations = stringRedisTemplate.opsForZSet();
    }

    /**
     * StringTest
     */
    @Test
    public void testSet() {
        //普通保存字符串格式的key-value
        stringValueOperations.set("name", "lizhenyu");
        //设置有效时间为10sec的限时保存
        stringValueOperations.set("name1", "lizhenyu1", 10, TimeUnit.SECONDS);
        //替换已有数据，offset为修改的起始位置
        stringValueOperations.set("name", "xx", 2);
        //当key不存在时，执行保存；存在时，什么都不做
        stringValueOperations.setIfAbsent("name2", "123");
        //利用Map进行批量保存
        HashMap<String, String> map = new HashMap<>();
        map.put("name3", "lizhenyu3");
        map.put("name4", "lizhenyu4");
        map.put("name5", "lizhenyu5");
        stringValueOperations.multiSet(map);
        //追加
        stringValueOperations.append("name", "aaaaaa");
    }

    /**
     * Test get.
     */
    @Test
    public void testGet() {
        //根据key获取value
        String name = stringValueOperations.get("name");
        System.out.println(name);
        //截取value，包含start和end
        String name1 = stringValueOperations.get("name", 0, 2);
        System.out.println(name1);
        //批量获取
        List<String> list = stringValueOperations.multiGet(Arrays.asList("name", "name3", "name2"));
        System.out.println(list);
        //获取value的长度
        System.out.println(stringValueOperations.size("name"));
    }

    /**
     * Test increment.
     */
    @Test
    public void testIncrement() {
        stringValueOperations.set("age", "18");
        //自增
        stringValueOperations.increment("age");
        stringValueOperations.increment("age", 5);
        //自减
        stringValueOperations.decrement("age");
        stringValueOperations.decrement("age", 5);
    }

    /**
     * Test delete.
     */
    @Test
    public void testDelete() {
        //注意用的是redisTemplate
        redisTemplate.delete("name");
        //批量删除
        redisTemplate.delete(Arrays.asList("name2", "name3"));
    }

    /**
     * HashTest
     */
    @Test
    public void testPutHash() {
        Article article = new Article("lzy", new Date(), "book");
        hashOperations.put("article", "1", article);
    }

    /**
     * Test get hash.
     */
    @Test
    public void testGetHash() {
        //获取指定键的值
        Article article = hashOperations.get("article", "1");
        //获取key下的所有hashMap
        Map<String, Article> entries = hashOperations.entries("article");
        //获取key下的所有hashKey
        Set<String> hashKeys = hashOperations.keys("article");
        //获取key下的所有hashValue
        List<Article> hashValues = hashOperations.values("article");
    }

    /**
     * Test delete hash.
     */
    @Test
    public void testDeleteHash() {
        //当hashKey被删完后，key也会消失
        hashOperations.delete("article", "1");
    }

    /**
     * ListTest
     */
    @Test
    public void testAddList() {

        listOperations.leftPush("books", new Article("lzy", new Date(), "book1"));
        listOperations.leftPush("books", new Article("lzy", new Date(), "book2"));
        listOperations.leftPush("books", new Article("lzy", new Date(), "book3"));

        //注意范型一定要一直，否则不会解析
        ArrayList<Object> list = new ArrayList<>();
        list.add(new Article("lzy", new Date(), "book4"));
        list.add(new Article("lzy", new Date(), "book5"));
        list.add(new Article("lzy", new Date(), "book6"));
        listOperations.rightPushAll("books", list);

    }

    /**
     * Test get list.
     */
    @Test
    public void testGetList() {
        //根据下标查询
        Article article = (Article) listOperations.index("books", 0);
        System.out.println(article);
        //根据范围查询
        List<Object> articles = listOperations.range("books", 0, 2);
        System.out.println(articles);
    }

    /**
     * Test delete list.
     */
    @Test
    public void testDeleteList() {
        //从左边删除一个元素
        Object leftPop = listOperations.leftPop("books");
        System.out.println(leftPop);
        //从右边删除一个元素
        Object rightPop = listOperations.rightPop("books");
        System.out.println(rightPop);
        //删除指定的值,count表示list中重复值的第几个
        listOperations.remove("books", 1, new Article("lzy", new Date(), "book2"));
    }

    /**
     * SetTest
     */
    @Test
    public void testAddSet(){
        setOperations.add("students", "zhang3","li4","wang5");
    }

    /**
     * Test find set.
     */
    @Test
    public void testFindSet(){
        //查询所有
        Set<String> students = setOperations.members("students");
        System.out.println(students);
        //随机一个
        String s = setOperations.randomMember("students");
        //随机多个,可能会重复
        List<String> list = setOperations.randomMembers("students", 2);
        System.out.println(list);
    }

    /**
     * Test remove set.
     */
    @Test
    public void testRemoveSet(){
        //移除指定的元素，返回成功移除的个数。如果不存在就不操作
        Long count = setOperations.remove("students", "zhang3", "li4", "liu6");

        //随机移除
        List<String> students = setOperations.pop("students", 2);
    }

    /**
     * Test compare set.
     */
    @Test //多集合操作，交集、并集、差集
    public void testCompareSet(){
        setOperations.add("names1", "zhang3","li4","wang5");
        setOperations.add("names2", "zhang3","li4","zhao6");
        //交集
        Set<String> intersect = setOperations.intersect("names1", "names2");
        System.out.println(intersect);
        //并集
        Set<String> union = setOperations.union("names1", "names2");
        System.out.println(union);
        //差集【第一个集合中有，第二个集合中没有】
        Set<String> difference = setOperations.difference("names1", "names2");
        System.out.println(difference);
    }

    /**
     * ZsetTest
     */
    @Test
    public void testAddZset(){
        zSetOperations.add("students", "张三", 90);
        zSetOperations.add("students", "李四", 60);
        zSetOperations.add("students", "王五", 100);
    }

    /**
     * Test increment zset.
     */
    @Test//分数的增减
    public void testIncrementZset(){
        zSetOperations.incrementScore("students", "张三", -50);
    }

    /**
     * Test get zset.
     */
    @Test
    public void testGetZset(){
        //查询一个元素的分数
        Double score = zSetOperations.score("students", "张三");
        System.out.println(score);

        //根据排名查询一个范围的结果
        Set<String> students = zSetOperations.range("students", 0, 2);
        System.out.println(students);
        Set<ZSetOperations.TypedTuple<String>> students1 = zSetOperations.rangeWithScores("students", 0, 2);
        for (ZSetOperations.TypedTuple<String> tuple : students1) {
            System.out.println(tuple.getValue()+":"+tuple.getScore());
        }

        System.out.println("-------------------------------------------------------------------");
        //根据分数查询一个范围的结果
        Set<String> students2 = zSetOperations.rangeByScore("students", 0, 100);
        System.out.println(students2);
        Set<ZSetOperations.TypedTuple<String>> students3 = zSetOperations.rangeByScoreWithScores("students", 0, 100);
        for (ZSetOperations.TypedTuple<String> tuple : students3) {
            System.out.println(tuple.getValue()+":"+tuple.getScore());
        }
    }

    /**
     * Test count zset.
     */
    @Test
    public void testCountZset(){
        //查询所有元素的个数
        Long zCard = zSetOperations.zCard("students");
        System.out.println(zCard);
        //根据分数范围查询元素的个数
        Long count = zSetOperations.count("students", 0, 70);
        System.out.println(count);
    }

    /**
     * Test remove zset.
     */
    @Test
    public void testRemoveZset(){
//        zSetOperations.remove("students", "张三");
        zSetOperations.removeRange("students", 0, 2);
//        zSetOperations.removeRangeByScore("students", 0, 100);
    }

    /**
     * Test.
     */
//试试redisTemplate
    @Test
    public void test() {
        ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
        operations.set("key", new Article("lzy", new Date(), "book"));
        Article article = (Article) operations.get("key");
        System.out.println(article);
    }

    //模拟发送消息
    @Test
    public void testMessage(){
        redisTemplate.convertAndSend("mytopic","asd");
    }
}
