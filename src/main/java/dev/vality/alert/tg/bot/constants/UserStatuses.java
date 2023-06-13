package dev.vality.alert.tg.bot.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum UserStatuses {

    CREATOR("creator"),
    ADMINISTRATOR("administrator"),
    MEMBER("member");

    private final String value;

    public static final Set<String> ALLOWED_USER_STATUSES =
            Set.of(
                    CREATOR.getValue(),
                    ADMINISTRATOR.getValue(),
                    MEMBER.getValue()
            );
}
