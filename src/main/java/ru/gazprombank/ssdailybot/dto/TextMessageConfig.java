package ru.gazprombank.ssdailybot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TextMessageConfig {

    private String prefix;
    private List<String> bodies;
    private String suffix;
}
