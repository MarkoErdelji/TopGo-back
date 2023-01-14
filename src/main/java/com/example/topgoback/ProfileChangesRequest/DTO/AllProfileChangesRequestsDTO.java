package com.example.topgoback.ProfileChangesRequest.DTO;

import java.util.List;

public class AllProfileChangesRequestsDTO {
    private int count;
    private List<ProfileChangeRequestDTO> profileChangeRequestDTOS;

    public AllProfileChangesRequestsDTO() {
    }

    public AllProfileChangesRequestsDTO(int count, List<ProfileChangeRequestDTO> profileChangeRequestDTOS) {
        this.count = count;
        this.profileChangeRequestDTOS = profileChangeRequestDTOS;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ProfileChangeRequestDTO> getProfileChangeRequestDTOS() {
        return profileChangeRequestDTOS;
    }

    public void setProfileChangeRequestDTOS(List<ProfileChangeRequestDTO> profileChangeRequestDTOS) {
        this.profileChangeRequestDTOS = profileChangeRequestDTOS;
    }
}
