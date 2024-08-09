package com.xmartin.authservice.domain.ports.out;

import com.xmartin.authservice.domain.model.RequestModel;

public interface RouteValidatorPort {
    boolean isAdminPath(RequestModel requestModel);
}
