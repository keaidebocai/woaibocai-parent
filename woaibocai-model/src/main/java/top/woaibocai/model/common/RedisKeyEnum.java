package top.woaibocai.model.common;

import lombok.Getter;

@Getter
public enum RedisKeyEnum {
    BLOG_ARTICLE("blog:article");
    //文章的索引 redis数据类型: list: String String
    public static final String BLOG_AERICLE_INDEX = "blog:article:index";
    // 博客初始化数据 存储类型 hash: String String String
    public static final String BLOG_FETCHDATE_ARTICLE_AND_URL = "blog:fetchDate:articleAndUrl";
    // 博客信息 存储类型 hash: String String Objcet
    public static final String BLOG_FETCHDATE_BLOG_INFO = "blog:fetchDate:blogInfo";
    // 一个标签有多少文章 存储类型 hash: String String Intager
    public static final String BLOG_FETCHDATE_TAG_HAS_ARTICLE_COUNT_MAP = "blog:fetchDate:tagHasArtilceCountMap";
    // 一个标签有多少文章 存储类型 hash: String String String
    public static final String BLOG_HEADER_GET_ALL_CATEGORY = "blog:header:getAllCategory";
    //文章的索引 redis数据类型: list: String Menu
    public static final String BLOG_HEADER_GET_ALL_MENU = "blog:header:getAllMenu";
    private String prefix ;      // 业务状态码

    RedisKeyEnum(String prefix) {
        this.prefix = prefix ;
    }
    // 拼接完整的redis key
    public String articleId(String... values) {
        StringBuffer sb = new StringBuffer(45);
        sb.append(this.prefix);
        for (String value : values) {
            sb.append(":").append(value);
        }
        return sb.toString();
    }
}
