package com.mycompany.assignment2server;

public class GetRequest {
    private Integer skierId;
    private Integer dayId;

    public GetRequest(Integer skierId, Integer dayId) {
        this.skierId = skierId;
        this.dayId = dayId;
    }

    public Integer getSkierId() {
        return skierId;
    }

    public void setSkierId(Integer skierId) {
        this.skierId = skierId;
    }

    public Integer getDayId() {
        return dayId;
    }

    public void setDayId(Integer dayId) {
        this.dayId = dayId;
    }
}
