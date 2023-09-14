package com.blog.filter;


import com.blog.BlogAdminApplication;
import com.blog.filter.saveIntoMongo.AbstractForumWorker;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GetHtmlTest  extends AbstractForumWorker {


    @org.junit.jupiter.api.Test
    public void gethtmltest() throws Exception {
        String url1 = String.format( "https://c.vanpeople.com/zufang/?page=1");
        String listPageHtml = grabForHtml(url1);
        //jsoup解析每个列表的html文本
        Document document1 = Jsoup.parse(listPageHtml);
        //拿到html里面的body
        Element element = document1.body();
        Elements elementss = element.select("div[class^=c-list-contxt]");
        Elements elements = elementss.select("a[href]");
        ArrayList<String> arrayList = new ArrayList<>();
        for (Element topicElement : elements) {
            String href = topicElement.attr("href");
            URL url = new URL("https://c.vanpeople.com/lvguan/");
            String protocol = url.getProtocol();
            String host = url.getHost();
            UriComponents uriComponents = UriComponentsBuilder.newInstance()
                    .scheme(protocol)
                    .host(host)
                    .path(href)
                    .encode(StandardCharsets.UTF_8)
                    .build();
            arrayList.add(uriComponents.toString());
        }
        System.out.println(arrayList);

    }



}

