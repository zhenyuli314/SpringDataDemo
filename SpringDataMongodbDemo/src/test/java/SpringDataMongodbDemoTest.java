import com.lzy.SpringDataMongodbDemoApplication;
import com.lzy.entity.Article;
import com.lzy.mapper.ArticleMapper;
import javafx.geometry.VPos;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Spring data mongodb demo test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringDataMongodbDemoApplication.class)
public class SpringDataMongodbDemoTest {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Test save.
     */
    @Test
    public void testSave(){
        Article article = new Article();
        article.setId(1).setName("mongodb教程3").setContent("这是一本关于mongodb的教程").setHits(3000);
        articleMapper.save(article);
        article.setId(2).setName("mongodb教程4").setContent("这是一本关于mongodb的教程").setHits(4000);
        articleMapper.save(article);

    }

    /**
     * Test update.
     */
    @Test
    public void testUpdate(){
        Article article = new Article();
        article.setId(1).setName("mongodb教程").setContent("这是一本修\"改后的\"关于mongodb的教程").setHits(1000);
        articleMapper.save(article);
    }

    /**
     * Test delete.
     */
    @Test
    public void testDelete(){
        articleMapper.deleteById(1L);
    }

    /**
     * Test get.
     */
    @Test
    public void testGet(){
        //查询所有
        List<Article> articles = articleMapper.findAll();
        System.out.println(articles);

        //根据主键查询
        Optional<Article> article = articleMapper.findById(1L);
        System.out.println(article.get());

        //分页加排序
        Sort sort = Sort.by(Sort.Order.desc("hits"));
        List<Article> all = articleMapper.findAll(sort);
        System.out.println(all);

        Pageable pageable = PageRequest.of(0, 2,sort);
        Page<Article> page = articleMapper.findAll(pageable);
        System.out.println(page.getContent());
    }

    /**
     * 命名规则查询
     */
    @Test
    public void testNameGet(){
        List<Article> list = articleMapper.findByNameLike("教程");
        System.out.println(list);
        List<Article> list1 = articleMapper.findByHitsGreaterThan(3000);
        System.out.println(list1);
    }

    /**
     * Test template.
     */
    @Test
    public void testTemplate(){
        //增加点赞数（效率低）
        // 此方法虽然实现起来比较简单，但是执行效率并不高，因为我只
        // 需要将点赞数加1就可以了，没必要查询出所有字段修改后再更新所有字段。(蝴蝶效应)
        Article article = articleMapper.findById(1L).get();
        article.setHits(article.getHits()+1);
        articleMapper.save(article);

        //增加点击数（效率高）
        Query query = Query.query(Criteria.where("_id").is(1));
        Update update = new Update();
        update.inc("hits");
        mongoTemplate.updateFirst(query, update, Article.class);
    }
}
