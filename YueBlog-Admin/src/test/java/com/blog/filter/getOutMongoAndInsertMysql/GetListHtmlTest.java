package com.blog.filter.getOutMongoAndInsertMysql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.blog.BlogAdminApplication;
import com.blog.doman.entity.ForumTopic3;
import com.blog.doman.entity.UserRole;
import com.blog.filter.saveIntoMongo.MongoDBUtil;
import com.blog.filter.saveIntoMongo.User;
import com.blog.mapper.ForumTopic3Mapper;
import com.blog.service.ForumTopic3Service;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogAdminApplication.class)
public class GetListHtmlTest {



    @Autowired
    private ForumTopic3Service forumTopic3Service;

    @Test
    public void getListHtml() throws Exception {
        MongoCollection<User> collection =  MongoDBUtil.getConnect().getCollection("forum_topic3", User.class).withCodecRegistry(MongoDBUtil.getCodecRegistry());

//    ArrayList<String> nameList = new ArrayList<>();
//    Collection<? super User> results = collection.find().projection(Projections.include("pageHtml")).into(new ArrayList<>());
//    System.out.println(results);
// 查询所有文档
        List<User> userList = new ArrayList<>();
        MongoCursor<User> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            User user = cursor.next();
            userList.add(user);
        }
//        System.out.println(userList);


//        //给每个analysis设置url
//        List<String> collect = userList.stream()
//            .map(User::getUrl)
//            .collect(Collectors.toList());

//        for (String single:collect){
//            ForumTopic3 forumTopic3 = new ForumTopic3();
//            //把每个url存到analysis实体类的TopicUrl属性中
//            forumTopic3.setUrl(single);
//            //把设置完url的analysis保存到数据库
//            save(forumTopic3);
//        }

        List<ForumTopic3> userRoleList = userList.stream()
        .map(item -> new ForumTopic3(null,item.getEncryptid(),item.getWeixin(),item.getEmail(),item.getName(),item.getTelephone(),item.getLocation(),item.getUrl()))
        .collect(Collectors.toList());
        forumTopic3Service.saveBatch(userRoleList);

    }



//    @Test
//    public void GetListHtmltest(){
//        getListHtml.testMethod();
//    }
}
