package me.wuwenbin.noteblogv4.model.constant;

/**
 * 上传相关的常量
 * created by Wuwenbin on 2018/7/16 at 15:26
 *
 * @author wuwenbin
 */
public interface Upload {

    /**
     * 上传方式类型
     * 本地上传和七牛云服务器上传
     */
    enum Method {
        /**
         * 本地上传
         */
        LOCAL,

        /**
         * 七牛云上传
         */
        QINIU
    }

    /**
     * 上传文件的类型
     */
    interface FileType {
        /**
         * 图片
         */
        String IMAGE = "/img";

        /**
         * 非图片文件
         */
        String FILE = "/file";
    }
}
