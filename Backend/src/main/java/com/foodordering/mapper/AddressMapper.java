package com.foodordering.mapper;

import com.foodordering.dto.AddressDto;
import com.foodordering.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDto toDto(Address address);
    @Mapping(target = "user", ignore = true)
    Address toEntity(AddressDto dto);
}
