package top.woaibocai.model.Enum;

import lombok.Getter;

@Getter
public enum RedisKeyEnum {
    BLOG_ARTICLE("blog:article"),
    // tag下 文章的索引 list: String String
    BLOG_TAG_INDEX("blog:tag:index"),
    BLOG_CATEGORY_INDEX("blog:category:index"),
    BLOG_COMMENT_ALL("blog:comment:all"),
    // blog:comment:articleId?:pComment
    BLOG_COMMENT_ARTICLE("blog:comment:article"),
    BLOG_COMMENT_ONECOMMENT("blog:comment:onecomment"),
    BLOG_REGISTER_EMAIL("blog:register:email"),
    BLOG_LOGIN_COUNT("blog:login:count"),
    BLOG_FORGOT_EMAIL("blog:forgot:email"),
    EMAIL_USER_HOUR_MAX("email:user:hour:max"),
    EMAIL_PUBLIC_HASH("email:public:hash"),
   ;

    //文章的索引 redis数据类型: list: String String
    public static final String BLOG_AERICLE_INDEX = "blog:article:index";
    // 博客初始化数据 存储类型 hash: String String String
    public static final String BLOG_FETCHDATE_ARTICLE_AND_URL = "blog:fetchDate:articleAndUrl";
    // 博客信息 存储类型 hash: String String Objcet
    public static final String BLOG_FETCHDATE_BLOG_INFO = "blog:fetchDate:blogInfo";
    // 一个标签有多少文章 存储类型 hash: String String Intager
    public static final String BLOG_FETCHDATE_TAG_HAS_ARTICLE_COUNT_MAP = "blog:fetchDate:tagHasArtilceCountMap";
    // list String Object 所有 category的 url name icon
    public static final String BLOG_CATEGORY_INFO = "blog:category:info";
    // list String Object
    public static final String BLOG_TAG_ALL_INFO = "blog:tag:all:info";
    // SSI
    public static final String BLOG_COMMENT_COUNT = "blog:comment:count";
    public static final String BLOG_COMMENT_LIKE = "blog:comment:like";
    public static final String BLOG_LINK_ALL = "blog:link:all";
    public static final String BLOG_ABOUT = "blog:about";
    public static final String BLOG_LINK = "blog:link";
    public static final String BLOG_SITEMAP = "blog:sitemap";
    public static final String BLOG_RSS = "blog:rss";

    // "email:day:max"
    public static final String EMAIL_DAY_MAX = "email:day:max";

    public static final String EMAIL_S_MAX = "email:s:max";

    public static final String EMAIL_PUBLIC_ZSET_SELECTION = "email:public:zset:selection";
    public static final String EMAIL_PUBLIC_LIST_DELIVERY = "email:public:list:delivery";
    public static final String EMAIL_PUBLIC_LIST_WRITING = "email:public:list:writing";
    private String prefix ;      // 业务状态码

    RedisKeyEnum(String prefix) {
        this.prefix = prefix ;
    }
    // 拼接完整的redis key
    public String articleUrl(String... values) {
        StringBuffer sb = new StringBuffer(45);
        sb.append(this.prefix);
        for (String value : values) {
            sb.append(":").append(value);
        }
        return sb.toString();
    }
    public String tagUrl(String... values) {
        StringBuffer sb = new StringBuffer(45);
        sb.append(this.prefix);
        for (String value : values) {
            sb.append(":").append(value);
        }
        return sb.toString();
    }
    public String categoryUrl(String... values) {
        StringBuffer sb = new StringBuffer(45);
        sb.append(this.prefix);
        for (String value : values) {
            sb.append(":").append(value);
        }
        return sb.toString();
    }
    public String comment(String... values) {
        StringBuffer sb = new StringBuffer(45);
        sb.append(this.prefix);
        for (String value : values) {
            sb.append(":").append(value);
        }
        return sb.toString();
    }
    public String common(String... values) {
        StringBuffer sb = new StringBuffer(45);
        sb.append(this.prefix);
        for (String value : values) {
            sb.append(":").append(value);
        }
        return sb.toString();
    }
}
