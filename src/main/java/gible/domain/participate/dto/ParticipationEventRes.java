package gible.domain.participate.dto;

import gible.domain.event.dto.EventInfoRes;
import gible.domain.event.entity.Event;
import gible.domain.participate.entity.Participate;


public record ParticipationEventRes(
        EventInfoRes event
) {
    public static ParticipationEventRes fromEntity(Participate participate) {
        Event event = participate.getEvent();
        return new ParticipationEventRes(new EventInfoRes(event.getId(), event.getTitle()));
    }
}
