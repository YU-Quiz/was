package yuquiz.common.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import yuquiz.common.s3.ImageType;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StorageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucket;

    // 이미지 업로드
    public String uploadImage(MultipartFile file, Long id, ImageType type) {
        String fileName = generateFileName(id, file.getOriginalFilename(), type);
        ObjectMetadata metadata = createObjectMetadata(file);

        uploadToS3(file, fileName, metadata);

        return getFileUrl(fileName);
    }

    // 이미지 삭제
    public void deleteImage(String imageUrl) {
        String fileName = extractFileNameFromUrl(imageUrl);
        amazonS3.deleteObject(bucket, fileName);
    }

    // 파일 이름 생성 (id(PK)와 UUID 기반)
    private String generateFileName(Long id, String originalFileName, ImageType type) {
        String dir = "";
        switch (type) {
            case QUIZ -> dir = "quiz/";
        }

        return dir + id + "/" + UUID.randomUUID() + "-" + originalFileName;
    }

    // S3 업로드
    private void uploadToS3(MultipartFile file, String fileName, ObjectMetadata metadata) {
        try {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata));
        } catch (IOException e) {
            throw new IllegalArgumentException("이미지 저장 중 오류 발생.");
        }
    }

    // 메타데이터 생성
    private ObjectMetadata createObjectMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        return metadata;
    }

    // 파일 URL 가져오기
    private String getFileUrl(String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    // 이미지 URL에서 파일 이름 추출
    private String extractFileNameFromUrl(String imageUrl) {
        int startIndex = imageUrl.indexOf("/", imageUrl.indexOf("//") + 2);
        return imageUrl.substring(startIndex + 1);
    }
}