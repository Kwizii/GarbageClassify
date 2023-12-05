package bistu.gbg.qo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadModel2 {
    /**
     * 要保存的文件列表
     */
    private MultipartFile file;
    /**
     * 文件要存储的文件夹
     */
    private String dir;
}
