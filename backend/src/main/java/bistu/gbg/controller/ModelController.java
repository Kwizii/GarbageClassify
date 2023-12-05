package bistu.gbg.controller;

import bistu.gbg.dto.ModelEndResponseDTO;
import bistu.gbg.dto.PredictionDTO;
import bistu.gbg.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Api(tags = "人工智能模型检测")
@RequestMapping("/api/model")
@Slf4j
public class ModelController {

    @ApiOperation("图像分类检测")
    @PostMapping(value = "/classify")
    public ResultVO<List<PredictionDTO>> classify(@RequestParam("file") MultipartFile file) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        // 设置请求头，指定内容类型为multipart/form-data
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 创建请求体，将MultipartFile对象添加到MultiValueMap中
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        ByteArrayResource resource;
        try {
            resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    // 设置文件名
                    return file.getOriginalFilename();
                }
            };
        } catch (IOException e) {
            // 处理异常情况
            e.printStackTrace();
            return new ResultVO<>(1, "请求异常", null);
        }
        body.add("file", resource);

        // 创建HttpEntity，将请求头和请求体添加到其中
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String targetUrl = "http://localhost:9528/ai/model";
        // 发送POST请求
        ResponseEntity<ModelEndResponseDTO> responseEntity = restTemplate.postForEntity(targetUrl, requestEntity, ModelEndResponseDTO.class);

        // 处理响应
        ModelEndResponseDTO<List<PredictionDTO>> response = responseEntity.getBody();
        assert response != null;
        return new ResultVO<>(0, "", response.getData());
    }
}
