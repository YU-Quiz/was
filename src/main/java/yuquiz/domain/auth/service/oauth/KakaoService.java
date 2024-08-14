package yuquiz.domain.auth.service;

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
import yuquiz.domain.auth.dto.OauthCodeDto;
import yuquiz.domain.auth.dto.UserInfoDto;
import yuquiz.domain.auth.exception.AuthExceptionCode;
import yuquiz.domain.user.entity.OAuthPlatform;
import yuquiz.domain.user.repository.OAuthRepository;

@Service
@RequiredArgsConstructor
public class KakaoService implements OAuthClient {

    private final OAuthRepository oAuthRepository;
    private final KakaoConfig kakaoConfig;
    private final ObjectMapper objectMapper;

    @Override
    public String getAccessToken(OauthCodeDto codeDto) {
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
            String email = rootNode.path("kakao_account").path("email").asText();
            String nickname = rootNode.path("properties").path("nickname").asText();

            return UserInfoDto.of(id, email, nickname);
        } catch (JsonProcessingException e) {
            throw new CustomException(AuthExceptionCode.JSON_PROCESSING_ERROR);
        } catch (Exception e) {
            throw new CustomException(GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean isExists(String email) {
        return oAuthRepository.existsByPlatformAndEmail(OAuthPlatform.KAKAO, email);
    }
}
