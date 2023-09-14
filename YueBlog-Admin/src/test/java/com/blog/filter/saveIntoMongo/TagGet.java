package com.blog.filter.saveIntoMongo;


import com.google.common.collect.Lists;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Request.Builder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author chizongze created on 2023/7/25
 * @version $
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TagGet extends AbstractForumWorker {
    public void testMethod() throws Exception {
        beginGrab();
    }
    private void beginGrab() throws Exception {
        //    正式使用时用的，为了防止分页爬取大量数据被跟踪地址，到时会有代理服务器去爬取数据
            int pagenum=1;
            while (true){
                String url = String.format( "https://c.vanpeople.com/zufang/?page=%d", pagenum);
                String listPageHtml = grabForHtml(url);
                Document document1 = Jsoup.parse(listPageHtml);
                //拿到html里面的body
                Element element = document1.body();
                //先拿到class=info-content-slide的div
                Elements ul = element.select("ul[class=c-list]");
                Elements li = ul.select("li");
                String liInfo = li.text();
                if(StringUtils.isBlank(liInfo)){
                    System.out.println("已经取到最后一页，停止爬取");
                    break;
                }
                ++pagenum;
                System.out.println(pagenum);

                analyzeListPage(listPageHtml,url);
            }
        //目前使用的，用for循环爬取3页数据即可
//        int pagenum = 1;
//        for (int i = 0; i < 3; i++) {
//            //发现首页分页的url只有后缀数字不同，于是使用java渲染字符串模板，然后将这个数逐次加1实现翻页获取列表url的功能
//            String url = String.format("https://c.vanpeople.com/fwxinxi/?page=%d", pagenum);
//            String listPageHtml = grabForHtml(url);
//            ++pagenum;
//            //System.out.println(listPageHtml);
//            analyzeListPage(listPageHtml);
//        }

        // 抓取 topic 页 存库
        //    grabTopicPage();

        // 一页抓完了 做一些更新 和 重置 param 对象的操作
        // 更新 config 对象 lastPageUrl 字段, 写入数据库
        // 更新 param 对象的 forumListUrl 为下一页 url，
        // 重置 topicUrlList 为空集合，重置 pagePartHtml 为空
        //    afterGrabPage();
    }
    private void analyzeListPage(String listPageHtml,String url) throws Exception {
        //jsoup解析html文本
        Document document = Jsoup.parse(listPageHtml);
        // 获取当前页数
        //fillCurrentPage(document);
        // 获取总页数
        //fillTotalPage(document);

        // 填充 topicUrlList
        fillTopicUrlList2(document,url);
    }
    private void fillTopicUrlList2(Document document,String listurl) throws Exception {
//    MongoCollection<User> collection =  MongoDBUtil.getConnect().getCollection("user", User.class).withCodecRegistry(MongoDBUtil.getCodecRegistry());

        //建立集合，设置集合的名字：forum_topic3，还有存储类型是User实体类
        MongoCollection<User> collection = MongoDBUtil.getConnect().getCollection("forum_topic3", User.class)
            .withCodecRegistry(MongoDBUtil.getCodecRegistry());

//        tagGetParam.setTopicUrlList(Lists.newArrayList());

        ArrayList<String> arrayList = new ArrayList<>();
        //新增，拿到html里面的body，进而拿到里面的a标签
        Element element = document.body();

        //发现这个网站所有的a标签都放在了class前缀为"c-list-contxt"的div里面,于是可以进一步筛选
        Elements elementss = element.select("div[class^=c-list-contxt]");
//  筛选一遍div后,再拿到a标签
//  Elements elements = elementss.getElementsByTag("a");
        Elements elements = elementss.select("a[href]");

//    Elements topicElements = document.select(htmlAnalyzeConfig.getChineseInTopicSelector());
//    if (topicElements.size() == 0) {
//      forumGrabLog.warn("ChineseInWorker_exp html_have_no_topic_info 无法获取分页标签 html:{}",
//          super.recordProblemHtml(tagGetParam.getPageHtml()));
//      FMetrics.recordOne("ChineseInWorker_exp", "type", "html_have_no_topic_info");
//      return;
//    }

        for (Element topicElement : elements) {
//      try {
            //拿到a标签后,再筛选拿到a标签的url
            String href = topicElement.attr("href");

//        String forumListUrl = tagGetParam.getForumListUrl();
            //这里实例化一个url不太清楚，最后会根据拿到的所有a标签的href获得url，将循环获得的url放到TopicUrlList里面
//        URL url = new URL(forumListUrl);
            URL url = new URL("https://c.vanpeople.com/lvguan/");
            String protocol = url.getProtocol();
            String host = url.getHost();
            UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(protocol)
                .host(host)
                .path(href)
                .encode(StandardCharsets.UTF_8)
                .build();

            //将url组成的arrayList添加到mongodb里面
//        tagGetParam.getTopicUrlList().add(uriComponents.toString());

            arrayList.add(uriComponents.toString());
//          System.out.println(arrayList);

//      } catch (Exception e) {
//        forumGrabLog.error("ChineseInWorker_exp fill_topic_url_exp 转换topic连接异常 html:{}",
//            super.recordProblemHtml(tagGetParam.getPageHtml()), e);
        }//      }
        // 查询已有的mongo表
        List<User> userList1 = new ArrayList<>();
        MongoCursor<User> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            User user = cursor.next();
            userList1.add(user);
        }

        //给每个analysis设置url
        List<String> collect = userList1.stream()
                .map(User::getEncryptid)
                .collect(Collectors.toList());

            String existEncryp = collect.get(0);
        System.out.println(existEncryp);

//        System.out.println(existEncryp);

//        log.info("代码A(执行for循环给实体类赋值之前花费的时间)耗时:{}", stopwatch.elapsed().toMillis());
//    collection.insertMany(arrayList);
        List<User> userList = Lists.newArrayList();
        for (String url : arrayList) {
            User user = new User();

            //发现url都是:
            //https://c.vanpeople.com/lvguan/item-3067155.html,
            //Uhttps://c.vanpeople.com/lvguan/item-3011220.html
            //这样的形式,于是用正则表达式进行筛选,只拿到需要的数字字符串作为mongo表的name,例如3067155
            //String name = url.replaceAll("^https\\:\\/\\/([a-zA-Z]+\\.)+com\\/[a-zA-Z]+\\/[a-zA-Z]+\\-", "")
            //.replaceAll("\\.html", "");

//      System.out.println(name);
//      利用md5将url作为独一无二的区分id
            String encrypturl = DigestUtils.md5DigestAsHex(listurl.getBytes());
            //这里添加一个if，encrypturl作为id与mongo表中已经有的id对比，如果相等，代表已爬过这条数据,那么不会将已有的数据存到mongo表里面
            if(!existEncryp.equals(encrypturl)){
                //      利用列表的url，okhttp拿到这些列表的html
                String listPageHtml = grabForHtml(url);

                Document document1 = Jsoup.parse(listPageHtml);
                //调用JsoupUserContactDetails方法，拿到p标签的联系方式
                String telephone = JsoupUserPhone(document1);
                String userName = JsoupUserName(document1);
                String email = JsoupUserEmail(document1);
                String location = JsoupUserLocation(document1);
                String weixin = JsoupUserWeChat(document1);
                //这些pageHtml赋值
//          user.setPageHtml(listPageHtml);
                user.setTelephone(telephone);
                //数字字符串名字赋值
                user.setName(userName);
                user.setEmail(email);
                user.setLocation(location);
                user.setWeixin(weixin);
                //这些列表url赋值
                user.setUrl(url);
//      将url进行md5加密作为独一无二的id
                user.setEncryptid(encrypturl);
//      forTest设置为true表示是测试
                user.setForTest(true);
                //telephone赋值
                user.setTelephone(telephone);

                //添加到List里面
                userList.add(user);
            } else{
                System.out.println("因为加密id和表中第一条id一致，所以这条列表信息已经爬取过了，停止爬取");
                break;
            }
        }
//        log.info("代码B(经过for循环给userList赋值花费的时间)耗时:{}", stopwatch.elapsed().toMillis());
//        System.out.println(userList);
            //如果有新的数据，那么会执行set将值添加到新的User类里面，userList就非空，可以将新数据添加到mongo里
            if (userList!=null && userList.size()>0){
                collection.insertMany(userList);
            }
//    log.info("代码C(插入数据库花费时间)耗时:{}", stopwatch.elapsed().toMillis());


    }


    //一共四个class=contact-box-list的盒子，分别是地址，名字，邮箱，电话，微信号。
    // 其中地址，名字有自己盒子类的名称能筛选拿，另外两个按顺序拿就行

    /**
     * 住址
     */
    private String JsoupUserLocation(Document document) {
        //拿到html里面的body
        Element element = document.body();
        //先拿到class=info-content-slide的div
        Elements elementss = element.select("div[class=contact-box-list]");
        //class=tel的div
        Elements location = elementss.select("p[class=ellipsis_1_line addrs]");
        //拿p标签
        String locationInfo = location.text();
//    System.out.println(text);
        return locationInfo;
    }
    /**
     * 用户名字
     */
    private String JsoupUserName(Document document) {
        //拿到html里面的body
        Element element = document.body();
        //先拿到class=info-content-slide的div
        Elements elementss = element.select("div[class=contact-box-list]");
        //class=tel的div
        Elements name = elementss.select("span[class=uname mr20]");
        //拿p标签
        String nameInfo = name.text();
//    System.out.println(text);
        return nameInfo;
    }


    /**
     * 邮箱
     */
    private String JsoupUserEmail(Document document) {
        //拿到html里面的body
        Element element = document.body();
        //拿到四个contact-box-list盒子的最后一个的上一个，再拿取他的第二个子元素，得到邮箱
        Element email = element.select("div[class=contact-box-list]").last().previousElementSibling().child(1);
        String emailInfo = email.text();
        //System.out.println(emailInfo);
        return emailInfo;
    }

    /**
     * 微信号
     */
    private String JsoupUserWeChat(Document document) {
        //拿到html里面的body
        Element element = document.body();
        //拿到四个contact-box-list盒子的最后一个，再拿去其中的第二个子元素
        Element weChat = element.select("div[class=contact-box-list]").last().child(1);
        String weChatInfo = weChat.text();
        //System.out.println(weChatInfo);
        return weChatInfo;
    }


    /**
     * 电话
     */
    private String JsoupUserPhone(Document document) {
        //拿到html里面的body
        Element element = document.body();
        //先拿到class=info-content-slide的div
        Elements elementss = element.select("div[class=info-content-slide]");
        //class=tel的div
        Elements telelement = elementss.select("div[class=side-tips]");
        //拿p标签
        Elements ptag = telelement.select("span");
        String ptext = ptag.text();
//    System.out.println(ptext);
        return ptext;
    }







    /**
     * 总页数
     *
     * @param document document
//     * @throws ForumHtmlAnalyzeException 解析html异常
     */
