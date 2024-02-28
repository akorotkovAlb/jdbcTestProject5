package org.example.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.utils.Gender;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String name;
    private LocalDate birthday;
    private boolean active;
    private Gender gender;

    public User (String name, LocalDate birthday, boolean active, Gender gender) {
        this.name = name;
        this.birthday = birthday;
        this.active = active;
        this.gender = gender;
    }
}
