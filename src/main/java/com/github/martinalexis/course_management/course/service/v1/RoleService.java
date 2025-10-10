package com.github.martinalexis.course_management.course.service.v1;

import com.github.martinalexis.course_management.common.exceptions.ResourceNotFoundException;
import com.github.martinalexis.course_management.course.model.RoleEnum;
import com.github.martinalexis.course_management.course.model.RoleModel;
import com.github.martinalexis.course_management.course.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleModel getRoleOrThrow(RoleEnum roleEnum) {
        return roleRepository.findByRole(roleEnum)
                .orElseThrow(() -> new ResourceNotFoundException("role", roleEnum));
    }


}
