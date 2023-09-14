package com.blog.filter.saveIntoMongo;


import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author chizongze created on 2023/7/26
 * @version $
 */
@Slf4j
public abstract class AbstractForumWorker {
//
//  private final Logger forumGrabLog = LoggerFactory.getLogger("forum_grab");
//
////  @Resource
////  private CrawlerConfig crawlerConfig;
////
////  @Resource
////  private MForumTopicDAO mForumTopicDAO;
//
//  private static final DateTimeFormatter TO_DATE_STR_FORMATTER = DateTimeFormatter
//      .ofPattern("yyyyMMdd");
//
//  @Value("${forum.analyze.err.html.dir:/opt/Log/fantuan.crawler/errHtml}")
//  private String errHtmlDir;
//
////  public Response executeCall(OkHttpClient client, Request request) throws CallRemoteException {
////    try {
////      Call call = client.newCall(request);
////      call.timeout().timeout(60, TimeUnit.SECONDS);
////      return call.execute();
////    } catch (Exception e) {
////      forumGrabLog.error("ForumWorker_exp executeCall_exp", e);
////      FMetrics.recordOne("ForumWorker_exp", "type", "executeCall_exp");
////      throw new CallRemoteException();
////    }
////  }
//
  /**
   * 抓取列表页
   *
   * @return 列表页html
   */
  protected String grabForHtml(String url)
      throws Exception {
    // 构造 okHttpClient

      // 创建OkHttpClient对象
      OkHttpClient client = new OkHttpClient();
      // 请求对应的百度百科地址
      URL urlStr = new URL(url);
      // 构建请求
      Request request = new Request.Builder()
          //请求接口。如果需要传参拼接到接口后面。
          .url(urlStr)
          // 请求类型
          .get()
          .build();

      // 创建 response 对象，用于获取返回信息
      Response response = null;
      // 发起请求，并拿到返回的 response
      response = client.newCall(request).execute();
      // 拿到返回的html代码，如果返回的是json信息可以直接转正对象
      String jsong = response.body().string();

//    System.out.println(jsong);
      return jsong;
  }
//    // 构造 okHttpClient
////    OkHttpClient okHttpClient = generateOkHttpClient();
////
////    // 构造请求
////    Request req = createReq(url);
////    // 发请求
////    Response response = executeCall(okHttpClient, req);
////
////    if (!response.isSuccessful()) {
////      forumGrabLog
////          .error("ForumWorker_exp resp_no_successful param:{}, resp:{}", getForumWorkerParam(),
////              response);
////      FMetrics.recordOne("ForumWorker_exp", "type", "resp_no_successful", "subSourceType",
////          getForumWorkerParam().getJobConfig().getJobDesc());
////      return null;
////    }
////    if (Objects.isNull(response.body())) {
////      forumGrabLog.error("ForumWorker_exp resp_null_body param:{}, resp:{}", getForumWorkerParam(),
////          response);
////      FMetrics.recordOne("ForumWorker_exp", "type", "resp_null_body", "subSourceType",
////          getForumWorkerParam().getJobConfig().getJobDesc());
////      return null;
////    }
////    try {
////      return response.body().string();
////    } catch (IOException e) {
////      forumGrabLog
////          .error("ForumWorker_exp resp_body_toStr_exp param:{}, resp:{}", getForumWorkerParam(),
////              response, e);
////      FMetrics.recordOne("ForumWorker_exp", "type", "resp_body_toStr_exp", "subSourceType",
////          getForumWorkerParam().getJobConfig().getJobDesc());
////      return null;
////    }
//
//
//  /**
//   * 根据 topicUrl 列表 过滤出没有抓过的
//   *
//   * @param topicUrlList topicUrlList
//   * @return 需要抓取的 url
//   */
//  protected List<String> filterTopicUrl(List<String> topicUrlList) {
//    List<MForumTopic> forumTopicList = mForumTopicDAO
//        .queryByUrlList(getForumWorkerParam().getJobConfig().getSubSourceType(), topicUrlList,
//            getForumWorkerParam().getMyCollectionNames());
//    if (CollectionUtils.isEmpty(forumTopicList)) {
//      return topicUrlList;
//    }
//
//    List<String> grabbedUrl = forumTopicList.stream().map(MForumTopic::getUrl)
//        .collect(Collectors.toList());
//
//    Collection<String> subtract = CollUtil.subtract(topicUrlList, grabbedUrl);
//
//    return Lists.newArrayList(subtract);
//
//  }
//
//  /**
//   * 将 话题信息存入 mongo
//   *
//   * @param topicHtml topicHtml 字符串
//   * @param topicUrl  topic url
//   * @param date
//   */
//  protected void saveContext(String topicHtml, String topicUrl, Date date)
//      throws ForumWorkerCommonException {
//    try {
//      int type = getForumWorkerParam().getJobConfig().getSourceType();
//      int subType = getForumWorkerParam().getJobConfig().getSubSourceType();
//      MForumTopic forumTopic = new MForumTopic();
//      forumTopic.setSourceType(type);
//      forumTopic.setSubSourceType(subType);
//      forumTopic.setUrl(topicUrl);
//      forumTopic.setPageContext(topicHtml);
//      forumTopic.setGrabDate(date);
//
//      // 得到最新的集合名
//      List<String> collectionList = Lists
//          .newArrayList(getForumWorkerParam().getMyCollectionNames());
//      List<String> sortedList = collectionList.stream().sorted().collect(Collectors.toList());
//      String latestCollectionName = sortedList.get(sortedList.size() - 1);
//
//      MForumTopic mForumTopic = mForumTopicDAO.queryByUrl(topicUrl, latestCollectionName);
//
//      if (Objects.nonNull(mForumTopic)) {
//        mForumTopic.setPageContext(topicHtml);
//        mForumTopicDAO.updateById(mForumTopic, latestCollectionName);
//      } else {
//        mForumTopicDAO.insertTopicRecord(forumTopic, latestCollectionName);
//      }
//    } catch (Exception e) {
//      String jobDesc = getForumWorkerParam().getJobConfig().getJobDesc();
//      forumGrabLog
//          .error("ForumWorker_exp save_topic_context_exp param:{}", getForumWorkerParam(), e);
//      FMetrics
//          .recordOne("ForumWorker_exp", "type", "save_topic_context_exp", "subSourceType", jobDesc);
//      throw new ForumWorkerCommonException("保存 " + jobDesc + " 类型 topic 信息异常");
//    }
//  }
//
//  protected OkHttpClient generateOkHttpClient() throws NoProxyException {
//    ForumWorkerParam forumWorkerParam = getForumWorkerParam();
//
//    if (Objects.nonNull(forumWorkerParam.getCurrentClient())) {
//      return forumWorkerParam.getCurrentClient();
//    }
//
//    Integer sourceType = forumWorkerParam.getJobConfig().getSourceType();
//    ForumSourceType forumSourceType = ForumSourceType.ofType(sourceType);
//    assert forumSourceType != null;
//    Integer subSourceType = forumWorkerParam.getJobConfig().getSubSourceType();
//
//    String cacheKey =
//        forumSourceType.name().toLowerCase() + StringConstants.UNDERLINE + subSourceType;
//
//    if (forumWorkerParam.isForTest()) {
//      log.info("forTest: {}, 不获取代理", forumWorkerParam.isForTest());
//      return HttpClientUtils.buildHttpClient(cacheKey + StringConstants.UNDERLINE + "client");
//    } else {
//      // 获取代理
//      Proxy proxy = createProxy();
//      //得到 okHttpClient
//      OkHttpClient okHttpClient = HttpClientUtils
//          .buildHttpClient(
//              new ProxyKey(proxy, cacheKey, "", ""))
//          .newBuilder().cookieJar(new LocalCookieJar()).build();
//      forumWorkerParam.setCurrentClient(okHttpClient);
//      return okHttpClient;
//    }
//  }
//
//  private Proxy createProxy() throws NoProxyException {
//    ForumWorkerParam forumWorkerParam = getForumWorkerParam();
//    ForumJobConfig jobConfig = forumWorkerParam.getJobConfig();
//
//    ProxyConfig proxyConfig;
//
//    try {
//      proxyConfig = getProxy(jobConfig.getCountry());
//      forumWorkerParam.addProxyConfig(proxyConfig);
//    } catch (Exception e) {
//      if (forumWorkerParam.hasLocalProxy()) {
//        log.warn("grab_proxy_warn 未获取到代理, 本地缓存量 > 1, 用本地缓存的");
//        proxyConfig = forumWorkerParam.getRandomProxyConfig();
//      } else {
//        log.warn("grab_proxy_warn 未获取到代理");
//        throw e;
//      }
//    }
//    log.info("代理ip:{}, port:{}", proxyConfig.getHostname(), proxyConfig.getPort());
//    return proxyConfig.getProxy();
//  }
//
//  /**
//   * 获取代理
//   *
//   * @param country 国家
//   * @return ProxyConfig
//   * @throws NoProxyException 无法获取代理
//   * @see ca.fantuan.crawler.app.enums.GrabCountry
//   */
//  private ProxyConfig getProxy(String country) throws NoProxyException {
//    try {
//      log.info("{} 开始获取代理", getForumWorkerParam().getJobConfig().getJobDesc());
//      ProxyConfig proxyConfig = crawlerConfig.selectHpProxyConfig(country);
//      log.info("{} 获取代理结束", getForumWorkerParam().getJobConfig().getJobDesc());
//      if (Objects.isNull(proxyConfig)) {
//        log.warn("no_proxyConfig .... proxyConfig:{}", proxyConfig);
//        throw new Exception("无法获取代理");
//      }
//      return proxyConfig;
//    } catch (Exception e) {
//      forumGrabLog.error("ForumWorker_exp cant_get_proxy jobConfig:{}",
//          getForumWorkerParam().getJobConfig(),
//          e);
//      FMetrics.recordOne("ForumWorker_exp", "type", "cant_get_proxy");
//      throw new NoProxyException("cant_get_proxy");
//    }
//  }
//
//  protected String recordProblemHtml(String htmlStr) {
//    ForumWorkerParam forumWorkerParam = getForumWorkerParam();
//    Integer sourceType = forumWorkerParam.getJobConfig().getSourceType();
//    ForumSourceType forumSourceType = ForumSourceType.ofType(sourceType);
//    assert forumSourceType != null;
//
//    Integer subSourceType = forumWorkerParam.getJobConfig().getSubSourceType();
//
//    LocalDateTime localDateTime = LocalDateTime.now();
//    String dateDir = localDateTime.format(TO_DATE_STR_FORMATTER);
//    String path = errHtmlDir + File.separatorChar +
//        forumSourceType.name().toLowerCase() + StringConstants.UNDERLINE + subSourceType +
//        File.separatorChar + dateDir;
//
//    FileUtil.mkdir(path);
//
//    String htmlFilePath = path + File.separatorChar + IdUtil.getSnowflakeNextId();
//    FileUtil.writeString(htmlStr, htmlFilePath, CharsetUtil.CHARSET_UTF_8);
//    return htmlFilePath;
//  }
//
//  public abstract void doGrab();
//
//  protected void doGrabSingleJob() {
//    Set<String> collectionNames = mForumTopicDAO.listMyCollections(COLLECTION_PREFIX);
//    getForumWorkerParam().setMyCollectionNames(collectionNames);
//  }
//
//  protected boolean continueGrab() throws Exception {
//    ForumJobConfig jobConfig = getForumWorkerParam().getJobConfig();
//
//    Set<String> myCollectionNames = getForumWorkerParam().getMyCollectionNames();
//    if (CollectionUtils.isEmpty(myCollectionNames)) {
//      forumGrabLog.error(
//          "ForumWorker_exp need_to_set_myList param:{}", getForumWorkerParam());
//      throw new ForumWorkerCommonException(
//          "缺少topic 对应的mongo集合,请在抓取前先执行 AbstractForumWorker#doGrabSingleJob方法");
//    }
//
//    return timeToGrab(jobConfig.getTimezone(), jobConfig.getGrabInterval());
//  }
//
//  /**
//   * 当前时区时间是否在可抓取时间中
//   *
//   * @param timezone     timezone
//   * @param grabInterval grabInterval 必须是这个格式否则会报错 ["10:00-13:00","17:00-21:00"]
//   * @return boolean true 在区间中
//   */
//  private boolean timeToGrab(String timezone, String grabInterval) throws Exception {
//    if (StringUtils.isEmpty(grabInterval)) {
//      // 没有区间默认全时段都能抓
//      forumGrabLog.info("{} 未设置时间 认为全时段抓取", getForumWorkerParam().getJobConfig().getJobDesc());
//      return true;
//    }
//
//    // 得到被比较时间 服务端是 utc 时间
//    ZonedDateTime nowUtc = ZonedDateTime.now();
//    ZonedDateTime zonedDateTime = nowUtc.withZoneSameInstant(ZoneId.of(timezone));
//    LocalTime comparedLocalTime = zonedDateTime.toLocalTime();
//
//    // 时间区间转成 list
//    List<String> grabIntervalList = JsonUtils.deserialize(grabInterval,
//        new TypeReference<List<String>>() {
//        });
//    forumGrabLog.info("当前时间: {}, {} 可抓取时间段: {} ", comparedLocalTime,
//        getForumWorkerParam().getJobConfig().getJobDesc(), grabIntervalList);
//    for (String intervalItem : grabIntervalList) {
//      // 满足一段就返回 true
//      List<String> beginAndEnd = StrUtil.split(intervalItem, StringConstants.HYPHEN);
//      String begin = beginAndEnd.get(0), end = beginAndEnd.get(1);
//      LocalTime compareBeginLocalTime = LocalTime.parse(begin);
//      LocalTime compareEndLocalTime = LocalTime.parse(end);
//
//      // 被比较时间 在 begin 之后, 在 end 之前
//      if (comparedLocalTime.isAfter(compareBeginLocalTime) &&
//          comparedLocalTime.isBefore(compareEndLocalTime)) {
//        return true;
//      }
//    }
//    forumGrabLog.info("{} 没在可执行时间中", getForumWorkerParam().getJobConfig().getJobDesc());
//    return false;
//  }
//
//  /**
//   * 给 okHttpClient 创建 request 需要自行实现
//   *
//   * @param url 目标 url
//   * @return request
//   */
//  protected abstract Request createReq(String url);
}