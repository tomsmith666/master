//package com.blog.job;
//
//import com.blog.doman.entity.Article;
//import com.blog.service.ArticleService;
//import com.blog.utils.RedisCache;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
////定时执行，每隔5秒把Redis中的浏览量更新到数据库中
//@Component
//public class UpdateViewCountJob {
//    @Autowired
//    RedisCache redisCache;
//    @Autowired
//    ArticleService articleService;
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void updateviewcount(){
//        Map<String, Integer> articleViewCount = redisCache.getCacheMap("ArticleViewCount");
//        List<Article> collect = articleViewCount.entrySet().stream()
//                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
//                .collect(Collectors.toList());
//        articleService.updateBatchById(collect);
//    }
//}
