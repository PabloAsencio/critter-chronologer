package com.udacity.jdnd.course3.critter.pet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet savePet(Pet pet) {
        return petRepository.save(pet);
    }

    public Pet getPetById(Long petId) {
        return petRepository.findById(petId).orElseThrow(RuntimeException::new);
    }

    public List<Pet> getAllPets() {
        List<Pet> pets = new ArrayList<>();
        petRepository.findAll().forEach(pets::add);
        return pets;
    }

    public List<Pet> getPetsByOwner(Long ownerId) {
        return petRepository.findAllByOwnerId(ownerId);
    }
}
