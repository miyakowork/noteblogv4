package me.wuwenbin.noteblogv4.service.mail;

import me.wuwenbin.noteblogv4.model.entity.NBArticle;

/**
 * created by Wuwenbin on 2019-01-08 at 22:27
 * @author wuwenbin
 */
public interface MailService {

    void sendNoticeMail(String site, NBArticle article, String comment);
}