//    private void fillTotalPage(Document document)
//        throws ForumHtmlAnalyzeException {
//        Elements pageElements = document.select(htmlAnalyzeConfig.getChineseInPageIndexLinkSelector());
//        if (pageElements.size() == 0 && tagGetParam.getTotalPageNum() == 0) {
//            forumGrabLog.warn("ChineseInWorker_exp html_have_no_page_info 无法获取分页标签 html:{}",
//                super.recordProblemHtml(tagGetParam.getPageHtml()));
//            FMetrics.recordOne("ChineseInWorker_exp", "type", "html_have_no_page_info");
//            throw new ForumHtmlAnalyzeException();
//        }
//        Element element = pageElements.get(pageElements.size() - 1);
//        int totalPage = getPageVal(element);
//        tagGetParam.setTotalPageNum(totalPage);
//    }

    /**
     * 当前页
     *
     * @param document document
     * @throws ForumHtmlAnalyzeException 解析html异常
     */
//    private void fillCurrentPage(Document document) throws ForumHtmlAnalyzeException {
//        Elements currentPageElements = document
//            .select(htmlAnalyzeConfig.getChineseInCurrentPageSelector());
//        if (currentPageElements.size() == 0) {
//            forumGrabLog.warn("ChineseInWorker_exp html_have_no_current_page_info 无法获取当前页标签 html:{}",
//                super.recordProblemHtml(tagGetParam.getPageHtml()));
//            FMetrics.recordOne("ChineseInWorker_exp", "type", "html_have_no_current_page_info");
//            throw new ForumHtmlAnalyzeException();
//        } else if (currentPageElements.size() > 1) {
//            String problemHtmlFilePath = super.recordProblemHtml(tagGetParam.getPageHtml());
//            forumGrabLog.warn("ChineseInWorker_exp html_many_page_info 多个当前页标签先取最小的 param:{}, html:{}",
//                tagGetParam, problemHtmlFilePath);
//            FMetrics.recordOne("ChineseInWorker_exp", "type", "html_many_page_info");
//            int currentPage = getMinVal(currentPageElements, problemHtmlFilePath);
//            tagGetParam.setCurrentPageNum(currentPage);
//        } else {
//            Element element = currentPageElements.get(0);
//            int currentPage = getPageVal(element);
//            tagGetParam.setCurrentPageNum(currentPage);
//        }
//    }

    /**
     * 单个 element 得到当前页
     *
     * @param element element
     * @return currentPage
     * @throws ForumHtmlAnalyzeException 解析html异常
     */
