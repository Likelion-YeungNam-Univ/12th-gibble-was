package gible.global.util;

import org.springframework.stereotype.Component;


@Component
public interface FirebaseDao {
    void delete(String imageId);
}
