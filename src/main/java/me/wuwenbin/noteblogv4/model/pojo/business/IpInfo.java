package me.wuwenbin.noteblogv4.model.pojo.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * created by Wuwenbin on 2018/2/8 at 16:41
 * @author wuwenbin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IpInfo implements Serializable {

    private int code;
    private Info data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        private String ip;
        private String country;
        private String area;
        private String region;
        private String city;
        private String county;
        private String isp;
        private String countryId;
        private String areaId;
        private String regionId;
        private String cityId;
        private String countyId;
        private String ispId;
    }
}
