/*
 * Copyright (C) 2018 Conductor Tecnologia SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.conductor.heimdall.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.conductor.heimdall.core.entity.Developer;

/**
 * Provides methods to access a {@link Developer}.
 *
 * @author Filipe Germano
 * @author <a href="https://dijalmasilva.github.io" target="_blank">Dijalma Silva</a>
 *
 */
public interface DeveloperRepository extends JpaRepository<Developer, String> {

	/**
	 * Finds a {@link Developer} by its email and password
	 *
	 * @param  email		The Developer email
	 * @param  password		The Developer password
	 * @return				The Developer found
	 */
     Developer findByEmailAndPassword(String email, String password);

}