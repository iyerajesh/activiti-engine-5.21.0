package org.activiti;

import org.springframework.stereotype.Component;

@Component
public class UnassignedClaimsService {

    public void storeClaims() {
        System.out.println("Storing unassigned claims ...");
    }

}
