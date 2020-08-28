/*-
 * -\-\-
 * Spotify Styx Scheduler Service
 * --
 * Copyright (C) 2016 - 2020 Spotify AB
 * --
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 * -/-/-
 */

package com.spotify.styx.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.spotify.styx.flyte.FlyteExecution;
import com.spotify.styx.flyte.FlyteRunner;
import com.spotify.styx.flyte.client.FlyteAdminClient;
import com.spotify.styx.model.FlyteExecConf;
import com.spotify.styx.model.FlyteIdentifier;
import flyteidl.admin.ExecutionOuterClass;
import flyteidl.core.IdentifierOuterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FlyteRunnerTest {

  @Mock private FlyteAdminClient flyteAdminClient;

  @Test
  public void testCreateExecution() {
    FlyteRunner flyteRunner = new FlyteRunner(flyteAdminClient);
    when(flyteAdminClient.createExecution(any(), any(), any(),any())).thenReturn(
        ExecutionOuterClass.ExecutionCreateResponse
            .newBuilder()
            .setId(IdentifierOuterClass.WorkflowExecutionIdentifier
                .newBuilder()
                .setProject("flyte-test")
                .setDomain("testing")
                .setName("test-create-execution")
                .build())
            .build());
    final FlyteExecConf flyteExecConf = FlyteExecConf.builder()
        .referenceId(FlyteIdentifier.builder()
            .project("flyte-test")
            .domain("testing")
            .name("test-create-execution")
            .version("1234")
            .resourceType("lp")
            .build())
        .build();
    final FlyteExecution flyteExecution = flyteRunner.createExecution(flyteExecConf);

    assertThat(flyteExecution.getProject(), is("flyte-test"));
    assertThat(flyteExecution.getDomain(), is("testing"));
    assertThat(flyteExecution.getName(), is("test-create-execution"));
    assertThat(flyteExecution.toUrn(), is("ex:flyte-test:testing:test-create-execution"));  }
}