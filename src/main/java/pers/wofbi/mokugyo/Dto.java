package pers.wofbi.mokugyo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static java.util.Objects.nonNull;
import static pers.wofbi.mokugyo.MokugyoApplication.*;


public class Dto {


        @Data
        @ToString
        @NoArgsConstructor
        public static class SaveUserDto {
            private Long id;
            private String to;
            private String from;
            private String message;
            private Long count;
            private Boolean enable;
            private String startDateTime;

        }


    @Data
    @NoArgsConstructor
    public static class UserDto {
        private Long id;

        private String to;

        private String from;

        private String message;

        private Boolean enable;

        private Long count;
        private Long autoCount;
        private String startDateTime;


        private OffsetDateTime createdAt;

        private OffsetDateTime updatedAt;

        public UserDto(User user) {
            this.id = user.getId();
            this.to = user.getTo();
            this.from = user.getFrom();
            this.message = user.getMessage();
            this.enable = user.getEnable();
            this.count = user.getCount();
            this.startDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(user.getStartDateTime());
            if (nonNull(user.getStartDateTime())) {
                Date now = new Date();
                this.autoCount = ChronoUnit.SECONDS.between(user.getStartDateTime().toInstant(), now.toInstant());
            } else {
                this.autoCount = 0L;
            }
            this.createdAt = user.getCreatedAt();
            this.updatedAt = user.getUpdatedAt();
        }
    }

    @Data
    @NoArgsConstructor
    public static class LoginDto {
        private String username;
        private String password;
    }
}