package me.wuwenbin.noteblogv4.exception;

/**
 * created by Wuwenbin on 2018/8/10 at 11:10
 *
 * @author wuwenbin
 */
public class ArticleFetchFailedException extends RuntimeException {
    public ArticleFetchFailedException() {
        super("文章获取失败！");
    }

    public ArticleFetchFailedException(String message) {
        super(message);
    }
}
