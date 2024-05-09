package pers.wofbi.mokugyo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static java.util.Objects.nonNull;

@Getter
@Setter
@Entity
@ToString
@Table(name = "xuser")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "t0")
    private String to;
    @Column(name = "fr0m")
    private String from = "";

    private String message = "";

    private Boolean enable = true;

    private Long count = 0L;
    private Date startDateTime;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    public void merge(Dto.SaveUserDto updateUserDto) {
        if (nonNull(updateUserDto.getTo())) {
            this.to = updateUserDto.getTo();
        }
        if (nonNull(updateUserDto.getFrom())) {
            this.from = updateUserDto.getFrom();
        }
        if (nonNull(updateUserDto.getMessage())) {
            this.message = updateUserDto.getMessage();
        }
        if (nonNull(updateUserDto.getEnable())) {
            this.enable = updateUserDto.getEnable();
        }
        if (nonNull(updateUserDto.getCount())) {
            this.count = updateUserDto.getCount();
        }
        if (nonNull(updateUserDto.getStartDateTime())) {
            try {
                this.startDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(updateUserDto.getStartDateTime());
            } catch (ParseException e) {
                if (this.startDateTime == null) {
                    System.out.println("日期格式化错误, 置为当前时间");
                    this.startDateTime = new Date();
                } else {
                    System.out.println("日期格式化错误, 不修改");
                }
            }
        }

    }
}