//    private int getPageVal(Element element) throws ForumHtmlAnalyzeException {
//        int val;
//        String text = element.text();
//        if (NumberUtil.isInteger(text)) {
//            val = NumberUtil.parseInt(text);
//        } else {
//            forumGrabLog
//                .info("{} 列表页页码标签 text 不是数字 text:{}", tagGetParam.getTagGetConfig().getJobDesc(), text);
//            throw new ForumHtmlAnalyzeException();
//        }
//        return val;
//    }

    /**
     * 多个 element 得到当前页
     *
     * @param currentPageElement  elements
     * @param problemHtmlFilePath problemHtmlFilePath
     * @return currentPage
     * @throws ForumHtmlAnalyzeException 解析html异常
     */
//    private int getMinVal(Elements currentPageElement, String problemHtmlFilePath)
//        throws ForumHtmlAnalyzeException {
//        int val = -1;
//        for (Element element : currentPageElement) {
//            String text = element.text();
//            if (NumberUtil.isInteger(text)) {
//                int i = NumberUtil.parseInt(text);
//                if (val == -1 || i < val) {
//                    val = i;
//                }
//            } else {
//                forumGrabLog.info("{} 列表页有多个最后一页标签 其中有 text 不是数字的 text:{}",
//                    tagGetParam.getTagGetConfig().getJobDesc(), text);
//            }
//        }
//        if (val <= 0) {
//            forumGrabLog
//                .warn(
//                    "ChineseInWorker_exp html_have_no_current_page 获取了当前页标签但内容不对,最终无法得到当前页码 html:{}",
//                    problemHtmlFilePath);
//            FMetrics.recordOne("ChineseInWorker_exp", "type", "html_have_no_current_page");
//            throw new ForumHtmlAnalyzeException();
//        }
//
//        return val;
//
//    }

    /**
     * 构造请求
     *
     * @return Request
     */
