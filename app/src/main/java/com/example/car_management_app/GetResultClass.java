package com.example.car_management_app;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetResultClass {
    @SerializedName("meta")
    @Expose
    public Meta meta;

    @SerializedName("documents")
    @Expose
    public List<Documents> documents;

    public class Meta {
        @SerializedName("same_name")
        @Expose
        private RegionInfo same_name;

        @SerializedName("pageable_count")
        @Expose
        private int pageable_count;

        @SerializedName("total_count")
        @Expose
        private int total_count;

        @SerializedName("is_end")
        @Expose
        private Boolean is_end;

        public Integer getTotal_count() {return total_count;}

        public void setTotal_count(int total_count) {this.total_count = total_count;}

        public Integer getPageable_count() {return pageable_count;}

        public void setPageable_count(Integer pageable_count) {this.pageable_count = pageable_count;}

        public Boolean getIs_end() {return is_end;}

        public void setIs_end(Boolean is_end) {this.is_end = is_end;}

        public RegionInfo getSame_name() {return same_name;}

        public void setSame_name(RegionInfo same_name) {this.same_name = same_name;}
    }

    public class RegionInfo {
        @SerializedName("region")
        @Expose
        private List<String>  region;

        @SerializedName("keyword")
        @Expose
        private String keyword;

        @SerializedName("selected_region")
        @Expose
        private String selected_region;

        public List<String> getRegion() {return region;}

        public void setRegion(List<String> region) {this.region = region;}

        public String getKeyword() {return keyword;}

        public void setKeyword(String keyword) {this.keyword = keyword;}

        public String getSelected_region() {return selected_region;}

        public void setSelected_region(String selected_region) {this.selected_region = selected_region;}

    }

    public class Documents {

        @SerializedName("place_name")
        private String place_name;

        @SerializedName("distance")
        private String distance;

        @SerializedName("place_url")
        private String place_url;

        @SerializedName("category_name")
        private String category_name;

        @SerializedName("address_name")
        private String address_name;

        @SerializedName("road_address_name")
        private String road_address_name;

        @SerializedName("id")
        private String id;

        @SerializedName("phone")
        private String phone;

        @SerializedName("category_group_code")
        private String category_group_code;

        @SerializedName("category_group_name")
        private String category_group_name;

        @SerializedName("x")
        private String x;

        @SerializedName("y")
        private String y;

        public String getId() {return id;}

        public void setId(String id) {this.id = id;}

        public String getPlace_name() {return place_name;}

        public void setPlace_name(String place_name) {this.place_name = place_name;}

        public String getCategory_name() {return category_name;}

        public void setCategory_name(String category_name) {this.category_name = category_name;}

        public String getCategory_group_code() {return  category_group_code;}

        public void setCategory_group_code(String category_group_code) {this.category_group_code = category_group_code;}

        public String getCategory_group_name() {return category_group_name;}

        public void setCategory_group_name(String category_group_name) {this.category_group_name = category_group_name;}

        public String getPhone() {return phone;}

        public void setPhone(String phone) {this.phone = phone;}

        public String getAddress_name() {return address_name;}

        public void setAddress_name(String address_name) {this.address_name = address_name;}

        public String getRoad_address_name() {return road_address_name;}

        public void setRoad_address_name(String road_address_name) {this.road_address_name = road_address_name;}

        public String getX() {return x;}

        public void setX(String x) {this.x = x;}

        public String getY() {return y;}

        public void setY(String y) {this.y = y;}

        public String getPlace_url() {return place_url;}

        public void setPlace_url(String place_url) {this.place_url = place_url;}

        public String getDistance() {return distance;}

        public void setDistance(String distance) {this.distance = distance;}
    }
}
