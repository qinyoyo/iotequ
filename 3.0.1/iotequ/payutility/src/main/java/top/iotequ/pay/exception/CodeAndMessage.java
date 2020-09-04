package top.iotequ.pay.exception;

import lombok.Getter;

@Getter
public class CodeAndMessage {

    private int code;
    private String message;

    public CodeAndMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