//    @Override
//    protected Request createReq(String url) {
//
//        Builder reqBuilder = new Builder().get();
//        reqBuilder.url(url);
//        reqBuilder.headers(commonChromeHeaderMap());
//
//        return reqBuilder.build();
//    }

    /**
     * 填充列表url
     */
//    private void fillListUrl() {
//        TagGetConfig tagGetConfig = tagGetParam.getTagGetConfig();
//        //HaveGotAll不为空且值为1(代表已经进行全部抓取)，
//        // 那么从第一页开始增量抓，其中ForumPlateBaseUrl的72条地址是手工录入的
//        //代表比如网站首页的租房，家政这些分板块的url
//        //有这些url才能请求获得分板块的html，然后获得列表的url
//        if (Objects.nonNull(tagGetConfig.getHaveGotAll()) &&
//            BooleanEnum.TRUE.getCode().equals(tagGetConfig.getHaveGotAll())) {
//            // 已抓取过全量，
//            tagGetParam.setForumListUrl(tagGetConfig.getForumPlateBaseUrl());
//            return;
//        }
//        // 未抓取过全量，接着最后一次的开始抓
//        if (StringUtils.isNotBlank(tagGetConfig.getLastPageUrl())) {
//            tagGetParam.setForumListUrl((tagGetConfig.getLastPageUrl()));
//        } else {
//            tagGetParam.setForumListUrl(tagGetConfig.getForumPlateBaseUrl());
//        }
//
//    }
}