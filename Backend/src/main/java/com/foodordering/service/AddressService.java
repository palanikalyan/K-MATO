package com.foodordering.service;

import com.foodordering.dto.AddressDto;
import com.foodordering.entity.Address;
import com.foodordering.entity.User;
import com.foodordering.exception.ResourceNotFoundException;
import com.foodordering.repository.AddressRepository;
import com.foodordering.repository.UserRepository;
import com.foodordering.mapper.AddressMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.addressMapper = addressMapper;
    }

    @Transactional
    public AddressDto createAddress(AddressDto dto) {
        String email = com.foodordering.security.SecurityUtils.getCurrentUserEmail();
        if (email == null) throw new ResourceNotFoundException("Authenticated user not found");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (dto.getIsDefault() != null && dto.getIsDefault()) {
            List<Address> existingAddresses = addressRepository.findByUserId(user.getId());
            existingAddresses.forEach(addr -> {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            });
        }

    Address address = addressMapper.toEntity(dto);
    // Ignore any client-supplied id for create operations to avoid accidental updates
    address.setId(null);
    address.setUser(user);
    address.setIsDefault(dto.getIsDefault() != null ? dto.getIsDefault() : false);

    address = addressRepository.save(address);
    return addressMapper.toDto(address);
    }

    public List<AddressDto> getUserAddresses() {
    String email = com.foodordering.security.SecurityUtils.getCurrentUserEmail();
    if (email == null) throw new ResourceNotFoundException("Authenticated user not found");
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    return addressRepository.findByUserId(user.getId()).stream()
        .map(addressMapper::toDto)
        .collect(Collectors.toList());
    }

    @Transactional
    public AddressDto updateAddress(Long id, AddressDto dto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        
        // Ensure the authenticated user owns this address
        String email = com.foodordering.security.SecurityUtils.getCurrentUserEmail();
        if (email == null) throw new ResourceNotFoundException("Authenticated user not found");
        if (address.getUser() == null || !email.equals(address.getUser().getEmail())) {
            throw new org.springframework.security.access.AccessDeniedException("Not authorized to update this address");
        }

        if (dto.getIsDefault() != null && dto.getIsDefault() && !address.getIsDefault()) {
            List<Address> userAddresses = addressRepository.findByUserId(address.getUser().getId());
            userAddresses.forEach(addr -> {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            });
        }

        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setZipCode(dto.getZipCode());
        address.setLandmark(dto.getLandmark());
        address.setIsDefault(dto.getIsDefault());

        address = addressRepository.save(address);
        return addressMapper.toDto(address);
    }

    @Transactional
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        // Ensure the authenticated user owns this address
        String email = com.foodordering.security.SecurityUtils.getCurrentUserEmail();
        if (email == null) throw new ResourceNotFoundException("Authenticated user not found");
        if (address.getUser() == null || !email.equals(address.getUser().getEmail())) {
            throw new org.springframework.security.access.AccessDeniedException("Not authorized to delete this address");
        }

        addressRepository.deleteById(id);
    }

    // Mapping handled by AddressMapper
}
