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
package br.com.conductor.heimdall.api.resource;

import br.com.conductor.heimdall.api.util.ConstantsPrivilege;
import br.com.conductor.heimdall.core.converter.GenericConverter;
import br.com.conductor.heimdall.core.dto.OperationDTO;
import br.com.conductor.heimdall.core.dto.PageableDTO;
import br.com.conductor.heimdall.core.entity.Operation;
import br.com.conductor.heimdall.core.service.OperationService;
//import br.com.conductor.heimdall.core.publisher.AMQPRouteService;
import br.com.conductor.heimdall.core.util.ConstantsTag;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static br.com.conductor.heimdall.core.util.ConstantsPath.PATH_OPERATIONS;

/**
 * Uses a {@link OperationService} to provide methods to create, read, update and delete a {@link Operation}.
 *
 * @author Filipe Germano
 */
@io.swagger.annotations.Api(
        value = PATH_OPERATIONS,
        produces = MediaType.APPLICATION_JSON_VALUE,
        tags = {ConstantsTag.TAG_OPERATIONS})
@RestController
@RequestMapping(value = PATH_OPERATIONS)
public class OperationResource {

    @Autowired
    private OperationService operationService;

    /**
     * Finds a {@link Operation} by its Id.
     *
     * @param apiId       The Api Id
     * @param resourceId  The Resource Id
     * @param operationId The Operation Id
     * @return {@link ResponseEntity}
     */
    @ResponseBody
    @ApiOperation(value = "Find Operation by id", response = Operation.class)
    @GetMapping(value = "/resources/{resourceId}/operations/{operationId}")
    @PreAuthorize(ConstantsPrivilege.PRIVILEGE_READ_OPERATION)
    public ResponseEntity<?> findById(@PathVariable("apiId") String apiId,
                                      @PathVariable("resourceId") String resourceId,
                                      @PathVariable("operationId") String operationId) {

        Operation operation = operationService.find(apiId, resourceId, operationId);

        return ResponseEntity.ok(operation);
    }

    /**
     * Finds all {@link Operation} from a request.
     *
     * @param apiId       The Api Id
     * @param resourceId  The Resource Id
     * @param pageableDTO {@link PageableDTO}
     * @return {@link ResponseEntity}
     */
    @ResponseBody
    @ApiOperation(value = "Find all Operations", responseContainer = "List", response = Operation.class)
    @GetMapping(value = "/resources/{resourceId}/operations")
    @PreAuthorize(ConstantsPrivilege.PRIVILEGE_READ_OPERATION)
    public ResponseEntity<?> findAll(@PathVariable("apiId") String apiId,
                                     @PathVariable("resourceId") String resourceId,
                                     @ModelAttribute PageableDTO pageableDTO) {

        if (!pageableDTO.isEmpty()) {

            final Pageable pageable = PageRequest.of(pageableDTO.getPage(), pageableDTO.getLimit());
            Page<Operation> operationPage = operationService.list(apiId, resourceId, pageable);

            return ResponseEntity.ok(operationPage);
        } else {

            List<Operation> operations = operationService.list(apiId, resourceId);
            return ResponseEntity.ok(operations);
        }
    }

    /**
     * Saves a {@link Operation}.
     *
     * @param apiId        The Api Id
     * @param resourceId   The Resource Id
     * @param operationDTO {@link OperationDTO}
     * @return {@link ResponseEntity}
     */
    @ResponseBody
    @ApiOperation(value = "Save a new Operation")
    @PostMapping(value = "/resources/{resourceId}/operations")
    @PreAuthorize(ConstantsPrivilege.PRIVILEGE_CREATE_OPERATION)
    public ResponseEntity<?> save(@PathVariable("apiId") String apiId,
                                  @PathVariable("resourceId") String resourceId,
                                  @RequestBody @Valid OperationDTO operationDTO) {

        Operation operation = GenericConverter.mapper(operationDTO, Operation.class);
        operation = operationService.save(apiId, resourceId, operation);

        return ResponseEntity.created(
                URI.create(
                        String.format("/%s/%s/%s/%s/%s/%s",
                                "apis", apiId,
                                "resources", resourceId,
                                "operations", operation.getId()
                        )
                )
        ).build();
    }

    /**
     * Updates a {@link Operation}.
     *
     * @param apiId        The Api Id
     * @param resourceId   The Resource Id
     * @param operationId  The Operation Id
     * @param operationDTO {@link OperationDTO}
     * @return {@link ResponseEntity}
     */
    @ResponseBody
    @ApiOperation(value = "Update Operation")
    @PutMapping(value = "/resources/{resourceId}/operations/{operationId}")
    @PreAuthorize(ConstantsPrivilege.PRIVILEGE_UPDATE_OPERATION)
    public ResponseEntity<?> update(@PathVariable("apiId") String apiId,
                                    @PathVariable("resourceId") String resourceId,
                                    @PathVariable("operationId") String operationId,
                                    @RequestBody OperationDTO operationDTO) {

        Operation operation = GenericConverter.mapper(operationDTO, Operation.class);

        operation = operationService.update(apiId, resourceId, operationId, operation);

        return ResponseEntity.ok(operation);
    }

    /**
     * Deletes a {@link Operation}.
     *
     * @param apiId       The Api Id
     * @param resourceId  The Resource Id
     * @param operationId The Operation Id
     * @return {@link ResponseEntity}
     */
    @ResponseBody
    @ApiOperation(value = "Delete Operation")
    @DeleteMapping(value = "/resources/{resourceId}/operations/{operationId}")
    @PreAuthorize(ConstantsPrivilege.PRIVILEGE_DELETE_OPERATION)
    public ResponseEntity<?> delete(@PathVariable("apiId") String apiId,
                                    @PathVariable("resourceId") String resourceId,
                                    @PathVariable("operationId") String operationId) {

        operationService.delete(apiId, resourceId, operationId);

        return ResponseEntity.noContent().build();
    }

//    /**
//     * Refreshes all {@link Operation}.
//     *
//     * @param apiId      The Api Id
//     * @param resourceId The Resource Id
//     * @return {@link ResponseEntity}
//     */
//    @ResponseBody
//    @ApiOperation(value = "Refresh all Operations")
//    @PostMapping(value = "/resources/{resourceId}/operations/refresh")
//    @PreAuthorize(ConstantsPrivilege.PRIVILEGE_REFRESH_OPERATION)
//    public ResponseEntity<?> refresh(@PathVariable("apiId") Long apiId,
//                                     @PathVariable("resourceId") Long resourceId) {
//
//        aMQPRouteService.dispatchRoutes();
//
//        return ResponseEntity.noContent().build();
//    }

    /**
     * Lists all Operations from an Api
     *
     * @param apiId The Api Id
     * @return The complete list of Operations
     */
    @ResponseBody
    @ApiOperation(value = "Find all Operations", responseContainer = "List", response = Operation.class)
    @GetMapping(value = "/operations")
    @PreAuthorize(ConstantsPrivilege.PRIVILEGE_READ_OPERATION)
    public ResponseEntity<?> findAllOperations(@PathVariable("apiId") String apiId) {

        List<Operation> operations = operationService.list(apiId);
        return ResponseEntity.ok(operations);
    }

}