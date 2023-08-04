package com.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                // 通过调用自定义方法apiInfo，获得文档的主要信息
                .apiInfo(apiInfo())
                // 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
                .select()
                // 扫描指定包
                .apis(RequestHandlerSelectors.basePackage("com.blog.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/doc.html
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 接口文档标题
                .title("接口文档")
                // 对于接口文档的相关描述
                .description("接口文档")
                // 接口文档内容的一下补充
                .contact(new Contact("月亮星", "http://localhost:8080/doc.html", "1571428795@qq.com"))
                // 版本
                .version("1.0")
                .build();
    }
}