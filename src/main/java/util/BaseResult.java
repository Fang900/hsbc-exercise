package util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResult {
    private boolean isSuccess;
    private int code;
    private String errorMessage;
    private Object data;
}
