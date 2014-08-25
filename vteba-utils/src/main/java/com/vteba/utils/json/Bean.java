package com.vteba.utils.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;

@JsonIgnoreProperties(value = {"URL"})
public class Bean {
    private String userName;
    private Date date;
    private String dates;
    
    @JsonProperty("name")
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    @JsonDeserialize(using = DateDeserializers.DateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonProperty("pubtime")
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonProperty("datetime")
    public String getDates() {
        return dates;
    }
    
    public void setDates(String dates) {
        this.dates = dates;
    }
    
}
