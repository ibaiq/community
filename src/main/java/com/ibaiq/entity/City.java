package com.ibaiq.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 十三
 */
@Data
public class City implements Serializable {

    @Serial
    private static final long serialVersionUID = -749986955668022607L;

    private Integer status;
    private String message;
    private Result result;

    @Data
    public static class Result {

        private String ip;
        private Location location;
        private AdInfo adInfo;

        @Data
        public static class Location {
            private Double lat;
            private Double lng;
        }

        @Data
        public static class AdInfo {
            private String nation;
            private String province;
            private String city;
            private String district;
            private Integer adcode;
        }

    }

}
