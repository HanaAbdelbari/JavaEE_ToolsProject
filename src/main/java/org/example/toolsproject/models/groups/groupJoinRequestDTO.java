package org.example.toolsproject.models.groups;

public class groupJoinRequestDTO {
    private Long groupid;
    private Long userid;

    public groupJoinRequestDTO() {}

    public groupJoinRequestDTO(Long groupId, Long userId) {
        this.groupid = groupId;
        this.userid = userId;
    }

    public Long getGroupId() {
        return groupid;
    }

    public void setGroupId(Long groupId) {
        this.groupid = groupId;
    }

    public Long getUserId() {
        return userid;
    }

    public void setUserId(Long userId) {
        this.userid = userId;
    }
}
