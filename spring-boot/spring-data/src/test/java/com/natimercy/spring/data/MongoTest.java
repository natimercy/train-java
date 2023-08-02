package com.natimercy.spring.data;

import com.natimercy.spring.data.entity.Person;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * TODO
 *
 * @author <a href="mailto:mercyblitz@gmail.com">natimercy</a>
 * @since 1.0.0
 */
@SpringBootTest(classes = SpringDataApplication.class)
public class MongoTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    //保存
    @Test
    public void testSave() {
        for (int i = 0; i < 10; i++) {
            Person person = new Person();
            // ObjectId.get()：获取一个唯一主键字符串
            person.setId(ObjectId.get());
            person.setName("张三" + i);
            person.setAddress("北京顺义" + i);
            person.setAge(18 + i);
            mongoTemplate.save(person);
        }
    }

    @Test
    public void testFindAll() {
        List<Person> list = mongoTemplate.findAll(Person.class);
        for (Person person : list) {
            System.out.println(person);
        }
    }

    @Test
    public void testFind() {
        Query query = new Query(Criteria.where("age")
                .lt(20));
        List<Person> list = mongoTemplate.find(query, Person.class);

        for (Person person : list) {
            System.out.println(person);
        }
    }

    @Test
    public void testPage() {
        Criteria criteria = Criteria.where("age").lt(30);
        //1、查询总数
        Query queryCount = new Query(criteria);
        long count = mongoTemplate.count(queryCount, Person.class);
        System.out.println(count);

        Query queryLimit = new Query(criteria)
                // 设置每页查询条数
                .limit(2)
                // 开启查询的条数 （page-1）*size
                .skip(2);
        List<Person> list = mongoTemplate.find(queryLimit, Person.class);
        for (Person person : list) {
            System.out.println(person);
        }
    }

    @Test
    public void testUpdate() {
        // 1、条件
        Query query = Query.query(Criteria.where("id").is("5fe404c26a787e3b50d8d5ad"));
        //2、更新的数据
        Update update = new Update();
        update.set("age", 20);
        mongoTemplate.updateFirst(query, update, Person.class);
    }

    @Test
    public void testRemove() {
        Query query = Query.query(Criteria.where("id").is("5fe404c26a787e3b50d8d5ad"));
        mongoTemplate.remove(query, Person.class);
    }
}
