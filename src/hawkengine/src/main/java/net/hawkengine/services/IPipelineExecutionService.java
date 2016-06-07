
package net.hawkengine.services;

import java.util.ArrayList;

import net.hawkengine.db.IDbRepository;
import net.hawkengine.model.ExecutionState;
import net.hawkengine.model.Pipeline;

public interface IPipelineExecutionService extends IDbRepository<Pipeline> {
	ArrayList<Pipeline> getPpipelinesByStatus(ExecutionState status) throws Exception;

	int getLastRunIDForPipeline(String pipelineName) throws Exception;

	Pipeline getPipelineExecutionByExecutionID(String pipelineName, int pipelineExecutionID) throws Exception;

}
