package yuquiz.domain.user.dto;

import lombok.Getter;

@Getter
public enum UserStatusReq {
    SUSPEND,
    UNSUSPEND,
    CANCEL;
}
