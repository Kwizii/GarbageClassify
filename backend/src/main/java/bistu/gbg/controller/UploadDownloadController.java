package bistu.gbg.controller;

import bistu.gbg.qo.UploadModel;
import bistu.gbg.qo.UploadModel2;
import bistu.gbg.qo.DownloadQo;
import bistu.gbg.utils.FileTransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Api(tags = "Upload And Download APIs")
@RequestMapping("/api/file")
@Slf4j
public class UploadDownloadController {

//    @Autowired
//    AITestConfig aiTestConfig;
//
//    @PostMapping("/upload/single")
//    @ApiOperation("单文件上传")
//    public String uploadFile(@RequestParam("file") MultipartFile uploadfile) {
//        return FileTransUtil.uploadFile(uploadfile, "/root/" + File.separator + uploadfile.getOriginalFilename());
//    }

    @ApiOperation("单文件上传,支持同时传入参数")
    @PostMapping("/api/upload/singleAndparas")
    public String uploadFileSingle(@RequestParam("dir") String dir, @RequestParam("file") MultipartFile uploadfile) {
        return FileTransUtil.uploadFile(uploadfile, dir);
    }

    @ApiOperation("单文件上传,支持同时传入参数,Model")
    @PostMapping("/upload/single/model")
    public String singleUploadFileModel(@ModelAttribute("model") UploadModel2 model) {
        return FileTransUtil.uploadFile(model.getFile(), model.getDir());
    }

    @ApiOperation("多文件上传,支持同时传入参数")
    @PostMapping("upload/multiAndparas")
    public String uploadFileMulti(@RequestParam("dir") String dir, @RequestParam("files") MultipartFile[] uploadfiles) {
        return FileTransUtil.uploadFiles(uploadfiles, dir);
    }

    @ApiOperation("多文件上传,支持同时传入参数")
    @PostMapping(value = "/upload/multi/model")
    public String multiUploadFileModel(@ModelAttribute(("model")) UploadModel model) {
        return FileTransUtil.uploadFiles(model.getFiles(), model.getDir());
    }

    @ApiOperation("Get下载文件")
    @GetMapping(value = "/download/get")
    public ResponseEntity<InputStreamResource> downloadFileGet(@RequestParam String filePath) throws IOException {
        return FileTransUtil.downloadFile(filePath);
    }

    @ApiOperation("Post下载文件")
    @PostMapping(value = "/download/post")
    public ResponseEntity<InputStreamResource> downloadFilePost(@RequestBody DownloadQo downloadQo) throws IOException {
        return FileTransUtil.downloadFile(downloadQo.getPath());
    }
}
