package vn.anpha.storage.File.DTO.Response;

public enum Action {
    UPLOAD("upload"),
    DOWNLOAD("download"),
    UPDATE("update");

    private String value;

    Action(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return this.value;
    }
}
