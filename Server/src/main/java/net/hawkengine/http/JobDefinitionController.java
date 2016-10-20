/*
 * Copyright (C) 2016 R&D Solutions Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.hawkengine.http;

import net.hawkengine.core.utilities.SchemaValidator;
import net.hawkengine.model.JobDefinition;
import net.hawkengine.model.ServiceResult;
import net.hawkengine.model.enums.NotificationType;
import net.hawkengine.services.JobDefinitionService;
import net.hawkengine.services.interfaces.IJobDefinitionService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Consumes("application/json")
@Produces("application/json")
@Path("/job-definitions")
public class JobDefinitionController {
    private IJobDefinitionService jobDefinitionService;
    private SchemaValidator schemaValidator;

    public JobDefinitionController() {
        this.jobDefinitionService = new JobDefinitionService();
        this.schemaValidator = new SchemaValidator();
    }

    public JobDefinitionController(IJobDefinitionService jobDefinitionService) {
        this.jobDefinitionService = jobDefinitionService;
        this.schemaValidator = new SchemaValidator();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllJobDefinitions() {
        ServiceResult result = this.jobDefinitionService.getAll();
        return Response.status(Status.OK)
                .entity(result.getObject())
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{jobDefinitionId}")
    public Response getJobDefinitionById(@PathParam("jobDefinitionId") String jobDefinitionId) {
        ServiceResult result = this.jobDefinitionService.getById(jobDefinitionId);
        if (result.getNotificationType() == NotificationType.ERROR) {
            return Response.status(Status.NOT_FOUND)
                    .entity(result.getMessage())
                    .type(MediaType.TEXT_HTML)
                    .build();
        }
        return Response.status(Status.OK)
                .entity(result.getObject())
                .build();

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewJobDefinition(JobDefinition jobDefinition) {
        String isValid = this.schemaValidator.validate(jobDefinition);
        if (isValid.equals("OK")) {
            ServiceResult result = this.jobDefinitionService.add(jobDefinition);
            if (result.getNotificationType() == NotificationType.ERROR) {
                return Response.status(Status.BAD_REQUEST)
                        .entity(result.getMessage())
                        .type(MediaType.TEXT_HTML)
                        .build();
            }
            return Response.status(Status.CREATED)
                    .entity(result.getObject())
                    .build();

        } else {
            return Response.status(Status.BAD_REQUEST)
                    .entity(isValid)
                    .type(MediaType.TEXT_HTML)
                    .build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateJobDefinition(JobDefinition jobDefinition) {
        String isValid = this.schemaValidator.validate(jobDefinition);
        if (isValid.equals("OK")) {
            ServiceResult result = this.jobDefinitionService.update(jobDefinition);
            if (result.getNotificationType() == NotificationType.ERROR) {
                return Response.status(Status.BAD_REQUEST)
                        .entity(result.getMessage())
                        .type(MediaType.TEXT_HTML)
                        .build();
            }
            return Response.status(Status.OK)
                    .entity(result.getObject())
                    .build();

        } else {
            return Response.status(Status.BAD_REQUEST)
                    .entity(isValid)
                    .type(MediaType.TEXT_HTML)
                    .build();
        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{jobDefinitionId}")
    public Response deleteJobDefinition(@PathParam("jobDefinitionId") String jobDefinitionId) {
        ServiceResult result = this.jobDefinitionService.delete(jobDefinitionId);
        if (result.getNotificationType() == NotificationType.ERROR) {
            return Response.status(Status.BAD_REQUEST)
                    .entity(result.getMessage())
                    .type(MediaType.TEXT_HTML)
                    .build();
        }
        return Response.status(Status.NO_CONTENT)
                .entity(result.getMessage())
                .type(MediaType.TEXT_HTML)
                .build();
    }
}