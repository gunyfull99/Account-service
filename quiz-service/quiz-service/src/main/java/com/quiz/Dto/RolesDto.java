package com.quiz.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolesDto {
    private long id;
    private String name;
    private Set<PermissionDto> permissions = new HashSet<>();
}
