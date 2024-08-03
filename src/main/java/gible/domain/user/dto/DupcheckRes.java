package gible.domain.user.dto;

public record DupcheckRes (Boolean isDuplicated){
    public static DupcheckRes from(Boolean isDuplicated) {
        return new DupcheckRes(isDuplicated);
    }
}
