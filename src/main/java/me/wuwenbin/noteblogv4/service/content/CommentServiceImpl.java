package me.wuwenbin.noteblogv4.service.content;

import cn.hutool.core.util.StrUtil;
import me.wuwenbin.noteblogv4.dao.repository.CommentRepository;
import me.wuwenbin.noteblogv4.model.entity.NBComment;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import me.wuwenbin.noteblogv4.model.pojo.bo.CommentBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * created by Wuwenbin on 2018/9/7 at 9:43
 *
 * @author wuwenbin
 */
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Page<NBComment> findPageInfo(Pageable pageable, CommentBO commentBO) {
        return commentRepository.findAll((Specification<NBComment>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (commentBO.getArticleId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("articleId"), commentBO.getArticleId()));
            }
            if (commentBO.getUserId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("userId"), commentBO.getUserId()));
            }
            if (commentBO.getClearComment() != null && StrUtil.isNotEmpty(commentBO.getClearComment())) {
                predicates.add(criteriaBuilder.like(root.get("clearComment"), "%" + commentBO.getClearComment() + "%"));
            }
            if (commentBO.getIpCnAddr() != null && StrUtil.isNotEmpty(commentBO.getIpCnAddr())) {
                predicates.add(criteriaBuilder.like(root.get("ipCnAddr"), "%" + commentBO.getIpCnAddr() + "%"));
            }
            Join<NBComment, NBSysUser> userJoin = root.join(root.getModel().getSingularAttribute("user", NBSysUser.class), JoinType.LEFT);
            predicates.add(criteriaBuilder.equal(userJoin.get("id").as(Long.class), root.get("userId")));
            Predicate[] pres = new Predicate[predicates.size()];
            return query.where(predicates.toArray(pres)).getRestriction();
        }, pageable);
    }
}
