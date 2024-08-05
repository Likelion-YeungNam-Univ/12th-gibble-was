package gible.domain.review.repository;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import gible.global.util.FirebaseDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Primary
public class ReviewDao implements FirebaseDao {
    @Value("${firebase.bucket-name}")
    private String bucketName;

    @Override
    public void delete(String eventUrl){
        String fileName = "event/" + eventUrl;
        Bucket bucket = StorageClient.getInstance().bucket(bucketName);
        Page<Blob> blobs = bucket.list(Storage.BlobListOption.prefix(fileName));
        for (Blob blob : blobs.iterateAll()) {
            blob.delete();
        }

    }

}
