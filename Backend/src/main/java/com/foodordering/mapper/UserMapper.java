package com.foodordering.mapper;

import com.foodordering.dto.AuthResponseDto;
import com.foodordering.dto.UserDto;
import com.foodordering.dto.UserRegistrationDto;
import com.foodordering.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {com.foodordering.mapper.AddressMapper.class, com.foodordering.mapper.OrderMapper.class, com.foodordering.mapper.RestaurantMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    // Map registration DTO to User entity. Service should encode password before saving.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(dto.getRole()!=null?com.foodordering.enums.Role.valueOf(dto.getRole()):null)")
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserRegistrationDto dto);

    // Map User to AuthResponseDto (token will be set by service)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "role", expression = "java(user.getRole()!=null?user.getRole().name():null)")
    AuthResponseDto toAuthResponse(User user);

    // Map User entity to a nested UserDto for frontend consumption.
    @Mapping(target = "role", expression = "java(user.getRole()!=null?user.getRole().name():null)")
    UserDto toDto(User user);
}
