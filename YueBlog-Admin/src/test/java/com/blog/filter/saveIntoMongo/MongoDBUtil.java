package com.blog.filter.saveIntoMongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class MongoDBUtil {
    //规定主机和端口号
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    //规定数据库的名字
    private static final String DATABASE_NAME = "crawl_data";
    //不通过认证获取连接数据库对象
    public static MongoDatabase getConnect(){
        //连接到 mongodb 服务
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);;
        //连接到数据库
        //返回连接数据库对象
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    //处理pojo和Bson之间的编码问题
    public static CodecRegistry getCodecRegistry(){
        //进行编码处理
        return CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    }
}