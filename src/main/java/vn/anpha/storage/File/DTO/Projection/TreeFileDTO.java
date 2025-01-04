package vn.anpha.storage.File.DTO.Projection;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TreeFileDTO {
    private FileDTO file;
    private List<TreeFileDTO> subFiles = new ArrayList<>();

    public TreeFileDTO(FileDTO file) {
        this.file = file;
    }

    public void addSubFile(TreeFileDTO subFile) {
        this.subFiles.add(subFile);
    }

}
