package ru.gazprombank.ssdailybot.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class FortuneWheelService {

    private final Cache<LocalDate, String> chosenParticipants = CacheBuilder.newBuilder()
            .expireAfterAccess(Duration.ofDays(6))
            .build();

    private final List<String> participants = List.of(
            "Николай П. @nick_pn",
            "Георгий Ш. @flyyet",
            "Константин Ш. @kshurukhin",
            "Евгений З. @ZiGiDi",
            "Алексей Б. @mr_fduch",
            "Олег Р. @olegidse",
            "Татьяна С. @nainats",
            "Салават А. @slavkoego",
            "Андрей Ш. @ap_shirokov",
            "Егор Г. @egorkawork",
            "Александр П. @sanman2005",
            "Александр Г. @xgubenko"
    );

    private final List<String> congratulationMessages = List.of(
            "Поздравляем \"%s\" с ролью ведущего ежедневной конференции! Теперь ты будешь в центре внимания и руководить важным событием.",
            "Отличная новость! Поздравляем \"%s\", что тебя выбрали ведущим ежедневной конференции. Твои навыки коммуникации и организации будут очень полезны.",
            "Поздравляем \"%s\" с новой должностью ведущего ежедневной конференции! Ты точно заслужил(-а) эту возможность своим профессионализмом и преданностью работе.",
            "От всей души поздравляем \"%s\", что стал(-а) ведущим ежедневной конференции! Уверен, ты прекрасно справишься с этой ответственной задачей.",
            "Поздравляем \"%s\" с новыми обязанностями ведущего ежедневной конференции! Теперь ты станешь еще более видимым и востребованным членом команды.",
            "От всего сердца поздравляем \"%s\", что выбрали ведущим ежедневной конференции! Твоя энергия и профессионализм принесут новый уровень в организацию мероприятий.",
            "Поздравляем \"%s\" с новой ролью ведущего ежедневной конференции! Теперь ты будешь играть ключевую роль в поддержании эффективности и связи в нашей команде.",
            "От всей души поздравляем \"%s\", что стал(-а) ведущим ежедневной конференции! Твоя способность управлять людьми и делать принципиальные решения поможет нам достичь новых высот.",
            "Поздравляем \"%s\" с новой должностью ведущего ежедневной конференции! Твоя харизма и профессионализм сделают каждое мероприятие незабываемым.",
            "От всей команды поздравляем \"%s\", что выбрали тебя ведущим ежедневной конференции! Твой талант и преданность работе не остались незамеченными."
    );

    private final Random random = new Random();

    public String createReadyMsg() {
        String participant = chooseParticipant(LocalDate.now().plusDays(1));

        return createCongratulationMsg(participant);
    }

    public String createCongratulationMsg(String participant) {
        String congratulationMsg = congratulationMessages.get(random.nextInt(congratulationMessages.size()));

        return String.format(congratulationMsg, participant);
    }

    public String chooseParticipant(LocalDate localDate) {
        chosenParticipants.invalidate(localDate);

        ConcurrentMap<LocalDate, String> chosenParticipantsMap = chosenParticipants.asMap();

        List<String> accessibleParticipants = participants.stream()
                .filter(p -> !chosenParticipantsMap.containsValue(p))
                .toList();

        String chosenParticipant = accessibleParticipants.get(random.nextInt(accessibleParticipants.size()));
        chosenParticipants.put(localDate, chosenParticipant);

        return chosenParticipant;
    }
}
