package org.example.toolsproject.models.groups;

public class groupDTO {
    private String Gname;
    private String groupdescription;
    private boolean isOpen;

    public groupDTO() {}

    public groupDTO(String name, String description, boolean isOpen) {
        this.Gname = name;
        this.groupdescription = description;
        this.isOpen = isOpen;
    }

    public String getName() {
        return Gname;
    }

    public void setName(String name) {
        this.Gname = name;
    }

    public String getDescription() {
        return groupdescription;
    }

    public void setDescription(String description) {
        this.groupdescription = description;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
