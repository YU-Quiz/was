package yuquiz.domain.auth.service.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import yuquiz.common.exception.CustomException;
import yuquiz.common.exception.exceptionCode.GlobalExceptionCode;
import yuquiz.domain.auth.config.KakaoConfig;
import yuquiz.domain.auth.dto.OAuthCodeDto;
import yuquiz.domain.auth.dto.UserInfoDto;
import yuquiz.domain.auth.exception.AuthExceptionCode;
import yuquiz.domain.user.entity.OAuthPlatform;
import yuquiz.domain.user.entity.Role;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class KakaoService implements OAuthClient {

    private final UserRepository userRepository;
    private final KakaoConfig kakaoConfig;
    private final ObjectMapper objectMapper;

    @Override
    public String getAccessToken(OAuthCodeDto codeDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("grant_type", "authorization_code");
        param.add("client_id", kakaoConfig.getClientId());
        param.add("redirect_uri", kakaoConfig.getRedirectUri());
        param.add("code", codeDto.code());

        HttpEntity<MultiValueMap<String, String>> requestInfo = new HttpEntity<>(param, headers);

        RestTemplate req = new RestTemplate();
        try {
            ResponseEntity<String> response = req.exchange(
                    kakaoConfig.getTokenUri(),
                    HttpMethod.POST,
                    requestInfo,
                    String.class);

            JsonNode rootNode = objectMapper.readTree(response.getBody());
            return rootNode.path("access_token").asText();
        } catch (JsonProcessingException e) {
            throw new CustomException(AuthExceptionCode.JSON_PROCESSING_ERROR);
        } catch (Exception e) {
            throw new CustomException(GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserInfoDto getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> requestInfo = new HttpEntity<>(headers);

        RestTemplate req = new RestTemplate();
        try {
            ResponseEntity<String> response = req.exchange(
                    kakaoConfig.getGetUserInfoUri(),
                    HttpMethod.GET,
                    requestInfo,
                    String.class);

            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // 필요한 정보 추출
            String id = rootNode.path("id").asText();

            return UserInfoDto.of(id);
        } catch (JsonProcessingException e) {
            throw new CustomException(AuthExceptionCode.JSON_PROCESSING_ERROR);
        } catch (Exception e) {
            throw new CustomException(GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public User getOAuthUser(String platformId) {

        return userRepository.findByUsername(OAuthPlatform.KAKAO + "_" + platformId)
                .orElseGet(() -> createOAuthUser(platformId));

    }

    private User createOAuthUser(String platformId) {

        return userRepository.save(User.builder()
                .username(OAuthPlatform.KAKAO + "_" + platformId)
                .role(Role.USER)
                .build()
        );
    }
}
