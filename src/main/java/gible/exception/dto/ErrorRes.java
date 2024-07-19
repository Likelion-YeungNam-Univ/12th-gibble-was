package gible.exception.dto;

public record ErrorRes(

        int status,
        String message
) {
    public static ErrorRes of(int status, String message) {
        return new ErrorRes(status, message);
    }
}
