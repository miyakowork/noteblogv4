package me.wuwenbin.noteblogv4.service.dashboard;

import me.wuwenbin.noteblogv4.dao.repository.ArticleRepository;
import me.wuwenbin.noteblogv4.dao.repository.CommentRepository;
import me.wuwenbin.noteblogv4.dao.repository.MessageRepository;
import me.wuwenbin.noteblogv4.dao.repository.UserRepository;
import me.wuwenbin.noteblogv4.model.pojo.vo.BaseDataStatistics;
import me.wuwenbin.noteblogv4.model.pojo.vo.LatestComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * created by Wuwenbin on 2019-01-08 at 01:24
 *
 * @author wuwenbin
 */
@Transactional(rollbackOn = Exception.class)
@Service
public class DashboardServiceImpl implements DashboardService {

    private final ArticleRepository articleRepository;
    private final MessageRepository messageRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Autowired
    public DashboardServiceImpl(ArticleRepository articleRepository,
                                MessageRepository messageRepository, CommentRepository commentRepository,
                                UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.messageRepository = messageRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BaseDataStatistics calculateData() {
        long articles = articleRepository.countByDraft(false);
        return null;
    }

    @Override
    public LatestComment findLatestComment() {
        return null;
    }
}
