package com.cskaoyan.wordmemorize.dao.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCheckStickDO {

    LocalDate curContinuousStartDate;

    Integer curContinuousDays;

    Long stickNo;
}
