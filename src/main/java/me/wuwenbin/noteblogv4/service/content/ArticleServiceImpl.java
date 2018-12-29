package me.wuwenbin.noteblogv4.service.content;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HtmlUtil;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.dao.repository.TagReferRepository;
import me.wuwenbin.noteblogv4.dao.repository.TagRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.constant.TagType;
import me.wuwenbin.noteblogv4.model.entity.NBArticle;
import me.wuwenbin.noteblogv4.model.entity.NBCate;
import me.wuwenbin.noteblogv4.model.entity.NBTag;
import me.wuwenbin.noteblogv4.model.entity.NBTagRefer;
import me.wuwenbin.noteblogv4.model.pojo.bo.ArticleQueryBO;
import me.wuwenbin.noteblogv4.service.param.ParamService;
import me.wuwenbin.noteblogv4.util.NBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.util.RandomUtil.randomInt;
import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;

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
    public void createArticle(NBArticle article, String tagNames) {
        if (StringUtils.isEmpty(tagNames)) {
            throw new RuntimeException("tagNames 不能为空！");
        }
        if (!StringUtils.isEmpty(article.getUrlSequence())) {
            boolean isExistUrl = articleRepository.countByUrlSequence(article.getUrlSequence()) > 0;
            if (isExistUrl) {
                throw new RuntimeException("已存在 url：" + article.getUrlSequence());
            }
        }
        setArticleSummaryAndTxt(article);
        decorateArticle(article);
        NBArticle newArticle = articleRepository.save(article);
        String[] tagNameArray = tagNames.split(",");
        saveTags(newArticle, tagNameArray, tagRepository, tagReferRepository);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateArticle(NBArticle article, String tagNames) {
        if (StringUtils.isEmpty(article.getId())) {
            throw new RuntimeException("未指定修改文章的ID");
        }
        if (StringUtils.isEmpty(tagNames)) {
            throw new RuntimeException("tagNames 不能为空！");
        }
        if (!StringUtils.isEmpty(article.getUrlSequence())) {
            boolean isExistUrl = articleRepository.countByUrlSequence(article.getUrlSequence()) > 0;
            if (isExistUrl) {
                throw new RuntimeException("已存在 url：" + article.getUrlSequence());
            }
        }
        setArticleSummaryAndTxt(article);
        decorateArticle(article);
        NBArticle updateArticle = articleRepository.save(article);
        if (updateArticle != null) {
            tagReferRepository.deleteByReferId(updateArticle.getId());
            String[] tagNameArray = tagNames.split(",");
            saveTags(updateArticle, tagNameArray, tagRepository, tagReferRepository);
        }
    }

    @Override
    public Page<NBArticle> findPageInfo(Pageable pageable, String title, Long authorId) {
        return articleRepository.findAll((Specification<NBArticle>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (title != null && StrUtil.isNotEmpty(title)) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }
            if (authorId != null) {
                predicates.add(criteriaBuilder.equal(root.get("authorId"), authorId));
            }
            Join<NBArticle, NBCate> cateCnNameJoin = root.join(root.getModel().getSingularAttribute("cate", NBCate.class), JoinType.LEFT);
            predicates.add(criteriaBuilder.equal(cateCnNameJoin.get("id").as(Long.class), root.get("cateId")));
            Predicate[] pres = new Predicate[predicates.size()];
            return query.where(predicates.toArray(pres)).getRestriction();
        }, pageable);
    }


    @Override
    public Page<NBArticle> findBlogArticles(Pageable pageable, ArticleQueryBO articleQueryBO) {
        if (StringUtil.isEmpty(articleQueryBO.getTagSearch())) {
            String searchStr = articleQueryBO.getSearchStr() == null ? "" : articleQueryBO.getSearchStr();
            NBArticle prob = NBArticle.builder().textContent(searchStr)
                    .title(searchStr).build();
            if (articleQueryBO.getCateId() != null) {
                prob.setCateId(articleQueryBO.getCateId());
            }
            prob.setDraft(false);
            ExampleMatcher matcher = ExampleMatcher.matchingAny()
                    .withMatcher("title", ExampleMatcher.GenericPropertyMatcher::contains)
                    .withMatcher("textContent", ExampleMatcher.GenericPropertyMatcher::contains)
                    .withIgnorePaths("post", "modify", "view", "approveCnt", "commented", "mdContent", "appreciable", "top", "draft")
                    .withIgnoreNullValues();
            Example<NBArticle> articleExample = Example.of(prob, matcher);
            Page<NBArticle> page = articleRepository.findAll(articleExample, pageable);
            List<NBArticle> result = page.getContent().stream().filter(article -> !article.getDraft()).collect(toList());
            return new PageImpl<>(result, pageable, result.size());
        } else {
            String tag = NBUtils.stripXSS(URLUtil.decode(articleQueryBO.getTagSearch(), "UTF-8"));
            NBTag t = tagRepository.findByName(tag);
            if (t == null) {
                return Page.empty(pageable);
            } else {
                long tagId = t.getId();
                List<NBTagRefer> tagRefers = tagReferRepository.findByTagIdAndType(tagId, "article");
                List<Long> articleIds = tagRefers.stream().map(NBTagRefer::getReferId).distinct().collect(toList());
                if (CollectionUtils.isEmpty(articleIds)) {
                    return Page.empty(pageable);
                } else {
                    List<NBArticle> articles = articleRepository.findByIdIn(articleIds, pageable.getPageNumber(), pageable.getPageSize());
                    return new PageImpl<>(articles, pageable, articles.size());
                }
            }
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateTopById(long articleId, boolean top) {
        if (top) {
            int maxTop = articleRepository.findMaxTop();
            return articleRepository.updateTopById(maxTop + 1, articleId) == 1;
        } else {
            int currentTop = articleRepository.getOne(articleId).getTop();
            articleRepository.updateTopsByTop(currentTop);
            return articleRepository.updateTopById(0, articleId) == 1;
        }
    }

    /**
     * 根据文章内容生成文章摘要
     *
     * @param article
     */
    private static void setArticleSummaryAndTxt(NBArticle article) {
        ParamService paramService = NBUtils.getBean(ParamService.class);
        int summaryLength = Integer.valueOf(paramService.getValueByName(NoteBlogV4.Param.ARTICLE_SUMMARY_WORDS_LENGTH));
        String clearContent = HtmlUtil.cleanHtmlTag(StrUtil.trim(article.getContent()));
        clearContent = StringUtils.trimAllWhitespace(clearContent);
        clearContent = clearContent.substring(0, clearContent.length() < summaryLength ? clearContent.length() : summaryLength);
        int allStandardLength = clearContent.length();
        int fullAngelLength = NBUtils.fullAngelWords(clearContent);
        int finalLength = allStandardLength - fullAngelLength / 2;
        if (StringUtils.isEmpty(article.getSummary())) {
            article.setSummary(clearContent.substring(0, finalLength < summaryLength ? finalLength : summaryLength));
        }
        article.setTextContent(clearContent);
    }

    /**
     * 装饰article的一些空值为默认值
     *
     * @param article
     */
    private static void decorateArticle(NBArticle article) {
        article.setPost(now());
        article.setView(randomInt(666, 1609));
        article.setApproveCnt(randomInt(6, 169));
        if (StringUtils.isEmpty(article.getAppreciable())) {
            article.setAppreciable(false);
        }
        if (StringUtils.isEmpty(article.getCommented())) {
            article.setCommented(false);
        }
        if (StringUtils.isEmpty(article.getTop())) {
            article.setTop(0);
        }
    }

    /**
     * 保存文章的 tags
     *
     * @param updateArticle
     * @param tagNameArray
     * @param tagRepository
     * @param tagReferRepository
     */
    private static void saveTags(NBArticle updateArticle, String[] tagNameArray, TagRepository tagRepository, TagReferRepository tagReferRepository) {
        int cnt = 0;
        for (String name : tagNameArray) {
            Example<NBTag> condition = Example.of(NBTag.builder().name(name).build());
            boolean isExist = tagRepository.count(condition) > 0;
            long tagId = isExist ?
                    tagRepository.findByName(name).getId() :
                    tagRepository.save(NBTag.builder().name(name).build()).getId();

            tagReferRepository.save(
                    NBTagRefer.builder()
                            .referId(updateArticle.getId())
                            .tagId(tagId)
                            .show(cnt <= 4)
                            .type(TagType.article.name()).build()
            );
            cnt++;
        }
    }
}
