package bistu.gbg.qo;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadModel {
    /**
     * 要保存的文件
     */
    private MultipartFile[] files;
    /**
     * 文件要存储的文件夹
     */
    private String dir;
}
