/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.petclinic.model.PersonDTO;

/**
 * Simple JavaBean domain object representing an owner.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
public class OwnerDTO extends PersonDTO {

	@NotEmpty
	private String address;

	@NotEmpty
	private String city;

	@NotEmpty
	@Digits(fraction = 0, integer = 10)
	private String telephone;

	private Set<PetDTO> pets;

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void movePet(PetDTO pet) {
		getPetsInternal().add(pet);
		pet.setOwner(this);
	}

	protected Set<PetDTO> getPetsInternal() {
		if (this.pets == null) {
			this.pets = new HashSet<>();
		}
		return this.pets;
	}

	protected void setPetsInternal(Set<PetDTO> pets) {
		this.pets = pets;
	}

	public List<PetDTO> getPets() {
		List<PetDTO> sortedPets = new ArrayList<>(getPetsInternal());
		PropertyComparator.sort(sortedPets, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedPets);
	}

	public void addPet(PetDTO pet) {
		if (pet.isNew()) {
			getPetsInternal().add(pet);
		}
		pet.setOwner(this);
	}

	/**
	 * Return the Pet with the given name, or null if none found for this Owner.
	 * @param name to test
	 * @return true if pet name is already in use
	 */
	public PetDTO getPet(String name) {
		return getPet(name, false);
	}

	/**
	 * Return the Pet with the given name, or null if none found for this Owner.
	 * @param name to test
	 * @return true if pet name is already in use
	 */

	/*
	 *
	 * /** Return the Pet with the given name, or null if none found for this Owner.
	 *
	 * @param name to test
	 *
	 * @return true if pet name is already in use
	 */

	public PetDTO getPet(String name, boolean ignoreNew) {
		name = name.toLowerCase();
		for (PetDTO pet : getPetsInternal()) {
			if (!ignoreNew || !pet.isNew()) {
				String compName = pet.getName();
				compName = compName.toLowerCase();
				if (compName.equals(name)) {
					return pet;
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)

				.append("id", this.getId()).append("new", this.isNew()).append("lastName", this.getLastName())
				.append("firstName", this.getFirstName()).append("address", this.address).append("city", this.city)
				.append("telephone", this.telephone).toString();
	}

}