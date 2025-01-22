package com.example.demo.dto.animal;

public class AnimalDtoUpdateTypeRequest {
    private long oldTypeId;

    private long newTypeId;

    public AnimalDtoUpdateTypeRequest() {
    }

    public long getOldTypeId() {
        return oldTypeId;
    }

    public void setOldTypeId(long oldTypeId) {
        this.oldTypeId = oldTypeId;
    }

    public long getNewTypeId() {
        return newTypeId;
    }

    public void setNewTypeId(long newTypeId) {
        this.newTypeId = newTypeId;
    }
}
