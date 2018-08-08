package me.wuwenbin.noteblogv4.service.content;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.dao.repository.TagReferRepository;
import me.wuwenbin.noteblogv4.dao.repository.TagRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.constant.TagType;
import me.wuwenbin.noteblogv4.model.entity.NBArticle;
import me.wuwenbin.noteblogv4.model.entity.NBTag;
import me.wuwenbin.noteblogv4.model.entity.NBTagRefer;
import me.wuwenbin.noteblogv4.service.param.ParamService;
import me.wuwenbin.noteblogv4.util.NBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

import static cn.hutool.core.util.RandomUtil.randomInt;
import static java.time.LocalDateTime.now;

/**
 * created by Wuwenbin on 2018/8/5 at 20:09
 *
 * @author wuwenbin
 */
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final TagReferRepository tagReferRepository;
    private final TagRepository tagRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, TagReferRepository tagReferRepository, TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.tagReferRepository = tagReferRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean createArticle(NBArticle article, String tagNames) {
        if (StringUtils.isEmpty(tagNames)) {
            throw new RuntimeException("tagNames 不能为空！");
        }
        setArticleSummaryAndTxt(article);
        article.setPost(now());
        article.setView(randomInt(666, 1609));
        article.setApproveCnt(randomInt(6, 169));
        article.setAppreciable(false);
        article.setCommented(false);
        article.setTop(0);
        NBArticle newArticle = articleRepository.save(article);
        String[] tagNameArray = tagNames.split(",");
        int cnt = 0;
        for (String name : tagNameArray) {
            Example<NBTag> condition = Example.of(NBTag.builder().name(name).build());
            boolean isExist = tagRepository.count(condition) == 0;
            long tagId = isExist ?
                    tagRepository.save(NBTag.builder().name(name).build()).getId() :
                    tagRepository.findByName(name).getId();

            tagReferRepository.save(
                    NBTagRefer.builder()
                            .referId(newArticle.getId())
                            .tagId(tagId)
                            .show(cnt < 4)
                            .type(TagType.article.name()).build()
            );
            cnt++;
        }
        return true;
    }

    /**
     * 根据文章内容生成文章摘要
     *
     * @param article
     */
    private void setArticleSummaryAndTxt(NBArticle article) {
        ParamService paramService = NBUtils.getBean(ParamService.class);
        int summaryLength = paramService.getValueByName(NoteBlogV4.Param.ARTICLE_SUMMARY_WORDS_LENGTH);
        String clearContent = HtmlUtil.cleanHtmlTag(StrUtil.trim(article.getContent()));
        clearContent = StringUtils.trimAllWhitespace(clearContent);
        clearContent = clearContent.substring(0, clearContent.length() < summaryLength ? clearContent.length() : summaryLength);
        int allStandardLength = clearContent.length();
        int fullAngelLength = NBUtils.fullAngelWords(clearContent);
        int finalLength = allStandardLength - fullAngelLength / 2;
        article.setSummary(clearContent.substring(0, finalLength < summaryLength ? finalLength : summaryLength));
        article.setTextContent(clearContent);
    }

    private void decorateArticle(NBArticle article) {

    }
}
