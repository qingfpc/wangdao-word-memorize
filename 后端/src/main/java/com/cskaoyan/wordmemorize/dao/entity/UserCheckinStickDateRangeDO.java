package com.cskaoyan.wordmemorize.dao.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCheckinStickDateRangeDO {

    LocalDate startDate;

    LocalDate endDate;
}
