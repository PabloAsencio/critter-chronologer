package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;
    private final CustomerService customerService;

    public PetController(PetService petService, CustomerService customerService) {
        this.petService = petService;
        this.customerService = customerService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = convertPetDTOToEntity(petDTO);
        // The following is necessary to make sure the relation between the pet
        // and the owner is persisted correctly when the API methods are called programmatically
        // when running the tests.
        // This doesn't seem to be necessary when the API is accessed via an HTTP connection.
        // See https://knowledge.udacity.com/questions/356184
        Customer customer = customerService.getCustomerById(petDTO.getOwnerId());
        pet.setOwner(customer);
        Pet savedPet = petService.savePet(pet);
        if (null != customer.getPets()) {
            customer.getPets().add(savedPet);
        } else {
            List<Pet> pets = new ArrayList<>();
            pets.add(savedPet);
            customer.setPets(pets);
        }

        return convertPetEntityToDTO(savedPet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return convertPetEntityToDTO(petService.getPetById(petId));
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = petService.getAllPets();
        List<PetDTO> petDTOList = new ArrayList<>();
        pets.forEach((pet) -> {
            petDTOList.add(convertPetEntityToDTO(pet));
        });

        return petDTOList;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = petService.getPetsByOwner(ownerId);
        List<PetDTO> petDTOList = new ArrayList<>();
        pets.forEach((pet) -> {
            petDTOList.add(convertPetEntityToDTO(pet));
        });

        return petDTOList;
    }

    private static Pet convertPetDTOToEntity(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        Customer owner  = new Customer();
        owner.setId(petDTO.getOwnerId());
        pet.setOwner(owner);
        return pet;
    }

    private static PetDTO convertPetEntityToDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setOwnerId(pet.getOwner().getId());
        return petDTO;
    }
}
