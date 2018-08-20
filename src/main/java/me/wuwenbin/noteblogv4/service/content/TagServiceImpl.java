package me.wuwenbin.noteblogv4.service.content;

import me.wuwenbin.noteblogv4.dao.repository.TagRepository;
import me.wuwenbin.noteblogv4.model.constant.TagType;
import me.wuwenbin.noteblogv4.model.pojo.vo.NBTagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * created by Wuwenbin on 2018/8/20 at 11:27
 *
 * @author wuwenbin
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<NBTagVO> findSelectedTagsByReferId(Long referId, TagType type) {
        List<Object[]> tags = tagRepository.findTagListSelected(referId, type.name());
        List<NBTagVO> tagVOList = new ArrayList<>(tags.size());
        for (Object[] objArr : tags) {
            NBTagVO nbTagVO = new NBTagVO();
            nbTagVO.setId(Long.valueOf(objArr[0].toString()));
            nbTagVO.setName(objArr[1].toString());
            nbTagVO.setSelected(objArr[2].toString());
            tagVOList.add(nbTagVO);
        }
        return tagVOList;
    }
}
