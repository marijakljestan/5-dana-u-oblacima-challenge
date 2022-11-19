package com.hakaton.challenge.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private int id;
    private String email;
    private String firstname;
    private String lastname;
    private static final Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public Boolean isValid()
    {
       Matcher matcher = pattern.matcher(this.email);
       return matcher.find();
    }


}
