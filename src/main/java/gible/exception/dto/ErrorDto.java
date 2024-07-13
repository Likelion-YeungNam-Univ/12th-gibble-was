package gible.exception.dto;

public record ErrorDto(

        int status,
        String message
) {
    public static ErrorDto of(int status, String message) {
        return new ErrorDto(status, message);
    }
}
