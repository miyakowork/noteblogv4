package me.wuwenbin.noteblogv4.model.constant;

/**
 * 一些全局的固定值
 * created by Wuwenbin on 2018/7/15 at 17:29
 *
 * @author wuwenbin
 */
public interface NoteBlogV4 {

    /**
     * 初始化的时候的一些参数key
     */
    interface Init {

        /**
         * @see Param
         */
        String INIT_STATUS = Param.INIT_STATUS;
        String INIT_NOT = "0";
        String INIT_SURE = "1";

        String INIT_WEBSITE_TILE = "笔记博客";
        String INIT_WEBSITE_LOGO_WORDS = "logo处文字";
        String INIT_WEBSITE_LOGO_SMALL_WORDS = "这是一个小标题";
        String INIT_FOOTER_WORDS = "此处一般可写一些备案号之类的文字";
        String INIT_INDEX_TOP_WORDS = "写下你的座右铭吧";
        String INIT_MENU_HOME = "主页";
        String INIT_MENU_PROJECT = "作品";
        String INIT_MENU_NOTE = "笔记";
        String INIT_MENU_SEARCH = "搜索";
        String INIT_MENU_LINK = "代码";
        String INIT_MENU_LINK_ICON = "fa fa-code";
        String INIT_MENU_MINE = "关于";
        String INIT_ALIPAY = "/static/assets/img/alipay.png";
        String INIT_WECHAT_PAY = "/static/assets/img/wechat.png";
        String INIT_INFO_LABEL = "此处填写网站的一些信息";
        String INIT_COMMENT_WORD = "遵守国家法律法规，请勿回复无意义内容，请不要回复嵌套过多的楼层！";
        String INIT_MESSAGE_PANEL_WORDS = "欢迎大家留言，有什么问题、建议、意见或者疑问可随时提出，qq群：<a href=\"https://jq.qq.com/?_wv=1027&k=5ypf8jR\" target=\"_blank\">697053454</a>。<span style=\"color:red;\">请不要回复嵌套过多的楼层！</span>";

        /**
         * 默认上传方式为本地服务器上传
         *
         * @see Upload
         */
        String INIT_UPLOAD_TYPE = Upload.Method.LOCAL.name();

        /**
         * 分页模式
         * 0：默认，流式，1：分页按钮，点击加载下一页（单页）
         *
         * @see ParamValue
         */
        String INIT_DEFAULT_PAGE_MODERN = ParamValue.PAGE_MODERN_DEFAULT;

        /**
         * 默认分页的大小
         *
         * @see ParamValue
         */
        String INIT_DEFAULT_PAGE_SIZE = ParamValue.DEFAULT_PAGE_SIZE;

        /**
         * 网站管理员所拥有的所有权限资源key
         */
        String MASTER_RESOURCES = "master_resources";
    }

    /**
     * 参数表中的一些key
     * 提供程序调用
     */
    interface Param {
        /**
         * 是否已经初始化
         * 0：否，1：是
         */
        String INIT_STATUS = "init_status";


        /**
         * 网站标题的文字
         */
        String WEBSITE_TITLE = "website_title";

        /**
         * 页脚的文字
         */
        String FOOTER_WORDS = "footer_words";

        /**
         * 首页置顶文字
         */
        String INDEX_TOP_WORDS = "index_top_words";

        /**
         * 导航菜单_首页
         */
        String MENU_HOME = "menu_home";

        /**
         * 我的项目导航
         */
        String MENU_PROJECT = "menu_project";
        String MENU_PROJECT_SHOW = "menu_project_show";
        /**
         * 导航菜单_笔记
         */
        String MENU_NOTE = "menu_note";
        String MENU_NOTE_SHOW = "menu_note_show";

        /**
         * 导航菜单_额外的链接
         */
        String MENU_LINK = "menu_link";

        /**
         * 导航菜单_额外的链接的字体图标logo
         */
        String MENU_LINK_ICON = "menu_link_icon";

        /**
         * 导航菜单_额外的链接url
         */
        String MENU_LINK_HREF = "menu_link_href";

        /**
         * 导航菜单_关于我
         */
        String MENU_MINE = "menu_mine";
        String MENU_MINE_SHOW = "menu_mine_show";

        /**
         * 导航菜单_搜索
         */
        String MENU_SEARCH = "menu_search";
        String MENU_SEARCH_SHOW = "menu_search_show";

        /**
         * 信息板内容
         */
        String INFO_LABEL = "info_label";

        /**
         * 网站logo的文字
         */
        String WEBSITE_LOGO_WORDS = "website_logo_words";

        /**
         * 网站logo的文字旁的小字
         */
        String WEBSITE_LOGO_SMALL_WORDS = "website_logo_small_words";

        /**
         * 评论置顶公告
         */
        String COMMENT_NOTICE = "comment_notice";

