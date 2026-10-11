package com.cskaoyan.wordmemorize.sms;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Data
public class SmsDelayMessage implements Delayed {

    Long userId;
    Integer vocCount;
    LocalTime remindTime;
    String userName;
    String phoneNumber;

    @Override
    public long getDelay(@NotNull TimeUnit unit) {
        LocalTime now = LocalTime.now();
        Duration between = Duration.between(now, remindTime);
        return between.toMinutes();
    }

    @Override
    public int compareTo(@NotNull Delayed other) {
        Long substraction = getDelay(TimeUnit.MINUTES) - other.getDelay(TimeUnit.MINUTES);
        return substraction.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmsDelayMessage that = (SmsDelayMessage) o;
        return Objects.equals(userId, that.userId) && Objects.equals(vocCount, that.vocCount) && Objects.equals(remindTime, that.remindTime) && Objects.equals(userName, that.userName) && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, vocCount, remindTime, userName, phoneNumber);
    }
}
