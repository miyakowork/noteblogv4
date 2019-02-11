package me.wuwenbin.noteblogv4.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv4.config.application.NBContext;
import me.wuwenbin.noteblogv4.exception.MethodNotMatchException;
import me.wuwenbin.noteblogv4.model.constant.NoteBlogV4;
import me.wuwenbin.noteblogv4.model.constant.Upload;
import me.wuwenbin.noteblogv4.model.entity.permission.NBSysUser;
import me.wuwenbin.noteblogv4.model.pojo.business.Base64MultipartFile;
import me.wuwenbin.noteblogv4.model.pojo.business.IpInfo;
import me.wuwenbin.noteblogv4.service.param.ParamService;
import me.wuwenbin.noteblogv4.service.upload.UploadService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * created by Wuwenbin on 2018/7/14 at 10:33
 *
 * @author wuwenbin
 */
@Component
@Slf4j
public class NBUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;
    private static final String UNKNOWN = "unknown";

    /**
     * 获取实际ip地址
     *
     * @param request
     * @return
     */
    public static String getRemoteAddress(HttpServletRequest request) {
        String remoteAddress;
        try {
            remoteAddress = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(remoteAddress) || UNKNOWN.equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(remoteAddress) || remoteAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(remoteAddress) || UNKNOWN.equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(remoteAddress) || UNKNOWN.equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(remoteAddress) || UNKNOWN.equalsIgnoreCase(remoteAddress)) {
                remoteAddress = request.getRemoteAddr();
            }
        } catch (Exception var3) {
            remoteAddress = request.getRemoteAddr();
        }
        return remoteAddress;
    }

    /**
     * 删除不必要的信息，避免暴露过多信息
     *
     * @param user
     * @return
     */
    public static Map<Object, Object> user2Map(NBSysUser user) {
        if (user == null) {
            return null;
        }
        return MapUtil.of(new Object[][]{
                {"id", user.getId()},
                {"nickname", user.getNickname()},
                {"avatar", user.getAvatar()},
                {"dri", user.getDefaultRoleId()}
        });
    }

    /**
     * 根据当前请求获取用户对象
     *
     * @param
     * @return
     */
    public static NBSysUser getSessionUser() {
        Cookie cookie = CookieUtils.getCookie(getCurrentRequest(), NoteBlogV4.Session.SESSION_ID_COOKIE);
        if (cookie != null) {
            String sessionId = cookie.getValue();
            return applicationContext.getBean(NBContext.class).getSessionUser(sessionId);
        }
        return null;
    }

    /**
     * 获取当前的request对象
     *
     * @return
     */
    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Objects.requireNonNull(attributes).getRequest();
    }


    /**
     * 获取工程的发布路径根目录
     * 即classes的绝对路径
     * file:/E:/idea_workplace/target/classes/
     *
     * @return
     */
    public static String getClassesPath() {
        return Objects.requireNonNull(NBUtils.class.getClassLoader().getResource("")).getPath();
    }

    /**
     * 获取改文件在工程中所在的完整绝对路径
     *
     * @param filePath 相对classes的路径
     * @return
     */
    public static String getFilePathInClassesPath(String filePath) {
        return getClassesPath() + filePath;
    }

    /**
     * 判断是否为ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return StrUtil.isNotBlank(request.getHeader("x-requested-with")) && "XMLHttpRequest".equals(request.getHeader("x-requested-with"));
    }

    /**
     * 是否为 json 请求
     *
     * @param request
     * @return
     */
    public static boolean isJson(HttpServletRequest request) {
        String headerAccept = request.getHeader("Accept");
        return !isEmpty(headerAccept) && headerAccept.contains("application/json");
    }

    /**
     * 是否为get请求
     *
     * @param request
     * @return
     */
    public static boolean isGetRequest(HttpServletRequest request) {
        String method = request.getMethod();
        return "GET".equalsIgnoreCase(method);
    }

    /**
     * 是否为 router 请求
     *
     * @param request
     * @return
     */
    public static boolean isRouterRequest(HttpServletRequest request) {
        String headerAccept = request.getHeader("Accept");
        return !isEmpty(headerAccept) && headerAccept.contains("text/html") && !isJson(request) && isAjaxRequest(request) && isGetRequest(request);
    }

    /**
     * 获取Bean
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    /**
     * http请求获取ip对象
     *
     * @param ip
     * @return
     */
    public static IpInfo getIpInfo(String ip) {
        String url = "http://ip.taobao.com/service/getIpInfo.php?ip=" + ip;
        String resp = HttpUtil.get(url);
        log.info("请求 ip.taobao.com，请求参数：{}", ip);
        try {
            JSONObject jsonObject = JSONUtil.parseObj(resp);
            return jsonObject.toBean(IpInfo.class);
        } catch (Exception e) {
            log.error("返回结果：" + resp);
            log.error("解析json出错，返回结果有误", e);
            return IpInfo.builder().data(null).code(1).build();
        }
    }

    /**
     * 获取ip地理位置信息
     *
     * @param ipInfo
     * @return
     */
    public static String getIpCnInfo(IpInfo ipInfo) {
        String undefined = "x";
        if (ipInfo == null || ipInfo.getData() == null) {
            return "未知位置";
        }
        boolean isLocal = ipInfo.getData().getIp().contains("127.0.0.1") || ipInfo.getData().getIp().contains("localhost");
        String temp = ipInfo.getData().getCountry() + ipInfo.getData().getRegion() + ipInfo.getData().getCity();
        if (!ipInfo.getData().getCounty().toLowerCase().contains(undefined) && !isLocal) {
            return temp + ipInfo.getData().getCounty();
        } else {
            return temp;
        }
    }

    /**
     * 返回值类型为Map<String, Object>
     *
     * @param properties
     * @return
     */
    public static Map<String, Object> getParameterMap(Map<String, String[]> properties) {
        Map<String, Object> returnMap = new HashMap<>(16);
        Iterator<Map.Entry<String, String[]>> iterator = properties.entrySet().iterator();
        String name;
        String value = "";
        while (iterator.hasNext()) {
            Map.Entry<String, String[]> entry = iterator.next();
            name = entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else {
                String[] values = (String[]) valueObj;
                //用于请求参数中有多个相同名称
                for (String value1 : values) {
                    value = value1 + ",";
                }
                value = value.substring(0, value.length() - 1);
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }

    /**
     * 根据设定的上传方式（本地服务器上传还是七牛云上传）来匹配相应的service实例
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> UploadService<T> getUploadServiceByConfig() {
        final String name = NoteBlogV4.Param.UPLOAD_TYPE;
        final String local = "local", qiniu = "qiniu";
        String config = applicationContext.getBean(ParamService.class).getValueByName(name);
        if (config != null) {
            Upload.Method method = Upload.Method.getMethodByName(config);
            if (local.equalsIgnoreCase(method.name())) {
                return applicationContext.getBean("localUpload", UploadService.class);
            } else if (qiniu.equalsIgnoreCase(method.name())) {
                return applicationContext.getBean("qiniuUpload", UploadService.class);
            } else {
                throw new MethodNotMatchException("未找到相应的上传类型的Service实例！");
            }
        }
        return null;
    }

    /**
     * 判断全角的字符串，包括全角汉字以及全角标点
     *
     * @param charSequence
     * @return
     */
    public static int fullAngelWords(CharSequence charSequence) {
        if (charSequence == null) {
            return 0;
        }
        char[] t1 = charSequence.toString().toCharArray();
        int count = 0;
        for (char aT1 : t1) {
            if (Character.toString(aT1).matches("[^\\x00-\\xff]")) {
                System.out.println(aT1);
                count++;
            }
        }
        return count;
    }


    /**
     * @param value 待处理内容
     * @return
     * @Description 过滤XSS脚本内容
     */
    public static String stripXSS(String value) {
        String rlt = null;

        if (null != value) {
            rlt = value.replaceAll("", "");
            Pattern scriptPattern = compile("<script>(.*?)</script>", CASE_INSENSITIVE);
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            scriptPattern = compile("</script>", CASE_INSENSITIVE);
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            scriptPattern = compile("<script(.*?)>", CASE_INSENSITIVE
                    | MULTILINE | DOTALL);
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            scriptPattern = compile("eval\\((.*?)\\)", CASE_INSENSITIVE
                    | MULTILINE | DOTALL);
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            scriptPattern = compile("expression\\((.*?)\\)", CASE_INSENSITIVE
                    | MULTILINE | DOTALL);
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            scriptPattern = compile("javascript:", CASE_INSENSITIVE);
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            scriptPattern = compile("vbscript:", CASE_INSENSITIVE);
            rlt = scriptPattern.matcher(rlt).replaceAll("");

            scriptPattern = compile("onload(.*?)=", CASE_INSENSITIVE
                    | MULTILINE | DOTALL);
            rlt = scriptPattern.matcher(rlt).replaceAll("");
        }

        return rlt;
    }

    /**
     * @param value 待处理内容
     * @return
     * @Description 过滤SQL注入内容
     */
    public static String stripSqlInjection(String value) {
        return (null == value) ? null : value.replaceAll("('.+--)|(--)|(%7C)", "");
    }

    /**
     * @param value 待处理内容
     * @return
     * @Description 过滤SQL/XSS注入内容
     */
    public static String stripSqlXSS(String value) {
        return stripXSS(stripSqlInjection(value));
    }

    /**
     * base64转multipart file
     *
     * @param base64
     * @return
     */
    public static MultipartFile base64ToMultipartFile(String base64) {
        try {
            String[] baseStrs = base64.split(",");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b;
            b = decoder.decodeBuffer(baseStrs[1]);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return new Base64MultipartFile(b, baseStrs[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    public static Map<String, Object> toLowerKeyMap(Map<String, Object> originMap) {
        if (CollectionUtils.isEmpty(originMap)) {
            return null;
        }
        Map<String, Object> newMap = new TreeMap<>();
        originMap.forEach((k, v) -> newMap.put(k.toLowerCase(), v));
        return newMap;
    }

    public static List<Map<String, Object>> toLowerKeyListMap(List<Map<String, Object>> originListMap) {
        if (CollectionUtils.isEmpty(originListMap)) {
            return null;
        }
        List<Map<String, Object>> newListMap = new LinkedList<>();
        originListMap.forEach(m -> newListMap.add(toLowerKeyMap(m)));
        return newListMap;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        NBUtils.applicationContext = applicationContext;
    }


}