        /**
         * 留言板的提示信息文字
         */
        String MESSAGE_PANEL_WORDS = "message_panel_words";


        /**
         * 是否全局开放评论
         */
        String ALL_COMMENT_OPEN = "all_comment_open";

        /**
         * 是否展示额外连接
         */
        String MENU_LINK_SHOW = "menu_link_show";

        /**
         * 支付宝付款码
         */
        String ALIPAY = "alipay";

        /**
         * 微信付款码
         */
        String WECHAT_PAY = "wechat_pay";

        /**
         * qq登录API的app_id，请自行去qq互联(https://connect.qq.com)申请
         */
        String APP_ID = "app_id";

        /**
         * qq登录API的app_key，请自行去qq互联(https://connect.qq.com)申请
         */
        String APP_KEY = "app_key";

        /**
         * 是否开放qq登录
         */
        String QQ_LOGIN = "qq_login";

        /**
         * 是否设置了网站管理员
         */
        String IS_SET_MASTER = "is_set_master";

        /**
         * 是否开启留言功能
         */
        String IS_OPEN_MESSAGE = "is_open_message";

        /**
         * 网站信息和会员中心显示顺序，1表示网站信息显示在首要位置
         */
        String INFO_PANEL_ORDER = "info_panel_order";


        /**
         * 是否开启云服务器上传
         */
        String IS_OPEN_OSS_UPLOAD = "is_open_oss_upload";

        /**
         * 标识上传的类型
         * 分别有本地服务器上传、七牛云服务器上传
         */
        String UPLOAD_TYPE = "upload_type";

        /**
         * 七牛云AccessKey
         */
        String QINIU_ACCESS_KEY = "qiniu_accessKey";

        /**
         * 七牛云SecretKey
         */
        String QINIU_SECRET_KEY = "qiniu_secretKey";

        /**
         * 七牛云Bucket
         */
        String QINIU_BUCKET = "qiniu_bucket";

        /**
         * 访问七牛云服务器的域名
         */
        String QINIU_DOMAIN = "qiniu_domain";

        /**
         * 设置博客的分页形式
         * 0：默认模式（流式下拉加载），1：显示分页按钮类型的加载（采用单页模式）
         */
        String PAGE_MODERN = "page_modern";

        /**
         * 首页风格：简约/普通（simple/normal）
         */
        String BLOG_STYLE = "index_style";

        /**
         * 博文首页分页的pageSize大小
         */
        String BLOG_INDEX_PAGE_SIZE = "blog_index_page_size";

        /**
         * 是否开启访问统计
         * 因为是频繁的插入数据库，所以默认是不开启此项的
         */
        String STATISTIC_ANALYSIS = "statistic_analysis";

        /**
         * 文章摘要文字长度
         */
        String ARTICLE_SUMMARY_WORDS_LENGTH = "article_summary_words_length";

        /**
         * 下面是邮件相关的参数
         */
        String MAIL_SMPT_SERVER_ADDR = "mail_smpt_server_addr";
        String MAIL_SMPT_SERVER_PORT = "mail_smpt_server_port";
        String MAIL_SERVER_ACCOUNT = "mail_server_account";
        String MAIL_SENDER_NAME = "mail_sender_name";
        String MAIL_SERVER_PASSWORD = "mail_server_password";
    }

    /**
     * 一些session或cookie中需要的参数
     */
    interface Session {

        /**
         * 初始化应用的页面
         */
        String INIT_PAGE = "/init";

        /**
         * sessionIdCookie的名称
         */
        String SESSION_ID_COOKIE = "noteblogv4sid";

        /**
         * cookie分隔符
         */
        String COOKIE_SPLIT = "__!!__";

        /**
         * name
         */
        String REMEMBER_COOKIE_NAME = "DA4DE6E44EEEC32C30C278A452C355B9";

        /**
         * 网站前台地址
         */
        String FRONTEND_INDEX = "/";
        /**
         * 后台首页地址
         */
        String MANAGEMENT_INDEX = "/management/index";

        /**
         * 登录地址
         */
        String LOGIN_URL = "/login";

        /**
         * 网站管理员的角色id，通过initListener初始化之后，会全局设置到NBContext对象中
         */
        String WEBMASTER_ROLE_ID = "webmaster_role_id";

        /**
         * 错误页面
         */
        String ERROR_PAGE = "error/page";

        /**
         * 错误路由
         */
        String ERROR_ROUTER = "error/router";
    }

    /**
     * 参数值
     */
    interface ParamValue {
        /**
         * 分页模式
         * 0，默认流式，1：分页按钮点击单页模式加载
         */
        String PAGE_MODERN_DEFAULT = "0";
        String PAGE_MODERN_BUTTON = "1";

        String DEFAULT_PAGE_SIZE = "10";

        String STYLE_SIMPLE = "simple";
        String STYLE_NORMAL = "normal";
    }

}
