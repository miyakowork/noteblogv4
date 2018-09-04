package me.wuwenbin.noteblogv4;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import me.wuwenbin.noteblogv4.dao.repository.CateRepository;
import me.wuwenbin.noteblogv4.dao.repository.TagRepository;
import me.wuwenbin.noteblogv4.model.entity.NBCate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.io.File;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NoteBlogV4ApplicationTests {

    //    @Test
    public void contextLoads() {
        String ACCESS_KEY = "tfrkgwObJguLqFJFC55LBFJKpe53MYVppdu0pkIP";
        String SECRET_KEY = "ei4sc7sfTr5QHa4BGaTXmv-GQmqKfDhpQAwhl_YE";
        String BUCKET_NAME = "wuwenbin";
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY); //密钥配置
        String token = auth.uploadToken(BUCKET_NAME); //简单上传，使用默认策略，只需要设置上传的空间名就可以了
        try {
            String resId = UUID.randomUUID().toString();
            File file = new File("C:\\Users\\wuwen\\Pictures\\壁纸\\QQ图片20171105121348.jpg");
            String fileName = file.getName();
            String extend = fileName.substring(fileName.lastIndexOf("."));
            Configuration configuration = new Configuration(Zone.autoZone());
            Response res = new UploadManager(configuration).put(file, resId.concat(extend), token); //调用put方法上传
            System.out.println(res);
        } catch (QiniuException e) {
            Response re = e.response;
            System.out.println(re);
            try {
                System.out.println(re.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CateRepository cateRepository;

    @Test
    @Transactional
    public void testTxJpaAndMybatis() {
        testTxJpa();
        testTxMybatis();
    }

    @Test
//    @Transactional
    public void testTxJpa() {
        NBCate cate1 = NBCate.builder().cnName("1").name("1").build();
        cateRepository.save(cate1);
        int s = 1 / 0;
        NBCate cate2 = NBCate.builder().cnName("2").name("2").build();
        cateRepository.save(cate2);
    }

    @Test
//    @Transactional
    public void testTxMybatis() {
        String name1 = "1";
//        tagMapper.save(name1);
        int s = 1 / 0;
        String name2 = "2";
//        tagMapper.save(name2);
    }
}
