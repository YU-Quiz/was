package yuquiz.common.api;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SuccessRes<T> {

    private T response;

    @Builder
    private SuccessRes(T response) {
        this.response = response;
    }

    public static <T> SuccessRes<T> from(T response) {
        return SuccessRes.<T>builder()
                .response(response)
                .build();
    }
}
