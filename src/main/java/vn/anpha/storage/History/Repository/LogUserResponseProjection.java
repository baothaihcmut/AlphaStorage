package vn.anpha.storage.History.Repository;

import vn.anpha.storage.File.Entity.File;
import vn.anpha.storage.History.Entity.ActionEnum;
import vn.anpha.storage.History.Entity.StatusEnum;

import java.time.LocalDateTime;

public interface LogUserResponseProjection {

    public String getLogId();

    public String getEmail();

    public ActionEnum getAction();

    public StatusEnum getStatus();

    public LocalDateTime getCreatedAt() ;

    public File getFile() ;
}
