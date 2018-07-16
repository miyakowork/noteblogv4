package me.wuwenbin.noteblogv4.model.constant;

/**
 * 一些全局的固定值
 * created by Wuwenbin on 2018/7/15 at 17:29
 *
 * @author wuwenbin
 */
public interface Noteblogv4 {

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
        String INIT_FOOTER_WORDS = "此处一般可写一些备案号之类的文字";
        String INIT_INDEX_TOP_WORDS = "写下你的座右铭吧";
        String INIT_MENU_HOME = "主页";
        String INIT_MENU_NOTE = "笔记";
        String INIT_MENU_SEARCH = "搜索";
        String INIT_MENU_LINK = "代码";
        String INIT_MENU_LINK_ICON = "fa fa-code";
        String INIT_MENU_MINE = "关于";
        String INIT_ALIPAY = "/static/img/alipay.png";
        String INIT_WECHAT_PAY = "/static/img/wechat.png";
        String INIT_INFO_LABEL = "此处填写网站的一些信息";
        String INIT_COMMENT_WORD = "<span style=\"color:#FF5722;\">遵守国家法律法规，请勿回复无意义内容，请不要回复嵌套过多的楼层！</span>";
        String INIT_MESSAGE_PANEL_WORDS = "欢迎大家留言，有什么问题、建议、意见或者疑问可随时提出，qq群：697053454。<span style=\"color:red;\">请不要回复嵌套过多的楼层！</span>";

        /**
         * 默认上传方式为本地服务器上传
         *
         * @see UploadMethod
         */
        String INIT_UPLOAD_TYPE = UploadMethod.LOCAL.name();

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
         * 导航菜单_笔记
         */
        String MENU_NOTE = "menu_note";

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

        /**
         * 导航菜单_搜索
         */
        String MENU_SEARCH = "menu_search";

        /**
         * 信息板内容
         */
        String INFO_LABEL = "info_label";

        /**
         * 网站logo的文字
         */
        String WEBSITE_LOGO_WORDS = "website_logo_words";

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
         * 支付宝付款码
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
         * 网站文件上传路径
         */
        String UPLOAD_PATH = "upload_path";

        /**
         * 是否开启云服务器上传
         */
        String IS_OPEN_OSS_UPLOAD = "is_open_oss_upload";

        /**
         * 标识上传的类型
         * 分别有本地服务器上传、七牛云服务器上传
         */
        String UPLOAD_TYPE = "upload_type";
    }

    /**
     * 一些session或cookie中需要的参数
     */
    interface Session {

        /**
         * sessionIdCookie的名称
         */
        String SESSION_ID_COOKIE = "noteblogv4sid";
    }

}
