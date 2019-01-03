package me.wuwenbin.noteblogv4.service.login;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.dao.repository.ParamRepository;
import me.wuwenbin.noteblogv4.dao.repository.UserRepository;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import me.wuwenbin.noteblogv4.model.pojo.business.QqLoginModel;
import me.wuwenbin.noteblogv4.model.pojo.framework.NBR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static me.wuwenbin.noteblogv4.model.pojo.framework.NBR.error;
import static me.wuwenbin.noteblogv4.model.pojo.framework.NBR.ok;

/**
 * created by Wuwenbin on 2018/1/23 at 12:33
 * @author wuwenbin
 */
@Slf4j
@Service
public class QqLoginServiceImpl implements LoginService<QqLoginModel> {

    private final ParamRepository paramRepository;
    private final UserRepository userRepository;

    @Autowired
    public QqLoginServiceImpl(ParamRepository paramRepository, UserRepository userRepository) {
        this.paramRepository = paramRepository;
        this.userRepository = userRepository;
    }


    @Override
    public NBR doLogin(QqLoginModel model) {
        try {
            String appId = paramRepository.findByName(NoteBlogV4.Param.APP_ID).getValue();
            String appKey = paramRepository.findByName(NoteBlogV4.Param.APP_KEY).getValue();

            Map<String, Object> p1 = MapUtil.of("grant_type", "authorization_code");
            p1.put("client_id", appId);
            p1.put("client_secret", appKey);
            p1.put("code", model.getCode());
            p1.put("redirect_uri", model.getCallbackDomain());

            String resp1 = HttpUtil.get("https://graph.qq.com/oauth2.0/token", p1);
            String accessToken = resp1.substring(13, resp1.length() - 66);
            String callback = HttpUtil.get("https://graph.qq.com/oauth2.0/me", MapUtil.of("access_token", accessToken));
            String openId = callback.substring(45, callback.length() - 6);

            Map<String, Object> p2 = MapUtil.of("access_token", accessToken);
            p2.put("oauth_consumer_key", appId);
            p2.put("openid", openId);

            JSONObject json2 = JSONUtil.parseObj(HttpUtil.get("https://graph.qq.com/user/get_user_info", p2));
            if (json2.getInt("ret") == 0) {
                NBSysUser user = userRepository.findByQqOpenIdAndEnable(openId, true);
                if (user != null) {
                    return ok("授权成功！", "/").put(NoteBlogV4.Session.LOGIN_USER, user);
                } else {
                    NBSysUser lockedUser = userRepository.findByQqOpenIdAndEnable(openId, false);
                    if (lockedUser != null) {
                        return error("QQ登录授权失败，原因：用户已被锁定！");
                    }
                    String nickname = json2.getStr("nickname");
                    String avatar = json2.getStr("figureurl_qq_2").replace("http://", "https://");
                    NBSysUser registerUser = NBSysUser.builder().nickname(nickname).avatar(avatar).qqOpenId(openId).build();
                    NBSysUser afterRegisterUser = userRepository.save(registerUser);
                    if (afterRegisterUser != null) {
                        return ok("授权成功！", "/").put(NoteBlogV4.Session.LOGIN_USER, afterRegisterUser);
                    } else {
                        return error("QQ登录授权失败，原因：注册失败！");
                    }
                }
            } else {
                String errorMsg = json2.getStr("msg");
                log.error("QQ登录授权失败，原因：{}", errorMsg);
                return error("QQ登录授权失败，原因：{}", errorMsg);
            }
        } catch (StringIndexOutOfBoundsException e) {
            log.error("[accessToken] 返回值有误！");
            return error("请求重复或返回 [accessToken] 值有误！");
        } catch (Exception e) {
            log.error("QQ登录授权失败，原因：{}", e);
            return error("QQ登录授权失败，原因：{}", e.getMessage());
        }
    }
}
